package com.ecnu.easybook.easybookstockservice.cacheservice;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import static com.ecnu.easybook.easybookstockservice.config.ConstantConfig.CONF_KEY;

/**
 * @author LEO D PEN
 * @date 2021/1/6
 * @desc 配置缓存
 */
@Component("ConfCacheService")
@RequiredArgsConstructor
public class ConfCacheService {

    private final StringRedisTemplate stringRedisTemplate;

    // 只缓存时间busyPeriod
    // 不设置过期时间
    // 可以考虑是不是要定期diff【暂时懒得搞】
    public void setConf(String v) {
        stringRedisTemplate.opsForValue().set(CONF_KEY, v);
    }

    public void deleteConf() {
        stringRedisTemplate.opsForValue().getOperations().delete(CONF_KEY);
    }

}
