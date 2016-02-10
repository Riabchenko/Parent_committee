package com.committee.control;

import com.committee.mail.MailSend;
import com.committee.model.action.Action;
import com.committee.model.action.ActionFactory;
import com.committee.model.dao.AccessDAO;
import com.committee.model.dao.AdminDAO;
import com.committee.model.data.Child;
import com.committee.model.data.ReportOfMoney;
import com.committee.model.data.User;
import com.committee.model.util.ConnectionPool;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servlet implementation class ServletAdmin
 */
// @WebServlet("/ServletAdmin")
public class ServletAdmin extends DispatcherServlet {

	private static final long serialVersionUID = 1L;
	private List<String> infoAccount;
	private List<ReportOfMoney> reports;
	AdminDAO admin;
	private ConnectionPool dao = ConnectionPool.getConnectionPool();

	private static List<Child> listOfChild;

	// Log4j logger
	private static final Logger log = Logger.getLogger(AccessDAO.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletAdmin() {
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
			log.error("IOException " + e.getMessage());
		}

	}

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
//		request.getSession().setAttribute("access", "admin");
		Action action = ActionFactory.getAction(request);
		if (admin == null) {
//			admin = new AdminServiceImpl();
		  admin = (AdminDAO) action.handleRequest(request, response, dao);
		}
		// Get list of all Child------------
		if (listOfChild == null) {
			try {
				listOfChild = new ArrayList<Child>();
				listOfChild = admin.getAllChildWithParentsFromDB();
			}
			catch (SQLException e) {
				listOfChild = null;
				log.error("SQLException " + e.getMessage());
			}
		}
		request.setAttribute("listOfChild", listOfChild);
		// ------------
		if (infoAccount == null) {
			User parent = admin.getMyAccont();
			infoAccount = new LinkedList<String>();
			String idAccount = String.valueOf(parent.getIdAccount());
			infoAccount.add(idAccount);
			String firstNameOfAccount = parent.getFirstName();
			infoAccount.add(firstNameOfAccount);
			String byFatherNameOfAccount = parent.getByFather();
			if (byFatherNameOfAccount != null)
				infoAccount.add(byFatherNameOfAccount);
			else
				infoAccount.add("");
		}
		request.setAttribute("infoAccount", infoAccount);
		// ------------
		request.setAttribute("admin", admin);
		// -----------
		HttpSession lastAct = request.getSession();
		String la = (String) lastAct.getAttribute("lastActions");
		if (la.compareTo("/view/admin.jsp") == 0) {
			super.forward("/view/admin.jsp", request, response);
		}
		else {
			wayToPage(la, request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = (String) request.getSession().getAttribute("lastActions");
		// clear all errors
		HttpSession sessionError = request.getSession();
		sessionError.removeAttribute("error");

		if (request.getParameterValues("infoParents") != null) {
			action = "/view/admin.jsp";
			wayToPage(action, request, response);
		}
		if (request.getParameterValues("reports") != null) {
			methodReports(request, response);
		}
		if (request.getParameterValues("goToAddChild") != null) {
			action = "/view/addChild.jsp";
			wayToPage(action, request, response);
		}
		if (request.getParameterValues("goToEditChild") != null) {
			methodGoToEditChild(request, response);
		}
		// go to edit password
		if (request.getParameterValues("changePassword") != null) {
			methodGoToPageChangePassword(request, response);
		}
		if (request.getParameterValues("newMail") != null) {
			methodGoToPageNewMail(request, response);
		}
		// change password - method for change
		if (request.getParameterValues("btnPassword") != null) {
			changePassword(request, response);
		}
		// send email
		if (request.getParameterValues("mainForMail") != null) {
			methodForSendMail(request, response);
		}
		if (request.getParameterValues("addChild") != null) {
			addNewChild(request, response);
		}
		if (request.getParameterValues("editChild") != null) {
			editChild(request, response);
		}
		if (request.getParameterValues("btnIdChild") != null) {
			searchChild(request, response);
		}
		if (request.getParameterValues("btnAddIn") != null) {
			addInCashToReport(request, response);
		}
		if (request.getParameterValues("btnAddOut") != null) {
			addOutCashToReport(request, response);
		}
		
	}

	private void methodForSendMail(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String action = (String) request.getSession().getAttribute("lastActions");
		String subject = request.getParameter("subject");
		String body = request.getParameter("body");
		String who = request.getParameter("idParent");
		StringBuilder recipients = new StringBuilder();
		if ((subject.compareTo("") != 0) && (body.compareTo("") != 0)
				&& (who.compareTo("") != 0)
				&& (request.getParameter("sender").compareTo("") != 0)
				&& (request.getParameter("login").compareTo("") != 0)
				&& (request.getParameter("fieldOfpassword").compareTo("") != 0)) {
			if (listOfChild == null) {
				try {
					listOfChild = new ArrayList<Child>();
					listOfChild = admin.getAllChildWithParentsFromDB();
				}
				catch (SQLException e) {
					listOfChild = null;
					log.error("methodForSendMail - listOfChild didn't create"
							+ e.getMessage());
				}
			}

			if (listOfChild != null) {
				if (who.compareTo("all") == 0) {
					for (Child ch : listOfChild) {
						if (ch.getParents().get(0).getEmails().size() > 0) {
							recipients.append(ch.getParents().get(0).getEmails().get(0));
							recipients.append(",");
						}
					}
				}
				else {
					int whoInt = Integer.valueOf(who);
					for (Child ch : listOfChild) {
						if (ch.getParents().get(0).getIdAccount() == whoInt) {
							if(ch.getParents().get(0).getEmails().size()>0)
								recipients.append(ch.getParents().get(0).getEmails().get(0));
							else {
								request.setAttribute("message", "014");
								action = "/view/addchildsuccess.jsp";
								wayToPage(action, request, response);
							}
							break;
						}
					}

				MailSend ms = new MailSend();
				if (ms.createMessage(request.getParameter("sender"),
						request.getParameter("login"),
						request.getParameter("fieldOfpassword"), subject, body,
						recipients.toString())) {
					request.setAttribute("message", "011");
					action = "/view/addchildsuccess.jsp";
					wayToPage(action, request, response);
				}
				else {
					request.setAttribute("message", "012");
					action = "/view/addchildsuccess.jsp";
					wayToPage(action, request, response);
				}
			}
			}
			else
				wayToPage(action, request, response); // go to back
		}
		else {
			request.setAttribute("message", "001");
			wayToPage(action, request, response);
		}

		HttpSession lastActions = request.getSession();
		lastActions.setAttribute("lastActions", action);
	}

	private void methodReports(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String action = (String) request.getSession().getAttribute("lastActions");

		if(admin != null){ 
		if(reports == null){
				reports = new ArrayList<ReportOfMoney>();
			try {
				reports = admin.getCommonReportOfMoney();
			}
			catch (SQLException e) {
				reports = null;
				log.error("Error into methodReports. " + e.getMessage());
			}	
		} 

			request.setAttribute("reports", reports);
			//-------------
			//set list of parents
			List<User> parents = new ArrayList<User>();
			if (listOfChild == null) {
				listOfChild = new ArrayList<Child>();
				try {
					listOfChild = admin.getAllChildWithParentsFromDB();
				}
				catch (SQLException e) {
					listOfChild = null;
					log.error("methodGoToPageChangePassword - listOfChild didn't create"
							+ e.getMessage());
				}
			}
			if (listOfChild != null) {
				for (Child ch : listOfChild) {
					parents.add(ch.getParents().get(0));
				}
			}
			request.setAttribute("parents", parents);
		}
		//-------------------
		// go to page		
			action = "/view/reports.jsp";
			super.forward(action, request, response);

		HttpSession lastActions = request.getSession();
		lastActions.setAttribute("lastActions", action);

	}


	private void methodGoToEditChild(HttpServletRequest request,
											  HttpServletResponse response) throws IOException, ServletException {
//		List<User> parents = new ArrayList<User>();
		String action = (String) request.getSession().getAttribute("lastActions");
		makeListOfChildren(request);
		if (listOfChild != null) {
			request.setAttribute("listOfChild", listOfChild);
		}
//		if (parents.size() > 0) {
			action = "/view/editChild.jsp";
			super.forward(action, request, response);
//		}
//		else {
//			super.forward(action, request, response); // back
//		}
		HttpSession lastActions = request.getSession();
		lastActions.setAttribute("lastActions", action);
	}

	private void methodGoToPageChangePassword(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		List<User> parents = new ArrayList<User>();
		String action = (String) request.getSession().getAttribute("lastActions");
		makeListOfChildren(request);
		if (listOfChild != null) {
			for (Child ch : listOfChild) {
				parents.add(ch.getParents().get(0));
			}
		}
		request.setAttribute("parents", parents);
		if (parents.size() > 0) {
			action = "/view/changePassword.jsp";
			super.forward(action, request, response);
		}
		else {
			super.forward(action, request, response); // back
		}
		HttpSession lastActions = request.getSession();
		lastActions.setAttribute("lastActions", action);
	}

	private void methodGoToPageNewMail(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String action = (String) request.getSession().getAttribute("lastActions");
		List<User> parents = new ArrayList<User>();
		makeListOfChildren(request);
		if (listOfChild != null) {
			for (Child ch : listOfChild) {
				parents.add(ch.getParents().get(0));
			}

			request.setAttribute("parents", parents);
			action = "/view/newMail.jsp";
			super.forward(action, request, response);
		}
		else {
			super.forward(action, request, response);
		}

		HttpSession lastActions = request.getSession();
		lastActions.setAttribute("lastActions", action);

	}

	private void addNewChild(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String action = (String) request.getSession().getAttribute("lastActions");
		HttpSession sessionError = request.getSession();
		sessionError.removeAttribute("error");
		boolean found = false;

		if (request.getParameterValues("addChild") != null) {
			// check min information
			String lastName = request.getParameter("inLastName");
			String firstName = request.getParameter("inFirstName");
			String lastNameParent = request.getParameter("inLastNameParent1");
			String parentTelephone1_ = request.getParameter("inP1Tel1");

			lastName.trim();
			firstName.trim();
			lastNameParent.trim();
			parentTelephone1_.trim();

			if (lastName.compareTo("") != 0 && firstName.compareTo("") != 0
					&& lastNameParent.compareTo("") != 0
					&& parentTelephone1_.compareTo("") != 0) {
				try {
					if (admin.addNewChild(request, response, dao)) {
						listOfChild = admin.getAllChildWithParentsFromDB(); // update list
						if (sessionError.getAttribute("error") != null) {
							if (((String) sessionError.getAttribute("error"))
									.compareTo("009") == 0)
								request.setAttribute("message", "009"); // в сессии хранится
																												// пароль который не
																												// смог отправится
						}
						else
							request.setAttribute("message", "008");
						action = "/view/addchildsuccess.jsp";
						wayToPage(action, request, response);
					}
					else {
						request.setAttribute("message", "002");
						action = "/view/addchildsuccess.jsp";
						wayToPage(action, request, response);
					}
				}
				catch (ServletException e) {
					log.error("ServletException " + e.getMessage());
					wayToPage(action, request, response);
				}
				catch (SQLException e) {
					log.error("SQLException " + e.getMessage());
					wayToPage(action, request, response);
				}
			}
			else {
				request.setAttribute("message", "001");
				wayToPage(action, request, response);
			}
		}
		HttpSession lastActions = request.getSession();
		lastActions.setAttribute("lastActions", action);
	}

	private void searchChild(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String action = (String) request.getSession().getAttribute("lastActions");
		HttpSession sessionError = request.getSession();
		sessionError.removeAttribute("error");



		if (request.getParameterValues("btnIdChild") != null) {

			Integer idChildForEdit = 0;
			String idChildForEdit_ = (String) request.getParameter("idChildForEdit");

			makeListOfChildren(request);
			System.out.println("====" + request.getAttribute("listOfChild"));

			if (idChildForEdit_.compareTo("") != 0) {

				Pattern p = Pattern.compile("^([0-9]{1,6})$");
				Matcher m = p.matcher(idChildForEdit_);
				try {
					if (m.find()) {
						idChildForEdit = Integer.valueOf(idChildForEdit_);
						for(Child childForEdit : listOfChild){
							if(childForEdit.getIdChild() == idChildForEdit){
								request.setAttribute("childForEdit", childForEdit);
								// if size.....
								if (childForEdit.getParents().size() >= 1) {
									request.setAttribute("matherOfChildForEdit", childForEdit
											.getParents().get(0));
								if (childForEdit.getParents().get(0).getTelephones().size() >= 1)
										request.setAttribute("telMather", childForEdit.getParents()
												.get(0).getTelephones());
								if (childForEdit.getParents().get(0).getEmails().size() >= 1)
										request.setAttribute("emMather", childForEdit.getParents()
												.get(0).getEmails());
								
							}
								action = "/view/editChild.jsp";
								wayToPage(action, request, response);
								break;
								}
						}						
						}	
					else {
						request.setAttribute("message", "007");
						action = "/view/editChild.jsp";
						wayToPage(action, request, response);
					}
				}
				catch (ServletException e) {
					log.error("ServletException " + e.getMessage());
					wayToPage(action, request, response);
				}
				
			}
			else {
				request.setAttribute("message", "006");
				action = "/view/editChild.jsp";
				wayToPage(action, request, response);
			}

		}
		HttpSession lastActions = request.getSession();
		lastActions.setAttribute("lastActions", action);
	}

	public void makeListOfChildren(HttpServletRequest request){
		if (listOfChild == null) {
			listOfChild = new ArrayList<Child>();
			try {
				listOfChild = admin.getAllChildWithParentsFromDB();
			}
			catch (SQLException e) {
				listOfChild = null;
				log.error("methodGoToPageChangePassword - listOfChild didn't create"
						+ e.getMessage());
			}
		}
		if (listOfChild != null) {
			request.setAttribute("listOfChild", listOfChild);
		}
	}

	private void editChild(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String action = (String) request.getSession().getAttribute("lastActions");
		HttpSession sessionError = request.getSession();
		sessionError.removeAttribute("error");
		boolean found = false;

		makeListOfChildren(request);

		if (request.getParameterValues("editChild") != null) {
			// check min information
			String lastName = request.getParameter("inLastName");
			String firstName = request.getParameter("inFirstName");
			String lastNameParent = request.getParameter("inLastNameParent1");
			String parentTelephone1_ = request.getParameter("inP1Tel1");

			lastName.trim();
			firstName.trim();
			lastNameParent.trim();
			parentTelephone1_.trim();

			if (lastName.compareTo("") != 0 && firstName.compareTo("") != 0
					&& lastNameParent.compareTo("") != 0
					&& parentTelephone1_.compareTo("") != 0) {

				String idChildForEditS = (String) request
						.getParameter("idChildForEdit");
				if (idChildForEditS != null) {
					Integer idChildForEdit = Integer.valueOf(idChildForEditS);
					if (idChildForEdit != 0) {
						try {
							if (admin.editChild(idChildForEdit, request, response, dao)) {
								listOfChild = admin.getAllChildWithParentsFromDB(); // update list
								request.setAttribute("message", "005");
								action = "/view/addchildsuccess.jsp";
								wayToPage(action, request, response);
							} else {
								request.setAttribute("message", "002");
								action = "/view/addchildsuccess.jsp";
								wayToPage(action, request, response);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}else {
						request.setAttribute("message", "007");
						action = "/view/editChild.jsp";
						wayToPage(action, request, response);
					}
				}

				else if (idChildForEditS == null) {
					request.setAttribute("message", "007");
					action = "/view/editChild.jsp";
					wayToPage(action, request, response);
				}
			}
			else {

				request.setAttribute("message", "001");
				action = "/view/editChild.jsp";
				wayToPage(action, request, response);
			}
		}

		HttpSession lastActions = request.getSession();
		lastActions.setAttribute("lastActions", action);
	}

	private void changePassword(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String action = (String) request.getSession().getAttribute("lastActions");
		boolean found = false;

		if (request.getParameterValues("btnPassword") != null) {
			String error = null;
			if ((request.getParameter("idParent").compareTo("") != 0)
					&& (request.getParameter("sender").compareTo("") != 0)
					&& (request.getParameter("login").compareTo("") != 0)
					&& (request.getParameter("fieldOfpassword").compareTo("") != 0)) {
				int id = Integer.valueOf(request.getParameter("idParent"));
				try {
					// ----looks email of parent---//
					for (Child child : listOfChild) {
						for (User parent : child.getParents()) {
							if (parent.getIdAccount() == id) {
								// ----generated password----//
								Random r = new Random();
								Random r2 = new Random();
								String p = String.valueOf(r.nextInt(90000) + id * 23
										+ r2.nextInt(3333));// случайное число
								// --------------------------//
								if(!admin.editPassword(id, p)) error = "016";
								
								// ----send email ---//
								MailSend ms = new MailSend();
								String subject = "Access for Parent committee";
								String body = "Password was change. New password is: " + p;
								// ----get email of parent---//
								if (parent.getEmails().size() > 0) {
									parent.getEmails().get(0);
									if (!ms.createMessage(request.getParameter("sender"),
											request.getParameter("login"),
											request.getParameter("fieldOfpassword"), subject, body,
											parent.getEmails().get(0))) {
										error = "009";
									}
								}
								else {
									error = "013";
								}
								if (error != null) {
									request.setAttribute("message", error);
								}
								else
									request.setAttribute("message", "010");
								found = true;
								break;

							}
						}
					}
					if (!found) request.setAttribute("message", "004");
					// go to page
					action = "/view/addchildsuccess.jsp";
					wayToPage(action, request, response);
				}
				catch (SQLException e) {
					log.error("Error in the proccess to edit passwod! Password "
							+ request.getParameter("idParent") + " didn't save!");
					// add переход to page
				}
			}
			else {
				request.setAttribute("message", "001");
				wayToPage(action, request, response);
			}
		}
		HttpSession lastActions = request.getSession();
		lastActions.setAttribute("lastActions", action);
	}
//add cash to Reports
	private void addInCashToReport(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String action = (String) request.getSession().getAttribute("lastActions");
			Integer addInInt = 0;
			String addIn = (String) request.getParameter("inCash");

			if (addIn.compareTo("") != 0) {

				Pattern p = Pattern.compile("^([0-9]{1,5})$");
				Matcher m = p.matcher(addIn);
				try {
					if (m.find()) {
						addInInt = Integer.valueOf(addIn);
						if(addInInt>0){
								if(admin.inReport(request, response, dao)){
									//update reports
									try {
										reports = admin.getCommonReportOfMoney();
									}
									catch (SQLException e) {
										reports = null;
										log.error("Error into methodReports. " + e.getMessage());
									}			
									action = "/view/reports.jsp";
									wayToPage(action, request, response);	}	
						}	
					else {
						request.setAttribute("message", "015");
						action = "/view/reports.jsp";
						wayToPage(action, request, response);
					}
					}else {
						request.setAttribute("message", "001");//проверить
						action = "/view/reports.jsp";
						wayToPage(action, request, response);
					}
				}
				catch (ServletException e) {
					log.error("ServletException - reports " + e.getMessage());
					wayToPage(action, request, response);
				}
				catch (SQLException e) {
					log.error("SQLException - reports " + e.getMessage());
				}
				
			}
			else {
				request.setAttribute("message", "006");
				action = "/view/reports.jsp";
				wayToPage(action, request, response);
			}

		
		HttpSession lastActions = request.getSession();
		lastActions.setAttribute("lastActions", action);

	}
	
//add cash to Reports
	private void addOutCashToReport(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String action = (String) request.getSession().getAttribute("lastActions");
			Integer addInInt = 0;
			String addIn = (String) request.getParameter("outCash");

			if (addIn.compareTo("") != 0) {

				Pattern p = Pattern.compile("^([0-9]{1,5})$");
				Matcher m = p.matcher(addIn);
				try {
					if (m.find()) {
						addInInt = Integer.valueOf(addIn);
						if(addInInt>0){
								if(admin.outReport(request, response, dao)){
									//update reports
									try {
										reports = admin.getCommonReportOfMoney();
									}
									catch (SQLException e) {
										reports = null;
										log.error("Error into methodReports. " + e.getMessage());
									}			
									action = "/view/reports.jsp";
									wayToPage(action, request, response);	}	
						}	
					else {
						request.setAttribute("message", "015");
						action = "/view/reports.jsp";
						wayToPage(action, request, response);
					}
					}else {
						request.setAttribute("message", "017");//error pater
						action = "/view/reports.jsp";
						wayToPage(action, request, response);
					}
				}
				catch (ServletException e) {
					log.error("ServletException - reports " + e.getMessage());
					wayToPage(action, request, response);
				}
				catch (SQLException e) {
					log.error("SQLException - reports " + e.getMessage());
				}
				
			}
			else {
				request.setAttribute("message", "001");
				action = "/view/reports.jsp";
				wayToPage(action, request, response);
			}

		
		HttpSession lastActions = request.getSession();
		lastActions.setAttribute("lastActions", action);

	}
	
	public void wayToPage(String action, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		if (action.compareTo("/view/reports.jsp") == 0) {
			methodReports(request, response);
		}
		if (action.compareTo("/view/changePassword.jsp") == 0) {
			methodGoToPageChangePassword(request, response);
		}
		if (action.compareTo("/view/newMail.jsp") == 0) {
			methodGoToPageNewMail(request, response);
		}
		if (action.compareTo("/view/editChild.jsp") == 0) {
			super.forward(action, request, response);
		}
		if (action.compareTo("/view/addChild.jsp") == 0) {
			super.forward(action, request, response);
		}
		if (action.compareTo("/view/addchildsuccess.jsp") == 0) {
			super.forward(action, request, response);
		}
		if (action.compareTo("/view/admin.jsp") == 0) {
			response.sendRedirect("ServletAdmin");
		}

		HttpSession lastActions = request.getSession();
		lastActions.setAttribute("lastActions", action);
	}
}
