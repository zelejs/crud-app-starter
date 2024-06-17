package com.jfeat;

import com.jfeat.jar.dep.properties.JarDeployProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * SpringBoot Application
 * @author zxchengb
 */
@SpringBootApplication
@EnableSwagger2
public class AmApplication extends WebMvcConfigurerAdapter {
    static Logger log = LoggerFactory.getLogger(AmApplication.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // swagger-ui.html
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

        // dependency-ui.html
        registry.addResourceHandler("dependency-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/dependency-ui/**").addResourceLocations("classpath:/META-INF/resources/dependency-ui/");
    }

    public static void main(String[] args) {
        SpringApplication.run(AmApplication.class, args);
        log.info("dev-dependency app run success !");
    }
}

