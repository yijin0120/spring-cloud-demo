package com.cloud.quartz.service;


import org.quartz.JobExecutionException;

public interface JobTestService  {
	void test() throws JobExecutionException;
}
