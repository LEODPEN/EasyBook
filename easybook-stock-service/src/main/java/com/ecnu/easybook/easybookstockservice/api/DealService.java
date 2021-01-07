package com.ecnu.easybook.easybookstockservice.api;

import com.ecnu.easybook.easybookstockservice.DO.Deal;
import com.ecnu.easybook.easybookstockservice.enums.DealStatus;
import com.ecnu.easybook.easybookstockservice.response.EBResponse;
import com.ecnu.easybook.easybookstockservice.response.PageInfo;

import java.util.List;

/**
 * @author LEO D PEN
 * @date 2021/1/3
 * @desc 核心服务；直接透传
 */
public interface DealService {

            /********** 查询类服务 **********/

    /**
     * @param id
     * @desc 根据id精确查找，组装时候可能用，一般不常用
     * @return
     */
    EBResponse<Deal> findCertainDeal(Long id);

    /**
     * @param uid
     * @desc 某人的全部订单信息，直接透传
     * @return
     */
    EBResponse<List<Deal>> findDealsByUID(Long uid);

    /**
     * @param uid
     * @param pageIdx
     * @param pageSize
     * @desc 某人的全部订单信息，直接透传
     * @return
     */
    EBResponse<PageInfo<Deal>> findDealsByUID(Long uid, Integer pageIdx, Integer pageSize);

    EBResponse<Integer> getDealCountByUID(Long uid);

    /**
     * @param bid bookId
     * @desc 某书的的全部订单信息，直接透传
     * @return
     */
    EBResponse<List<Deal>> findDealsByBID(Long bid);

    /**
     * @param uid
     * @param status
     * @see com.ecnu.easybook.easybookstockservice.enums.DealStatus
     * @desc 某人的状态订单(失败亦有记录)
     * @return
     */
    EBResponse<List<Deal>> findDealsByUIDAndStatus(Long uid, DealStatus status);

    /**
     * @param uid
     * @param status
     * @param pageIdx
     * @param pageSize
     * @see com.ecnu.easybook.easybookstockservice.enums.DealStatus
     * @desc 某人的状态订单(失败亦有记录)
     * @return
     */
    EBResponse<PageInfo<Deal>> findDealsByUIDAndStatus(Long uid, DealStatus status, Integer pageIdx, Integer pageSize);

    /**
     * @param bid
     * @param status
     * @see com.ecnu.easybook.easybookstockservice.enums.DealStatus
     * @desc 某书的状态订单
     * @return
     */
    EBResponse<List<Deal>> findDealsByBIDAndStatus(Long bid, DealStatus status);

    /**
     * @param uid
     * @param bid
     * @desc 查看某人借阅某一本书的情况
     * @return
     */
    EBResponse<List<Deal>> findDealByUIDAndBID(Long uid, Long bid);

            /********** 更新类服务 **********/

    /**
     * @param deal
     * @desc 核心下单服务，异步调用，无即时返回
     * @see com.ecnu.easybook.easybookstockservice.DO.Deal
     */
    @Deprecated
    void makeOneDealNormal(Deal deal);

    EBResponse<Boolean> finishOneDeal(Long id);

    /**
     * @param deal
     * @desc 核心下单服务，高峰期调用
     */
    @Deprecated
    void makeOneDealAbNormal(Deal deal);

    /**
     * @param id 日志记录的id
     * @desc 提供删除接口，一般被认为是删除借阅失败的记录（给出一定的可扩展性）
     */
    EBResponse<Boolean> deleteOneDeal(Long id);

    /**
     * @param id
     * @param status 期望被改成的状态
     * @see com.ecnu.easybook.easybookstockservice.enums.DealStatus
     * @desc 更改订单状态
     */
    EBResponse<Boolean> updateOneDealStatus(Long id, DealStatus status);

}
