package com.ecnu.easybook.easybookstockservice.DO;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @desc 图书基本信息
 */
@Data
public class Book implements Serializable {

    private static final long serialVersionUID = 8661710192238013439L;

    private Long id;

    private String bookName;

    private String desc;

    // 图片链接【存储】
    private String picUrl;

    // 总库存
    private int totalCnt;

    // 单价
    private String price;

    // 图书存放地点
    private String place;

    private LocalDateTime updateTime;

}
