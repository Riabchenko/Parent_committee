package com.committee.model.action;

/**
 *
 * @author Aliona Riabchenko
 * @version 1.0 Build 21.06.2015
 *
 *
 */
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.committee.model.util.ConnectionPool;

public interface Action {

	public <T> T handleRequest(HttpServletRequest request,
			HttpServletResponse response, ConnectionPool dao)
			throws ServletException, IOException;
}
