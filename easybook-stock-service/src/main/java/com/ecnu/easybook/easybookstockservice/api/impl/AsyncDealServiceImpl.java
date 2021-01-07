package com.ecnu.easybook.easybookstockservice.api.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.ecnu.easybook.easybookstockservice.DO.Deal;
import com.ecnu.easybook.easybookstockservice.api.AsyncDealService;
import com.ecnu.easybook.easybookstockservice.cacheservice.BookStockCacheJobService;
import com.ecnu.easybook.easybookstockservice.cacheservice.RedisLockService;
import com.ecnu.easybook.easybookstockservice.config.ConstantConfig;
import com.ecnu.easybook.easybookstockservice.enums.DealStatus;
import com.ecnu.easybook.easybookstockservice.mapper.BookMapper;
import com.ecnu.easybook.easybookstockservice.mapper.DealMapper;
import com.ecnu.easybook.easybookstockservice.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author LEO D PEN
 * @date 2021/1/7
 * @desc
 */

@Service(interfaceClass = AsyncDealService.class, async = true, timeout = 5000)
@Component
public class AsyncDealServiceImpl implements AsyncDealService{

    private final DealMapper dealRepository;

    private final StockMapper stockRepository;

    @Resource(name = "RedisLockService")
    private RedisLockService redisLockService;

    @Resource(name = "BookStockCacheJobService")
    private BookStockCacheJobService stockCacheService;

    @Autowired
    public AsyncDealServiceImpl(DealMapper dealMapper, BookMapper bookMapper, StockMapper stockMapper) {
        this.dealRepository = dealMapper;
        this.stockRepository = stockMapper;
    }

    @Override
    public void makeOneDeal(Deal deal) {
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
}
