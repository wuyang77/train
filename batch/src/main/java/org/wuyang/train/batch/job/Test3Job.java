package org.wuyang.train.batch.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class Test3Job implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("TestJob TEST开始 3333333333");
//         try {
//             Thread.sleep(3000);
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }
        System.out.println("TestJob TEST结束 3333333333");
    }
}
