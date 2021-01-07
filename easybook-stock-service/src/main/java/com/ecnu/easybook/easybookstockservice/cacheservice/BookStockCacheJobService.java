package com.ecnu.easybook.easybookstockservice.cacheservice;

import com.ecnu.easybook.easybookstockservice.DO.Stock;
import com.ecnu.easybook.easybookstockservice.config.ConstantConfig;
import com.ecnu.easybook.easybookstockservice.mapper.StockMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author LEO D PEN
 * @date 2021/1/5
 * @desc 图书库存缓存更新job
 */
@Component("BookStockCacheJobService")
public class BookStockCacheJobService {

    private static final Object lock = new Object();

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private StockMapper stockRepository;

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;


    /**
     * @param bid
     * @param oldV
     * @param newV
     * @desc 更新及写后更新用，常用【如果不一致，只可能实际比缓存多，交给mq去对比就行】
     * @return
     */
    public void updateStockCache(Long bid, int oldV, int newV) {
        if (setStockIfAbsent(bid, newV, -1)) {
            return;
        }
        if (oldV != getAndSetStock(bid, newV, oldV)) {
            // mq异步diff
            kafkaTemplate.send(ConstantConfig.TOPIC2, String.valueOf(bid));
        }
    }

    public Integer getStock(Long bid) {
        Integer stock_cnt;
        // 查缓存库存【redis】
        if ((stock_cnt = getStockCache(bid)) == null) {
            synchronized (lock) {
                // 没有则去查db
                Stock stock = stockRepository.getOneByBID(bid);
                if  (stock == null) return null;
                // 获得
                stock_cnt = stock.getCnt();
                // 立即反哺到缓存【check twice】
                setStockIfAbsent(bid, stock_cnt, 18);
            }
        }
        return stock_cnt;
    }

    private Integer getStockCache(Long bid) {
        String cnt = stringRedisTemplate.opsForValue().get(ConstantConfig.STOCK_CACHE_PREFIX + bid);
        if (!StringUtils.isEmpty(cnt)) {
            return Integer.parseInt(cnt);
        }
        return null;
    }

    /**
     * @param bid
     * @param stock
     * @param hours 小时数，如果hours为 null 或 -1 则设置过期时间 default
     * @desc 不存在则使用，一般极端情况才会出现这种情况【真不一致了还有mq来diff兜底嘛】
     * @return
     */
    public Boolean setStockIfAbsent(Long bid, Integer stock, Integer hours) {
        if (hours == null || hours < 0) {
            return stringRedisTemplate.opsForValue().setIfAbsent(ConstantConfig.STOCK_CACHE_PREFIX + bid,
                    String.valueOf(stock),
                    ConstantConfig.STOCK_CACHE_DURATION,
                    TimeUnit.SECONDS);
        }else {
            return stringRedisTemplate.opsForValue()
                    .setIfAbsent(ConstantConfig.STOCK_CACHE_PREFIX + bid,
                                 String.valueOf(stock),
                                 hours * 3600,
                                 TimeUnit.SECONDS);
        }
    }


    // 写后更新，延长时间，基本做到热缓存永不删除
    private int getAndSetStock(Long bid, Integer newStock, Integer oldStock) {

        String ov = stringRedisTemplate.opsForValue().getAndSet(ConstantConfig.STOCK_CACHE_PREFIX + bid, String.valueOf(newStock));
        stringRedisTemplate.opsForValue().getOperations().expire(
                ConstantConfig.STOCK_CACHE_PREFIX + bid,
                ConstantConfig.STOCK_CACHE_DURATION,
                TimeUnit.SECONDS);
        // 说明刚刚才过期或者被删除
        if (StringUtils.isEmpty(ov)) {
            return oldStock;
        }
        return Integer.parseInt(ov);
    }
}
