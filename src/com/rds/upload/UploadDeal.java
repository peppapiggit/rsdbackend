package com.rds.upload;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.rds.json.ApiKcbJson;
import com.rds.json.ApiSpJson;
import com.rds.json.ApiTradeJson;
import com.rds.util.ConnectionSource;
import com.rs.model.Config;

public class UploadDeal {
	static Logger logger = Logger.getLogger(UploadDeal.class
			.getName());
	//档案上传
	public void uploadApiSp(Config config) {
		Connection conn = null;
		CloseableHttpClient httpclient  = null;
		try {
			conn = ConnectionSource.getConnection();
			if(conn != null) {
				httpclient = HttpClients.createDefault();
				long startTime =System.currentTimeMillis();
				new ApiSpJson().deal(httpclient,conn,config,"SyncGoods");
				logger.info("耗时"+(System.currentTimeMillis()-startTime));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
				try {
					if(conn !=null) {
						conn.close();
						conn = null;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//库存上传
	public void uploadApiKCB(Config config) {
		Connection conn = null;
		CloseableHttpClient httpclient  = null;
		try {
			conn = ConnectionSource.getConnection();
			if(conn != null) {
				httpclient = HttpClients.createDefault();
				long startTime =System.currentTimeMillis();
				new ApiKcbJson().deal(httpclient,conn,config,"SyncStorage");
				logger.info("耗时"+(System.currentTimeMillis()-startTime));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
				try {
					if(conn !=null) {
						conn.close();
						conn = null;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//库存上传
		public void uploadApiTrade(Config config) {
			Connection conn = null;
			CloseableHttpClient httpclient  = null;
			try {
				conn = ConnectionSource.getConnection();
				if(conn != null) {
					httpclient = HttpClients.createDefault();
					long startTime =System.currentTimeMillis();
					new ApiTradeJson().deal(httpclient,conn,config,"QueryTradeByMTime");
					logger.info("耗时"+(System.currentTimeMillis()-startTime));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					httpclient.close();
					try {
						if(conn !=null) {
							conn.close();
							conn = null;
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
}
