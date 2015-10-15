package com.rds.json;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;

import com.rds.db.GoodDB;
import com.rds.util.ApiUtil;
import com.rds.util.CharacterUtil;
import com.rs.model.Config;
import com.rs.model.Good;

public class ApiGoodsJson {
	static Logger logger = Logger.getLogger(ApiGoodsJson.class.getName());
	final int pageSize = 20;

	public void deal(CloseableHttpClient httpclient, Connection conn,
			Config config, String method) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select  ID,DPMC from API_DPMC ";
		String shopName = "";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				shopName = rs.getString("DPMC");
				this.dealApi(shopName,httpclient, conn, config, method, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}


	public void dealApi(String shopName,CloseableHttpClient httpclient, Connection conn,
			Config config, String method, int pageNo) {
		ArrayList<Good> goods = new ArrayList<Good>();
		JSONObject jsonObjSend = new JSONObject();
		jsonObjSend.put("ShopName", shopName);
		jsonObjSend.put("PageNO", pageNo);
		jsonObjSend.put("PageSize", pageSize);
		String content = jsonObjSend.toString();
		logger.info("发送内容" + content);
		String returnInfo = new ApiUtil().sendContent(httpclient, content,
				config, method);
		logger.info("接收内容" + returnInfo);
		JSONObject jsonObject = (JSONObject) JSONObject.fromObject(returnInfo);
		int returnCode = jsonObject.getInt("ResultCode");
		if (returnCode == 0) {
			// 成功
			int totalCount = jsonObject.getInt("TotalCount");
			if (totalCount > 0) {
				if (jsonObject.containsKey("GoodsList")) {
					JSONObject jsonGoodsList = jsonObject
							.getJSONObject("GoodsList");
					JSONArray jsonGoodsArray = jsonGoodsList
							.getJSONArray("Goods");
					int returnCount = jsonGoodsArray.size();
					for (int i = 0; i < returnCount; i++) {
						Good good = new Good();
						JSONObject jsonGood = (JSONObject) jsonGoodsArray
								.get(i);
						good.setShopName(jsonObject.getString("ShopName"));
						good.setNumIID(jsonGood.getString("NumIID"));
						good.setTitle(jsonGood.getString("Title"));
						good.setOuterID(jsonGood.getString("OuterID"));
						good.setSkuName(jsonGood.getString("SkuName"));
						good.setSkuOuterID(jsonGood.getString("SkuOuterID"));
						good.setGoodsNO(jsonGood.getString("GoodsNO"));
						good.setGoodsName(jsonGood.getString("GoodsName"));
						good.setSpecName(jsonGood.getString("SpecName"));
						good.setSpecCode(jsonGood.getString("SpecCode"));
						good.setSkuCode(jsonGood.getString("SkuCode"));
						good.setbFit(jsonGood.getString("bFit"));
						goods.add(good);
					}
					// 更新数据库
					GoodDB.insertData(goods, conn);
					dealApi(shopName,httpclient, conn, config, method, pageNo + 1);
				}
			}
		} else {
			logger.error("下载货品信息失败,返回码" + returnCode + ",返回信息："
					+ jsonObject.getString("ResultMsg"));
		}
	}
}
