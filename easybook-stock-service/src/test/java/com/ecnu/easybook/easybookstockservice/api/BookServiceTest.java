package com.ecnu.easybook.easybookstockservice.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author LEO D PEN
 * @date 2021/1/6
 * @desc
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class BookServiceTest {

    @Resource
    private BookService bookService;

    @Test
    void loadAll() {
        assertNotNull(bookService.loadAll());
    }
}
