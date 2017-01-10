package com.rds.upload;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.rds.json.APiLogisticsJson;
import com.rds.json.ApiFlagJson;
import com.rds.json.ApiGoodsJson;
import com.rds.json.ApiKcbJson;
import com.rds.json.ApiOutOrderJson;
import com.rds.json.ApiSpJson;
import com.rds.json.ApiTradeJson;
import com.rds.json.ApiWarehouseJson;
import com.rds.json.ApiYsTradeJson;
import com.rds.util.ConnectionSource;
import com.rs.model.Config;

public class UploadDeal {
	static Logger logger = Logger.getLogger(UploadDeal.class.getName());

	// 档案上传
	public void uploadApiSp(Config config) {
		Connection conn = null;
		CloseableHttpClient httpclient = null;
		try {
			conn = ConnectionSource.getConnection();
			if (conn != null) {
				httpclient = HttpClients.createDefault();
				long startTime = System.currentTimeMillis();
				new ApiSpJson().deal(httpclient, conn, config, "SyncGoods");
				logger.info("耗时" + (System.currentTimeMillis() - startTime));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
				try {
					if (conn != null) {
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

	// 库存上传
	public void uploadApiKCB(Config config) {
		Connection conn = null;
		CloseableHttpClient httpclient = null;
		try {
			conn = ConnectionSource.getConnection();
			if (conn != null) {
				httpclient = HttpClients.createDefault();
				long startTime = System.currentTimeMillis();
				new ApiKcbJson().deal(httpclient, conn, config, "SyncStorage");
				logger.info("耗时" + (System.currentTimeMillis() - startTime));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
				try {
					if (conn != null) {
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

	// 订单下载
	public void uploadApiTrade(Config config) {
		Connection conn = null;
		CloseableHttpClient httpclient = null;
		try {
			conn = ConnectionSource.getConnection();
			if (conn != null) {
				httpclient = HttpClients.createDefault();
				long startTime = System.currentTimeMillis();
				new ApiTradeJson().deal(httpclient, conn, config,
						"QueryTradeByMTime");
				logger.info("耗时" + (System.currentTimeMillis() - startTime));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
				try {
					if (conn != null) {
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
	
	// 库存下载
		public void uploadApiHouseware(Config config) {
			Connection conn = null;
			CloseableHttpClient httpclient = null;
			try {
				conn = ConnectionSource.getConnection();
				if (conn != null) {
					httpclient = HttpClients.createDefault();
					long startTime = System.currentTimeMillis();
					new ApiWarehouseJson().deal(httpclient, conn, config,
							"QueryStorage");
					logger.info("耗时" + (System.currentTimeMillis() - startTime));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					httpclient.close();
					try {
						if (conn != null) {
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
		
		// 查询详细出库单
		public void downloadOutOrder(Config config) {
			Connection conn = null;
			CloseableHttpClient httpclient = null;
			try {
				conn = ConnectionSource.getConnection();
				if (conn != null) {
					httpclient = HttpClients.createDefault();
					long startTime = System.currentTimeMillis();
					new ApiOutOrderJson().deal(httpclient, conn, config,
							"QueryStockoutOrder");
					logger.info("耗时" + (System.currentTimeMillis() - startTime));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					httpclient.close();
					try {
						if (conn != null) {
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

	// 原始订单下载
	public void uploadYsApiTrade(Config config) {
		Connection conn = null;
		CloseableHttpClient httpclient = null;
		try {
			conn = ConnectionSource.getConnection();
			if (conn != null) {
				httpclient = HttpClients.createDefault();
				long startTime = System.currentTimeMillis();
				new ApiYsTradeJson().deal(httpclient, conn, config,
						"QueryOriginalTradeByTime");
				logger.info("耗时" + (System.currentTimeMillis() - startTime));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
				try {
					if (conn != null) {
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

	//标记修改
	public void uploadApiFlag(Config config) {
		Connection conn = null;
		CloseableHttpClient httpclient = null;
		try {
			conn = ConnectionSource.getConnection();
			if (conn != null) {
				httpclient = HttpClients.createDefault();
				long startTime = System.currentTimeMillis();
				new ApiFlagJson().deal(httpclient, conn, config, "ModifyTrade");
				logger.info("耗时" + (System.currentTimeMillis() - startTime));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
				try {
					if (conn != null) {
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
	
	//货品下载
		public void uploadApiGoods(Config config) {
			Connection conn = null;
			CloseableHttpClient httpclient = null;
			try {
				conn = ConnectionSource.getConnection();
				if (conn != null) {
					httpclient = HttpClients.createDefault();
					long startTime = System.currentTimeMillis();
					new ApiGoodsJson().deal(httpclient, conn, config, "QueryPlatformGoodsInfo");
					logger.info("耗时" + (System.currentTimeMillis() - startTime));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					httpclient.close();
					try {
						if (conn != null) {
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

	// 快递修改
	public void uploadApiLogistics(Config config) {
		Connection conn = null;
		CloseableHttpClient httpclient = null;
		try {
			conn = ConnectionSource.getConnection();
			if (conn != null) {
				httpclient = HttpClients.createDefault();
				long startTime = System.currentTimeMillis();
				new APiLogisticsJson().deal(httpclient, conn, config, "ModifyTrade");
				logger.info("耗时" + (System.currentTimeMillis() - startTime));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
				try {
					if (conn != null) {
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
