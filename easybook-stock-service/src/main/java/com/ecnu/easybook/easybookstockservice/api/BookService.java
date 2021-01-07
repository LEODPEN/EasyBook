package com.ecnu.easybook.easybookstockservice.api;

import com.ecnu.easybook.easybookstockservice.DO.Book;
import com.ecnu.easybook.easybookstockservice.response.EBResponse;
import com.ecnu.easybook.easybookstockservice.response.PageInfo;

import java.util.List;

/**
 * @author LEO D PEN
 * @date 2021/1/3
 * @desc book服务对外接口
 */
public interface BookService {

    /**
     * @param id
     * @desc 使用id唯一查找【本地缓存 + db】
     * @return
     */
    EBResponse<Book> findCertainBook(Long id);

    /**
     * @param bookName
     * @desc 图书名字精确查找
     * @return 结果可能不止一个
     */
    EBResponse<List<Book>> findCertainBooks(String bookName);

    /**
     * @param keywords
     * @desc 关键词查找
     * @return
     */
    EBResponse<List<Book>> fuzzySearchBooksByKeywords(String keywords);

    /**
     * @param book
     * @desc 注意，oss服务在外部接口侧完成，到这里是正确的url
     */
    EBResponse<Boolean> addOneBook(Book book);

    EBResponse<Boolean> updateOneBook(Book book);

    EBResponse<Boolean> deleteOneBook(Long id);

    /**
     * @param pageIdx >= 0 因此page记得-1
     * @param pageSize default size应该为20
     * @return
     */
    EBResponse<PageInfo<Book>> loadAll(Integer pageIdx, Integer pageSize);

    EBResponse<List<Book>> loadAll();

    /**
     * @desc 最多借阅：暂定10本
     * @deprecated 直接删了这个方法没必要
     * @return
     */
    EBResponse<List<Book>> loadMostBorrowed();

    /**
     * @version 0.2.0
     * @desc 最新上架：暂定20本
     * @return
     */
    EBResponse<List<Book>> loadNewest();

    /**
     * @param bid
     * @desc 查询库存，缓存 + DB
     * @return
     */
    EBResponse<Integer> queryStock(Long bid);
}
