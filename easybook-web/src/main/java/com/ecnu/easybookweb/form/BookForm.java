package com.ecnu.easybookweb.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author LEO D PEN
 * @date 2021/1/7
 * @desc 添加图书表单
 */
@Data
public class BookForm implements Serializable {

    @NotBlank(message = "书名不能为空")
    private String bookName;

    private String desc;

    private String picUrl;

    @Min(value = 0, message = "数量应该大于0")
    private Integer totalCnt;

    private String price;

    private String place;
}
