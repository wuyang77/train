package org.wuyang.train.batch.controller;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wuyang.train.batch.feign.BusinessOpenFeign;

@RestController
public class TestController {

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    @Resource
    BusinessOpenFeign businessOpenFeign;

    @GetMapping("/hello")
    public String hello(){
        // 测试调用business的hello
        String helloBusinessOpenFeign = businessOpenFeign.helloOpenFeign();
        LOG.info(helloBusinessOpenFeign);
        return "Hello Batch模块";
    }
}
