package net.justapie.ookami;

import jakarta.annotation.PostConstruct;
import net.justapie.ookami.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

@SpringBootApplication
@EnableCaching
public class OokamiApplication {
    @Value("${app.jwks.url}")
    private String jwkUrl;

    public static void main(String[] args) throws MalformedURLException, URISyntaxException {
        SpringApplication.run(OokamiApplication.class, args);
    }

    @PostConstruct
    private void init() throws MalformedURLException, URISyntaxException {
        JwtUtils.initJwks(jwkUrl);
    }
}
