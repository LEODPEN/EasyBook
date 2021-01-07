package com.ecnu.easybook.easybookstockservice.config;

/**
 * @author LEO D PEN
 * @date 2021/1/5
 * @desc 一些全局的变量
 */
public interface ConstantConfig {

    /**
     * 非本地缓存
     */
    String NEWEST_BOOKS = "nb";

    String MOST_BORROWED = "mb";

    int DEFAULT_BOOK_COMMEND_CACHE_SIZE = 20;

    int DEFAULT_BOOK_CACHE_SIZE = 500; // 从deal日志表排序去重来筛选

    /**
     * 本地缓存
     */
    String CACHED_BOOKS = "cb";

    /**
     * 库存缓存前缀
     */
    String STOCK_LOCK_PREFIX = "stock_lock_";

    long DEFAULT_STOCK_LOCK_TRY_TIMES = 1500L;

    String STOCK_CACHE_PREFIX = "stock_";

    long STOCK_CACHE_DURATION = 18 * 3600L;

    /**
     * 配置缓存key
     */

    String CONF_KEY = "BUSY_P";


    /**
     * Kafka Topic [deal & stock]
     */
    String TOPIC = "deal";

    String TOPIC2 = "stock";

}
