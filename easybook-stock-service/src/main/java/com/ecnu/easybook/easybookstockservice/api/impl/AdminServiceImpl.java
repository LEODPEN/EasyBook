package com.ecnu.easybook.easybookstockservice.api.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.ecnu.easybook.easybookstockservice.DO.Admin;
import com.ecnu.easybook.easybookstockservice.DO.Conf;
import com.ecnu.easybook.easybookstockservice.api.AdminService;
import com.ecnu.easybook.easybookstockservice.cacheservice.ConfCacheService;
import com.ecnu.easybook.easybookstockservice.mapper.AdminMapper;
import com.ecnu.easybook.easybookstockservice.mapper.ConfMapper;
import com.ecnu.easybook.easybookstockservice.response.EBResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author LEO D PEN
 * @date 2021/1/3
 * @desc 为了节约时间，异常不抛枚举了，直接先硬编码了
 * @desc 参数的校验要不全交给上层做算了
 */
@Service(interfaceClass = AdminService.class)
@Component
@Slf4j
public class AdminServiceImpl implements AdminService {


    private final AdminMapper adminRepository;

    private final ConfMapper confRepository;

    @Resource(name = "ConfCacheService")
    private ConfCacheService confCacheService;

    // 反正就一两个注入源
    @Autowired
    private AdminServiceImpl(AdminMapper adminMapper, ConfMapper confMapper) {
        this.adminRepository = adminMapper;
        this.confRepository = confMapper;
    }

    @Override
    public EBResponse<Admin> findCertainAdmin(Long id) {
        try {
            return EBResponse.success(adminRepository.getOne(id));
        }catch (Exception e) {
            // 打印log，日志还是需要打一下的。。。
            log.error("AdminService -> findCertainAdmin id :{} :" + e.getMessage(), id);
        }
        return EBResponse.error("无用户", null);
    }

    @Override
    public EBResponse<Admin> findCertainAdmin(String name, String pwd) {
        try {
            return EBResponse.success(adminRepository.queryByNameAndPassword(name, pwd));
        }catch (Exception e) {
            log.error("AdminService -> findCertainAdmin :" + e.getMessage());
        }
        return EBResponse.error("无用户", null);
    }

    @Override
    public EBResponse<Boolean> addOneAdmin(Admin admin) {
        try {
            if (admin != null) {
                adminRepository.insert(admin);
            }
        }catch (Exception e) {
            log.error("AdminService -> addOneAdmin :" + e.getMessage());
            return EBResponse.success(Boolean.FALSE);
        }
        return EBResponse.success(Boolean.TRUE);
    }

    @Override
    public EBResponse<Boolean> updateOneAdmin(Admin admin) {
        try {
            if (admin != null) {
                adminRepository.update(admin);
            }
        }catch (Exception e) {
            log.error("AdminService -> updateOneAdmin :" + e.getMessage());
            return EBResponse.error("更新admin信息错误", Boolean.FALSE);
        }
        return EBResponse.success(Boolean.TRUE);
    }

    @Override
    public EBResponse<Boolean> deleteOneAdmin(Long id) {
        try {
            adminRepository.delete(id);
            return EBResponse.success(Boolean.TRUE);
        }catch (Exception e) {
            log.error("AdminService -> deleteOneAdmin :" + e.getMessage());
            return EBResponse.error("删除admin信息错误", Boolean.FALSE);
        }
    }

    @Override
    public EBResponse<List<Conf>> loadAllConfs() {
        return EBResponse.success(confRepository.loadAll());
    }

    // 新加的不能立即成为selected
    @Override
    public EBResponse<Boolean> addConf(Conf conf) {
        if (conf != null) {
            confRepository.insert(conf);
            return EBResponse.success(Boolean.TRUE);
        }
        return EBResponse.error("",Boolean.FALSE);
    }

    // 不能删除已经选中的配置
    @Override
    public EBResponse<Boolean> deleteConf(Long id) {
        try {
            confRepository.delete(id);
            return EBResponse.success(Boolean.TRUE);
        }catch (Exception e) {
            log.error("AdminService -> deleteConf : id : {} :" + "大概是没有这个id吧", id);
            log.error(e.getMessage());
            return EBResponse.error("",Boolean.FALSE);
        }
    }

    // 这里的更新不能更新select的状态，select需要单独的更新函数
    @Override
    public EBResponse<Boolean> updateConf(Conf conf) {
        if (conf != null) {
            confRepository.update(conf);
        }
        return EBResponse.success(Boolean.TRUE);
    }

    @Override
    public EBResponse<Boolean> selectConf(Long id) {
        Conf curSelected = confRepository.loadSelected();
        if (curSelected != null && curSelected.getId().equals(id)) {
            return EBResponse.success("已经是被选中配置", Boolean.TRUE);
        }else if (curSelected != null) {
            confRepository.unselect(curSelected.getId());
        }
        confRepository.select(id);
        // 删除缓存【写后删除】
        confCacheService.deleteConf();
        return EBResponse.success(Boolean.TRUE);
    }

    @Override
    public EBResponse<Boolean> unselectConf(Long id) {
        Conf curSelected = confRepository.loadSelected();
        if (curSelected != null && id.equals(curSelected.getId())) {
            confRepository.unselect(curSelected.getId());
            confCacheService.deleteConf();
        }
        return EBResponse.success(Boolean.TRUE);
    }

    // 直接更新缓存
    @Override
    public EBResponse<Conf> querySelectedConf() {
        Conf curSelected = confRepository.loadSelected();
        if (curSelected != null) {
            confCacheService.setConf(curSelected.getBusyPeriod());
        }
        return EBResponse.success(curSelected);
    }
}
