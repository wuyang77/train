package org.wuyang.train.business.controller.admin;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.wuyang.train.business.req.TrainSeatQueryReq;
import org.wuyang.train.business.req.TrainSeatSaveReq;
import org.wuyang.train.business.resp.TrainSeatQueryResp;
import org.wuyang.train.business.service.TrainSeatService;
import org.wuyang.train.common.resp.CommonResp;
import org.wuyang.train.common.resp.PageResp;

@RestController
@RequestMapping("/admin/train-seat")
public class TrainSeatAdminController {

    @Resource
    private TrainSeatService trainSeatService;

    @PostMapping("/save")
    public CommonResp<Object> saveOrEditTrainSeat(@Valid @RequestBody TrainSeatSaveReq req) {
        trainSeatService.saveOrEditTrainSeat(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainSeatQueryResp>> queryTrainSeatList(@Valid TrainSeatQueryReq req) {
        PageResp<TrainSeatQueryResp> pageResp = trainSeatService.queryTrainSeatList(req);
        return new CommonResp<>(pageResp);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> deleteTrainSeat(@PathVariable("id") Long id) {
        trainSeatService.deleteTrainSeat(id);
        return new CommonResp<>();
    }
}
