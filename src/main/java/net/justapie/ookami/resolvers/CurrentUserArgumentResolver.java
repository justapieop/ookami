package net.justapie.ookami.resolvers;

import jakarta.servlet.http.HttpServletRequest;
import net.justapie.ookami.annotations.AuthenticatedUser;
import net.justapie.ookami.repositories.user.User;
import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthenticatedUser.class) != null &&
                parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        return request.getAttribute("authenticatedUser");
    }
}