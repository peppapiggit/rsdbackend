package com.rds.json;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;

import com.rds.util.ApiUtil;
import com.rds.util.CharacterUtil;
import com.rs.model.Config;

public class ApiSpJson {
	static Logger logger = Logger.getLogger(ApiSpJson.class
			.getName());
	public void  deal(CloseableHttpClient httpclient,Connection conn, Config config,String method) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String content ="";
		String sql = "select  ID,HH,MC,ZL,JE,TM from API_SP  where zt = 0 ";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			JSONArray jsonArray = null ;
			JSONObject jsonObjRs = null;
			int id = 0;
			while (rs.next()) {
				jsonArray = new JSONArray();
				jsonObjRs = new JSONObject();
				jsonObjRs.put("GoodsNO", rs.getString("HH"));
				jsonObjRs.put("GoodsName", rs.getString("MC"));
				jsonObjRs.put("Weight", rs.getString("ZL"));
				jsonObjRs.put("Price", rs.getString("JE"));
				jsonObjRs.put("Barcode", rs.getString("TM"));
				jsonArray.add(jsonObjRs);
				id = rs.getInt("ID");
				Map<String, JSONArray> goodsMap = new HashMap<String, JSONArray>();
				goodsMap.put("Goods", jsonArray);
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("GoodsList", goodsMap);
				content = jsonObj.toString();
				this.dealApi(httpclient, content,id,conn, config,method);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs !=null) {
					rs.close();
					rs = null;
				}
				if(pstmt !=null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//更新本地数据库上传状态
	public void updateDB(int id,Connection conn,String returnInfo) {
		String sql = "update API_SP  set ZT=1,APITIME=getdate(),HK = ?,HKMSG = ? where ID = ?";
		PreparedStatement pstmt = null;
		try {
			JSONObject jsonObject = (JSONObject)JSONObject.fromObject(returnInfo);
			pstmt = conn.prepareStatement(sql);
			int returnCode = jsonObject.getInt("ResultCode");
			pstmt.setInt(1, returnCode);
			if(returnCode==0) {
				pstmt.setString(2, "");
			} else {
				pstmt.setString(2, jsonObject.getString("ResultMsg"));
			}
			pstmt.setInt(3, id);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt !=null) {
					pstmt.close();
					pstmt = null;
				}
						
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void dealApi(CloseableHttpClient httpclient,String content,int id,Connection conn, Config config,String method) {
		logger.info("发送内容" + content);
		String returnInfo = new ApiUtil().sendContent(httpclient, content,config,method);
		logger.info("接收内容" + returnInfo);
		this.updateDB(id, conn, returnInfo);
	}
}
