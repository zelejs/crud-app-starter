package com.jfeat;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * SpringBoot Application
 * @author zxchengb
 */
@SpringBootApplication
public class AmApplication extends WebMvcConfigurerAdapter {

    /**
     * 增加swagger的支持
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

        // dependency-ui.html
        registry.addResourceHandler("connection-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/connection-ui/**").addResourceLocations("classpath:/META-INF/resources/connection-ui/");
    }

    public static void main(String[] args) {
        SpringApplication.run(AmApplication.class, args);
    }
}
