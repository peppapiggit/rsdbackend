package com.rds.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.rs.model.Config;

public class ApiUtil {
	static Logger logger = Logger.getLogger(ApiUtil.class.getName());
	public String sendContent(CloseableHttpClient httpclient,String content, Config config,String method) {
		String returnInfo = "";
		String signSour = content + config.getKey();
		String md5 = MD5Util.MD5(signSour);
		String base64 = Base64Util.getBASE64(md5);
		String sign;
		CloseableHttpResponse response2 = null;
		try {
			sign = URLEncoder.encode(base64, "utf-8");
			HttpPost httpPost = new HttpPost(config.getApiUrl());
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("Method", method));
			nvps.add(new BasicNameValuePair("SellerID", config.getSellerID()));
			nvps.add(new BasicNameValuePair("InterfaceID", config.getInterfaceID()));
			nvps.add(new BasicNameValuePair("Sign", sign));
			nvps.add(new BasicNameValuePair("Content", content));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			response2 = httpclient.execute(httpPost);
			HttpEntity entity2 = response2.getEntity();
			returnInfo = EntityUtils.toString(entity2);
			EntityUtils.consume(entity2);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response2.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return returnInfo;
	}
	
	
}
