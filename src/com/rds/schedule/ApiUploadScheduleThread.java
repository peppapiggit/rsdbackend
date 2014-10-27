package com.rds.schedule;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.rds.upload.UploadDeal;
import com.rs.model.Config;
import com.rs.model.Task;

public class ApiUploadScheduleThread extends TimerTask {
	static Logger logger = Logger.getLogger(ApiUploadScheduleThread.class
			.getName());
	private Task task;
	private Config config;
	public ApiUploadScheduleThread(Task task,Config config) {
		this.task = task;
		this.config = config;
	}
	@Override
	public void run() {
		logger.info(task.getTaskName() + "执行开始");
		if(task.getTaskName().equals("档案上传")) {
			new UploadDeal().uploadApiSp(config);
		} else if(task.getTaskName().equals("库存上传")) {
			new UploadDeal().uploadApiKCB(config);
		} else if(task.getTaskName().equals("订单下载")) {
			new UploadDeal().uploadApiTrade(config);
		}
		
		logger.info(task.getTaskName() + "执行结束");
	}
}
