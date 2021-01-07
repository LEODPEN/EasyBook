package com.ecnu.easybookweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ecnu.easybook.easybookstockservice.DO.Reader;
import com.ecnu.easybook.easybookstockservice.api.DealService;
import com.ecnu.easybook.easybookstockservice.api.UserService;
import com.ecnu.easybook.easybookstockservice.enums.DealStatus;
import com.ecnu.easybookweb.dto.UserDTO;
import com.ecnu.easybookweb.exception.BookException;
import com.ecnu.easybookweb.form.UserForm;
import com.ecnu.easybookweb.form.UserInfoForm;
import com.ecnu.easybookweb.util.ResultUtil;
import com.ecnu.easybookweb.vo.ResultVO;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

import static com.ecnu.easybookweb.enums.ResultEnum.NO_SUCH_USER;

/**
 * @author pengfeng
 * @desc 用户普通操作
 * 暂时不加拦截与鉴权？后续根据时间来看
 */
@RestController
@RequestMapping("/usr")
public class UserController {

    @Reference
    private UserService userService;

    @Reference
    private DealService dealService;


            /******** 借阅记录相关 ********/
    @GetMapping("/deal/allDeals")
    public ResultVO allDeals(@RequestParam(name = "uid") Long uid,
                             @RequestParam(name = "page", defaultValue = "1")Integer page,
                             @RequestParam(name = "size", defaultValue = "20")Integer size) {
            return ResultUtil.success(dealService.findDealsByUID(uid, page - 1, size).getData());
    }

    @GetMapping("/deal/process")
    public ResultVO processDeals(@RequestParam(name = "uid") Long uid,
                                 @RequestParam(name = "page", defaultValue = "1")Integer page,
                                 @RequestParam(name = "size", defaultValue = "20")Integer size) {
        return ResultUtil.success(dealService.findDealsByUIDAndStatus(uid, DealStatus.PROCESS, page - 1, size).getData());
    }

    @GetMapping("/deal/done")
    public ResultVO doneDeals(@RequestParam(name = "uid") Long uid,
                                 @RequestParam(name = "page", defaultValue = "1")Integer page,
                                 @RequestParam(name = "size", defaultValue = "20")Integer size) {
        return ResultUtil.success(dealService.findDealsByUIDAndStatus(uid, DealStatus.DONE, page - 1, size).getData());
    }

    @GetMapping("/deal/fail")
    public ResultVO failDeals(@RequestParam(name = "uid") Long uid,
                              @RequestParam(name = "page", defaultValue = "1")Integer page,
                              @RequestParam(name = "size", defaultValue = "20")Integer size) {
        return ResultUtil.success(dealService.findDealsByUIDAndStatus(uid, DealStatus.FAIL, page - 1, size).getData());
    }

    /******** 用户信息相关 ********/

    @PostMapping("/log")
    public ResultVO logIn(@RequestBody @NotNull UserForm userForm) {
        Reader reader = userService.findOneCertainUser(userForm.getStuid(), userForm.getPwd()).getData();
        if (reader == null) throw new BookException(NO_SUCH_USER);
        // todo 用xml方式配置dubbo，然后把得到用户借书次数成异步的
        Integer count = dealService.getDealCountByUID(reader.getId()).getData();
        UserDTO userDTO = new UserDTO(reader.getStuId(), reader.getNickname(), count);
        return ResultUtil.success(userDTO);
    }

    @PostMapping("/upd")
    public ResultVO updU(@RequestBody @NotNull UserInfoForm userInfoForm) {
        Reader r = userService.findOneCertainUserByUID(userInfoForm.getUid()).getData();
        if (r == null || !StringUtils.isEmpty(userInfoForm.getNewPwd()) && !r.getPassword().equals(userInfoForm.getOriginPwd())) {
            throw new BookException(NO_SUCH_USER);
        }
        if (!StringUtils.isEmpty(userInfoForm.getNickName())) {
            r.setNickname(userInfoForm.getNickName());
        }
        if (!StringUtils.isEmpty(userInfoForm.getOriginPwd())
                && !StringUtils.isEmpty(userInfoForm.getNewPwd())) {
            r.setPassword(userInfoForm.getNewPwd());
        }
        return ResultUtil.success(userService.updateOneUser(r).getData());
    }

    @GetMapping("/info")
    public ResultVO info(@RequestParam(name = "stuId") Long stuId) {
        Reader reader = userService.findOneCertainUserByStuId(stuId).getData();
        if (reader == null) throw new BookException(NO_SUCH_USER);
        Integer count = dealService.getDealCountByUID(reader.getId()).getData();
        UserDTO userDTO = new UserDTO(reader.getStuId(), reader.getNickname(), count);
        return ResultUtil.success(userDTO);
    }


}

