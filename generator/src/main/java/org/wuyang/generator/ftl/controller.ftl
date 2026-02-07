package org.wuyang.member.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.wuyang.common.context.LoginMemberContext;
import org.wuyang.common.resp.CommonResp;
import org.wuyang.common.resp.PageResp;
import org.wuyang.member.resp.${Domain}QueryResp;
import org.wuyang.member.req.${Domain}QueryReq;
import org.wuyang.member.req.${Domain}SaveReq;
import org.wuyang.member.service.${Domain}Service;

@RestController
@RequestMapping("/${do_main}")
public class ${Domain}Controller {

    @Resource
    private ${Domain}Service ${domain}Service;

    @PostMapping("/save")
    public CommonResp<Object> saveOrEdit${Domain}(@Valid @RequestBody ${Domain}SaveReq req) {
        ${domain}Service.saveOrEdit${Domain}(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<${Domain}QueryResp>> query${Domain}List(@Valid ${Domain}QueryReq req) {
        req.setMemberId(LoginMemberContext.getId());
        PageResp<${Domain}QueryResp> pageResp = ${domain}Service.query${Domain}List(req);
        return new CommonResp<>(pageResp);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete${Domain}(@PathVariable("id") Long id) {
        ${domain}Service.delete${Domain}(id);
        return new CommonResp<>();
    }
}
