package com.ecnu.easybookweb.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author LEO D PEN
 * @date 2021/1/7
 * @desc
 */
@Data
public class ConfForm implements Serializable {

    /**
     * 繁忙时段的时期，相当于便于管理员运营展示使用 （平常期、特定节假日、周末等等）
     */
    private String desc;

    /**
     * 繁忙时段——eg: 14:00:00-16:00:00 【时分秒，中间无空格】
     */
    @NotBlank
    private String busyPeriod;

}
