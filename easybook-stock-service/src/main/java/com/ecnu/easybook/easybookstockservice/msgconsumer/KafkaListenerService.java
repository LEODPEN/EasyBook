package com.ecnu.easybook.easybookstockservice.msgconsumer;

import com.ecnu.easybook.easybookstockservice.DO.Deal;
import com.ecnu.easybook.easybookstockservice.DO.Stock;
import com.ecnu.easybook.easybookstockservice.api.DealService;
import com.ecnu.easybook.easybookstockservice.config.ConstantConfig;
import com.ecnu.easybook.easybookstockservice.mapper.StockMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author LEO D PEN
 * @date 2021/1/7
 * @desc
 */
@Component("KafkaListenerService")
@Slf4j
public class KafkaListenerService {

    @Resource(name = "DealService")
    private DealService dealService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private StockMapper stockMapper;

    @KafkaListener(topics = {"test"})
    public void test(Deal data) throws InterruptedException {
        System.out.println(data);
        Thread.sleep(100000);
    }

    @KafkaListener(topics = {ConstantConfig.TOPIC})
    public void listenFromDealTopic(Deal data) {
        dealService.makeOneDealNormal(data);
        log.info("busy-period -> [KafkaListenerService -> listenFromDealTopic]: id = {} 创建借阅成功", data.getId());
    }

    @KafkaListener(topics = {ConstantConfig.TOPIC2})
    public void doDiffForStockCache(String bid) {
        String cur_cached_value = stringRedisTemplate.opsForValue().
                get(ConstantConfig.STOCK_CACHE_PREFIX + bid);
        // bei shan le jiu bu guan
        if (StringUtils.isEmpty(cur_cached_value)) return;
        Stock stock = stockMapper.getOneByBID(Long.parseLong(bid));
        String true_value = stock.getCnt().toString();
        if (!true_value.equals(cur_cached_value)) {
            stringRedisTemplate.opsForValue()
                    .set(ConstantConfig.STOCK_CACHE_PREFIX + bid,
                            true_value,
                            ConstantConfig.STOCK_CACHE_DURATION,
                            TimeUnit.SECONDS);
        }
    }
}
