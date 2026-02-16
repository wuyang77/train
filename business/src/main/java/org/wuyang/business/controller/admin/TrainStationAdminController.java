package org.wuyang.business.controller.admin;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.wuyang.business.req.TrainStationQueryReq;
import org.wuyang.business.req.TrainStationSaveReq;
import org.wuyang.business.resp.TrainStationQueryResp;
import org.wuyang.business.service.TrainStationService;
import org.wuyang.common.resp.CommonResp;
import org.wuyang.common.resp.PageResp;

@RestController
@RequestMapping("/admin/train-station")
public class TrainStationAdminController {

    @Resource
    private TrainStationService trainStationService;

    @PostMapping("/save")
    public CommonResp<Object> saveOrEditTrainStation(@Valid @RequestBody TrainStationSaveReq req) {
        trainStationService.saveOrEditTrainStation(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainStationQueryResp>> queryTrainStationList(@Valid TrainStationQueryReq req) {
        PageResp<TrainStationQueryResp> pageResp = trainStationService.queryTrainStationList(req);
        return new CommonResp<>(pageResp);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> deleteTrainStation(@PathVariable("id") Long id) {
        trainStationService.deleteTrainStation(id);
        return new CommonResp<>();
    }

}
