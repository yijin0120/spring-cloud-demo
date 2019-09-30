package com.cloud.quartz.controller;

import com.cloud.quartz.model.JobEntity;
import com.cloud.quartz.service.DynamicJobService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping(value = "/job")
public class JobController {
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    @Autowired
    private DynamicJobService jobService;

    //初始化启动所有的Job
    @PostConstruct
    public void initialize() {
        try {
            reStartAllJobs();
            log.info("INIT SUCCESS");
        } catch (Exception e) {
            log.info("INIT EXCEPTION : " + e.getMessage());
            e.printStackTrace();
        }
    }

    //根据ID重启某个Job
    @PostMapping("/refresh/{id}")
    public Map<String, Object> refresh(@PathVariable Integer id) throws Exception {
        String result;
        JobEntity entity = jobService.getJobEntityById(id);
        Map<String, Object> resmap = new HashMap<>();
        if (entity == null) {
            result = "error: id is not exist ";
            resmap.put("msg", result);
            resmap.put("code", -1);
            return resmap;
        }
        String status = StringUtils.trim(entity.getStatus());
        if (status.equals("CLOSE") || status.equals("ERR")) {
            //状态置为open
            entity.setStatus("OPEN");
        }
        //
        TriggerKey triggerKey = new TriggerKey(entity.getName(), entity.getGroup());
        JobKey jobKey = jobService.getJobKey(entity);
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        //停止触发器
        scheduler.pauseTrigger(triggerKey);
        //移除触发器
        scheduler.unscheduleJob(triggerKey);
        //删除任务
        scheduler.deleteJob(jobKey);
        JobDataMap map = jobService.getJobDataMap(entity);
        JobDetail jobDetail = jobService.geJobDetail(jobKey, entity.getDescription(), map);
        if (entity.getStatus().equals("OPEN")) {
            scheduler.scheduleJob(jobDetail, jobService.getTrigger(entity));
            result = "Refresh Job : " + entity.getName() + " success !";
        } else {
            result = "Refresh Job : " + entity.getName() + " failed ! , " +
                    "Because the Job status is " + entity.getStatus();
        }
        entity.setUpdateTime(new Date());
        jobService.edit(entity);
        resmap.put("code", 200);
        resmap.put("msg", result);
        return resmap;
    }

    //移除任务/关闭任务
    @PostMapping("/remove/{id}")
    public Map<String, Object> stop(@PathVariable Integer id) throws Exception {
        String result;
        JobEntity entity = jobService.getJobEntityById(id);
        Map<String, Object> map = new HashMap<>();
        if (entity == null) {
            result = "error: id is not exist ";
            map.put("msg", result);
            map.put("code", -1);
            return map;
        }
        TriggerKey triggerKey = new TriggerKey(entity.getName(), entity.getGroup());
        JobKey jobKey = jobService.getJobKey(entity);
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        //停止触发器
        scheduler.pauseTrigger(triggerKey);
        //移除触发器
        scheduler.unscheduleJob(triggerKey);
        //删除任务
        scheduler.deleteJob(jobKey);
        //状态置为close
        JobEntity job = new JobEntity();
        job.setId(id);
        job.setStatus("CLOSE");
        job.setUpdateTime(new Date());
        jobService.edit(job);
        result = "SUCCESS STOP";
        map.put("code", 200);
        map.put("msg", result);
        return map;
    }
     /*
    //关闭所有任务
    @PostMapping("/stopAll")
    public Map<String, Object> stopAll() {
        String result;
        Map<String, Object> map = new HashMap<>();
        Scheduler scheduler = factory.getScheduler();
        try {
            if (!scheduler.isShutdown()) {
                scheduler.standby();
            }
            result = "SUCCESS STOPALL";
            map.put("code",200);
        } catch (SchedulerException e) {
            result = "Error while StopJob " + e.getMessage();
            map.put("code",-1);
            e.printStackTrace();
        }
        map.put("msg",result);
        return map;
    }
    //重启数据库中所有的Job
    @PostMapping("/refresh/all")
    public Map<String, Object> refreshAll() {
        String result;
        Map<String, Object> map = new HashMap<>();
        try {
            reStartAllJobs();
            result = "SUCCESS";
            map.put("code",200);
        } catch (Exception e) {
            result = "EXCEPTION : " + e.getMessage();
            map.put("code",-1);
            e.printStackTrace();
        }
        map.put("msg",result);
        return map;
    }
    */

    /**
     * 重新启动所有的job
     */
    private void reStartAllJobs() throws Exception {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        if (scheduler.isInStandbyMode()) {
            scheduler.start();
        }
        Set<JobKey> set = scheduler.getJobKeys(GroupMatcher.anyGroup());
        for (JobKey jobKey : set) {
            scheduler.deleteJob(jobKey);
        }
        for (JobEntity job : jobService.loadJobs()) {
            log.info("Job register name : {} , group : {} , cron : {}", job.getName(), job.getGroup(), job.getCron());
            JobDataMap map = jobService.getJobDataMap(job);
            JobKey jobKey = jobService.getJobKey(job);
            JobDetail jobDetail = jobService.geJobDetail(jobKey, job.getDescription(), map);
            if (job.getStatus().equals("OPEN")) {
                scheduler.scheduleJob(jobDetail, jobService.getTrigger(job));
            } else
                log.info("Job jump name : {} , Because {} status is {}", job.getName(), job.getName(), job.getStatus());
        }
    }

}