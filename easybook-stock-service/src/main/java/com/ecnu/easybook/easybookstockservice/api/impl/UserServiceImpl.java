package com.ecnu.easybook.easybookstockservice.api.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.ecnu.easybook.easybookstockservice.DO.Reader;
import com.ecnu.easybook.easybookstockservice.api.UserService;
import com.ecnu.easybook.easybookstockservice.mapper.UserMapper;
import com.ecnu.easybook.easybookstockservice.response.EBResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author LEO D PEN
 * @date 2021/1/4
 * @desc
 */
@Service(interfaceClass = UserService.class)
@Component
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userRepository;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userRepository = userMapper;
    }

    @Override
    public EBResponse<Reader> findOneCertainUserByUID(Long id) {
        try {
            return EBResponse.success(userRepository.getOne(id));
        }catch (Exception e) {
            log.error("UserService -> findOneCertainUserByUID : + id :{} :" + e.getMessage(), id);
        }
        return EBResponse.error("无用户", null);
    }

    @Override
    public EBResponse<Reader> findOneCertainUserByStuId(Long stuId) {
        try {
            return EBResponse.success(userRepository.getByStuId(stuId));
        }catch (Exception e) {
            log.error("UserService -> findOneCertainUserByStuId : + stuId :{} :" + e.getMessage(), stuId);
        }
        return EBResponse.error("无用户", null);
    }

    @Override
    public EBResponse<Reader> findOneCertainUser(Long stuId, String pwd) {
        try {
            Reader reader;
            if ((reader = userRepository.getByUIDAndPW(stuId, pwd)) != null) {
                return EBResponse.success(reader);
            }
        }catch (Exception e) {
            log.error("UserService -> findOneCertainUser :" + e.getMessage());
            return EBResponse.error(e.getMessage(), null);
        }
        return EBResponse.success("账号或用户名错误", null);
    }

    @Override
    public EBResponse<Boolean> addOneUser(Reader reader) {
        try {
            if (reader != null) {
                userRepository.insert(reader);
            }
        }catch (Exception e) {
            log.error("UserService -> addOneUser :" + e.getMessage());
            return EBResponse.success(Boolean.FALSE);
        }
        return EBResponse.success(Boolean.TRUE);
    }

    @Override
    public EBResponse<Boolean> updateOneUser(Reader reader) {
        try {
            if (reader != null) {
                userRepository.update(reader);
            }
        }catch (Exception e) {
            log.error("UserService -> updateOneUser :" + e.getMessage());
            return EBResponse.error("更新reader信息错误", Boolean.FALSE);
        }
        return EBResponse.success(Boolean.TRUE);
    }

    @Override
    public EBResponse<Boolean> deleteOneUser(Long id) {
        try {
            userRepository.delete(id);
            return EBResponse.success(Boolean.TRUE);
        }catch (Exception e) {
            log.error("UserService -> deleteOneUser :" + e.getMessage());
            return EBResponse.error("删除user信息错误", Boolean.FALSE);
        }
    }
}
