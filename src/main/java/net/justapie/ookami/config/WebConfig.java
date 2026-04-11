package net.justapie.ookami.config;

import net.justapie.ookami.interceptors.AccessTokenInterceptor;
import net.justapie.ookami.resolvers.CurrentUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AccessTokenInterceptor accessTokenInterceptor;
    private final CurrentUserArgumentResolver currentUserArgumentResolver;

    public WebConfig(AccessTokenInterceptor accessTokenInterceptor, CurrentUserArgumentResolver currentUserArgumentResolver) {
        this.accessTokenInterceptor = accessTokenInterceptor;
        this.currentUserArgumentResolver = currentUserArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessTokenInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserArgumentResolver);
    }
}
