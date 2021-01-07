package com.ecnu.easybook.easybookstockservice.api.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.ecnu.easybook.easybookstockservice.DO.Book;
import com.ecnu.easybook.easybookstockservice.api.BookService;
import com.ecnu.easybook.easybookstockservice.cacheservice.BookStockCacheJobService;
import com.ecnu.easybook.easybookstockservice.config.ConstantConfig;
import com.ecnu.easybook.easybookstockservice.mapper.BookMapper;
import com.ecnu.easybook.easybookstockservice.response.EBResponse;
import com.ecnu.easybook.easybookstockservice.response.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author LEO D PEN
 * @date 2021/1/4
 * @desc 先搞完测一下吧，之后再来优化异常情况
 */
@Service(interfaceClass = BookService.class)
@Component
@Slf4j
public class BookServiceImpl implements BookService {


    private final BookMapper bookRepository;

    // 🐴 的用resource了免得看见黄线
    @Resource
    private RedisTemplate<String, Serializable> redisTemplate;

    @Resource(name = "BookStockCacheJobService")
    private BookStockCacheJobService stockCacheService;

    @Autowired
    public BookServiceImpl(BookMapper bookMapper) {
        this.bookRepository = bookMapper;
    }

    private static final Object lock1 = new Object();

    private static final Object lock2 = new Object();

    @Override
    public EBResponse<Book> findCertainBook(Long id) {
        try {
            // todo 先查本地缓存 【单独来一个可触发的服务来构建本地缓存】
            return EBResponse.success(bookRepository.getOne(id));
        }catch (Exception e) {
            // 打印log，日志还是需要打一下的。。。
            log.error("BookService -> findCertainBook id : {} :" + e.getMessage(), id);
        }
        return EBResponse.error("无图书", null);
    }

    @Override
    public EBResponse<List<Book>> findCertainBooks(String bookName) {
        return EBResponse.success(bookRepository.searchByName1(bookName));
    }

    @Override
    public EBResponse<List<Book>> fuzzySearchBooksByKeywords(String keywords) {
        return EBResponse.success(bookRepository.searchByName2(keywords));
    }

    @Override
    public EBResponse<Boolean> addOneBook(Book book) {
        try {
            if (book != null) {
                bookRepository.insert(book);
            }
        }catch (Exception e) {
            log.error("BookService -> addOneBook :" + e.getMessage());
            return EBResponse.success(Boolean.FALSE);
        }
        return EBResponse.success(Boolean.TRUE);
    }

    // 估计时效性一般，懒得更新本地缓存
    @Override
    public EBResponse<Boolean> updateOneBook(Book book) {
        try {
            if (book != null) {
                bookRepository.update(book);
            }
        }catch (Exception e) {
            log.error("BookService -> updateOneBook + id :{}:" + e.getMessage(), book.getId());
            return EBResponse.error("更新book信息错误", Boolean.FALSE);
        }
        return EBResponse.success(Boolean.TRUE);
    }

    @Override
    public EBResponse<Boolean> deleteOneBook(Long id) {
        try {
            bookRepository.delete(id);
            return EBResponse.success(Boolean.TRUE);
        }catch (Exception e) {
            log.error("BookService -> deleteOneBook :" + e.getMessage());
            return EBResponse.error("删除book信息错误", Boolean.FALSE);
        }
    }

    @Override
    public EBResponse<List<Book>> loadNewest() {
        List<Book> books;
        // 先从redis中拿
        if ((books = (List<Book>) redisTemplate.opsForValue().get(ConstantConfig.NEWEST_BOOKS)) == null || books.size() < 5) {
            books = new ArrayList<>();
            // 没有或者太少了 -> 单线程从db lao
            synchronized (lock1) {
                books.addAll(bookRepository.loadNewest(ConstantConfig.DEFAULT_BOOK_COMMEND_CACHE_SIZE));
                // 设置一天过期，防止定时构建job挂了
                // 双重check
                redisTemplate.opsForValue().setIfAbsent(ConstantConfig.NEWEST_BOOKS,
                        (Serializable) books,
                        24 * 3600,
                        TimeUnit.SECONDS);
            }
        }
        return EBResponse.success(books);
    }

    @Override
    public EBResponse<List<Book>> loadMostBorrowed() {
        return loadNewest();
    }

    @Override
    public EBResponse<PageInfo<Book>> loadAll(Integer pageIdx, Integer pageSize) {
        if (pageIdx < 0) pageIdx = 0;
        if (pageSize > 20) pageSize = 20;
        PageInfo<Book> pageInfo = new PageInfo<Book>(pageIdx, pageSize);

        List<Book> books = bookRepository.loadAllBooksPageable(pageIdx, pageSize);
        int total = bookRepository.getCount();

        pageInfo.setTotal(total);
        pageInfo.setData(books);
        pageInfo.setCurSize(books.size());
        return EBResponse.success(pageInfo);
    }

    @Override
    public EBResponse<List<Book>> loadAll() {
        return EBResponse.success(bookRepository.loadAllBooks());
    }

    @Override
    public EBResponse<Integer> queryStock(Long bid) {
        return EBResponse.success(stockCacheService.getStock(bid));
    }
}
