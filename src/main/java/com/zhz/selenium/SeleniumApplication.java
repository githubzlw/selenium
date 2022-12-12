package com.zhz.selenium;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.zhz.selenium.mapper")
@EnableScheduling
public class SeleniumApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeleniumApplication.class, args);
//        SpringApplication springApplication = new SpringApplication(SeleniumApplication.class);
//        springApplication.setBannerMode(Banner.Mode.OFF);
//        springApplication.run();
    }

}
