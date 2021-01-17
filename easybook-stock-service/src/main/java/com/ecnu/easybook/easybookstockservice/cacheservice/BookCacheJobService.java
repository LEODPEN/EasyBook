package com.ecnu.easybook.easybookstockservice.cacheservice;

import com.ecnu.easybook.easybookstockservice.DO.Book;
import com.ecnu.easybook.easybookstockservice.mapper.BookMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author LEO D PEN
 * @date 2021/1/5
 * @desc 图书缓存的构建任务【本地ehCache，实时更新删除，固定个数LRU，
 * 一天自动删除timeToLiveSeconds:86400】
 */
@Component("BookCacheJobService")
@CacheConfig(cacheNames = "books")
@Slf4j
public class BookCacheJobService {

    @Resource
    private BookMapper bookMapper;

    @Cacheable(value = "books", key = "#id")
    public Book getById(Long id) {
        log.info("BookCacheJobService -> getById({})", id);
        return bookMapper.getOne(id);
    }

    @CacheEvict(value = "books", key = "#id")
    public void remove(Long id) {
        log.info("BookCacheJobService -> remove({})", id);
    }

    @CachePut(value = "books", key = "#book.id")
    public Book update(Book book) {
        log.info("BookCacheJobService -> update({})", book.getId());
        return book;
    }

}
