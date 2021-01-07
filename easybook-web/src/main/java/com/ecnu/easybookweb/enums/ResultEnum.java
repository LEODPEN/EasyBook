package com.ecnu.easybookweb.enums;

import lombok.Getter;

/**
 * @author LEO D PEN
 * @date 2021/1/3
 * @desc
 */

@Getter
public enum ResultEnum {

    SERVER_TIMEOUT(10111, "服务调用超时"),
    OSS_WRITE_FILE_ERROR(10112, "OSS写文件错误"),
    OSS_CREATE_FILE_ERROR(10113, "OSS创建文件错误"),
    PIC_FILE_ERROR(10114, "非图片文件"),
    NO_SUCH_USER(10115, "无此用户"),
    ;


    Integer code;

    String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
