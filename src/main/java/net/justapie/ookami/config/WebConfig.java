package net.justapie.ookami.config;

import net.justapie.ookami.interceptors.AccessTokenInterceptor;
import net.justapie.ookami.interceptors.RoleInterceptor;
import net.justapie.ookami.resolvers.CurrentUserArgumentResolver;
import net.justapie.ookami.resolvers.RequireRoleResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AccessTokenInterceptor accessTokenInterceptor;
    private final RoleInterceptor roleInterceptor;
    private final RequireRoleResolver requireRoleResolver;
    private final CurrentUserArgumentResolver currentUserArgumentResolver;

    public WebConfig(AccessTokenInterceptor accessTokenInterceptor, RoleInterceptor roleInterceptor, RequireRoleResolver requireRoleResolver, CurrentUserArgumentResolver currentUserArgumentResolver) {
        this.accessTokenInterceptor = accessTokenInterceptor;
        this.roleInterceptor = roleInterceptor;
        this.requireRoleResolver = requireRoleResolver;
        this.currentUserArgumentResolver = currentUserArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.accessTokenInterceptor);
        registry.addInterceptor(this.roleInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(this.currentUserArgumentResolver);
        resolvers.add(this.requireRoleResolver);
    }
}
