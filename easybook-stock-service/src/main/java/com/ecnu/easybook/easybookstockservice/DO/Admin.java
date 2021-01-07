package com.ecnu.easybook.easybookstockservice.DO;

import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class Admin implements Serializable {

    private static final long serialVersionUID = 3237151243586514386L;

    private Long id;

    // unique
    private String name;

    @Size(min = 6, max = 20, message = "密码长度为6-20位")
    private String password;

}
