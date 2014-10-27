package com.rds.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MsSqlUtil {
	public static Connection getConn(String host,String userName,String pwd,String dbName) {
		Connection conn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection("jdbc:sqlserver://"+host+";DatabaseName="+dbName, userName, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}  
		return conn;
	}
	
	//连接测试
	public static boolean testConn(String host,String userName,String pwd,String dbName) {
		Connection conn = getConn(host,userName,pwd,dbName);
		if(conn!=null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return true;
		} else {
			return false;
		}
	}
	
	public static void close(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void close(PreparedStatement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void close(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
