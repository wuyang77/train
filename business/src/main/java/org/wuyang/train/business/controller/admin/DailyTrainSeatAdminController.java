package org.wuyang.train.business.controller.admin;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.wuyang.train.common.resp.CommonResp;
import org.wuyang.train.common.resp.PageResp;
import org.wuyang.train.business.resp.DailyTrainSeatQueryResp;
import org.wuyang.train.business.req.DailyTrainSeatQueryReq;
import org.wuyang.train.business.req.DailyTrainSeatSaveReq;
import org.wuyang.train.business.service.DailyTrainSeatService;

@RestController
@RequestMapping("/admin/daily-train-seat")
public class DailyTrainSeatAdminController {

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    @PostMapping("/save")
    public CommonResp<Object> saveOrEditDailyTrainSeat(@Valid @RequestBody DailyTrainSeatSaveReq req) {
        dailyTrainSeatService.saveOrEditDailyTrainSeat(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainSeatQueryResp>> queryDailyTrainSeatList(@Valid DailyTrainSeatQueryReq req) {
        PageResp<DailyTrainSeatQueryResp> pageResp = dailyTrainSeatService.queryDailyTrainSeatList(req);
        return new CommonResp<>(pageResp);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> deleteDailyTrainSeat(@PathVariable("id") Long id) {
        dailyTrainSeatService.deleteDailyTrainSeat(id);
        return new CommonResp<>();
    }
}
