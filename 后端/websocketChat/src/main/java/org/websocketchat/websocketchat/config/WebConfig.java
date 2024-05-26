package org.websocketchat.websocketchat.config;

import org.websocketchat.websocketchat.interceptors.LoginInterceptor;
import org.websocketchat.websocketchat.interceptors.RefreshTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ALL
 * @date 2024/4/15
 * @Description
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private RefreshTokenInterceptor refreshTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录注册接口不拦截
//        registry.addInterceptor(loginInterceptor).excludePathPatterns(
//                "/user/login",
//                "/user/register",
//                "/user/logout",
//                "/user/code",
//                "/user/loginByPhone"
//        ).order(1);
//        registry.addInterceptor(refreshTokenInterceptor).order(0);
    }
}
