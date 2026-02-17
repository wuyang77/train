package org.wuyang.train.business.controller.admin;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.wuyang.train.business.req.TrainQueryReq;
import org.wuyang.train.business.req.TrainSaveReq;
import org.wuyang.train.business.resp.TrainQueryResp;
import org.wuyang.train.business.service.TrainSeatService;
import org.wuyang.train.business.service.TrainService;
import org.wuyang.train.common.resp.CommonResp;
import org.wuyang.train.common.resp.PageResp;

import java.util.List;

@RestController
@RequestMapping("/admin/train")
public class TrainAdminController {

    @Resource
    private TrainService trainService;
    @Autowired
    private TrainSeatService trainSeatService;

    @PostMapping("/save")
    public CommonResp<Object> saveOrEditTrain(@Valid @RequestBody TrainSaveReq req) {
        trainService.saveOrEditTrain(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainQueryResp>> queryTrainList(@Valid TrainQueryReq req) {
        PageResp<TrainQueryResp> pageResp = trainService.queryTrainList(req);
        return new CommonResp<>(pageResp);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> deleteTrain(@PathVariable("id") Long id) {
        trainService.deleteTrain(id);
        return new CommonResp<>();
    }

    @GetMapping("/query-all")
    public CommonResp<List<TrainQueryResp>>  queryTrainAll() {
        List<TrainQueryResp> list = trainService.queryTrainAll();
        return new CommonResp<>(list);
    }
    @GetMapping("/generate-seat/{trainCode}")
    public CommonResp<Object> genSeat(@PathVariable String trainCode) {
        trainSeatService.generateTrainSeat(trainCode);
        return new CommonResp<>();
    }
}
