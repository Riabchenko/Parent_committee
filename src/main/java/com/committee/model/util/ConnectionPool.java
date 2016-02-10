package com.committee.model.util;

/**
 *
 * @author Aliona Riabchenko
 * @version 1.0 Build 21.06.2015
 *
 *
 */


import com.committee.model.dao.UserServiceImpl;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;

public class ConnectionPool {
	 public static ConnectionPool connectonPool;
	
	//Log4j logger
		 private static final Logger log = Logger.getLogger(UserServiceImpl.class);

	 
    private Queue<Connection> availableConns = new LinkedList<Connection>();

//    private String DRIVER = "com.mysql.jdbc.Driver";
    private String URL = "jdbc:mysql://localhost:3306/ParentsCommittee"; //DB
    private String USER_NAME = "root";
    private String PASSWORD = "1";

  private String DRIVER = "com.mysql.jdbc.Driver";
//	private String URL = "jdbc:mysql://sql4.freemysqlhosting.net:3306/sql482163"; // DB
//	private String USER_NAME = "sql482163";
//	private String PASSWORD = "zK8%kL8*";
//	private String URL = "jdbc:mysql://us-cdbr-iron-east-02.cleardb.net:3306/ad_966d650b70e5c2b?reconnect=true"; // DB
//	private String USER_NAME = "b83b2f2476b7e2";
//	private String PASSWORD = "a3fbbe82bcf2df2";//"b0a507b6";//"a3fbbe82bcf2df2";


    
    private int INIT_COUNT = 4;


    public ConnectionPool() {
        try {
            Class.forName(DRIVER);
        } catch (Exception e) {
        	log.error("Didn't find a driver! "+e.getMessage());
        }
//        for (int i = 0; i < INIT_COUNT; i++) {
//            availableConns.add(getConnection());
//        }
    }
    
    public static ConnectionPool getConnectionPool(){
    	if(connectonPool == null) {
    		connectonPool = new ConnectionPool();
    	}
    	return connectonPool;
    }

    private Connection getConnection() {
        Connection conn = null;
        try {
       // Параметры соединения с базой
          Properties connInfo = new Properties();

          connInfo.put("user",USER_NAME);
          connInfo.put("password",PASSWORD);
          connInfo.put("useUnicode","true");          
          connInfo.put("characterEncoding","UTF-8");
          conn = DriverManager.getConnection(URL, connInfo);
        } catch (Exception e) {
        	log.error("Didn't creat the connection with DB! "+e.getMessage());
        }
        return conn;
    }

    /**
     * This method retrieve the connection
     * @return
     * @throws SQLException
     */
    public synchronized Connection retrieve() throws SQLException {
//        Connection newConn = null;
//        if (availableConns.size() == 0) {
//            newConn = getConnection();
//        } else {
//            newConn = (Connection) availableConns.poll();
//        }
        return getConnection();
    }

    public synchronized void putback(Connection c) throws NullPointerException {
        try {
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//    if (c != null) {
//        if (availableConns.offer(c)) {
//        } else {
//            log.error("Connection not in the usedConns array");
//        }
//     }
    }
    
    public void destroyConnectionPool(){
    	 for (Connection conn : availableConns) {
         try {
					conn.close();
				}
				catch (SQLException e) {
					log.error("Connection conn didn't close!");
				}
    	 }

     	 availableConns = null;
    	 connectonPool = null;
    }

    public int getAvailableConnsCnt() {
        return availableConns.size();
    }
}

