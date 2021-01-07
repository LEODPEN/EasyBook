package com.ecnu.easybookweb.form;

import lombok.Data;

import java.io.Serializable;

/**
 * @author LEO D PEN
 * @date 2021/1/7
 * @desc
 */
@Data
public class UserForm implements Serializable {

    private static final long serialVersionUID = -7060704720402802752L;

    private Long stuid;

    private String pwd;

}
