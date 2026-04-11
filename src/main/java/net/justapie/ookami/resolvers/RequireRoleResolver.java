package net.justapie.ookami.resolvers;

import net.justapie.ookami.annotations.RequireRole;
import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

public class RequireRoleResolver extends CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(@NonNull MethodParameter parameter) {
        return super.supportsParameter(parameter) && parameter.getParameterAnnotation(RequireRole.class) != null;
    }
}
