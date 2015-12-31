package com.rs.model;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class Test {
	public static void main(String[] args) {
		Calendar cal1 = Calendar.getInstance();
		Timestamp curTime = new Timestamp(new Date().getTime());
		System.out.println(curTime);
		cal1.setTime(curTime);
		cal1.add(Calendar.MINUTE, -1);
		Timestamp nextStartTime = new Timestamp(cal1
				.getTime().getTime());
		System.out.println(nextStartTime);
	}

}
