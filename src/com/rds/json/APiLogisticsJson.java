package com.rds.json;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.json.JSONObject;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;

import com.rds.util.ApiUtil;
import com.rds.util.CharacterUtil;
import com.rs.model.Config;

public class APiLogisticsJson {
	static Logger logger = Logger.getLogger(APiLogisticsJson.class
			.getName());
	public void  deal(CloseableHttpClient httpclient,Connection conn, Config config,String method) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String content ="";
		String sql = "select  ID,TradeNO,LogisticsCode from API_GWL  where zt = 0 ";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			JSONObject jsonObjRs = null;
			int id = 0;
			while (rs.next()) {
				jsonObjRs = new JSONObject();
				jsonObjRs.put("TradeNO", rs.getString("TradeNO"));
				jsonObjRs.put("Flag", "");
				jsonObjRs.put("LogisticsCode", rs.getObject("LogisticsCode"));
				id = rs.getInt("ID");
				content = jsonObjRs.toString();
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
		String sql = "update API_GWL  set ZT=1,APITIME=getdate(),HK = ?,HKMSG = ? where ID = ?";
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
