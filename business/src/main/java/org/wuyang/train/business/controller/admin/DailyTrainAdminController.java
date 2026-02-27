package org.wuyang.train.business.controller.admin;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.wuyang.train.common.resp.CommonResp;
import org.wuyang.train.common.resp.PageResp;
import org.wuyang.train.business.resp.DailyTrainQueryResp;
import org.wuyang.train.business.req.DailyTrainQueryReq;
import org.wuyang.train.business.req.DailyTrainSaveReq;
import org.wuyang.train.business.service.DailyTrainService;

import java.util.Date;

@RestController
@RequestMapping("/admin/daily-train")
public class DailyTrainAdminController {

    @Resource
    private DailyTrainService dailyTrainService;

    @PostMapping("/save")
    public CommonResp<Object> saveOrEditDailyTrain(@Valid @RequestBody DailyTrainSaveReq req) {
        dailyTrainService.saveOrEditDailyTrain(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainQueryResp>> queryDailyTrainList(@Valid DailyTrainQueryReq req) {
        PageResp<DailyTrainQueryResp> pageResp = dailyTrainService.queryDailyTrainList(req);
        return new CommonResp<>(pageResp);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> deleteDailyTrain(@PathVariable("id") Long id) {
        dailyTrainService.deleteDailyTrain(id);
        return new CommonResp<>();
    }

    // 1. 根据前端的日期生成某一天的车次数据 2.或者通过跑批模块生成15天后的车次数据
    @GetMapping("/generate-daily/{date}")
    public CommonResp<Object> genDailyTrainAll(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        dailyTrainService.generateDailyTrainAll(date);
        return new CommonResp<>();
    }
}
