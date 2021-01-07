package com.ecnu.easybookweb.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.ecnu.easybook.easybookstockservice.DO.Conf;
import com.ecnu.easybook.easybookstockservice.DO.Deal;
import com.ecnu.easybook.easybookstockservice.api.AdminService;
import com.ecnu.easybook.easybookstockservice.api.AsyncDealService;
import com.ecnu.easybook.easybookstockservice.api.DealService;
import com.ecnu.easybook.easybookstockservice.config.ConstantConfig;
import com.ecnu.easybook.easybookstockservice.enums.DealStatus;
import com.ecnu.easybookweb.exception.BookException;
import com.ecnu.easybookweb.form.DealForm;
import com.ecnu.easybookweb.util.ResultUtil;
import com.ecnu.easybookweb.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import static com.ecnu.easybookweb.enums.ResultEnum.*;


/**
 * @author pengfeng
 * @desc 图书借与还
 **/
@RestController
@RequestMapping("/op")
public class OpController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm::ss");

    @Reference
    private DealService dealService;

    @Reference
    private AdminService adminService;

    @Reference
    private AsyncDealService asyncDealService;

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping("/test")
    public void test() {
        Deal deal = new Deal();
        deal.setId(1L);
        deal.setStatus(1);
        deal.setBookName("fking babu啊啊");
        kafkaTemplate.send("test", deal);
        System.out.println("嘤嘤嘤");
    }

    @PostMapping("/makeDeal")
    public ResultVO makeDeal(@RequestBody @NotNull DealForm dealForm) {
        Deal deal = new Deal();
        BeanUtils.copyProperties(dealForm, deal);
//        deal.setStatus(DealStatus.PROCESS.getCode());
        if (isBusy()) {
            // mq削峰
            kafkaTemplate.send(ConstantConfig.TOPIC, deal);
        }else {
            // 正常异步流程
            asyncDealService.makeOneDeal(deal);
        }
        return ResultUtil.success(Boolean.TRUE);
    }

    @GetMapping("/finishDeal")
    public ResultVO finishDeal(@RequestParam(name = "dealId") Long dealId) {
        return ResultUtil.success(dealService.finishOneDeal(dealId).getCode());
    }

    @GetMapping("/deleteOneDeal")
    public ResultVO deleteDeal(@RequestParam(name = "dealId") Long dealId) {
        return ResultUtil.success(dealService.deleteOneDeal(dealId).getCode());
    }

    private boolean isBusy() {
        String busy_period = stringRedisTemplate.opsForValue().get(ConstantConfig.CONF_KEY);
        if (StringUtils.isEmpty(busy_period)) {
            synchronized (this) {
                try {
                    // 双重检测，没必要多调用
                    if (StringUtils.isEmpty(busy_period = stringRedisTemplate.opsForValue().get(ConstantConfig.CONF_KEY))) {
                        Conf conf = adminService.querySelectedConf().getData();
                        if (conf == null) return false;
                        busy_period = conf.getBusyPeriod();
                    }
                }catch (Exception e) {
                    throw new BookException(SERVER_TIMEOUT);
                }
            }
        }
        String[] time = busy_period.split("-");

        LocalTime b_begin = LocalTime.parse(time[0], FORMATTER);
        LocalTime b_end = LocalTime.parse(time[1], FORMATTER);
        LocalTime now = LocalTime.now();
        return now.isAfter(b_begin) && now.isBefore(b_end);
    }
}
