package com.rds.util;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

public class ConnectionSource {
    private static BasicDataSource dataSource = null;

    public ConnectionSource() {
    }

    public static void init() {
        if (dataSource != null) {
            try {
                dataSource.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            dataSource = null;
        }
        InputStream in = null;
        try {
            Properties p = new Properties();
            Properties proConf = new Properties();
            in = new BufferedInputStream(new FileInputStream(
					"config.properties"));
            proConf.load(in);
            p.setProperty("driverClassName", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
            p.setProperty("url", "jdbc:sqlserver://"+proConf.getProperty("host")+";DatabaseName="+proConf.getProperty("dbName"));
            p.setProperty("password", proConf.getProperty("pwd"));
            p.setProperty("username", proConf.getProperty("userName"));
            p.setProperty("maxActive", "40");
            p.setProperty("maxIdle", "10");
            p.setProperty("maxWait", "6okk0000");
            p.setProperty("removeAbandoned", "false");
            p.setProperty("removeAbandonedTimeout", "120");
            p.setProperty("testOnBorrow", "true");
            p.setProperty("logAbandoned", "true");
            dataSource = (BasicDataSource) BasicDataSourceFactory.createDataSource(p);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	if(in !=null) {
        		try {
					in.close();
					in = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
        		
        	}
        }
    }


    public static synchronized Connection getConnection() throws  SQLException {
        if (dataSource == null) {
            init();
        }
        Connection conn = null;
        if (dataSource != null) {
            conn = dataSource.getConnection();
        }
        return conn;
    }
}