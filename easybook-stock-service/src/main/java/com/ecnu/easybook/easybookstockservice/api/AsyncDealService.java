package com.ecnu.easybook.easybookstockservice.api;

import com.ecnu.easybook.easybookstockservice.DO.Deal;

/**
 * @author LEO D PEN
 * @date 2021/1/7
 * @desc 异步下单专用，如果有其他也需要异步的也可以加进来
 */
public interface AsyncDealService {

    void makeOneDeal(Deal deal);

}
