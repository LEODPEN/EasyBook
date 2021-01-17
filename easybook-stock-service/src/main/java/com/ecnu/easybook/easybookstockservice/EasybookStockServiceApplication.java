package com.ecnu.easybook.easybookstockservice;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.ecnu.easybook.easybookstockservice.mapper")
@EnableDubboConfiguration
@EnableCaching
public class EasybookStockServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasybookStockServiceApplication.class, args);
    }

}
