package com.cloud.quartz.service.impl;


import com.cloud.quartz.service.JobTestService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Service
public class JobTestImpl implements JobTestService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void test() throws JobExecutionException {
        try {
            System.out.println(Thread.currentThread().getId()+"----测试任务1----"+Thread.currentThread().getName());
        } catch (Exception e) {
            JobExecutionException e2 = new JobExecutionException(e);
            // true 表示 Quartz 会自动取消所有与这个 job 有关的 trigger，从而避免再次运行 job
            e2.setUnscheduleAllTriggers(true);
            throw e2;
        }
    }
}
