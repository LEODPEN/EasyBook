package com.ecnu.easybook.easybookstockservice.api;

import com.ecnu.easybook.easybookstockservice.DO.Admin;
import com.ecnu.easybook.easybookstockservice.DO.Conf;
import com.ecnu.easybook.easybookstockservice.response.EBResponse;

import java.util.List;

/**
 * @author LEO D PEN
 * @date 2021/1/3
 * @desc 管理员对外服务接口
 */

public interface AdminService {

    /**
     * @param id
     * @desc 根据id唯一查找
     * @return
     */
    EBResponse<Admin> findCertainAdmin(Long id);

    /**
     * @param name
     * @param pwd
     * @desc 根据name和密码唯一  查找
     * @return
     */
    EBResponse<Admin> findCertainAdmin(String name, String pwd);

    EBResponse<Boolean> addOneAdmin(Admin admin);

    EBResponse<Boolean> updateOneAdmin(Admin admin);

    EBResponse<Boolean> deleteOneAdmin(Long id);

    /**
     * @desc 加载配置信息
     */
    EBResponse<List<Conf>> loadAllConfs();

    /**
     * @desc 调用到这里了说明已经需要从库里面取了
     * 这是由于写后删除所致的，记得更新下缓存
     */
    EBResponse<Conf> querySelectedConf();

    EBResponse<Boolean> addConf(Conf conf);

    /**
     * @param id
     * @desc 更新选中的有效配置
     * @return
     */
    EBResponse<Boolean> selectConf(Long id);


    EBResponse<Boolean> unselectConf(Long id);


    EBResponse<Boolean> updateConf(Conf conf);

    EBResponse<Boolean> deleteConf(Long id);
}
