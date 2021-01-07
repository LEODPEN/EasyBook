package com.ecnu.easybook.easybookstockservice.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author LEO D PEN
 * @date 2021/1/4
 * @desc
 */
@Data
@Builder
public class EBResponse<T> implements Serializable {

    private Integer code;

    private String msg;

    private T data;

    public static EBResponse success(Object data) {
        return EBResponse.builder()
                .code(0)
                .msg("success")
                .data(data)
                .build();
    }

    public static EBResponse success(String msg, Object data) {
        return EBResponse.builder()
                .code(0)
                .msg(msg)
                .data(data)
                .build();
    }

    public static EBResponse error(String msg, Object data) {
        return EBResponse.builder()
                .code(-1)
                .msg(msg)
                .data(data)
                .build();
    }

}
