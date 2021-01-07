package com.ecnu.easybook.easybookstockservice.mapper;

import com.ecnu.easybook.easybookstockservice.DO.Stock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author LEO D PEN
 * @date 2021/1/5
 * @desc
 */
@Mapper
@Repository
public interface StockMapper {

    void addOne(Stock stock);

    Stock getOne(Long id);

    Stock getOneByBID(Long bid);

    void updateOne(Stock stock);

    @Deprecated
    void updateByBID(@Param("bid") Long bid, @Param("cnt") Integer cnt);

    void decreaseStock(@Param("bid") Long bid);

    void increaseStock(@Param("bid") Long bid);

}
