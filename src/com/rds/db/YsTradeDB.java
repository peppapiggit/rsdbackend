package com.rds.db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.rs.model.YsTrade;
import com.rs.model.YsTradeDetail;
import com.rs.model.YsTradeInfo;

public class YsTradeDB {
	static Logger logger = Logger.getLogger(YsTradeDB.class.getName());
	public static void insertData(ArrayList<YsTradeInfo> ysTradeInfoes,Connection conn) {
		String sqlYsTradeListInsert = "INSERT INTO API_YS_TradeList(TradeNO,CurStatus,ShopType,ShopName,NickName,CustomerName,PayID,RefundStatus,Country,Province,City,Town,Adr,Zip,Phone,Email,"
				+ "CustomerRemark,Remark,PostFee,GoodsFee,TotalMoney,FavourableMoney,ChargeType,InvoiceTitle,InvoiceContent,CreateTime,LastModifyTime,TradeTime,PayTime) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sqlYsTradeListDelete = "delete from API_YS_TradeList where TradeNO = ?";
		String sqlYsDetailListInsert = "INSERT INTO API_YS_DetailList(TradeNO,OrderNO,GoodsNO,GoodsName,SpecCode,SpecName,GoodsCount,Price,GoodsTotal,DiscountMoney,Remark,RefundStatus) values"
				+ "(?,?,?,?,?,?,?,?,?,?,?,?)";
		String sqlYsDetailListDelete = "delete from API_YS_DetailList where TradeNO = ?";
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		for(int i=0;i<ysTradeInfoes.size();i++) {
			YsTradeInfo ysTradeInfo = ysTradeInfoes.get(i);
			YsTrade ysTrade = ysTradeInfo.getYsTrade();
			ArrayList<YsTradeDetail> ysTradeDetails = ysTradeInfo.getYsTradeDetails();
			try {
				pstmt1= conn.prepareStatement(sqlYsTradeListDelete);
				pstmt1.setString(1, ysTrade.getTradeNO());
				pstmt1.executeUpdate();
				pstmt2 = conn.prepareStatement(sqlYsDetailListDelete);
				pstmt2.setString(1, ysTrade.getTradeNO());
				pstmt2.executeUpdate();
				pstmt = conn.prepareStatement(sqlYsTradeListInsert);
				pstmt.setString(1, ysTrade.getTradeNO());
				pstmt.setString(2, ysTrade.getCurStatus());
				pstmt.setString(3, ysTrade.getShopType());
				pstmt.setString(4, ysTrade.getShopName());
				pstmt.setString(5, ysTrade.getNickName());
				pstmt.setString(6, ysTrade.getCustomerName());
				pstmt.setString(7, ysTrade.getPayID());
				pstmt.setInt(8, Integer.parseInt(ysTrade.getRefundStatus()));
				pstmt.setString(9, ysTrade.getCountry());
		        pstmt.setString(10, ysTrade.getProvince());
		        pstmt.setString(11, ysTrade.getCity());
		        pstmt.setString(12, ysTrade.getTown());
		        pstmt.setString(13, ysTrade.getAdr());
		        pstmt.setString(14, ysTrade.getZip());
		        pstmt.setString(15, ysTrade.getPhone());
		        pstmt.setString(16, ysTrade.getEmail());
		        pstmt.setString(17, ysTrade.getCustomerRemark());
		        pstmt.setString(18, ysTrade.getRemark());
		        pstmt.setBigDecimal(19, new BigDecimal(ysTrade.getPostFee()));
				pstmt.setBigDecimal(20, new BigDecimal(ysTrade.getGoodsFee()));
				pstmt.setBigDecimal(21, new BigDecimal(ysTrade.getTotalMoney()));
				pstmt.setBigDecimal(22, new BigDecimal(ysTrade.getFavourableMoney()));
				pstmt.setInt(23, Integer.parseInt(ysTrade.getChargeType()));
				pstmt.setString(24, ysTrade.getInvoiceTitle());
			    pstmt.setString(25, ysTrade.getInvoiceContent());
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(ysTrade.getCreateTime().equals("0000-00-00 00:00:00")) {
					pstmt.setTimestamp(26, null);
				} else {
					pstmt.setTimestamp(26, new Timestamp(df.parse(ysTrade.getCreateTime()).getTime()));
				}
				if(ysTrade.getLastModifyTime().equals("0000-00-00 00:00:00")) {
					pstmt.setTimestamp(27, null);
				} else {
					pstmt.setTimestamp(27, new Timestamp(df.parse(ysTrade.getLastModifyTime()).getTime()));
				}
				if(ysTrade.getTradeTime().equals("0000-00-00 00:00:00")) {
					pstmt.setTimestamp(28, null);
				} else {
					pstmt.setTimestamp(28, new Timestamp(df.parse(ysTrade.getTradeTime()).getTime()));
				}
				if(ysTrade.getPayTime().equals("0000-00-00 00:00:00")) {
					pstmt.setTimestamp(29, null);
				} else {
					pstmt.setTimestamp(29, new Timestamp(df.parse(ysTrade.getPayTime()).getTime()));
				}
				
				pstmt.executeUpdate();
				for(int j=0;j<ysTradeDetails.size();j++) {
					YsTradeDetail ysTradeDetail = ysTradeDetails.get(j);
					int countDetail = 0;
						//插入
					pstmt = conn.prepareStatement(sqlYsDetailListInsert);
					
					pstmt.setString(1, ysTrade.getTradeNO());
					pstmt.setString(2, ysTradeDetail.getOrderNO());
					pstmt.setString(3, ysTradeDetail.getGoodsNO());
					pstmt.setString(4, ysTradeDetail.getGoodsName());
					pstmt.setString(5, ysTradeDetail.getSpecCode());
					pstmt.setString(6, ysTradeDetail.getSpecName());
					pstmt.setBigDecimal(7, new BigDecimal(ysTradeDetail.getGoodsCount()));
					pstmt.setBigDecimal(8, new BigDecimal(ysTradeDetail.getPrice()));
					pstmt.setBigDecimal(9, new BigDecimal(ysTradeDetail.getGoodsTotal()));
					pstmt.setBigDecimal(10, new BigDecimal(ysTradeDetail.getDiscountMoney()));
					pstmt.setString(11, ysTradeDetail.getRemark());
					pstmt.setInt(12, Integer.parseInt(ysTradeDetail.getRefundStatus()));
					if(countDetail ==1) {
						//更新
						pstmt.setString(13, ysTrade.getTradeNO());
						pstmt.setString(14, ysTradeDetail.getOrderNO());
					}
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
