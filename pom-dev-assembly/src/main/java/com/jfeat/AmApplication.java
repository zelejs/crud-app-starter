package com.jfeat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SpringBoot CG Test Application
 *
 * @author Admin
 * @Date 2017/5/21 12:06
 */
@SpringBootApplication
// @EnableSwagger2
// public class AmApplication extends WebMvcConfigurerAdapter {
public class AmApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmApplication.class, args);
    }
}
