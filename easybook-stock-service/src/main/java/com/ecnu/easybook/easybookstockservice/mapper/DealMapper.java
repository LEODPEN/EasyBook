package com.ecnu.easybook.easybookstockservice.mapper;

import com.ecnu.easybook.easybookstockservice.DO.Deal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DealMapper {

    Deal getOne(Long id);

    List<Deal> loadAllDeals(@Param("page") int page, @Param("size") int size);

    List<Deal> queryByBID(@Param("BID") Long bookID);

    List<Deal> queryBySTUID(@Param("STUID") Long stuId);

    List<Deal> queryByUID(@Param("UID") Long uid);

    List<Deal> queryByUIDPageable(@Param("UID") Long uid, @Param("page") int page, @Param("size") int size);

    int getCountByUID(@Param("UID") Long uid);

    List<Deal> queryByUIDAndBID(@Param("UID") Long uid, @Param("BID") Long bookId);

    List<Deal> queryByBIDAndStatus(@Param("BID") Long bookId, @Param("status") int status);

    List<Deal> queryByUIDAndStatus(@Param("UID") Long uid, @Param("status") int status);

    List<Deal> queryByUIDAndStatusPageable(@Param("UID") Long uid, @Param("status") int status,
                                   @Param("page") int page, @Param("size") int size);

    int getCountByUIDAndStatus(@Param("UID")Long uid, @Param("status") int status);

    void makeDeal(Deal deal);

    void updateStatus(@Param("id") Long id ,@Param("status") int status);

    void delete(Long id);
}
