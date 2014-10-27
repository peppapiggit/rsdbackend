package com.rds.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {
	/**
	 * 
	 * @param startTime：设置的任务执行开始时间
	 * @param type:类型，0代表每天执行一次，1代表周期任务
	 * @return：任务真正执行开始执行时间
	 */
	public Date getExeTime(String setTime,int type) {
		Date newStartTime = null;
		Calendar calNow = Calendar.getInstance();
		calNow.setTime(new Date());
		Calendar calSetTime = Calendar.getInstance();
		calSetTime.setTime(this.setStartTime(setTime));
		int result=calNow.compareTo(calSetTime);
		if(result<0) {
			//现在时间<（今日日期+开始时间）,开始执行时间：今日日期+开始时间(即传入的时间)
			newStartTime= calSetTime.getTime();
		} else {
			//现在时间>（今日日期+开始时间）,如果类型为每天则输出  次日日期+开始时间
			//如果类型为周期任务则输出  今天日期+现在时间+1分钟
			if(type==0) {
				calSetTime.add(Calendar.DAY_OF_MONTH,1);
				newStartTime= calSetTime.getTime();
			} else {
				calNow.add(Calendar.MINUTE,1);
				newStartTime= calNow.getTime();
			}
		}
		return newStartTime;
	}
	
	/**
	 * 
	 * @param 开始时间
	 * @return 当前日期+时间
	 */
	private Date setStartTime(String startTime) {
		Date newTime = null;
		SimpleDateFormat dfDate=new SimpleDateFormat("yyyy-MM-dd");
		Date nowDate=new Date();
		String nowDateStr = dfDate.format(nowDate);
		String startDateTime = nowDateStr + " " + startTime;
		SimpleDateFormat dfTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			newTime = dfTime.parse(startDateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return newTime;
	}

}
