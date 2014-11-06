package com.rds.json;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;

import com.rds.db.YsTradeDB;
import com.rds.util.ApiUtil;
import com.rs.model.Config;
import com.rs.model.YsTrade;
import com.rs.model.YsTradeDetail;
import com.rs.model.YsTradeInfo;

public class ApiYsTradeJson {
	static Logger logger = Logger.getLogger(ApiYsTradeJson.class.getName());
	final int pageSize = 20;
	static int executeCount = 0;

	public void deal(CloseableHttpClient httpclient, Connection conn,
			Config config, String method) {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select id,TradeStatus,ZXTIME,endTime from api_down_info_ystrade";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int id = 0;
			while (rs.next()) {
				id = rs.getInt("id");
				Timestamp startTime = rs.getTimestamp("ZXTIME");
				Timestamp endTime = new Timestamp(new Date().getTime());
				// 多次处理
				Calendar cal1 = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				Calendar cal3 = Calendar.getInstance();
				cal1.setTime(startTime);
				cal3.setTime(startTime);
				cal2.setTime(endTime);
				if ((cal3.before(cal2)) == true) {
					while (cal1.before(cal2) == true) {
						cal1.add(Calendar.DAY_OF_MONTH, 1);
						if (cal1.before(cal2) == true) {
							Timestamp nextStartTime = new Timestamp(cal1
									.getTime().getTime());
							this.dealApi(startTime, nextStartTime,
									rs.getString("TradeStatus"), id,
									httpclient, conn, config, method, 1);
							startTime = nextStartTime;
						} else {
							cal1.add(Calendar.DAY_OF_MONTH, -1);
							startTime = new Timestamp(cal1.getTime().getTime());
							this.dealApi(startTime, endTime,
									rs.getString("TradeStatus"), id,
									httpclient, conn, config, method, 1);
							break;
						}
					}
				}

				// 更新本次处理时间
				this.updateEndTime(id, conn, endTime);
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

	// 更新时间
	public void updateEndTime(int id, Connection conn, Timestamp endTime) {
		String sql = "update api_down_info_ystrade set ZXTIME = ? where ID =?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setTimestamp(1, endTime);
			pstmt.setInt(2, id);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void dealApi(Timestamp startTime, Timestamp endTime,
			String TradeStatus, int id, CloseableHttpClient httpclient,
			Connection conn, Config config, String method, int pageNo) {
		System.out.println("处理"+pageNo+"页");
		ArrayList<YsTradeInfo> ysTradeInfoes = new ArrayList<YsTradeInfo>();
		JSONObject jsonObjSend = new JSONObject();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		jsonObjSend.put("StartTime", df.format(startTime));
		jsonObjSend.put("EndTime", df.format(endTime));
		jsonObjSend.put("CurStatus", TradeStatus);
		jsonObjSend.put("PageNO", pageNo);
		jsonObjSend.put("PageSize", pageSize);
		String content = jsonObjSend.toString();
		logger.info("发送内容" + content);
		String returnInfo = new ApiUtil().sendContent(httpclient, content,
				config, method);
		logger.info("接收解析内容" + returnInfo);
		returnInfo = returnInfo.replaceAll("\\\\r\\\\n", "");// 去掉回车换行
		
		//String resInfo = CharacterUtil.unicodeToUtf8(returnInfo);
		//System.out.println(resInfo);
		JSONObject jsonObject = (JSONObject) JSONObject.fromObject(returnInfo);
		int returnCode = jsonObject.getInt("ResultCode");
		if (returnCode == 0) {
			// 成功
			int totalCount = jsonObject.getInt("TotalCount");
			if (totalCount > 0) {
				if (jsonObject.containsKey("TradeList")) {
					JSONObject jsonTradeList = jsonObject
							.getJSONObject("TradeList");
					JSONArray jsonTradeArray = jsonTradeList
							.getJSONArray("Trade");
					int returnCount = jsonTradeArray.size();
					for (int i = 0; i < returnCount; i++) {
						YsTradeInfo ysTradeInfo = new YsTradeInfo();
						ArrayList<YsTradeDetail> ysTradeDetails = new ArrayList<YsTradeDetail>();
						YsTrade ysTrade = new YsTrade();
						YsTradeDetail ysTradeDetail = null;
						JSONObject jsonTrade = (JSONObject) jsonTradeArray
								.get(i);
						ysTrade.setTradeNO(jsonTrade.getString("TradeNO"));
						ysTrade.setShopType(jsonTrade.getString("ShopType"));
						ysTrade.setShopName(jsonTrade.getString("ShopName"));
						ysTrade.setCurStatus(jsonTrade.getString("CurStatus"));
						ysTrade.setRefundStatus(jsonTrade
								.getString("RefundStatus"));
						ysTrade.setPayID(jsonTrade.getString("PayID"));
						ysTrade.setInvoiceTitle(jsonTrade
								.getString("InvoiceTitle"));
						ysTrade.setInvoiceContent(jsonTrade
								.getString("InvoiceContent"));
						ysTrade.setNickName(jsonTrade.getString("NickName"));
						ysTrade.setCustomerName(jsonTrade
								.getString("CustomerName"));
						ysTrade.setCountry(jsonTrade.getString("Country"));
						ysTrade.setProvince(jsonTrade.getString("Province"));
						ysTrade.setCity(jsonTrade.getString("City"));
						ysTrade.setTown(jsonTrade.getString("Town"));
						ysTrade.setAdr(jsonTrade.getString("Adr"));
						ysTrade.setPhone(jsonTrade.getString("Phone"));
						ysTrade.setZip(jsonTrade.getString("Zip"));
						ysTrade.setEmail(jsonTrade.getString("Email"));
						ysTrade.setChargeType(jsonTrade.getString("ChargeType"));
						ysTrade.setGoodsFee(jsonTrade.getString("GoodsFee"));
						ysTrade.setPostFee(jsonTrade.getString("PostFee"));
						ysTrade.setFavourableMoney(jsonTrade
								.getString("FavourableMoney"));
						ysTrade.setTotalMoney(jsonTrade.getString("TotalMoney"));
						ysTrade.setCustomerRemark(jsonTrade
								.getString("CustomerRemark"));
						ysTrade.setRemark(jsonTrade.getString("Remark"));
						ysTrade.setCreateTime(jsonTrade.getString("CreateTime"));
						ysTrade.setTradeTime(jsonTrade.getString("TradeTime"));
						ysTrade.setPayTime(jsonTrade.getString("PayTime"));
						ysTrade.setLastModifyTime(jsonTrade
								.getString("LastModifyTime"));
						JSONObject jsonDetailList = jsonTrade
								.getJSONObject("ItemList");
						JSONArray jsonDetailArray = jsonDetailList
								.getJSONArray("Detail");
						for (int j = 0; j < jsonDetailArray.size(); j++) {
							ysTradeDetail = new YsTradeDetail();
							JSONObject jsonDetail = (JSONObject) jsonDetailArray
									.get(j);
							ysTradeDetail.setTradeNO(jsonTrade
									.getString("TradeNO"));
							ysTradeDetail.setOrderNO(jsonDetail
									.getString("OrderNO"));
							ysTradeDetail.setGoodsNO(jsonDetail
									.getString("GoodsNO"));
							ysTradeDetail.setGoodsName(jsonDetail
									.getString("GoodsName"));
							ysTradeDetail.setSpecCode(jsonDetail
									.getString("SpecCode"));
							ysTradeDetail.setSpecName(jsonDetail
									.getString("SpecName"));
							ysTradeDetail.setGoodsCount(jsonDetail
									.getString("GoodsCount"));
							ysTradeDetail.setPrice(jsonDetail
									.getString("Price"));
							ysTradeDetail.setGoodsTotal(jsonDetail
									.getString("GoodsTotal"));
							ysTradeDetail.setDiscountMoney(jsonDetail
									.getString("DiscountMoney"));
							ysTradeDetail.setRemark(jsonDetail
									.getString("Remark"));
							ysTradeDetail.setRefundStatus(jsonDetail
									.getString("RefundStatus"));
							ysTradeDetails.add(ysTradeDetail);
						}
						ysTradeInfo.setYsTrade(ysTrade);
						ysTradeInfo.setYsTradeDetails(ysTradeDetails);
						ysTradeInfoes.add(ysTradeInfo);
					}
					// 更新数据库
					YsTradeDB.insertData(ysTradeInfoes, conn);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					dealApi(startTime, endTime, TradeStatus, id, httpclient,
							conn, config, method, pageNo + 1);
					
				}

			}
		} else {
			logger.error("下载原始订单信息失败,返回码" + returnCode + ",返回信息："
					+ jsonObject.getString("ResultMsg"));
			
			if(returnCode==9) {
				executeCount ++;
				if(executeCount<3) {
					logger.info("重试"+executeCount+"次");
					dealApi(startTime, endTime, TradeStatus, id, httpclient,
							conn, config, method, pageNo);
				}
				executeCount = 0;
			}
			
		}
	}
}
