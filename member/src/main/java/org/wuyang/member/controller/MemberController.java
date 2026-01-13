package org.wuyang.member.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wuyang.common.resp.CommonResp;
import org.wuyang.member.resq.MemberRegisterReq;
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
    public CommonResp<Long> register (MemberRegisterReq req) {
        Long register = memberService.register(req);
        // CommonResp<Long> commonResp = new CommonResp<>();
        // commonResp.setContent(register);
        // return commonResp;
        return new CommonResp<>(register);
    }
}
