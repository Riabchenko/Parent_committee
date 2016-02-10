package com.committee.control;

/**
 *
 * @author Aliona Riabchenko
 * @version 1.0 Build 21.06.2015
 *
 *
 */
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servlet implementation class ServletStart
 */
public class ServletStart extends DispatcherServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String login = request.getParameter("telephone");
		String password = request.getParameter("password");
		// for telephone
		Pattern p = Pattern.compile("^([0-9]{4,15})$");
		Matcher m = p.matcher(login);
		if (login == "" || password == "") {
			request.setAttribute("message", "001");
			super.forward("/index.jsp", request, response);
		}
		else {
			if (m.find()) {
				super.forward("/FrontController", request, response);
			}else {
				request.setAttribute("message", "003");
				super.forward("/index.jsp", request, response);
			}
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession act = request.getSession();
		act.setAttribute("lastActions", "/index.jsp");
		super.forward("/index.jsp", request, response);
	}

}
