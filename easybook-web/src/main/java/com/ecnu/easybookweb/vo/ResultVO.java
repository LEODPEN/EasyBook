package com.ecnu.easybookweb.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 结果VO
 */
@Data
@AllArgsConstructor
public class ResultVO {

    private Integer code;

    private String msg;

    private Object data;

}
