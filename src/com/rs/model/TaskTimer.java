package com.rs.model;

import java.util.Timer;

public class TaskTimer {
	private String md5;
	private Timer timer;
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public Timer getTimer() {
		return timer;
	}
	public void setTimer(Timer timer) {
		this.timer = timer;
	}
	
}
