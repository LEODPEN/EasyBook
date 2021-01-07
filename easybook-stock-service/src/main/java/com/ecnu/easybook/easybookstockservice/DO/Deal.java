package com.ecnu.easybook.easybookstockservice.DO;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @desc 借书订单表
 */
@Data
public class Deal implements Serializable {

    private static final long serialVersionUID = -3819792029494578524L;

    private Long id;

    private Long userId;

    private Long bookId;

    /**** 一定的信息冗余 ****/

    private Long stuId;

    private String username;

    private String bookName;

    private int status; // 三个or，用枚举做

    // 以下信息可以用来展示借书时间
    private LocalDateTime createdTime;

    private LocalDateTime updateTime;
}
