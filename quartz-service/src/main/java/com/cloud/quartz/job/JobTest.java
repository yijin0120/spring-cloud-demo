package com.cloud.quartz.job;


import com.cloud.quartz.model.JobEntity;
import com.cloud.quartz.service.DynamicJobService;
import com.cloud.quartz.service.JobTestService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@DisallowConcurrentExecution
@Component
public class JobTest implements Job {
    @Autowired
    private JobTestService jobTestService;
    @Autowired
    private DynamicJobService jobService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        try {
            jobTestService.test();
        } catch (JobExecutionException e) {
            log.error("测试任务异常，原因是："+e.getMessage());
            JobDataMap jobMap = jobExecutionContext.getMergedJobDataMap();
            int jobId = (int) jobMap.get("jobId");
            //状态置为close
            JobEntity job = new JobEntity();
            job.setId(jobId);
            job.setStatus("ERR");
            job.setUpdateTime(new Date());
            jobService.edit(job);
        }
    }
}
