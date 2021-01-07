package com.ecnu.easybookweb.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author LEO D PEN
 * @date 2021/1/7
 * @desc 用户改信息专用
 */
@Data
public class UserInfoForm implements Serializable {

    @NotNull
    private Long uid;

    private String nickName;

    // 一切从简了

    private String originPwd;

    private String newPwd;

}
