package com.jfeat;

import com.jfeat.crud.plus.META;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * SpringBoot方式启动类
 *
 * @author Admin
 * @Date 2017/5/21 12:06
 */
@SpringBootApplication
@EnableScheduling
public class AmApplication{

    private final static Logger logger = LoggerFactory.getLogger(AmApplication.class);

    public static void main(String[] args) {
        META.enabledImage(true);
        META.enabledTag(true);
        META.enabledEav(true);
        SpringApplication.run(AmApplication.class, args);
        logger.info("Test SaaS is success!");
    }
}
