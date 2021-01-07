package com.ecnu.easybookweb;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubboConfiguration
public class EasybookWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasybookWebApplication.class, args);
    }

}
