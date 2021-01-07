package com.ecnu.easybookweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ecnu.easybook.easybookstockservice.api.BookService;
import com.ecnu.easybook.easybookstockservice.response.EBResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author LEO D PEN
 * @date 2021/1/6
 * @desc dubbo 调用测试
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class BookTest {

    @Reference
    private BookService bookService;

    @Test
    public void testBooks() {
        EBResponse response = bookService.loadAll();
        assertNotNull(response);
        System.out.println(response.getCode());
        System.out.println(response.getData().toString());
    }

}
