package com.ecnu.easybook.easybookstockservice.response;

import com.ecnu.easybook.easybookstockservice.DO.Book;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author LEO D PEN
 * @date 2021/1/6
 * @desc
 */
@Data
public class PageInfo<T> implements Serializable {


    private static final long serialVersionUID = 2425496376268932821L;

    /**
     * 不同于pageIdx，这里是直接从0开始
     */
    private Integer pageIdx;

    /**
     * 默认20
     */
    private Integer pageSize;

    private Integer curSize;

    private Integer total;

    private List<T> data;

    public PageInfo(Integer pageIdx, Integer pageSize) {
        this.pageIdx = pageIdx;
        this.pageSize = pageSize;
    }
}
