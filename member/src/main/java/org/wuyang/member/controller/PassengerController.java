package org.wuyang.member.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.wuyang.common.context.LoginMemberContext;
import org.wuyang.common.resp.CommonResp;
import org.wuyang.common.resp.PageResp;
import org.wuyang.member.resp.PassengerQueryResp;
import org.wuyang.member.resq.PassengerQueryReq;
import org.wuyang.member.resq.PassengerSaveReq;
import org.wuyang.member.service.PassengerService;

@RestController
@RequestMapping("/passenger")
public class PassengerController {

    @Resource
    private PassengerService passengerService;

    @PostMapping("/save")
    public CommonResp<Object> savePassenger(@Valid @RequestBody PassengerSaveReq req) {
        passengerService.savePassenger(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<PassengerQueryResp>> queryPassengerList(@Valid PassengerQueryReq req) {
        req.setMemberId(LoginMemberContext.getId());
        PageResp<PassengerQueryResp> pageResp = passengerService.queryPassengerList(req);
        return new CommonResp<>(pageResp);
    }
}
