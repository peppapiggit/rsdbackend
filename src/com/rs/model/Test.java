package com.rs.model;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class Test {
	public static void main(String[] args) {
		/*Calendar cal1 = Calendar.getInstance();
		Timestamp curTime = new Timestamp(new Date().getTime());
		System.out.println(curTime);
		cal1.setTime(curTime);
		cal1.add(Calendar.MINUTE, -1);
		Timestamp nextStartTime = new Timestamp(cal1
				.getTime().getTime());*/
		test1(1,true);
	}

	
	public static void test1(int pageNo,boolean firstOrNot ) {
		System.out.println(firstOrNot);
		System.out.println("pageNo...."+pageNo);
		int i=9;
		if (firstOrNot == true && pageNo == 1) {
			System.out.println("aaa");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 计算页数
			test1(10,false);
			
		}
		pageNo--;
		if(i>0) {
			if (pageNo >= 1) {
				System.out.println("....bbb");
				test1(pageNo, firstOrNot);
			} 
		}
		}
		
}
