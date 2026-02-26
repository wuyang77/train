package org.wuyang.train.business.controller.admin;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.wuyang.train.common.resp.CommonResp;
import org.wuyang.train.common.resp.PageResp;
import org.wuyang.train.business.resp.DailyTrainStationQueryResp;
import org.wuyang.train.business.req.DailyTrainStationQueryReq;
import org.wuyang.train.business.req.DailyTrainStationSaveReq;
import org.wuyang.train.business.service.DailyTrainStationService;

@RestController
@RequestMapping("/admin/daily-train-station")
public class DailyTrainStationAdminController {

    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @PostMapping("/save")
    public CommonResp<Object> saveOrEditDailyTrainStation(@Valid @RequestBody DailyTrainStationSaveReq req) {
        dailyTrainStationService.saveOrEditDailyTrainStation(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainStationQueryResp>> queryDailyTrainStationList(@Valid DailyTrainStationQueryReq req) {
        PageResp<DailyTrainStationQueryResp> pageResp = dailyTrainStationService.queryDailyTrainStationList(req);
        return new CommonResp<>(pageResp);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> deleteDailyTrainStation(@PathVariable("id") Long id) {
        dailyTrainStationService.deleteDailyTrainStation(id);
        return new CommonResp<>();
    }
}
