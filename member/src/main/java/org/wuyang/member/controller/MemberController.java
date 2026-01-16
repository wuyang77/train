package org.wuyang.member.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.wuyang.common.resp.CommonResp;
import org.wuyang.common.resp.MemberLoginResp;
import org.wuyang.member.resq.MemberLoginReq;
import org.wuyang.member.resq.MemberRegisterReq;
import org.wuyang.member.resq.MemberSendCodeReq;
import org.wuyang.member.service.MemberService;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    private MemberService memberService;
    @GetMapping("/count")
    public Integer count () {
        return memberService.count();
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
    public CommonResp<Long> sendCode (@Valid @RequestBody MemberSendCodeReq req) {
        memberService.sendCode(req);
        return new CommonResp<>();
    }

    @PostMapping("/login")
    public CommonResp<MemberLoginResp> login (@Valid @RequestBody MemberLoginReq req) {
        MemberLoginResp resp=  memberService.login(req);
        return new CommonResp<>(resp);
    }
}
