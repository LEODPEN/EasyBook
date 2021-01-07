package com.ecnu.easybookweb.exception;

import com.ecnu.easybookweb.util.ResultUtil;
import com.ecnu.easybookweb.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author LEO D PEN
 * @date 2021/1/3
 * @desc
 */

@ControllerAdvice
@Slf4j
public class BookExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    public ResultVO exception(Exception e) {

        log.error(e.getMessage());
        if (e instanceof BookException) {
            return ResultUtil.error(((BookException) e).getCode(), e.getMessage());
        }

        return ResultUtil.error();
    }


}
