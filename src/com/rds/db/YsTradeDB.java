package com.rds.db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.rs.model.TradeDetail;
import com.rs.model.YsTrade;
import com.rs.model.YsTradeDetail;
import com.rs.model.YsTradeInfo;

public class YsTradeDB {
	public static void insertData(ArrayList<YsTradeInfo> ysTradeInfoes,Connection conn) {
		String sqlYsTradeListInsert = "INSERT INTO API_YS_TradeList(TradeNO,CurStatus,ShopType,ShopName,NickName,CustomerName,PayID,RefundStatus,Country,Province,City,Town,Adr,Zip,Phone,Email,"
				+ "CustomerRemark,Remark,PostFee,GoodsFee,TotalMoney,FavourableMoney,ChargeType,InvoiceTitle,InvoiceContent,CreateTime,LastModifyTime,TradeTime,PayTime) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sqlQueryYsTradeList = "select count(1) cnt from API_YS_TradeList where TradeNO = ?";
		String sqlYsTradeListUpdate = "UPDATE API_YS_TradeList SET  TradeNO=?,CurStatus=?,ShopType=?,ShopName=?,NickName=?,CustomerName=?,PayID=?,RefundStatus=?,Country=?,Province=?,City=?,Town=?,"
				+ "Adr=?,Zip=?,Phone=?,Email=?,CustomerRemark=?,Remark=?,PostFee=?,GoodsFee=?,TotalMoney=?,FavourableMoney=?,ChargeType=?,InvoiceTitle=?,InvoiceContent=?,CreateTime=?,"
				+ "LastModifyTime=?,TradeTime=?,PayTime=? where TradeNO=?";
		String sqlYsDetailListInsert = "INSERT INTO API_YS_DetailList(TradeNO,OrderNO,GoodsNO,GoodsName,SpecCode,SpecName,GoodsCount,Price,GoodsTotal,DiscountMoney,Remark,RefundStatus) values"
				+ "(?,?,?,?,?,?,?,?,?,?,?,?)";
		String sqlQueryYsDetailList = "select count(1) cnt from API_YS_DetailList where TradeNO = ? and OrderNo = ?";
		String sqlYsDetailListUpdate ="update API_YS_DetailList set TradeNO=?,OrderNO=?,GoodsNO=?,GoodsName=?,SpecCode=?,SpecName=?,GoodsCount=?,Price=?,GoodsTotal=?,DiscountMoney=?,Remark=?,RefundStatus=? where TradeNO = ? and OrderNO = ?";
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		for(int i=0;i<ysTradeInfoes.size();i++) {
			YsTradeInfo ysTradeInfo = ysTradeInfoes.get(i);
			YsTrade ysTrade = ysTradeInfo.getYsTrade();
			ArrayList<YsTradeDetail> ysTradeDetails = ysTradeInfo.getYsTradeDetails();
			try {
				pstmt1= conn.prepareStatement(sqlQueryYsTradeList);
				pstmt1.setString(1, ysTrade.getTradeNO());
				rs = pstmt1.executeQuery();
				int count = 0;
				while(rs.next()) {
					count = rs.getInt("cnt");
				}
				if(count ==1) {
					//更新
					pstmt = conn.prepareStatement(sqlYsTradeListUpdate);
				} else {
					pstmt = conn.prepareStatement(sqlYsTradeListInsert);
				}
				
				pstmt.setString(1, ysTrade.getTradeNO());
				pstmt.setString(2, ysTrade.getCurStatus());
				pstmt.setString(3, ysTrade.getShopType());
				pstmt.setString(4, ysTrade.getTradeNO());
				pstmt.setString(5, ysTrade.getShopName());
				pstmt.setString(6, ysTrade.getNickName());
				pstmt.setString(7, ysTrade.getCustomerName());
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
				
				if(count ==1) {
					//更新
					pstmt.setString(39, ysTrade.getTradeNO());
				}
				pstmt.executeUpdate();
				for(int j=0;j<ysTradeDetails.size();j++) {
					YsTradeDetail ysTradeDetail = ysTradeDetails.get(j);
					pstmt2 = conn.prepareStatement(sqlQueryYsDetailList);
					pstmt2.setString(1, ysTrade.getTradeNO());
					pstmt2.setString(2, ysTradeDetail.getOrderNO());
					rs = pstmt2.executeQuery();
					int countDetail = 0;
					while(rs.next()) {
						countDetail = rs.getInt("cnt");
					}
					if(countDetail ==1) {
						//更新
						pstmt = conn.prepareStatement(sqlYsDetailListUpdate);
					} else {
						//插入
						pstmt = conn.prepareStatement(sqlYsDetailListInsert);
					}
					
					pstmt.setString(1, ysTrade.getTradeNO());
					pstmt.setString(2, ysTradeDetail.getOrderNO());
					pstmt.setString(3, ysTradeDetail.getGoodsNO());
					pstmt.setString(4, ysTradeDetail.getGoodsName());
					pstmt.setString(5, ysTradeDetail.getSpecCode());
					pstmt.setString(6, ysTradeDetail.getSpecName());
					pstmt.setInt(7, Integer.parseInt(ysTradeDetail.getGoodsCount()));
					pstmt.setBigDecimal(8, new BigDecimal(ysTradeDetail.getPrice()));
					pstmt.setBigDecimal(9, new BigDecimal(ysTradeDetail.getGoodsTotal()));
					pstmt.setBigDecimal(10, new BigDecimal(ysTradeDetail.getDiscountMoney()));
					pstmt.setString(11, ysTradeDetail.getRemark());
					pstmt.setInt(12, Integer.parseInt(ysTradeDetail.getRefundStatus()));
					if(countDetail ==1) {
						//更新
						pstmt.setString(12, ysTrade.getTradeNO());
						pstmt.setString(13, ysTradeDetail.getOrderNO());
					}
					pstmt.executeUpdate();
				}
			} catch (Exception e) {
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
				if(rs != null) {
					try {
						rs.close();
						rs = null;
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
