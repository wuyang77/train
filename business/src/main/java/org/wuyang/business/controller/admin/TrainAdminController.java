package org.wuyang.business.controller.admin;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.wuyang.business.req.TrainQueryReq;
import org.wuyang.business.req.TrainSaveReq;
import org.wuyang.business.resp.TrainQueryResp;
import org.wuyang.business.service.TrainService;
import org.wuyang.common.resp.CommonResp;
import org.wuyang.common.resp.PageResp;

import java.util.List;

@RestController
@RequestMapping("/admin/train")
public class TrainAdminController {

    @Resource
    private TrainService trainService;

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
}
