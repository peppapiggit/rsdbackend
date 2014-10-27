package com.rds.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.rs.model.Task;

public class TaskDB {
	public ArrayList<Task> getAllList(Connection conn){
		ArrayList<Task> tasks = new ArrayList<Task>();
		Task task = null;
		String sql = "select id,task_name,table_name,start_time,task_type,valid,period,md5 from test.dbo.apiupload_task_info";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				task =  new Task();
				task.setId(rs.getInt("id"));
				task.setTaskName(rs.getString("task_name"));
				task.setTableName(rs.getString("table_name"));
				SimpleDateFormat sdf = new SimpleDateFormat(
						"HH:mm:ss");
				Timestamp timestamp = rs.getTimestamp("start_time");
				task.setStartTime(sdf.format(timestamp));
				task.setTaskType(rs.getInt("task_type"));
				task.setValid(rs.getInt("valid"));
				task.setMd5(rs.getString("md5"));
				task.setPeriod(rs.getInt("period"));
				tasks.add(task);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tasks;
	}
}
