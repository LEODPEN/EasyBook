package com.ecnu.easybook.easybookstockservice.mapper;

import com.ecnu.easybook.easybookstockservice.DO.Conf;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ConfMapper {

    Conf loadByID(Long id);

    List<Conf> loadAll();

    // 查出当前生效的配置
    Conf loadSelected();

    void select(Long id);

    void unselect(Long id);

    void insert(Conf conf);

    void update(Conf conf);

    void delete(Long id);
}
