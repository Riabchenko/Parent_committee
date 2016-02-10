package com.committee.control;
/**
*
* @author Aliona Riabchenko
* @version 1.0 Build 21.06.2015
*
*
*/
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DispatcherServlet
 */
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	public void forward (String to, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(to);
		dispatcher.forward(request, response);
	}

}