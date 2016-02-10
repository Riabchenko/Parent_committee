package com.committee.control;

/**
 *
 * @author Aliona Riabchenko
 * @version 1.0 Build 21.06.2015
 *
 *
 */
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.committee.model.action.Action;
import com.committee.model.action.ActionFactory;
import com.committee.model.dao.UserDAO;
import com.committee.model.data.User;
import com.committee.model.util.ConnectionPool;

/**
 * Servlet implementation class ServletAdmin
 */

public class ServletUser extends DispatcherServlet {
	private static final long serialVersionUID = 1L;
	UserDAO user;
	ConnectionPool dao;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletUser() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	boolean flag = true;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		performTask(request, response);

	}

	private void performTask(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		// создание сессии и установка времени инвалидации
		HttpSession session = request.getSession();
		int timeLive = 12 * 24 * 60; // в секундах!
		session.setMaxInactiveInterval(timeLive);
		try {
			processRequest(request, response);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		dao = new ConnectionPool();
		request.getSession().setAttribute("access", "user");
		Action action = ActionFactory.getAction(request);
		user = (UserDAO) action.handleRequest(request, response, dao);

		// ------------
		User myInfo = user.getMyAccont();
		// ------------
		request.setAttribute("user", user); // delete! don't need transferring
		// ------------
		HttpSession lastAct = request.getSession();
		String la = (String) lastAct.getAttribute("lastActions");
		if (la.compareTo("/view/user.jsp") == 0) {
			super.forward("/view/user.jsp", request, response);
		}
		else {
			super.forward(la, request, response);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
