package org.wuyang.train.batch.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.wuyang.train.common.resp.CommonResp;

import java.util.Date;

@FeignClient(name = "business",url = "http://127.0.0.1:8002/business")
public interface BusinessOpenFeign {

    @GetMapping("/hello")
    String helloOpenFeign();

    @GetMapping("/admin/daily-train/generate-daily/{date}")
    public CommonResp<Object> genAllDailyTrain(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date);
}
