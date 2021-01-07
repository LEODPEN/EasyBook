package com.ecnu.easybookweb.form;

import lombok.Data;

import java.io.Serializable;

/**
 * @author LEO D PEN
 * @date 2021/1/7
 * @desc
 */
@Data
public class DealForm implements Serializable {

    private Long userId;

    private Long bookId;

    private Long stuId;

    private String username;

    private String bookName;
}
