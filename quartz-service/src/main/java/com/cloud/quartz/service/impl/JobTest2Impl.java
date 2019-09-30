package com.cloud.quartz.service.impl;


import com.cloud.quartz.service.JobTest2Service;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class JobTest2Impl implements JobTest2Service {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void test2() throws JobExecutionException {
        try {
            System.out.println(Thread.currentThread().getId()+"----测试任务2----"+Thread.currentThread().getName());
        } catch (Exception e) {
            JobExecutionException e2 = new JobExecutionException(e);
            // true 表示 Quartz 会自动取消所有与这个 job 有关的 trigger，从而避免再次运行 job
            e2.setUnscheduleAllTriggers(true);
            throw e2;
        }
    }
}
