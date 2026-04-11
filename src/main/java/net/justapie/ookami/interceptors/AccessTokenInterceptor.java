package net.justapie.ookami.interceptors;

import com.auth0.jwk.JwkException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.justapie.ookami.annotations.VerifyAccessToken;
import net.justapie.ookami.repositories.user.User;
import net.justapie.ookami.services.UserService;
import net.justapie.ookami.utils.JwtUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.NoSuchElementException;
import java.util.UUID;

@Component
public class AccessTokenInterceptor implements HandlerInterceptor {
    private final UserService userService;

    public AccessTokenInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws ErrorResponseException {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        boolean hasAnnotation = handlerMethod.hasMethodAnnotation(VerifyAccessToken.class) ||
                handlerMethod.getBeanType().isAnnotationPresent(VerifyAccessToken.class);

        if (!hasAnnotation) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST, new Throwable("Invalid Header Format"));
        }

        String token = authHeader.substring(7);
        String sub = null;

        try {
            sub = JwtUtils.verifyJwt(token);
        } catch (JwkException e) {
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST, new Throwable("Invalid Access Token"));
        }

        UUID id = UUID.fromString(sub);

        User user = null;

        try {
            user = userService.getUserById(id);
        } catch (NoSuchElementException e) {
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST, new Throwable("Invalid Access Token"));
        }

        request.setAttribute("authenticatedUser", user);

        return true;
    }
}
