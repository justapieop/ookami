package net.justapie.ookami.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.justapie.ookami.annotations.RequireRole;
import net.justapie.ookami.repositories.user.User;
import net.justapie.ookami.services.UserService;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.method.HandlerMethod;

@Component
public class RoleInterceptor extends AccessTokenInterceptor {
    public RoleInterceptor(UserService userService) {
        super(userService);
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws ErrorResponseException {
        if (!super.preHandle(request, response, handler)) {
            return false;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        boolean hasAnnotation = handlerMethod.hasMethodAnnotation(RequireRole.class) ||
                handlerMethod.getBeanType().isAnnotationPresent(RequireRole.class);

        if (!hasAnnotation) {
            return true;
        }

        User user = (User) request.getAttribute("authenticatedUser");

        RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);

        if (requireRole == null) {
            requireRole = handlerMethod.getBeanType().getAnnotation(RequireRole.class);
        }

        if (!user.hasRole(requireRole.value())) {
            throw new ErrorResponseException(HttpStatus.UNAUTHORIZED);
        }

        return true;
    }
}
