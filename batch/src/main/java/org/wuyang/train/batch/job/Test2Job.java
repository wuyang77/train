package org.wuyang.train.batch.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class Test2Job implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("TestJob TEST开始 222222");
//         try {
//             Thread.sleep(3000);
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }
        System.out.println("TestJob TEST结束 222222");
    }
}
