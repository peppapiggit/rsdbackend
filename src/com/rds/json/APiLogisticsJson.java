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

import com.rds.util.ApiUtil;
import com.rds.util.CharacterUtil;
import com.rs.model.Config;

public class APiLogisticsJson {
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
				System.out.println(jsonObjRs);
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
			String resInfo = CharacterUtil.unicodeToUtf8(returnInfo);
			JSONObject jsonObject = (JSONObject)JSONObject.fromObject(resInfo);
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
		String returnInfo = new ApiUtil().sendContent(httpclient, content,config,method);
		this.updateDB(id, conn, returnInfo);
	}
}
