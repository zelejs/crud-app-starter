package com.jfeat;

import com.jfeat.jar.dep.properties.JarDeployProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    static Logger logger = LoggerFactory.getLogger(AmApplication.class);

    /**
     * 增加swagger的支持
     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//
//        // dependency-ui.html
//        registry.addResourceHandler("dependency-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/dependency-ui/**").addResourceLocations("classpath:/META-INF/resources/dependency-ui/");
//    }

    public static void main(String[] args) {
        logger.info("dev-dependency app run success !");
        SpringApplication.run(AmApplication.class, args);
    }
}

