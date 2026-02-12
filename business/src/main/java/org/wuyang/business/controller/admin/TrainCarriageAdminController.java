package org.wuyang.business.controller.admin;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.wuyang.business.req.TrainCarriageQueryReq;
import org.wuyang.business.req.TrainCarriageSaveReq;
import org.wuyang.business.resp.TrainCarriageQueryResp;
import org.wuyang.business.service.TrainCarriageService;
import org.wuyang.common.resp.CommonResp;
import org.wuyang.common.resp.PageResp;

@RestController
@RequestMapping("/admin/train-carriage")
public class TrainCarriageAdminController {

    @Resource
    private TrainCarriageService trainCarriageService;

    @PostMapping("/save")
    public CommonResp<Object> saveOrEditTrainCarriage(@Valid @RequestBody TrainCarriageSaveReq req) {
        trainCarriageService.saveOrEditTrainCarriage(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainCarriageQueryResp>> queryTrainCarriageList(@Valid TrainCarriageQueryReq req) {
        PageResp<TrainCarriageQueryResp> pageResp = trainCarriageService.queryTrainCarriageList(req);
        return new CommonResp<>(pageResp);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> deleteTrainCarriage(@PathVariable("id") Long id) {
        trainCarriageService.deleteTrainCarriage(id);
        return new CommonResp<>();
    }
}
