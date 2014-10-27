package com.rds.task;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;

import com.rds.db.TaskDB;
import com.rds.schedule.ApiUploadSchedule;
import com.rds.util.ConnectionSource;
import com.rs.model.Config;
import com.rs.model.Task;
import com.rs.model.TaskTimer;

public class TaskMonitorThread implements Runnable {
	public static Map<Integer, TaskTimer> taskMapper = new HashMap<Integer, TaskTimer>();
	Config config = null;
	public TaskMonitorThread(Config config) {
		Properties p = new Properties();
		 InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(
					"config.properties"));
			p.load(in);
			config.setApiUrl(p.getProperty("apiUrl"));
			config.setSellerID(p.getProperty("SellerID"));
			config.setInterfaceID(p.getProperty("InterfaceID"));
			this.config = config;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
					in = null;
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	@Override
	public void run() {
		while (true) {
			System.out.println("任务监控线程启动");
			Connection conn = null;
			try {
				// 查询任务
				conn = ConnectionSource.getConnection();
				if (conn != null) {
					ArrayList<Task> tasks = new TaskDB().getAllList(conn);
					for (int i = 0; i < tasks.size(); i++) {
						Task task = tasks.get(i);
						// 判断是否存在
						if (taskMapper.containsKey(task.getId())) {
							// 以前存在
							// 判断md5是否变化
							TaskTimer taskTimer = taskMapper.get(task.getId());
							if (taskTimer.getMd5().equals(task.getMd5()) == false) {
								// 以前任务存在并且任务变化了
								// 判断是否置为无效
								if (task.getValid() == 0) {
									// 取消任务
									System.out.println("存在任务置为无效,任务id"
											+ task.getId());
									taskMapper.remove(task.getId());
									ApiUploadSchedule.tasksCancle(task,
											taskTimer.getTimer());
								} else {
									// 取消任务，添加新任务
									System.out.println("修改存在任务,任务id"
											+ task.getId());
									taskMapper.remove(task.getId());
									TaskTimer task1 = new TaskTimer();
									Timer timer = new Timer();
									task1.setMd5(task.getMd5());
									task1.setTimer(timer);
									taskMapper.put(task.getId(), task1);
									ApiUploadSchedule.tasksCancle(task,
											taskTimer.getTimer());
									ApiUploadSchedule.taskschedule(task, timer,config);
								}
							}
						} else {
							// 以前不存在
							// 判断是否置为无效，有效为新增任务
							if (task.getValid() == 1) {
								// 取消任务
								System.out.println("新增任务,任务id" + task.getId());
								TaskTimer taskTimer = new TaskTimer();
								Timer timer = new Timer();
								taskTimer.setMd5(task.getMd5());
								taskTimer.setTimer(timer);
								taskMapper.put(task.getId(), taskTimer);
								ApiUploadSchedule.taskschedule(task, timer,config);
							}
						}
					}
				}
				Thread.sleep(10000);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					conn = null;
				}

			}
		}
	}

}
