package org.wuyang.train.business.controller.admin;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.wuyang.train.common.resp.CommonResp;
import org.wuyang.train.common.resp.PageResp;
import org.wuyang.train.business.resp.DailyTrainTicketQueryResp;
import org.wuyang.train.business.req.DailyTrainTicketQueryReq;
import org.wuyang.train.business.req.DailyTrainTicketSaveReq;
import org.wuyang.train.business.service.DailyTrainTicketService;

@RestController
@RequestMapping("/admin/daily-train-ticket")
public class DailyTrainTicketAdminController {

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @PostMapping("/save")
    public CommonResp<Object> saveOrEditDailyTrainTicket(@Valid @RequestBody DailyTrainTicketSaveReq req) {
        dailyTrainTicketService.saveOrEditDailyTrainTicket(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainTicketQueryResp>> queryDailyTrainTicketList(@Valid DailyTrainTicketQueryReq req) {
        PageResp<DailyTrainTicketQueryResp> pageResp = dailyTrainTicketService.queryDailyTrainTicketList(req);
        return new CommonResp<>(pageResp);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> deleteDailyTrainTicket(@PathVariable("id") Long id) {
        dailyTrainTicketService.deleteDailyTrainTicket(id);
        return new CommonResp<>();
    }
}
