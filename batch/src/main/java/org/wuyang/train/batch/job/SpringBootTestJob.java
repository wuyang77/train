package org.wuyang.train.batch.job;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SpringBootTestJob {

    /**
     * 1.适合小项目快速实现定时任务，自带的定时任务适合单体应用，不适用于集群任务，
     * 2.无法实时更新定时任务
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void test(){
        // 增加分布式锁，解决集群问题
        System.out.println("SpringBoot自带的定时任务节点");
    }
}
