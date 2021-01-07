package com.ecnu.easybookweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author LEO D PEN
 * @date 2021/1/2
 * @desc 凃简单我直接当VO用了，就隐藏下密码啥的吧
 */
@Data
@AllArgsConstructor
public class UserDTO implements Serializable {

    private Long stuId;

    private String nickName;

    private Integer dealCnt;

}
