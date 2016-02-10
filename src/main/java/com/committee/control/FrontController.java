package com.committee.control;

/**
 *
 * @author Aliona Riabchenko
 * @version 1.0 Build 21.06.2015
 *
 *
 */
import com.committee.model.action.Action;
import com.committee.model.action.ActionFactory;
import com.committee.model.util.ConnectionPool;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//@WebServlet("/FrontController")
public class FrontController extends DispatcherServlet {

	private static final long serialVersionUID = 882113104339309419L;

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		ConnectionPool dao = ConnectionPool.getConnectionPool();
		if(dao!=null){
		Action action = ActionFactory.getAction(request);
		String view = action.handleRequest(request, response, dao);
		if (view != null) {
			if (view.compareTo("003") == 0) {
				request.setAttribute("message", "003");
				super.forward("/index.jsp", request, response);
			}			
			else if (view.compareTo("004") == 0) {
				request.setAttribute("message", "004");
				super.forward("/index.jsp", request, response);
			}
			else if (view.compareTo("admin") == 0) {
				HttpSession act = request.getSession();
				act.setAttribute("lastActions", "/view/admin.jsp");
				response.sendRedirect("ServletAdmin");
			}
			else if (view.compareTo("user") == 0) {
				HttpSession act = request.getSession();
				act.setAttribute("lastActions", "/view/user.jsp");
				response.sendRedirect("ServletUser");
			}
			else {
				request.setAttribute("message", "004");
				super.forward("/index.jsp", request, response);
			}
		}
		else {
			request.setAttribute("message", "004");
			super.forward("/index.jsp", request, response);
		}
		}else {
			request.setAttribute("message", "013");
			super.forward("/index.jsp", request, response);
		}
	}

	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request
	 *          servlet request
	 * @param response
	 *          servlet response
	 * @throws ServletException
	 *           if a servlet-specific error occurs
	 * @throws IOException
	 *           if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request
	 *          servlet request
	 * @param response
	 *          servlet response
	 * @throws ServletException
	 *           if a servlet-specific error occurs
	 * @throws IOException
	 *           if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}
}
