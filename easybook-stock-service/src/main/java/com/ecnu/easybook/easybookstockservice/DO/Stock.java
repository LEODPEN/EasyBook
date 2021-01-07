package com.ecnu.easybook.easybookstockservice.DO;

import lombok.Data;

import java.io.Serializable;

/**
 * @author LEO D PEN
 * @date 2021/1/5
 * @desc 库存表
 */

@Data
public class Stock implements Serializable {

    private static final long serialVersionUID = -2264380373909138581L;

    private Long id;

    private Long bid;

    /**
     * 当前数量
     */
    private Integer cnt;
}
