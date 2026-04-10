package net.justapie.ookami.utils;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.experimental.UtilityClass;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.atomic.AtomicReference;

@UtilityClass
public class JwtUtils {
    private final AtomicReference<JwkProvider> jwks = new AtomicReference<>();

    public void initJwks(String jwksUrl) throws URISyntaxException, MalformedURLException {
        URL url = new URI(jwksUrl).normalize().toURL();

        jwks.compareAndSet(null, new JwkProviderBuilder(url)
                .cached(true).build());
    }

    public String verifyJwt(String jwt) throws JwkException {
        DecodedJWT decoded = JWT.decode(jwt);

        String alg = decoded.getAlgorithm();
        String kid = decoded.getKeyId();

        Jwk jwk = jwks.get().get(kid);

        Algorithm algo = switch (alg) {
            case "RS256" -> Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
            case "RS384" -> Algorithm.RSA384((RSAPublicKey) jwk.getPublicKey(), null);
            case "RS512" -> Algorithm.RSA512((RSAPublicKey) jwk.getPublicKey(), null);
            case "ES256" -> Algorithm.ECDSA256((ECPublicKey) jwk.getPublicKey(), null);
            case "ES384" -> Algorithm.ECDSA384((ECPublicKey) jwk.getPublicKey(), null);
            case "ES512" -> Algorithm.ECDSA512((ECPublicKey) jwk.getPublicKey(), null);
            default -> throw new JwkException("Unsupported algorithm: " + alg);
        };
        JWT.require(algo).build().verify(jwt);
        return decoded.getSubject();
    }
}
