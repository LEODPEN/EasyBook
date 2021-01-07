package com.ecnu.easybook.easybookstockservice.mapper;

import com.ecnu.easybook.easybookstockservice.DO.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BookMapper {

    Book getOne(Long id);

    int getCount();

    // 不用插件了，直接写SQL来分页
    List<Book> loadAllBooksPageable(@Param("page") int page, @Param("size") int size);

    List<Book> loadAllBooks();

    List<Book> searchByName1(@Param("bookName") String bookName);

    // 模糊查找
    List<Book> searchByName2(@Param("bookName") String bookName);

    void insert(Book book);

    void update(Book book);

    void delete(Long id);

    // 默认10个
    // List<Book> loadMostBorrowed(@Param("size") int size);

    List<Book> loadNewest(@Param("size") int size);
}
