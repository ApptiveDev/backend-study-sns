package com.example.devSns.config;

import com.example.devSns.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private JwtInterceptor JwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry Registry) {
        Registry.addInterceptor(JwtInterceptor)
                .addPathPatterns("/api/**")  // 인증이 필요한 경로
                .excludePathPatterns("/","/api/auth/signup",
                        "/api/auth/login",
                        "/error");
    }
}
