package com.jfeat.jar.dep.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

// @Configuration
public class DepUiMvcConfiguration extends WebMvcConfigurationSupport {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("dependency-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/dependency-ui/**").addResourceLocations("classpath:/META-INF/resources/dependency-ui/");
    }
}
