package org.wuyang.business.controller.admin;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.wuyang.business.req.StationQueryReq;
import org.wuyang.business.req.StationSaveReq;
import org.wuyang.business.resp.StationQueryResp;
import org.wuyang.business.service.StationService;
import org.wuyang.common.resp.CommonResp;
import org.wuyang.common.resp.PageResp;

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
}
