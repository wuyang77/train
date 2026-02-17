package org.wuyang.train.business.controller.admin;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.wuyang.train.business.req.StationQueryReq;
import org.wuyang.train.business.req.StationSaveReq;
import org.wuyang.train.business.resp.StationQueryResp;
import org.wuyang.train.business.service.StationService;
import org.wuyang.train.common.resp.CommonResp;
import org.wuyang.train.common.resp.PageResp;

import java.util.List;

@RestController
@RequestMapping("/admin/station")
public class StationAdminController {

    @Resource
    private StationService stationService;

    @PostMapping("/save")
    public CommonResp<Object> saveOrEditStation(@Valid @RequestBody StationSaveReq req) {
        stationService.saveOrEditStation(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<StationQueryResp>> queryStationList(@Valid StationQueryReq req) {
        PageResp<StationQueryResp> pageResp = stationService.queryStationList(req);
        return new CommonResp<>(pageResp);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> deleteStation(@PathVariable("id") Long id) {
        stationService.deleteStation(id);
        return new CommonResp<>();
    }

    @GetMapping("/query-all")
    public CommonResp<List<StationQueryResp>> queryStationAll() {
        List<StationQueryResp> list = stationService.queryStationAll();
        return new CommonResp<>(list);
    }

}
