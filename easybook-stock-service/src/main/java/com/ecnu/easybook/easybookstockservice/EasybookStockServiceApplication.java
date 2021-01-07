package com.ecnu.easybook.easybookstockservice;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ecnu.easybook.easybookstockservice.mapper")
@EnableDubboConfiguration
public class EasybookStockServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasybookStockServiceApplication.class, args);
    }

}
