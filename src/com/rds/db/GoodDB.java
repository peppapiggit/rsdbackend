package com.rds.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.rs.model.Good;

public class GoodDB {
	static Logger logger = Logger.getLogger(GoodDB.class.getName());
	public static void insertData(ArrayList<Good> goods,Connection conn) {
		String sqlTradeListInsert = "INSERT INTO API_DYGX(ShopName,NumIID,Title,OuterID,SkuName,SkuOuterID,GoodsNO,GoodsName,SpecName,SpecCode,SkuCode,bFit) values(?,?,?,?,?,?,?,?,?,?,?,?)";
		String sqlTradeListDelete = "delete from API_DYGX where ShopName=? and NumIID=? and OuterID=? and SkuOuterID=?";
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		for(int i=0;i<goods.size();i++) {
			Good good = goods.get(i);
			try {
				pstmt1= conn.prepareStatement(sqlTradeListDelete);
				pstmt1.setString(1, good.getShopName());
				pstmt1.setString(2, good.getNumIID());
				pstmt1.setString(3, good.getOuterID());
				pstmt1.setString(4, good.getSkuCode());
				pstmt1.executeUpdate();
				pstmt = conn.prepareStatement(sqlTradeListInsert);
				
				pstmt.setString(1, good.getShopName());
				pstmt.setString(2, good.getNumIID());
				pstmt.setString(3, good.getTitle());
				pstmt.setString(4, good.getOuterID());
				pstmt.setString(5, good.getSkuName());
				pstmt.setString(6, good.getGoodsNO());
				pstmt.setString(7, good.getGoodsName());
				pstmt.setString(8, good.getSkuOuterID());
				pstmt.setString(9, good.getShopName());
				pstmt.setString(10, good.getSpecCode());
				pstmt.setString(11, good.getSkuCode());
				pstmt.setString(12, good.getbFit());
				
				pstmt.executeUpdate();
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
			}
		}
	}
}
