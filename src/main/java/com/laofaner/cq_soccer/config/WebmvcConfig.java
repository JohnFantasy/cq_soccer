package com.laofaner.cq_soccer.config;

import com.laofaner.cq_soccer.interceptors.CheckSignatureInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: cq_soccer
 * @description: mvc配置类（配置拦截器注册信息）
 * @author: fyz
 * @create: 2019-11-25 16:11
 **/

@Configuration
public class WebmvcConfig implements WebMvcConfigurer {

    @Bean
    public CheckSignatureInterceptor checkSignatureInterceptor(){
        return new CheckSignatureInterceptor();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加验签处理拦截器，拦截所有请求，登录请求除外
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(checkSignatureInterceptor());
        //排除配置
        interceptorRegistration.excludePathPatterns("/sys/login.json");
        interceptorRegistration.excludePathPatterns("/charts/**");
        interceptorRegistration.excludePathPatterns("/css/**");
        interceptorRegistration.excludePathPatterns("/easyUi/**");
        interceptorRegistration.excludePathPatterns("/flashPlayer/**");
        interceptorRegistration.excludePathPatterns("/font/**");
        interceptorRegistration.excludePathPatterns("/images/**");
        interceptorRegistration.excludePathPatterns("/js/**");
        interceptorRegistration.excludePathPatterns("/pages/**");
        interceptorRegistration.excludePathPatterns("/plugin/**");
        interceptorRegistration.excludePathPatterns("/index.html");
        interceptorRegistration.excludePathPatterns("/show.html");
        //配置拦截策略
        interceptorRegistration.addPathPatterns("/**");
    }



}
