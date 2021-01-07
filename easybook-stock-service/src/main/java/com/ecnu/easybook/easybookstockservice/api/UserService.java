package com.ecnu.easybook.easybookstockservice.api;

import com.ecnu.easybook.easybookstockservice.DO.Reader;
import com.ecnu.easybook.easybookstockservice.response.EBResponse;

/**
 * @author LEO D PEN
 * @date 2021/1/3
 * @desc
 */
public interface UserService {

    /**
     * @param id
     * @desc 根据id精确查找
     * @return
     */
    EBResponse<Reader> findOneCertainUserByUID(Long id);

    /**
     * @param stuId
     * @return
     */
    EBResponse<Reader> findOneCertainUserByStuId(Long stuId);

    /**
     * @param stuId
     * @param pwd
     * @return
     */
    EBResponse<Reader> findOneCertainUser(Long stuId, String pwd);

    EBResponse<Boolean> addOneUser(Reader reader);

    EBResponse<Boolean> updateOneUser(Reader reader);

    EBResponse<Boolean> deleteOneUser(Long id);

}
