package org.wuyang.train.member.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.wuyang.train.common.resp.CommonResp;
import org.wuyang.train.common.resp.MemberLoginResp;
import org.wuyang.train.member.req.MemberLoginReq;
import org.wuyang.train.member.req.MemberRegisterReq;
import org.wuyang.train.member.req.MemberSendCodeReq;
import org.wuyang.train.member.service.MemberService;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    private MemberService memberService;
    @GetMapping("/count")
    public CommonResp<Integer> count () {
        Integer count = memberService.count();
        return new CommonResp<>(count);
    }

    @PostMapping("/register")
    public CommonResp<Long> register (@Valid MemberRegisterReq req) {
        Long register = memberService.register(req);
        // CommonResp<Long> commonResp = new CommonResp<>();
        // commonResp.setContent(register);
        // return commonResp;
        return new CommonResp<>(register);
    }

    @PostMapping("/send-code")
    public CommonResp<Void> sendCode (@Valid @RequestBody MemberSendCodeReq req) {
        memberService.sendCode(req);
        return new CommonResp<>();
    }

    @PostMapping("/login")
    public CommonResp<MemberLoginResp> login (@Valid @RequestBody MemberLoginReq req) {
        MemberLoginResp resp=  memberService.login(req);
        return new CommonResp<>(resp);
    }
}
