package com.rs.model;

public class Config {
	private String apiUrl;
	private String SellerID;
	private String InterfaceID;
	private String key;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getApiUrl() {
		return apiUrl;
	}
	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	public String getSellerID() {
		return SellerID;
	}
	public void setSellerID(String sellerID) {
		SellerID = sellerID;
	}
	public String getInterfaceID() {
		return InterfaceID;
	}
	public void setInterfaceID(String interfaceID) {
		InterfaceID = interfaceID;
	}
	
	
}
