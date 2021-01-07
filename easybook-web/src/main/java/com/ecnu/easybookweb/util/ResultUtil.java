package com.ecnu.easybookweb.util;

import com.ecnu.easybookweb.vo.ResultVO;

/**
 * @author LEO D PEN
 * @date 2021/1/3
 * @desc 组装result工具类
 */

public class ResultUtil {

    public static ResultVO success(Object data) {
        return new ResultVO(0, "成功", data);
    }

    public static ResultVO success() {
        return new ResultVO(0, "成功",  null);
    }

    public static ResultVO error() {
        return new ResultVO(-1, "失败", null);
    }

    public static ResultVO error(String msg) {
        return new ResultVO(-1, msg, null);
    }

    public static ResultVO error(Integer code, String msg) {
        return new ResultVO(code, msg, null);
    }


}
