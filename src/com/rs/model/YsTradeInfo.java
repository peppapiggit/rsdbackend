package com.rs.model;

import java.util.ArrayList;

public class YsTradeInfo {
	private YsTrade ysTrade;
	private ArrayList<YsTradeDetail> YsTradeDetails;
	public YsTrade getYsTrade() {
		return ysTrade;
	}
	public void setYsTrade(YsTrade ysTrade) {
		this.ysTrade = ysTrade;
	}
	public ArrayList<YsTradeDetail> getYsTradeDetails() {
		return YsTradeDetails;
	}
	public void setYsTradeDetails(ArrayList<YsTradeDetail> ysTradeDetails) {
		YsTradeDetails = ysTradeDetails;
	}
	
}
