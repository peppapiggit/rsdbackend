package com.rds.schedule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import org.apache.log4j.Logger;

import com.rds.util.DateUtil;
import com.rs.model.Config;
import com.rs.model.Task;


public class ApiUploadSchedule {
	static Logger logger = Logger.getLogger(ApiUploadSchedule.class.getName());
	public static void taskschedule(Task task,Timer timer,Config config) {
		logger.info("设置"+task.getTaskName()+"开始执行时间："+task.getStartTime());
		Date exeTime = new DateUtil().getExeTime(task.getStartTime(), task.getTaskType());
		DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");       
		logger.info(task.getTaskName()+"实际开始执行时间："+fmt.format(exeTime));
		timer.scheduleAtFixedRate(new ApiUploadScheduleThread(task,config), exeTime, task.getPeriod()*60*1000);
		
		}
	
	public static void tasksCancle(Task task,Timer timer) {
		logger.info(task.getTaskName()+"定时器取消");
		timer.cancel();
	}
}
