package com.ecnu.easybook.easybookstockservice.enums;

/**
 * @desc 借书订单状态
 * @desc 可扩展，比如逾期等等
 */
public enum DealStatus {

    PROCESS(1, "进行中"),
    FAIL(2, "借阅失败"),
    DONE(3, "归还成功"),
    ;

    int statusCode;
    String statusDesc;

    DealStatus(int statusCode, String statusDesc) {
        this.statusCode = statusCode;
        this.statusDesc = statusDesc;
    }

    public int getCode() {
        return statusCode;
    }

    public String getDesc() {
        return statusDesc;
    }

}
