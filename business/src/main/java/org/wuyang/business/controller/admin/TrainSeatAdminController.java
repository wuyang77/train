package org.wuyang.business.controller.admin;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.wuyang.business.req.TrainSeatQueryReq;
import org.wuyang.business.req.TrainSeatSaveReq;
import org.wuyang.business.resp.TrainSeatQueryResp;
import org.wuyang.business.service.TrainSeatService;
import org.wuyang.common.resp.CommonResp;
import org.wuyang.common.resp.PageResp;

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
