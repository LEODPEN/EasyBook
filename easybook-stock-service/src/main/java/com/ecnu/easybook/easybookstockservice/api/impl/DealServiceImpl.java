package com.ecnu.easybook.easybookstockservice.api.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.ecnu.easybook.easybookstockservice.DO.Deal;
import com.ecnu.easybook.easybookstockservice.DO.Stock;
import com.ecnu.easybook.easybookstockservice.api.DealService;
import com.ecnu.easybook.easybookstockservice.cacheservice.RedisLockService;
import com.ecnu.easybook.easybookstockservice.config.ConstantConfig;
import com.ecnu.easybook.easybookstockservice.enums.DealStatus;
import com.ecnu.easybook.easybookstockservice.cacheservice.BookStockCacheJobService;
import com.ecnu.easybook.easybookstockservice.mapper.BookMapper;
import com.ecnu.easybook.easybookstockservice.mapper.DealMapper;
import com.ecnu.easybook.easybookstockservice.mapper.StockMapper;
import com.ecnu.easybook.easybookstockservice.response.EBResponse;
import com.ecnu.easybook.easybookstockservice.response.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author LEO D PEN
 * @date 2021/1/4
 * @desc 先完善，有异常之后再解决
 */
@Service(interfaceClass = DealService.class)
@Component("DealService")
@Slf4j
public class DealServiceImpl implements DealService {

    private final DealMapper dealRepository;

    private final StockMapper stockRepository;

    @Resource(name = "RedisLockService")
    private RedisLockService redisLockService;

    @Resource(name = "BookStockCacheJobService")
    private BookStockCacheJobService stockCacheService;

    @Autowired
    public DealServiceImpl(DealMapper dealMapper, BookMapper bookMapper, StockMapper stockMapper) {
        this.dealRepository = dealMapper;
        this.stockRepository = stockMapper;
    }

    @Override
    public EBResponse<Deal> findCertainDeal(Long id) {
        try {
            return EBResponse.success(dealRepository.getOne(id));
        }catch (Exception e) {
            log.error("DealService -> findCertainDeal id : {} :" + e.getMessage(), id);
        }
        return EBResponse.error("无订单记录", null);
    }

    @Override
    public EBResponse<List<Deal>> findDealsByUID(Long uid) {
        return EBResponse.success(dealRepository.queryByUID(uid));
    }

    @Override
    public EBResponse<Integer> getDealCountByUID(Long uid) {
        return EBResponse.success(dealRepository.getCountByUID(uid));
    }

    @Override
    public EBResponse<PageInfo<Deal>> findDealsByUID(Long uid, Integer pageIdx, Integer pageSize) {
        if (pageIdx < 0) pageIdx = 0;
        if (pageSize > 20) pageSize = 20;
        PageInfo<Deal> pageInfo = new PageInfo<>(pageIdx, pageSize);

        List<Deal> deals = dealRepository.queryByUIDPageable(uid, pageIdx, pageSize);
        int count = dealRepository.getCountByUID(uid);

        pageInfo.setTotal(count);
        pageInfo.setData(deals);
        pageInfo.setCurSize(deals.size());
        return EBResponse.success(pageInfo);
    }

    @Override
    public EBResponse<List<Deal>> findDealsByBID(Long bid) {
        return EBResponse.success(dealRepository.queryByBID(bid));
    }

    @Override
    public EBResponse<List<Deal>> findDealsByUIDAndStatus(Long uid, DealStatus status) {
        return EBResponse.success(dealRepository.queryByUIDAndStatus(uid, status.getCode()));
    }

    @Override
    public EBResponse<PageInfo<Deal>> findDealsByUIDAndStatus(Long uid, DealStatus status, Integer pageIdx, Integer pageSize) {
        if (pageIdx < 0) pageIdx = 0;
        if (pageSize > 20) pageSize = 20;
        PageInfo<Deal> pageInfo = new PageInfo<>(pageIdx, pageSize);

        List<Deal> deals = dealRepository.queryByUIDAndStatusPageable(uid, status.getCode(), pageIdx, pageSize);
        int total = dealRepository.getCountByUIDAndStatus(uid, status.getCode());
        pageInfo.setTotal(total);
        pageInfo.setData(deals);
        pageInfo.setCurSize(deals.size());
        return EBResponse.success(pageInfo);
    }

    @Override
    public EBResponse<List<Deal>> findDealsByBIDAndStatus(Long bid, DealStatus status) {
        return EBResponse.success(dealRepository.queryByBIDAndStatus(bid, status.getCode()));
    }

    @Override
    public EBResponse<List<Deal>> findDealByUIDAndBID(Long uid, Long bid) {
        return EBResponse.success(dealRepository.queryByUIDAndBID(uid, bid));
    }

    /**
     * @param deal
     * @desc 核心方法 异步调用 平常期
     */
    @Override
    @Transactional
    public void makeOneDealNormal(Deal deal) {
        if (deal == null) return;
        Long bid = deal.getBookId();
        Integer stock_cnt = stockCacheService.getStock(bid);
        // 不存在书的信息
        if (stock_cnt == null) return;
        // 库存不足够
        if (stock_cnt <= 0) {
            deal.setStatus(DealStatus.FAIL.getCode());
        }else {
            // 加锁
            String key = ConstantConfig.STOCK_LOCK_PREFIX + bid;
            boolean isLock = redisLockService.tryLock(key, ConstantConfig.DEFAULT_STOCK_LOCK_TRY_TIMES);
            // 再取库存
            stock_cnt = stockCacheService.getStock(bid);
            if (!isLock || stock_cnt <= 0) {
                deal.setStatus(DealStatus.FAIL.getCode());
            }else {
                stockRepository.decreaseStock(bid);
                deal.setStatus(DealStatus.PROCESS.getCode());
                // 缓存写后更新
                stockCacheService.updateStockCache(bid, stock_cnt, stock_cnt - 1);
                redisLockService.unlock(key);
            }
        }
        // 订单信息入库
        dealRepository.makeDeal(deal);
    }

    @Override
    // 只需要改库存，不需要改缓存
    public EBResponse<Boolean> finishOneDeal(Long id) {
        Deal deal = dealRepository.getOne(id);
        if (deal == null || deal.getStatus() != DealStatus.PROCESS.getCode()) {
            return EBResponse.error("订单信息错误", Boolean.FALSE);
        }
        dealRepository.updateStatus(id, DealStatus.DONE.getCode());
        stockRepository.increaseStock(deal.getBookId());
        return EBResponse.success(Boolean.TRUE);
    }

    /**
     * @param deal
     * @desc 核心方法 异步调用 高峰期
     */
    @Override
    public void makeOneDealAbNormal(Deal deal) {

    }

    @Override
    public EBResponse<Boolean> deleteOneDeal(Long id) {
        try {
            dealRepository.delete(id);
            return EBResponse.success(Boolean.TRUE);
        }catch (Exception e) {
            // 估计没有这个单
            return EBResponse.error(e.getMessage(), Boolean.FALSE);
        }
    }

    // 默认有这个单
    @Override
    public EBResponse<Boolean> updateOneDealStatus(Long id, DealStatus status) {
        Deal deal;
        if ((deal = dealRepository.getOne(id)) == null) {
            return EBResponse.error("错误单号", Boolean.FALSE);
        }
        if (deal.getStatus() != status.getCode()) {
            dealRepository.updateStatus(id, status.getCode());
        }
        return EBResponse.success(Boolean.TRUE);
    }

}
