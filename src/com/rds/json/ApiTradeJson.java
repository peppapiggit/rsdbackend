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

import com.rds.db.TradeDB;
import com.rds.util.ApiUtil;
import com.rs.model.Config;
import com.rs.model.Trade;
import com.rs.model.TradeDetail;
import com.rs.model.TradeInfo;

public class ApiTradeJson {
	static Logger logger = Logger.getLogger(ApiTradeJson.class.getName());
	final int pageSize = 20;
	static int executeCount = 0;

	public void deal(CloseableHttpClient httpclient, Connection conn,
			Config config, String method) {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select id,TradeStatus,ZXTIME,endTime from api_down_info_trade";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int id = 0;
			while (rs.next()) {
				Timestamp startTime = rs.getTimestamp("ZXTIME");
				Timestamp endTime = new Timestamp(new Date().getTime());
				id = rs.getInt("ID");
				// 多次处理
				Calendar cal1 = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				Calendar cal3 = Calendar.getInstance();
				cal1.setTime(startTime);
				cal3.setTime(startTime);
				cal2.setTime(endTime);
				cal2.add(Calendar.MINUTE, -config.getBeforeCurTime());
				if ((cal3.before(cal2)) == true) {
					while (cal1.before(cal2) == true) {
						cal1.add(Calendar.DAY_OF_MONTH, 1);
						if (cal1.before(cal2) == true) {
							Timestamp nextStartTime = new Timestamp(cal1
									.getTime().getTime());
							this.dealApi(startTime, nextStartTime,
									rs.getString("TradeStatus"), id,
									httpclient, conn, config, method, 1, true);
							startTime = nextStartTime;
						} else {
							cal1.add(Calendar.DAY_OF_MONTH, -1);
							startTime = new Timestamp(cal1.getTime().getTime());
							this.dealApi(startTime, new Timestamp(cal2
									.getTime().getTime()), rs
									.getString("TradeStatus"), id, httpclient,
									conn, config, method, 1, true);
							break;
						}
					}
				}
				// 更新本次处理时间
				this.updateEndTime(id, conn, new Timestamp(cal2.getTime()
						.getTime()));
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
		String sql = "update api_down_info_trade set ZXTIME = ? where ID =?";
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
			Connection conn, Config config, String method, int pageNo,
			boolean firstOrNot) {
		ArrayList<TradeInfo> tradeInfoes = new ArrayList<TradeInfo>();
		JSONObject jsonObjSend = new JSONObject();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		jsonObjSend.put("StartTime", df.format(startTime));
		jsonObjSend.put("EndTime", df.format(endTime));
		jsonObjSend.put("TradeStatus", TradeStatus);
		jsonObjSend.put("PageNO", pageNo);
		jsonObjSend.put("PageSize", pageSize);
		String content = jsonObjSend.toString();
		logger.info("发送内容" + content);
		String returnInfo = new ApiUtil().sendContent(httpclient, content,
				config, method);
		returnInfo = returnInfo.replaceAll("\\\\r\\\\n", "");// 去掉回车换行
		logger.info("接收内容" + returnInfo);
		JSONObject jsonObject = (JSONObject) JSONObject.fromObject(returnInfo);
		int returnCode = jsonObject.getInt("ResultCode");

		if (returnCode == 0) {
			// 成功
			int totalCount = jsonObject.getInt("TotalCount");
			if (totalCount > 0) {
				if (firstOrNot == true && pageNo == 1) {
					// 计算页数
					pageNo = (totalCount + pageSize - 1) / pageSize;
					dealApi(startTime, endTime, TradeStatus, id, httpclient,
							conn, config, method, pageNo, false);
				} else {
					if (jsonObject.containsKey("TradeList")) {
						JSONObject jsonTradeList = jsonObject
								.getJSONObject("TradeList");
						JSONArray jsonTradeArray = jsonTradeList
								.getJSONArray("Trade");
						int returnCount = jsonTradeArray.size();
						for (int i = 0; i < returnCount; i++) {
							TradeInfo tradeInfo = new TradeInfo();
							ArrayList<TradeDetail> tradeDetails = new ArrayList<TradeDetail>();
							Trade trade = new Trade();
							TradeDetail tradeDetail = null;
							JSONObject jsonTrade = (JSONObject) jsonTradeArray
									.get(i);
							trade.setTradeNO(jsonTrade.getString("TradeNO"));
							trade.setTradeNO2(jsonTrade.getString("TradeNO2"));
							trade.setWarehouseNO(jsonTrade
									.getString("WarehouseNO"));
							trade.setRegTime(jsonTrade.getString("RegTime"));
							trade.setTradeTime(jsonTrade.getString("TradeTime"));
							trade.setPayTime(jsonTrade.getString("PayTime"));
							trade.setChkTime(jsonTrade.getString("ChkTime"));
							trade.setStockOutTime(jsonTrade
									.getString("StockOutTime"));
							trade.setSndTime(jsonTrade.getString("SndTime"));
							trade.setLastModifyTime(jsonTrade
									.getString("LastModifyTime"));
							trade.setTradeStatus(jsonTrade
									.getString("TradeStatus"));
							trade.setRefundStatus(jsonTrade
									.getString("RefundStatus"));
							trade.setbInvoice(jsonTrade.getString("bInvoice"));
							trade.setInvoiceTitle(jsonTrade
									.getString("InvoiceTitle"));
							trade.setInvoiceContent(jsonTrade
									.getString("InvoiceContent"));
							trade.setNickName(jsonTrade.getString("NickName"));
							trade.setLogisticsName(jsonTrade
									.getString("LogisticsName"));
							trade.setLogisticsID(jsonTrade
									.getString("LogisticsID"));
							trade.setSndTo(jsonTrade.getString("SndTo"));
							trade.setCountry(jsonTrade.getString("Country"));
							trade.setProvince(jsonTrade.getString("Province"));
							trade.setCity(jsonTrade.getString("City"));
							trade.setTown(jsonTrade.getString("Town"));
							trade.setAdr(jsonTrade.getString("Adr"));
							trade.setTel(jsonTrade.getString("Tel"));
							trade.setZip(jsonTrade.getString("Zip"));
							trade.setChargeType(jsonTrade
									.getString("ChargeType"));
							trade.setSellSkuCount(jsonTrade
									.getString("SellSkuCount"));
							trade.setGoodsTotal(jsonTrade
									.getString("GoodsTotal"));
							trade.setPostageTotal(jsonTrade
									.getString("PostageTotal"));
							trade.setFavourableTotal(jsonTrade
									.getString("FavourableTotal"));
							trade.setAllTotal(jsonTrade.getString("AllTotal"));
							trade.setLogisticsCode(jsonTrade
									.getString("LogisticsCode"));
							trade.setPostID(jsonTrade.getString("PostID"));
							trade.setCustomerRemark(jsonTrade
									.getString("CustomerRemark"));
							trade.setRemark(jsonTrade.getString("Remark"));
							trade.setShopType(jsonTrade.getString("ShopType"));
							trade.setShopName(jsonTrade.getString("ShopName"));
							trade.setTradeFlag(jsonTrade.getString("TradeFlag"));
							trade.setChkOperatorName(jsonTrade
									.getString("ChkOperatorName"));
							JSONObject jsonDetailList = jsonTrade
									.getJSONObject("DetailList");
							JSONArray jsonDetailArray = jsonDetailList
									.getJSONArray("Detail");
							for (int j = 0; j < jsonDetailArray.size(); j++) {
								tradeDetail = new TradeDetail();
								JSONObject jsonDetail = (JSONObject) jsonDetailArray
										.get(j);
								tradeDetail.setSkuCode(jsonDetail
										.getString("SkuCode"));
								tradeDetail.setSkuName(jsonDetail
										.getString("SkuName"));
								tradeDetail.setPlatformGoodsCode(jsonDetail
										.getString("PlatformGoodsCode"));
								tradeDetail.setPlatformGoodsName(jsonDetail
										.getString("PlatformGoodsName"));
								tradeDetail.setPlatformSkuCode(jsonDetail
										.getString("PlatformSkuCode"));
								tradeDetail.setPlatformSkuName(jsonDetail
										.getString("PlatformSkuName"));
								tradeDetail.setSellCount(jsonDetail
										.getString("SellCount"));
								tradeDetail.setSellPrice(jsonDetail
										.getString("SellPrice"));
								tradeDetail.setDiscountMoney(jsonDetail
										.getString("DiscountMoney"));
								tradeDetail.setbGift(jsonDetail
										.getString("bGift"));
								tradeDetails.add(tradeDetail);
							}
							tradeInfo.setTrade(trade);
							tradeInfo.setTradeDetails(tradeDetails);
							tradeInfoes.add(tradeInfo);
						}
						// 更新数据库
						TradeDB.insertData(tradeInfoes, conn);
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						pageNo--;
						if (pageNo >= 1) {
							dealApi(startTime, endTime, TradeStatus, id,
									httpclient, conn, config, method, pageNo,
									false);
						}

					}
				}

			}
		} else {
			logger.error("下载订单信息失败,返回码" + returnCode + ",返回信息："
					+ jsonObject.getString("ResultMsg"));
			if (returnCode == 9) {
				executeCount++;
				if (executeCount < 3) {
					logger.info("重试" + executeCount + "次");
					dealApi(startTime, endTime, TradeStatus, id, httpclient,
							conn, config, method, pageNo, firstOrNot);
				}
				executeCount = 0;
			}
		}
	}
}
