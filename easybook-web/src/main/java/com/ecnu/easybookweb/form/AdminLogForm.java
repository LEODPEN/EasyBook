package com.ecnu.easybookweb.form;

import lombok.Data;

import java.io.Serializable;

/**
 * @author LEO D PEN
 * @date 2021/1/7
 * @desc
 */
@Data
public class AdminLogForm implements Serializable {

    private static final long serialVersionUID = -6774602196721548866L;

    private String name;

    private String pwd;
}
