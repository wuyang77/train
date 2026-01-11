package org.wuyang.member.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wuyang.member.service.MemberService;

@RestController
@RequestMapping("/member")
public class TestController {

    @Resource
    private MemberService memberService;
    @GetMapping("/count")
    public Integer count () {
        return memberService.count();
    }
}
