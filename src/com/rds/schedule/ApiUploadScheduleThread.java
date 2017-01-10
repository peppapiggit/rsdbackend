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
		System.out.println(task.getTaskName() + "执行开始");
		if(task.getTaskName().equals("档案上传")) {
			new UploadDeal().uploadApiSp(config);
		} else if(task.getTaskName().equals("库存上传")) {
			new UploadDeal().uploadApiKCB(config);
		} else if(task.getTaskName().equals("订单下载")) {
			new UploadDeal().uploadApiTrade(config);
		} else if(task.getTaskName().equals("原始订单下载")) {
			new UploadDeal().uploadYsApiTrade(config);
		} else if(task.getTaskName().equals("标记修改")) {
			new UploadDeal().uploadApiFlag(config);
		} else if(task.getTaskName().equals("快递修改")) {
			new UploadDeal().uploadApiLogistics(config);
		} else if(task.getTaskName().equals("货品下载")) {
			new UploadDeal().uploadApiGoods(config);
		} else if(task.getTaskName().equals("库存下载")) {
			new UploadDeal().uploadApiHouseware(config);
		} else if(task.getTaskName().equals("查询详细出库单")) {
			new UploadDeal().downloadOutOrder(config);
		}
		System.out.println(task.getTaskName() + "执行结束");
		logger.info(task.getTaskName() + "执行结束");
	}
}
