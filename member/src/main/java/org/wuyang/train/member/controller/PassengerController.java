package org.wuyang.train.member.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.wuyang.train.common.context.LoginMemberContext;
import org.wuyang.train.common.resp.CommonResp;
import org.wuyang.train.common.resp.PageResp;
import org.wuyang.train.member.resp.PassengerQueryResp;
import org.wuyang.train.member.req.PassengerQueryReq;
import org.wuyang.train.member.req.PassengerSaveReq;
import org.wuyang.train.member.service.PassengerService;

@RestController
@RequestMapping("/passenger")
public class PassengerController {

    @Resource
    private PassengerService passengerService;

    @PostMapping("/save")
    public CommonResp<Object> saveOrEditPassenger(@Valid @RequestBody PassengerSaveReq req) {
        passengerService.saveOrEditPassenger(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<PassengerQueryResp>> queryPassengerList(@Valid PassengerQueryReq req) {
        req.setMemberId(LoginMemberContext.getId());
        PageResp<PassengerQueryResp> pageResp = passengerService.queryPassengerList(req);
        return new CommonResp<>(pageResp);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> deletePassenger(@PathVariable("id") Long id) {
        passengerService.deletePassenger(id);
        return new CommonResp<>();
    }
}
