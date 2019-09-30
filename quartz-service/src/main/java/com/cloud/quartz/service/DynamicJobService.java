package com.cloud.quartz.service;
import com.cloud.quartz.model.JobEntity;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;

import java.util.List;


public interface DynamicJobService {
	//通过Id获取Job
	JobEntity getJobEntityById(Integer id) throws Exception;

	//从数据库中加载获取到所有Job
	List<JobEntity> loadJobs() throws Exception;

	JobDataMap getJobDataMap(JobEntity job) throws Exception;

	JobDetail geJobDetail(JobKey jobKey, String description, JobDataMap map) throws Exception;

	Trigger getTrigger(JobEntity job) throws Exception;

	JobKey getJobKey(JobEntity job);

	void edit(JobEntity job);
}