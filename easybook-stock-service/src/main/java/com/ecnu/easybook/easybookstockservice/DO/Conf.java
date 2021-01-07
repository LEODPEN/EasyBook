package com.ecnu.easybook.easybookstockservice.DO;

import lombok.Data;

import java.io.Serializable;

/**
 * @desc 配置文件，做分布式配置用
 */
@Data
public class Conf implements Serializable {


    private static final long serialVersionUID = -4779334566021190319L;

    private Long id;

    /**
     * 是否处于生效状态
     */
    private boolean isSelected;

    /**
     * 繁忙时段的时期，相当于便于管理员运营展示使用 （平常期、特定节假日、周末等等）
     */
    private String desc;

    /**
     * 繁忙时段——eg: 14:00:00-16:00:00 【时分秒，中间无空格】
     */
    private String busyPeriod;
}
