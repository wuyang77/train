package org.wuyang.train.${module}.controller.admin;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.wuyang.train.common.context.LoginMemberContext;
import org.wuyang.train.common.resp.CommonResp;
import org.wuyang.train.common.resp.PageResp;
import org.wuyang.train.${module}.resp.${Domain}QueryResp;
import org.wuyang.train.${module}.req.${Domain}QueryReq;
import org.wuyang.train.${module}.req.${Domain}SaveReq;
import org.wuyang.train.${module}.service.${Domain}Service;

@RestController
@RequestMapping("/admin/${do_main}")
public class ${Domain}AdminController {

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
