package com.rds.db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.rs.model.Trade;
import com.rs.model.TradeDetail;
import com.rs.model.TradeInfo;

public class TradeDB {
	static Logger logger = Logger.getLogger(TradeDB.class.getName());
	public static void insertData(ArrayList<TradeInfo> tradeInfoes,Connection conn) {
		String sqlTradeListInsert = "INSERT INTO API_TradeList(TradeNO,TradeNO2,WarehouseNO,RegTime,TradeTime,PayTime,ChkTime,StockOutTime,SndTime,LastModifyTime,TradeStatus,RefundStatus,"
				+ "bInvoice,InvoiceTitle,InvoiceContent,NickName,SndTo,Country,Province,City,Town,Adr,Tel,Zip,ChargeType,SellSkuCount,GoodsTotal,PostageTotal,FavourableTotal,"
				+ "AllTotal,LogisticsCode,PostID,CustomerRemark,Remark,ShopType,ShopName,TradeFlag,ChkOperatorName) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
				+ ",?,?,?,?,?,?,?,?,?,?,?,?)";
		String sqlTradeListDelete = "delete from API_TradeList where TradeNO = ?";
		String sqlDetailListInsert = "INSERT INTO API_DetailList(TradeNO,SkuCode,SkuName,PlatformGoodsCode,PlatformGoodsName,PlatformSkuCode,PlatformSkuName,SellCount,SellPrice,DiscountMoney,bGift) values"
				+ "(?,?,?,?,?,?,?,?,?,?,?)";
		String sqlDetailListDelete = "delete from API_DetailList where TradeNO = ?";
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		for(int i=0;i<tradeInfoes.size();i++) {
			TradeInfo tradeInfo = tradeInfoes.get(i);
			Trade trade = tradeInfo.getTrade();
			ArrayList<TradeDetail> tradeDetails = tradeInfo.getTradeDetails();
			try {
				pstmt1= conn.prepareStatement(sqlTradeListDelete);
				pstmt1.setString(1, trade.getTradeNO());
				pstmt1.executeUpdate();
				pstmt2 = conn.prepareStatement(sqlDetailListDelete);
				pstmt2.setString(1, trade.getTradeNO());
				pstmt2.executeUpdate();
				
				pstmt = conn.prepareStatement(sqlTradeListInsert);
				
				pstmt.setString(1, trade.getTradeNO());
				pstmt.setString(2, trade.getTradeNO2());
				pstmt.setString(3, trade.getWarehouseNO());
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(trade.getRegTime().equals("0000-00-00 00:00:00")) {
					pstmt.setTimestamp(4, null);
				} else {
					pstmt.setTimestamp(4, new Timestamp(df.parse(trade.getRegTime()).getTime()));
				}
				
				if(trade.getTradeTime().equals("0000-00-00 00:00:00")) {
					pstmt.setTimestamp(5, null);
				} else {
					pstmt.setTimestamp(5, new Timestamp(df.parse(trade.getTradeTime()).getTime()));
				}
				if(trade.getPayTime().equals("0000-00-00 00:00:00")) {
					pstmt.setTimestamp(6, null);
				} else {
					pstmt.setTimestamp(6, new Timestamp(df.parse(trade.getPayTime()).getTime()));
				}
				if(trade.getChkTime().equals("0000-00-00 00:00:00")) {
					pstmt.setTimestamp(7, null);
				} else {
					pstmt.setTimestamp(7, new Timestamp(df.parse(trade.getChkTime()).getTime()));
				}
				
				if(trade.getStockOutTime().equals("0000-00-00 00:00:00")) {
					pstmt.setTimestamp(8, null);
				} else {
					pstmt.setTimestamp(8, new Timestamp(df.parse(trade.getStockOutTime()).getTime()));
				}
				
				if(trade.getSndTime().equals("0000-00-00 00:00:00")) {
					pstmt.setTimestamp(9, null);
				} else {
					pstmt.setTimestamp(9, new Timestamp(df.parse(trade.getSndTime()).getTime()));
				}
				if(trade.getLastModifyTime().equals("0000-00-00 00:00:00")) {
					pstmt.setTimestamp(10, null);
				} else {
					pstmt.setTimestamp(10, new Timestamp(df.parse(trade.getLastModifyTime()).getTime()));
				}
				
				pstmt.setString(11, trade.getTradeStatus());
				pstmt.setString(12, trade.getRefundStatus());
				pstmt.setInt(13, Integer.parseInt(trade.getbInvoice()));
				pstmt.setString(14, trade.getInvoiceTitle());
				pstmt.setString(15, trade.getInvoiceContent());
				pstmt.setString(16, trade.getNickName());
				pstmt.setString(17, trade.getSndTo());
				pstmt.setString(18, trade.getCountry());
				pstmt.setString(19, trade.getProvince());
				pstmt.setString(20, trade.getCity());
				pstmt.setString(21, trade.getTown());
				pstmt.setString(22, trade.getAdr());
				pstmt.setString(23, trade.getTel());
				pstmt.setString(24, trade.getZip());
				pstmt.setInt(25, Integer.parseInt(trade.getChargeType()));
				pstmt.setBigDecimal(26, new BigDecimal(trade.getSellSkuCount()));
				pstmt.setBigDecimal(27, new BigDecimal(trade.getGoodsTotal()));
				pstmt.setBigDecimal(28, new BigDecimal(trade.getPostageTotal()));
				pstmt.setBigDecimal(29, new BigDecimal(trade.getFavourableTotal()));
				pstmt.setBigDecimal(30, new BigDecimal(trade.getAllTotal()));
				pstmt.setString(31, trade.getLogisticsCode());
				pstmt.setString(32, trade.getPostID());
				pstmt.setString(33, trade.getCustomerRemark());
				pstmt.setString(34, trade.getRemark());
				pstmt.setString(35, trade.getShopType());
				pstmt.setString(36, trade.getShopName());
				pstmt.setString(37, trade.getTradeFlag());
				pstmt.setString(38, trade.getChkOperatorName());
				pstmt.executeUpdate();
				for(int j=0;j<tradeDetails.size();j++) {
					TradeDetail tradeDetail = tradeDetails.get(j);
						//插入
					pstmt = conn.prepareStatement(sqlDetailListInsert);
					pstmt.setString(1, trade.getTradeNO());
					pstmt.setString(2, tradeDetail.getSkuCode());
					pstmt.setString(3, tradeDetail.getSkuName());
					pstmt.setString(4, tradeDetail.getPlatformGoodsCode());
					pstmt.setString(5, tradeDetail.getPlatformGoodsName());
					pstmt.setString(6, tradeDetail.getPlatformSkuCode());
					pstmt.setString(7, tradeDetail.getPlatformSkuName());
					pstmt.setBigDecimal(8, new BigDecimal(tradeDetail.getSellCount()));
					pstmt.setBigDecimal(9, new BigDecimal(tradeDetail.getSellPrice()));
					pstmt.setBigDecimal(10, new BigDecimal(tradeDetail.getDiscountMoney()));
					pstmt.setInt(11, Integer.parseInt(tradeDetail.getbGift()));
					pstmt.executeUpdate();
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			} finally {
				if(pstmt != null) {
					try {
						pstmt.close();
						pstmt = null;
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if(pstmt1 != null) {
					try {
						pstmt1.close();
						pstmt1 = null;
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if(pstmt2 != null) {
					try {
						pstmt2.close();
						pstmt2 = null;
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
