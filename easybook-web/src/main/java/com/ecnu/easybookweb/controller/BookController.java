package com.ecnu.easybookweb.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.ecnu.easybook.easybookstockservice.DO.Book;
import com.ecnu.easybook.easybookstockservice.api.BookService;
import com.ecnu.easybook.easybookstockservice.response.PageInfo;
import com.ecnu.easybookweb.enums.ResultEnum;
import com.ecnu.easybookweb.exception.BookException;
import com.ecnu.easybookweb.form.BookForm;
import com.ecnu.easybookweb.oss.OssService;
import com.ecnu.easybookweb.util.ResultUtil;
import com.ecnu.easybookweb.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author pengfeng
 * @desc 通用图书方法
 **/
@RestController
@RequestMapping("/book")
public class BookController {

    @Reference
    private BookService bookService;

    @Resource(name = "OssService")
    private OssService ossService;


    @GetMapping("/stock")
    public ResultVO queryStock(@RequestParam("bid") Long bid) {
        return ResultUtil.success(bookService.queryStock(bid).getData());
    }

    @GetMapping("/allBooks")
    public ResultVO allBooks(@RequestParam(name = "page", defaultValue = "1")Integer page,
                             @RequestParam(name = "size", defaultValue = "20")Integer size) {
        return ResultUtil.success((PageInfo<Book>)(bookService.loadAll(page-1, size).getData()));
    }

    @GetMapping("/detail")
    public ResultVO detail(@RequestParam(value = "bid") Long bid) {
        return ResultUtil.success(bookService.findCertainBook(bid).getData());
    }

    @GetMapping("/newest")
    public ResultVO newest() {
        return ResultUtil.success(bookService.loadNewest().getData());
    }

    @GetMapping("/searchByName")
    public ResultVO searchByName(@RequestParam(name = "name")String name) {
        return ResultUtil.success(bookService.findCertainBooks(name).getData());
    }

    @GetMapping("/searchByKeyword")
    public ResultVO searchByKeyword(@RequestParam(name = "keyword") @NotNull String keyword) {
        return ResultUtil.success(bookService.fuzzySearchBooksByKeywords(keyword).getData());
    }

    @PostMapping("/add")
    public ResultVO addOneBook(@RequestBody @NotNull BookForm bookForm) {
        Book book = new Book();
        BeanUtils.copyProperties(bookForm, book);
        book.setUpdateTime(LocalDateTime.now());
        return ResultUtil.success(bookService.addOneBook(book).getData());
    }

    @PostMapping("/update")
    public ResultVO updateOneBook(@RequestBody @NotNull Book book) {
        if (book.getId() == null) return ResultUtil.error();
        book.setUpdateTime(LocalDateTime.now());
        return ResultUtil.success(bookService.updateOneBook(book).getData());
    }

    @PostMapping("/addPic")
    public ResultVO uploadPic(@RequestParam(value = "file", required = true) MultipartFile file) throws IOException {
        String res = null;
        String[] suffixNames = {".jpg", ".jpeg", ".png", ".JPG", ".JPEG", ".PNG"};
        List<String> suffixNamesList = Arrays.asList(suffixNames);
        // 获取图片的文件名
        String fileName = file.getOriginalFilename();
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex < 0) {
            dotIndex = fileName.length();
        }
        //通过判断后缀判断
        String suffixName = fileName.substring(dotIndex);
        //根据文件头名判断文件实际类型
        boolean isPic = suffixNamesList.contains(suffixName);
        if (isPic) {
            byte[] bFile = file.getBytes();
            res = ossService.storeFile(bFile);
        } else {
            throw new BookException(ResultEnum.PIC_FILE_ERROR);
        }
        return ResultUtil.success(res);
    }
}
