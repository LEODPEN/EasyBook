package com.ecnu.easybookweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ecnu.easybook.easybookstockservice.DO.Admin;
import com.ecnu.easybook.easybookstockservice.DO.Conf;
import com.ecnu.easybook.easybookstockservice.api.AdminService;

import com.ecnu.easybookweb.form.AdminLogForm;
import com.ecnu.easybookweb.form.ConfForm;
import com.ecnu.easybookweb.util.ResultUtil;
import com.ecnu.easybookweb.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LEO D PEN
 * @date 2021/1/6
 * @desc 调用异常以后有时间再完善吧
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Reference
    private AdminService adminService;

    @PostMapping("/log")
    public ResultVO login(@RequestBody AdminLogForm adminLogForm) {
        Admin admin = adminService.findCertainAdmin(adminLogForm.getName(), adminLogForm.getPwd()).getData();
        Map<String, Object> map = new HashMap<>();
        map.put("id", admin.getId());
        map.put("name", admin.getName());
        return ResultUtil.success(map);
    }

            /******** 配置相关 ********/
    @GetMapping("/loadConfs")
    public ResultVO loadConfs() {
        return ResultUtil.success(adminService.loadAllConfs().getData());
    }

    @PostMapping("/addConf")
    public ResultVO addConf(@RequestBody @NotNull ConfForm confForm) {
        Conf conf = new Conf();
        BeanUtils.copyProperties(confForm, conf);
        conf.setSelected(false);
        return ResultUtil.success(adminService.addConf(conf).getData());
    }

    @GetMapping("/deleteConf")
    public ResultVO deleteConf(@RequestParam(name = "confId") Long confId) {
        return ResultUtil.success(adminService.deleteConf(confId).getData());
    }

    @GetMapping("/unselectConf")
    public ResultVO unselectConf(@RequestParam(name = "confId") Long confId) {
        return ResultUtil.success(adminService.unselectConf(confId).getData());
    }

    @GetMapping("/selectConf")
    public ResultVO selectConf(@RequestParam(name = "confId") Long confId) {
        return ResultUtil.success(adminService.selectConf(confId).getData());
    }


}
