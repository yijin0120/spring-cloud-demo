package com.cloud.quartz.service.impl;
import com.cloud.quartz.dao.DaoSupport;
import com.cloud.quartz.model.JobEntity;
import com.cloud.quartz.service.DynamicJobService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class DynamicJobServiceImpl implements DynamicJobService {
	@Autowired
	private DaoSupport dao;
	//通过Id获取Job
	@Override
	public JobEntity getJobEntityById(Integer id) throws Exception{
		return (JobEntity) dao.findForObject("JobEntityMapper.getById",id);
	}


	//从数据库中加载获取到所有Job
	@Override
	@SuppressWarnings("unchecked")
	public List<JobEntity> loadJobs() throws Exception{
		return (List<JobEntity>) dao.findForList("JobEntityMapper.findAllOpen");
	}


	//获取JobDataMap.(Job参数对象)
	@Override
	public JobDataMap getJobDataMap(JobEntity job) throws Exception{
		JobDataMap map = new JobDataMap();
		map.put("jobId", job.getId());
		map.put("name", job.getName());
		map.put("group", job.getGroup());
		map.put("cronExpression", job.getCron());
		map.put("className", job.getClassName());
		map.put("JobDescription", job.getDescription());
		map.put("status", job.getStatus());
		return map;
	}
	//获取JobDetail,JobDetail是任务的定义,而Job是任务的执行逻辑,JobDetail里会引用一个Job Class来定义
	@Override
	@SuppressWarnings("unchecked")
	public JobDetail geJobDetail(JobKey jobKey, String description, JobDataMap map) throws Exception{
		String parm = (String) map.get("className");
		Class jobClass = Class.forName(parm);
		return JobBuilder.newJob(jobClass)
				.withIdentity(jobKey)
				.withDescription(description)
				.setJobData(map)
				.storeDurably()
				.build();
	}
	//获取Trigger (Job的触发器,执行规则)
	@Override
	public Trigger getTrigger(JobEntity job) throws Exception{
		return TriggerBuilder.newTrigger()
				.withIdentity(job.getName(), job.getGroup())
				.withSchedule(CronScheduleBuilder.cronSchedule(job.getCron()))
				.build();
	}
	//获取JobKey,包含Name和Group
	@Override
	public JobKey getJobKey(JobEntity job){
		return JobKey.jobKey(job.getName(), job.getGroup());
	}

	@Override
	public void edit(JobEntity job){
		int flag = 0;
		try {
			if (job.getId() == null) {
				job.setCreateTime(new Date());
				job.setUpdateTime(new Date());
				job.setStatus("CLOSE");
				flag = (int)dao.save("JobEntityMapper.insertSelective",job);
			} else {
				job.setUpdateTime(new Date());
				flag = (int)dao.update("JobEntityMapper.updateByPrimaryKeySelective",job);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}