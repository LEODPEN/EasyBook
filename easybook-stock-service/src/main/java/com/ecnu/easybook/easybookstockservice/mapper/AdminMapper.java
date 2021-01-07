package com.ecnu.easybook.easybookstockservice.mapper;

import com.ecnu.easybook.easybookstockservice.DO.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AdminMapper {

    Admin getOne(Long id);

    Admin queryByNameAndPassword(@Param("name") String name, @Param("password") String password);

    void insert(Admin admin);

    void update(Admin admin);

    void delete(Long id);
}
