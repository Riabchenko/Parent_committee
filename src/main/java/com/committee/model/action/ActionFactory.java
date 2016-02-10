package com.committee.model.action;

/**
 *
 * @author Aliona Riabchenko
 * @version 1.0 Build 21.06.2015
 *
 *
 */
import com.committee.model.dao.AccessDAO;
import com.committee.model.dao.AdminServiceImpl;
import com.committee.model.dao.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ActionFactory {

	private static final Map<String, Action> actions;
	static {
		actions = new HashMap();
		actions.put("/FrontController", new AccessDAO());
		actions.put("/ServletUser", new UserServiceImpl());
		actions.put("/ServletAdmin", new AdminServiceImpl());

	}

	public static Action getAction(HttpServletRequest request) {
		System.out.println("1"+request.getServletPath());
		System.out.println("2"+request.getContextPath());
		System.out.println("3"+request.getAuthType());
		System.out.println("4"+request.getMethod());
		System.out.println("5"+request.getAuthType());
		System.out.println("6"+request.getPathInfo());
		System.out.println("7"+request.getRequestURI());
		System.out.println("71"+request.getRequestURL());
		System.out.println("8"+request.getMethod());
		System.out.println("9"+request.getQueryString());
		System.out.println("10"+request.getPathTranslated());
		return actions.get(request.getServletPath());
	}
}
