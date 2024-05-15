package com.example.safeapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: LinZiYu
 * \* Date: 2020/6/3
 * \* Time: 19:30
 * \* Description:
 * \
 */

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport{

        private static final String[] excludePathPatterns  = {"/api/token/api_token"};

        @Autowired
        private TokenInterceptor tokenInterceptor;
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            super.addInterceptors(registry);
            registry.addInterceptor(tokenInterceptor)
                    .addPathPatterns("/api/**")
                    .addPathPatterns("/test/**")
                    .excludePathPatterns(excludePathPatterns);
        }
}


