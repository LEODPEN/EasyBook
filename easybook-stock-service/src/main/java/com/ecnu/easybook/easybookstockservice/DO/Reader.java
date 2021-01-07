package com.ecnu.easybook.easybookstockservice.DO;

import lombok.Data;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @desc 读者 or 借阅者基本信息表
 */
@Data
public class Reader implements Serializable {

    private static final long serialVersionUID = 7817628765627859937L;

    private Long id;

    // unique
    private Long stuId;

    private String nickname;

    @Size(min = 6, max = 20, message = "密码长度为6-20位")
    private String password;

    private LocalDate registerDate;

    private LocalDate lastLoginDate;

}
