package com.rs.model;

import java.util.ArrayList;

public class TradeInfo {
	private Trade trade;
	private ArrayList<TradeDetail> TradeDetails;
	public Trade getTrade() {
		return trade;
	}
	public void setTrade(Trade trade) {
		this.trade = trade;
	}
	public ArrayList<TradeDetail> getTradeDetails() {
		return TradeDetails;
	}
	public void setTradeDetails(ArrayList<TradeDetail> tradeDetails) {
		TradeDetails = tradeDetails;
	}
	
}
