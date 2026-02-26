package org.wuyang.train.business.controller.admin;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.wuyang.train.common.resp.CommonResp;
import org.wuyang.train.common.resp.PageResp;
import org.wuyang.train.business.resp.DailyTrainCarriageQueryResp;
import org.wuyang.train.business.req.DailyTrainCarriageQueryReq;
import org.wuyang.train.business.req.DailyTrainCarriageSaveReq;
import org.wuyang.train.business.service.DailyTrainCarriageService;

@RestController
@RequestMapping("/admin/daily-train-carriage")
public class DailyTrainCarriageAdminController {

    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;

    @PostMapping("/save")
    public CommonResp<Object> saveOrEditDailyTrainCarriage(@Valid @RequestBody DailyTrainCarriageSaveReq req) {
        dailyTrainCarriageService.saveOrEditDailyTrainCarriage(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainCarriageQueryResp>> queryDailyTrainCarriageList(@Valid DailyTrainCarriageQueryReq req) {
        PageResp<DailyTrainCarriageQueryResp> pageResp = dailyTrainCarriageService.queryDailyTrainCarriageList(req);
        return new CommonResp<>(pageResp);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> deleteDailyTrainCarriage(@PathVariable("id") Long id) {
        dailyTrainCarriageService.deleteDailyTrainCarriage(id);
        return new CommonResp<>();
    }
}
