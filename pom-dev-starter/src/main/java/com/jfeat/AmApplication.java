package com.jfeat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class AmApplication extends WebMvcConfigurerAdapter  {
    protected final static Logger logger = LoggerFactory.getLogger(AmApplication.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        // registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        // registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/views/");
    }

    public static void main(String[] args) {
        SpringApplication.run(AmApplication.class, args);
        logger.info("dev-starter run success!");
    }
}
