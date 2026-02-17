package org.wuyang.train.business.controller.admin;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.wuyang.train.business.req.TrainStationQueryReq;
import org.wuyang.train.business.req.TrainStationSaveReq;
import org.wuyang.train.business.resp.TrainStationQueryResp;
import org.wuyang.train.business.service.TrainStationService;
import org.wuyang.train.common.resp.CommonResp;
import org.wuyang.train.common.resp.PageResp;

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
