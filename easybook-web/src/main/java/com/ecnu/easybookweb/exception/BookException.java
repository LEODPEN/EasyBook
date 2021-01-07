package com.ecnu.easybookweb.exception;

import com.ecnu.easybookweb.enums.ResultEnum;
import lombok.Getter;

/**
 * @author LEO D PEN
 * @date 2021/1/3
 * @desc
 */
@Getter
public class BookException extends RuntimeException{

    private Integer code;


    public BookException(ResultEnum resultEnums){
        super(resultEnums.getMsg());
        this.code=resultEnums.getCode();
    }

    public BookException(String msg, Integer code){
        super(msg);
        this.code=code;
    }
}
