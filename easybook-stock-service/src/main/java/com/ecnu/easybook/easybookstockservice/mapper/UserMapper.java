package com.ecnu.easybook.easybookstockservice.mapper;

import com.ecnu.easybook.easybookstockservice.DO.Reader;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    Reader getOne(Long id);

    Reader getByStuId(@Param("stuId") Long stuId); // 学号

    void insert(Reader r);

    void update(Reader r);

    Reader getByUIDAndPW(@Param("uid")Long uid, @Param("password") String password);

    void delete(Long id);

}
