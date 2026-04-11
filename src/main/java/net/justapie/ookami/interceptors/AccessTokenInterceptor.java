package net.justapie.ookami.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.justapie.ookami.annotations.VerifyAccessToken;
import net.justapie.ookami.services.UserService;
import net.justapie.ookami.utils.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class AccessTokenInterceptor implements HandlerInterceptor {
    private final UserService userService;

    public AccessTokenInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
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
            throw new SecurityException("Invalid Header Format");
        }

        String token = authHeader.substring(7);
        String sub = JwtUtils.verifyJwt(token);

        UUID id = UUID.fromString(sub);

        request.setAttribute("authenticatedUser", userService.getUserById(id));

        return true;
    }
}
