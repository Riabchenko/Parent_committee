package com.committee.model.util;

/**
 *
 * @author Aliona Riabchenko
 * @version 1.0 Build 21.06.2015
 *
 *
 */
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public final class ConnectionPoolManager implements ServletContextListener {
	ConnectionPool connPool;

	public void contextInitialized(ServletContextEvent arg0) {
		connPool = ConnectionPool.getConnectionPool();
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		connPool.destroyConnectionPool();
	}

}
