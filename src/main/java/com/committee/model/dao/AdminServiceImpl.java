package com.committee.model.dao;

/**
 *
 * @author Aliona Riabchenko
 * @version 1.0 Build 21.06.2015
 *
 *
 */

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import com.committee.mail.MailSend;
import com.committee.model.util.ConnectionPool;
import com.committee.model.data.Child;
import com.committee.model.data.ChildImpl;
import com.committee.model.data.ReportOfMoney;
import com.committee.model.data.TypeOfAccess;
import com.committee.model.data.User;
import com.committee.model.data.UserImpl;
import com.committee.security.ScryptPasswordHashingDemo;

public class AdminServiceImpl extends ServiceImpl<AdminDAO> implements AdminDAO {

	HttpServletRequest request;
	HttpServletResponse response;

	// private String errorEditPassword;
	private String errorAdd;
	// T - AdminDAO
	// Log4j logger
	private static final Logger log = Logger.getLogger(AdminServiceImpl.class);

	public AdminDAO handleRequest(HttpServletRequest request,
			HttpServletResponse response, ConnectionPool dao)
			throws ServletException, IOException {
		connectionPool = dao;
		Integer id = (Integer) request.getSession().getAttribute("accesAdmin");
		user = new UserImpl(id);
		user.setTypeOfAccess(TypeOfAccess.ADMIN);
		try {
			createAccount(user);
		}
		catch (SQLException e) {
			log.error("Can't created account of user with id " + id + " : "
					+ e.getMessage());
		}
		AdminDAO admin = this;

		return admin;
	}

	public int getIdUser() {
		return user.getIdAccount();
	}

	// add new Child
	public boolean addNewChild(HttpServletRequest request,
			HttpServletResponse response, ConnectionPool dao)
			throws ServletException, IOException, SQLException {
		connectionPool = dao;
		boolean isAdd = false;

		String sender = request.getParameter("sender");
		String login = request.getParameter("login");
		String password = request.getParameter("password");

		String lastName = request.getParameter("inLastName");
		String firstName = request.getParameter("inFirstName");
		String birthday = null;//request.getParameter("inBirthday");
		String lastNameParent = request.getParameter("inLastNameParent1");
		String firstNameParent = request.getParameter("inFirstNameParent1");
		String byFatherParent = request.getParameter("inByFather1");

		String parentTelephone1 = request.getParameter("inP1Tel1");
		String parentTelephone2 = request.getParameter("inP1Tel2");

		String parentEmail1 = request.getParameter("inP1Email1");
		String parentEmail2 = request.getParameter("inP1Email2");

		boolean isM = true;

		Matcher matcher = null;
		// for email
		Pattern patternE = Pattern
				.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		if (parentEmail1.compareTo("") != 0) {
			matcher = patternE.matcher(parentEmail1);
			if (!matcher.find())
				isM = false;
		}
		if (parentEmail2.compareTo("") != 0) {
			matcher = patternE.matcher(parentEmail2);
			if (!matcher.find())
				isM = false;
		}

		// for telephone
		Pattern patternT = Pattern.compile("^([0-9]{4,15})$");
		if (parentTelephone1.compareTo("") != 0) {
			matcher = patternT.matcher(parentTelephone1);
			if (!matcher.find())
				isM = false;
		}
		if (parentTelephone2.compareTo("") != 0) {
			matcher = patternT.matcher(parentTelephone2);
			if (!matcher.find())
				isM = false;
		}

		if (isM) {

			// create object
			Child child = addNewChildObj(lastName, firstName, birthday,
					lastNameParent, firstNameParent, byFatherParent, parentTelephone1,
					parentTelephone2, parentEmail1, parentEmail2);
			// create in DB
			if (addNewChildInDB(sender, login, password, child))
				isAdd = true;
			HttpSession s = request.getSession();
			s.removeAttribute("error");
			if (errorAdd != null) {
				s.setAttribute("error", errorAdd);
			}
			return isAdd;
		}
		else {
			log.warn("Pattern is not valid");
			return isM;
		}
	}

	// edit Child
	public boolean editChild(int idChild, HttpServletRequest request,
			HttpServletResponse response, ConnectionPool dao)
			throws ServletException, IOException, SQLException {
		boolean isEdit = false;

		String editLastName = request.getParameter("inLastName");
		editLastName = editLastName.trim();
		String editFirstName = request.getParameter("inFirstName");
		editFirstName = editFirstName.trim();
//		String editBirthday = request.getParameter("inBirthday");
//		editBirthday = editBirthday.trim();
		// ------------parent 1 -----------------//
		String editLastNameParent1 = request.getParameter("inLastNameParent1");
		editLastNameParent1 = editLastNameParent1.trim();
		String editFirstNameParent1 = request.getParameter("inFirstNameParent1");
		editFirstNameParent1 = editFirstNameParent1.trim();
		String editByFatherParent1 = request.getParameter("inByFather1");
		editByFatherParent1 = editByFatherParent1.trim();

		String editParent1TelephoneNew1 = request.getParameter("inP1Tel1");
		editParent1TelephoneNew1 = editParent1TelephoneNew1.trim();
		String editParent1TelephoneNew2 = request.getParameter("inP1Tel2");
		editParent1TelephoneNew2 = editParent1TelephoneNew2.trim();

		String editParent1EmailNew1 = request.getParameter("inP1Email1");
		editParent1EmailNew1 = editParent1EmailNew1.trim();
		String editParent1EmailNew2 = request.getParameter("inP1Email2");
		editParent1EmailNew2 = editParent1EmailNew2.trim();

		boolean isM = true;

		Matcher matcher = null;
		// for email
		Pattern patternE = Pattern
				.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		if (editParent1EmailNew1.compareTo("") != 0) {
			matcher = patternE.matcher(editParent1EmailNew1);
			if (!matcher.find())
				isM = false;
		}
		if (editParent1EmailNew2.compareTo("") != 0) {
			matcher = patternE.matcher(editParent1EmailNew2);
			if (!matcher.find())
				isM = false;
		}

		// for telephone
		Pattern patternT = Pattern.compile("^([0-9]{4,15})$");
		if (editParent1TelephoneNew1.compareTo("") != 0) {
			matcher = patternT.matcher(editParent1TelephoneNew1);
			if (!matcher.find())
				isM = false;
		}
		if (editParent1TelephoneNew2.compareTo("") != 0) {
			matcher = patternT.matcher(editParent1TelephoneNew2);
			if (!matcher.find())
				isM = false;
		}

		if (isM) {

			// get object of Child
			Child child = getChildFromDB(idChild);
			if (editLastName.compareTo(child.getLastName()) != 0) {
				child.setLastName(editLastName);
				editLastNameChild(child);
			}
			if (editFirstName.compareTo(child.getFirstName()) != 0) {
				child.setFirstName(editFirstName);
				editFirstNameChild(child);
			}
//			if (editBirthday.compareTo(child.getBirthday()) != 0) {
//				child.setBirthday(editBirthday);
//				editBirthdayOfChild(child);
//			}

			for (User parent : child.getParents()) {
				// edit object of Parent
				if (editLastNameParent1.compareTo(parent.getLastName()) != 0) {
					parent.setLastName(editLastNameParent1);
					editLastNameParentOfChild(parent);
				}
				if (editFirstNameParent1.compareTo(parent.getFirstName()) != 0) {
					parent.setFirstName(editFirstNameParent1);
					editFirstNameParentOfChild(parent);
				}
				if (editByFatherParent1.compareTo(parent.getByFather()) != 0) {
					parent.setByFather(editByFatherParent1);
					editByFatherParentOfChild(parent);
				}

				// ----telephones----

				// 2 list of telephones => old and new
				List<String> oldTelForParent1 = parent.getTelephones();
				List<String> newTelForParent1 = new ArrayList<String>();
				if (editParent1TelephoneNew1.compareTo("") != 0)
					newTelForParent1.add(editParent1TelephoneNew1);
				if (editParent1TelephoneNew2.compareTo("") != 0)
					newTelForParent1.add(editParent1TelephoneNew2);

				// equals size of two list
				// if old equals new
				if ((oldTelForParent1.size() - newTelForParent1.size()) == 0) {
					if (oldTelForParent1.size() != 0) {
						if (!Arrays.deepEquals(oldTelForParent1.toArray(),
								newTelForParent1.toArray()))
							for (int x = 0; x < oldTelForParent1.size(); x++)
								editTelephone(oldTelForParent1.get(x), newTelForParent1.get(x));
					}
				}

				// if old < new
				if (oldTelForParent1.size() < newTelForParent1.size()) {
					if (oldTelForParent1.size() != 0) {
						if (!oldTelForParent1.get(0).equals(newTelForParent1.get(0))) {
							editTelephone(oldTelForParent1.get(0), newTelForParent1.get(0));
						}
						addTelephoneInDBAfterEdit(newTelForParent1.get(1),
								parent.getIdAccount());
					}
					else {
						for (int y = 0; y < newTelForParent1.size(); y++) {
							addTelephoneInDBAfterEdit(newTelForParent1.get(y),
									parent.getIdAccount());
						}
					}
				}

				// if old > new
				if (oldTelForParent1.size() > newTelForParent1.size()) {
					if (newTelForParent1.size() != 0) {
						int del = 0;
						for (int n = 0; n < oldTelForParent1.size(); n++) {
							if (!oldTelForParent1.get(n).equals(newTelForParent1.get(0))) {
								deleteTelephoneFromDB(oldTelForParent1.get(n));
								del++;
							}
						}
						if (del == 2)
							addTelephoneInDBAfterEdit(newTelForParent1.get(0),
									parent.getIdAccount());
					}
					else {
						for (int m = 0; m < oldTelForParent1.size(); m++) {
							deleteTelephoneFromDB(oldTelForParent1.get(m));
						}
					}
				}

				// ----email----

				// 2 list of email => old and new
				List<String> oldEmForParent1 = parent.getEmails();
				List<String> newEmForParent1 = new ArrayList<String>();
				if (editParent1EmailNew1.compareTo("") != 0)
					newEmForParent1.add(editParent1EmailNew1);
				if (editParent1EmailNew2.compareTo("") != 0)
					newEmForParent1.add(editParent1EmailNew2);

				// equals size of two list
				// if old equals new
				if ((oldEmForParent1.size() - newEmForParent1.size()) == 0) {
					if (oldEmForParent1.size() != 0) {
						if (!Arrays.deepEquals(oldEmForParent1.toArray(),
								newEmForParent1.toArray()))
							for (int x = 0; x < oldEmForParent1.size(); x++)
								editEmail(oldEmForParent1.get(x), newEmForParent1.get(x));
					}
				}

				// if old < new
				if (oldEmForParent1.size() < newEmForParent1.size()) {
					if (oldEmForParent1.size() != 0) {
						if (!oldEmForParent1.get(0).equals(newEmForParent1.get(0))) {
							editEmail(oldEmForParent1.get(0), newEmForParent1.get(0));
						}
						addEmInDBAfterEdit(newEmForParent1.get(1), parent.getIdAccount());
					}
					else {
						for (int y = 0; y < newEmForParent1.size(); y++) {
							addEmInDBAfterEdit(newEmForParent1.get(y), parent.getIdAccount());
						}
					}
				}

				// if old > new
				if (oldEmForParent1.size() > newEmForParent1.size()) {
					if (newEmForParent1.size() != 0) {
						int del = 0;
						for (int p = 0; p < oldTelForParent1.size(); p++) {
							if (!oldEmForParent1.get(p).equals(newEmForParent1.get(0))) {
								deleteEmFromDB(oldEmForParent1.get(p));// don't equals
								del++;
							}
						}
						if (del == 2)
							addEmInDBAfterEdit(newEmForParent1.get(0), parent.getIdAccount());
					}
					else {
						for (int o = 0; o < oldEmForParent1.size(); o++) {
							deleteEmFromDB(oldEmForParent1.get(o));
						}
					}
				}

			}

			isEdit = true;
		}
		else {
			isEdit = false;
			log.warn("Pattern is not valid");
		}

		return isEdit;
	}

	// change typeOfAccess
	public boolean changeTypeOfAccess(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			SQLException {
		Integer idParent = 2;
		String access = "admin";// or user
		return editAccessForParent(idParent, access);
	}

//add in Report
	public boolean inReport(HttpServletRequest request,
			HttpServletResponse response, ConnectionPool dao)
			throws ServletException, IOException, SQLException {
		connectionPool = dao;
		boolean isAdd = false;

		String inCash = request.getParameter("inCash");
		String infoIn = request.getParameter("infoIn");
		String idParent = request.getParameter("idParent");
		Integer idAdmin = user.getIdAccount();
		if(setInReportOfMoney(idAdmin,idParent,inCash,infoIn)) isAdd=true;
		return isAdd;
		
	}
	
	
//add out Report
	public boolean outReport(HttpServletRequest request,
			HttpServletResponse response, ConnectionPool dao)
			throws ServletException, IOException, SQLException {
		connectionPool = dao;
		boolean isAdd = false;

		String inCash = request.getParameter("outCash");
		String infoIn = request.getParameter("outInfo");
		Integer idAdmin = user.getIdAccount();
		if(setOutReportOfMoney(idAdmin,inCash,infoIn)) isAdd=true;
		return isAdd;
		
	}
	
	// DB - add Telephone and Email if they aren't---------------------//
	// main - edit - add Telephone In DB
	private boolean addTelephoneInDBAfterEdit(String telephone, Integer idParent)
			throws SQLException {
		Savepoint savepoint = null;
		Connection conn = null;
		boolean isEdit = false;
		String SQL = null;
		PreparedStatement ps = null;
		try {
			conn = connectionPool.retrieve();
			conn.setAutoCommit(false);
			savepoint = conn.setSavepoint("savepoint");
			// ------
			try {
				SQL = "INSERT INTO `Telephones`(`number`,`Parents_id_parents`) VALUE (?,?)";
				ps = conn.prepareStatement(SQL); // создание запроса ps
				ps.setString(1, telephone);
				ps.setInt(2, idParent);
				ps.execute(); // выполнение запроса
				isEdit = true;
			}
			catch (SQLException e) {
				log.error("SQLException - Error request at the access!"
						+ e.getMessage());
				isEdit = false;
			}
			finally {
				SQL = null;
				ps.close();
			}
			// ------
		}
		finally {
			if (isEdit == false) {
				conn.rollback(savepoint);
				log.error("Error! Data doesn't save!Problem in addTelephoneInDBAfterEdit");
			}
			conn.commit();
			connectionPool.putback(conn);
		}
		return isEdit;
	}

	// main - add Telephone In DB
	private boolean addTelephoneInDB(User parent) throws SQLException {
		Savepoint savepoint = null;
		Connection conn = null;
		boolean isAdd = false;
		;
		try {
			conn = connectionPool.retrieve();
			conn.setAutoCommit(false);
			savepoint = conn.setSavepoint("savepoint");
			// -----
			// add telephone for parent
			addInDBTelephoneForParentOfChild(conn, parent);
			isAdd = true;
			// ------
		}
		finally {
			if (isAdd == false) {
				conn.rollback(savepoint);
				log.error("Error! Data doesn't save!Problem in addTelephoneInDB");
			}
			conn.commit();
			connectionPool.putback(conn);
		}
		return isAdd;
	}

	// main - edit - add Email In DB
	private boolean addEmInDBAfterEdit(String email, Integer idParent)
			throws SQLException {
		Savepoint savepoint = null;
		Connection conn = null;
		boolean isEdit = false;
		String SQL = null;
		PreparedStatement ps = null;
		try {
			conn = connectionPool.retrieve();
			conn.setAutoCommit(false);
			savepoint = conn.setSavepoint("savepoint");
			// ------
			try {
				SQL = "INSERT INTO `Emails`(`email`,`Parents_id_parents`) VALUE (?,?)";
				ps = conn.prepareStatement(SQL); // создание запроса ps
				ps.setString(1, email);
				ps.setInt(2, idParent);
				ps.execute(); // выполнение запроса
				isEdit = true;
			}
			catch (SQLException e) {
				log.error("SQLException - Error request at the access!"
						+ e.getMessage());
				isEdit = false;
			}
			finally {
				SQL = null;
				ps.close();
			}
			// ------
		}
		finally {
			if (isEdit == false) {
				conn.rollback(savepoint);
				log.error("Error! Data doesn't save!Problem in addEmInDBAfterEdit");
			}
			conn.commit();
			connectionPool.putback(conn);
		}
		return isEdit;

	}

	// main - add Email In DB
	private boolean addEmailInDB(User parent) throws SQLException {
		Savepoint savepoint = null;
		Connection conn = null;
		boolean isAdd = false;
		;
		try {
			conn = connectionPool.retrieve();
			conn.setAutoCommit(false);
			savepoint = conn.setSavepoint("savepoint");
			// -----
			// add email for parent
			addInDBEmailsForParentOfChild(conn, parent);
			isAdd = true;
			// ------
		}
		finally {
			if (isAdd == false) {
				conn.rollback(savepoint);
				log.error("Error! Data doesn't save!Problem in addTelephoneInDB");
			}
			conn.commit();
			connectionPool.putback(conn);
		}
		return isAdd;
	}

	// DB - add new Child - Helper --------------------------------------//
	// main - add new Child in DB
	private boolean addNewChildInDB(String sender, String login, String password,
			Child child) throws SQLException {
		Savepoint savepoint = null;
		Connection conn = null;
		boolean isAdd = false;
		;
		try {
			conn = connectionPool.retrieve();
			conn.setAutoCommit(false);
			savepoint = conn.setSavepoint("savepoint");
			// -----
			// adds child
			addInDBInfoChild(conn, child);
			// adds parents
			for (User parent : child.getParents()) {
				addInDBInfoOfParentOfChild(conn, parent);
			}
			// bind child with parents
			childBringsWithParents(conn, child);

			// add telephone for parent
			for (User parent : child.getParents()) {
				addInDBTelephoneForParentOfChild(conn, parent);
			}
			// add emails for parent
			for (User parent : child.getParents()) {
				addInDBEmailsForParentOfChild(conn, parent);
			}
			// ----generated password----//
			Random r = new Random(90000);
			String p = String.valueOf(r.nextInt()) + child.getParents().get(0);// случайное
																																					// число
																																					// +
																																					// фамилия
			// -------------------------//
			// access for first parent
			if (addInDBAccessForParentOfChild(conn, child.getParents().get(0), p))
				log.error("Access didn't create for "
						+ child.getParents().get(0).getLastName());
			if (login.compareTo("") != 0 && password.compareTo("") != 0
					&& sender.compareTo("") != 0) {
				// send password to that parent
				MailSend ms = new MailSend();
				String subject = "Access for Parent committee open";
				String body = "Login is: "
						+ child.getParents().get(0).getTelephones().get(0)
						+ "\n     password is: " + p;
				errorAdd = null;

				if (!ms.createMessage(sender, login, password, subject, body, p))
					errorAdd = "009";
			}
			else
				errorAdd = "009";

			isAdd = true;
			// ------
		}
		finally {
			if (isAdd == false) {
				conn.rollback(savepoint);
				log.error("Error! Data doesn't save!Problem in addNewChildInDB");
			}
			conn.commit();
			connectionPool.putback(conn);
		}
		return isAdd;
	}

	// H - addInDBInfoChild
	private boolean addInDBInfoChild(Connection conn, Child child) {
		PreparedStatement ps = null;
		String SQL;
		java.sql.Date new_child_birthday;
		if (child.getBirthday().compareTo("") != 0) {
			new_child_birthday = java.sql.Date.valueOf(child.getBirthday()); // меняем
																																				// тип
																																				// переменной
																																				// с
																																				// String
																																				// на
																																				// Date
		}
		else {
			new_child_birthday = null;
		}
		boolean isadd = false;
		SQL = "INSERT INTO `Children`(`lastName`,`firstName`,`birthday`) VALUE (?,?,?)";

		try {
			ps = conn.prepareStatement(SQL); // создание запроса ps
			ps.setString(1, child.getLastName());
			ps.setString(2, child.getFirstName());
			ps.setDate(3, new_child_birthday);
			ps.execute(); // выполнение запроса
			isadd = true;
		}
		catch (SQLException e) {
			e.printStackTrace();
			isadd = false;
		}
		finally {
			SQL = null;
			try {
				ps.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
				isadd = false;
			}
		}
		return isadd;
	}

	// H - addInDBParentOfChild
	private boolean addInDBInfoOfParentOfChild(Connection conn, User parent)
			throws SQLException {
		PreparedStatement ps = null;
		String SQL;
		boolean isadd = false;
		try {
			SQL = "INSERT INTO `Parents`(`lastName`,`firstName`,`byFather`) VALUE (?,?,?)";
			ps = conn.prepareStatement(SQL); // создание запроса ps
			ps.setString(1, parent.getLastName());
			ps.setString(2, parent.getFirstName());
			ps.setString(3, parent.getByFather());
			ps.execute(); // выполнение запроса
			isadd = true;
		}
		catch (SQLException e) {
			log.error("SQLException - Error request!" + e.getMessage());
			isadd = false;
		}
		finally {
			SQL = null;
			ps.close();
		}
		return isadd;
	}

	// H - get id for Child
	private void getIdFromBDForChild(Connection conn, Child child)
			throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String SQL;
		try {
			SQL = "SELECT `id_children` " + "FROM `Children` "
					+ "WHERE `lastName` = ? AND `firstName`=? ";
			ps = conn.prepareStatement(SQL);
			ps.setString(1, child.getLastName());
			ps.setString(2, child.getFirstName());
			rs = ps.executeQuery(); // выполнение запроса
			rs.last();
			int rowsOfChild = rs.getRow();
			rs.beforeFirst();
			if (rowsOfChild > 0) {
				while (rs.next()) { // вывод значений полей в виде строк
					child.setIdChild(rs.getInt(1));
				}
			}
		}
		catch (SQLException e) {
			log.error("SQLException - Error request!" + e.getMessage());
		}
		finally {
			SQL = null;
			rs.close();
			ps.close();
		}
	}

	// H - get id for Parent
	private int getIdFromBDForChild(Connection conn, User parent)
			throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String SQL;
		try {
			SQL = "SELECT `id_parents` " + "FROM `Parents` "
					+ "WHERE `lastName` = ? AND `firstName`=? AND `byFather`=? ";
			ps = conn.prepareStatement(SQL);
			ps.setString(1, parent.getLastName());
			ps.setString(2, parent.getFirstName());
			ps.setString(3, parent.getByFather());
			rs = ps.executeQuery(); // выполнение запроса

			rs.last();
			int rowsOfEmails = rs.getRow();
			rs.beforeFirst();
			if (rowsOfEmails > 0) {
				while (rs.next()) { // вывод значений полей в виде строк
					parent.setIdAccount(rs.getInt(1));
				}
			}
		}
		catch (SQLException e) {
			log.error("SQLException - Error request!" + e.getMessage());
		}
		finally {
			SQL = null;
			rs.close();
			ps.close();
		}
		return parent.getIdAccount();
	}

	// H - child brings with parents
	private void childBringsWithParents(Connection conn, Child child)
			throws SQLException {
		getIdFromBDForChild(conn, child);
		for (User parent : child.getParents()) {
			getIdFromBDForChild(conn, parent); // this method you can use for parent
																					// too
		}
		// brings in DB
		for (User p : child.getParents()) {
			childBringsWithParentsInDB(conn, child, p);
		}
	}

	// H - child brings with parents in DB - helper for childBringsWithParents
	private boolean childBringsWithParentsInDB(Connection conn, Child child,
			User parent) throws SQLException {
		PreparedStatement ps = null;
		String SQL;
		boolean isadd = false;
		try {
			SQL = "INSERT INTO `ChildrenAndParents`(`Children_id_children`,`Parents_id_parents`) VALUE (?,?)";
			ps = conn.prepareStatement(SQL); // создание запроса ps
			ps.setInt(1, child.getIdChild());
			ps.setInt(2, parent.getIdAccount());
			ps.execute(); // выполнение запроса
			isadd = true;
		}
		catch (SQLException e) {
			log.error("SQLException - Error request!" + e.getMessage());
			isadd = false;
		}
		finally {
			SQL = null;
			ps.close();
		}
		return isadd;
	}

	// H - add telephone for parent
	private boolean addInDBTelephoneForParentOfChild(Connection conn, User parent)
			throws SQLException {
		boolean isadd = false;
		for (String telephone : parent.getTelephones()) {
			PreparedStatement ps = null;
			String SQL;
			try {
				SQL = "INSERT INTO `Telephones`(`number`,`Parents_id_parents`) VALUE (?,?)";
				ps = conn.prepareStatement(SQL); // создание запроса ps
				ps.setString(1, telephone);
				ps.setInt(2, parent.getIdAccount());
				ps.execute(); // выполнение запроса
				isadd = true;
			}
			catch (SQLException e) {
				log.error("SQLException - Error request!" + e.getMessage());
				isadd = false;
			}
			finally {
				SQL = null;
				ps.close();
			}
		}
		return isadd;
	}

	// H - add email for parent
	private boolean addInDBEmailsForParentOfChild(Connection conn, User parent)
			throws SQLException {
		boolean isadd = false;
		for (String email : parent.getEmails()) {
			PreparedStatement ps = null;
			String SQL;
			try {
				SQL = "INSERT INTO `Emails`(`email`,`Parents_id_parents`) VALUE (?,?)";
				ps = conn.prepareStatement(SQL); // создание запроса ps
				ps.setString(1, email);
				ps.setInt(2, parent.getIdAccount());
				ps.execute(); // выполнение запроса
				isadd = true;
			}
			catch (SQLException e) {
				log.error("SQLException - Error request!" + e.getMessage());
				isadd = false;
			}
			finally {
				SQL = null;
				ps.close();
			}
		}
		return isadd;
	}

	// H - add access for parent
	private boolean addInDBAccessForParentOfChild(Connection conn, User parent,
			String password) throws SQLException {
		boolean isadd = false;
		PreparedStatement ps = null;
		String SQL;
		// -----this encodes of password-----//

		ScryptPasswordHashingDemo sph = new ScryptPasswordHashingDemo();
		String passwordEnd = sph.encoder(password);// за основу берется фамилия
		// ---------------------------------//
		try {
			SQL = "INSERT INTO `Access`(`password`,`typeOfAccess`,`Parents_id_parents`) VALUE (?,?,?)";
			ps = conn.prepareStatement(SQL); // создание запроса ps
			ps.setString(1, passwordEnd);
			ps.setString(2, parent.getTypeOfAccess().name());
			ps.setInt(3, parent.getIdAccount());
			ps.execute(); // выполнение запроса
			isadd = true;
		}
		catch (SQLException e) {
			log.error("SQLException - Error request!" + e.getMessage());
			isadd = false;
		}
		finally {
			SQL = null;
			ps.close();
		}
		return isadd;
	}

	// DB - editAccess --------------------------------------------//
	// Main - edit Access
	public boolean editAccessForParent(int idParent, String access)
			throws SQLException {
		boolean isEdit = false;
		if (access.compareTo("user") == 0 || access.compareTo("admin") == 0) {
			if (access.compareTo("admin") == 0)
				access = TypeOfAccess.ADMIN.name();
			else
				access = TypeOfAccess.USER.name();

			Savepoint savepoint = null;
			Connection conn = null;
			String SQL = null;
			PreparedStatement ps = null;
			try {
				conn = connectionPool.retrieve();
				conn.setAutoCommit(false);
				savepoint = conn.setSavepoint("savepoint");
				// ------
				try {

					SQL = "UPDATE Access SET `typeOfAccess`=?  WHERE `Parens_id_parents` = ? ";
					ps = conn.prepareStatement(SQL);
					ps.setString(1, access);
					ps.setInt(2, idParent);
					ps.execute(); // выполнение запроса и присвоение rs результата
					isEdit = true;
				}
				catch (SQLException e) {
					log.error("SQLException - Error request at the access!"
							+ e.getMessage());
					isEdit = false;
				}
				finally {
					SQL = null;
					ps.close();
				}
				// ------
			}
			finally {
				if (isEdit == false) {
					conn.rollback(savepoint);
					log.error("Error! Data doesn't save!Problem in editAccessForParent");
				}
				conn.commit();
				connectionPool.putback(conn);
			}
		}
		return isEdit;
	}

	// DB - editPassword --------------------------------------------//
	// Main - edit Password
	public boolean editPassword(int idParent, String password)
			throws SQLException {
		boolean isEdit = false;
		String passwordEnd = null;
				// -----this encodes of password-----//

		ScryptPasswordHashingDemo sph = new ScryptPasswordHashingDemo();
		passwordEnd = sph.encoder(password);
		// ---------------------------------//
		Savepoint savepoint = null;
		Connection conn = null;
		String SQL = null;
		PreparedStatement ps = null;
		try {
			conn = connectionPool.retrieve();
			conn.setAutoCommit(false);
			savepoint = conn.setSavepoint("savepoint");
			// ------
			try {
				SQL = "UPDATE Access SET `password`=?  WHERE `Parents_id_parents` = ? ";
				ps = conn.prepareStatement(SQL);
				ps.setString(1, passwordEnd);
				ps.setInt(2, idParent);
				ps.execute(); // выполнение запроса и присвоение rs результата
				isEdit = true;
			}
			catch (SQLException e) {
				log.error("SQLException - Error request at the access!"
						+ e.getMessage());
				isEdit = false;
			}
			finally {
				SQL = null;
				ps.close();
			}
			// ------
		}
		finally {
			if (isEdit == false) {
				conn.rollback(savepoint);
				log.error("Error! Data doesn't save!Problem in editAccessForParent");
			}
			conn.commit();
			connectionPool.putback(conn);
		}
		return isEdit;
	}

	// DB - get allChild ----------------------------------------//
	// main - get allChild
	public List<Child> getAllChildWithParentsFromDB() throws SQLException {
		Child child = null;
		List<Child> children = new ArrayList<Child>();

		Savepoint savepoint = null;
		Connection conn = null;
		boolean isAdd = false;
		try {
			conn = connectionPool.retrieve();
			conn.setAutoCommit(false);
			savepoint = conn.setSavepoint("savepoint");
			// -----
			children = getFromDBAllChildren(conn);// this method create list of all
																						// children
			
			for (Child ch : children) {
				getParentsByIdChild(conn, ch); // this method adds idParents for each child
			}

			for (Child ch : children) {
				for (User parent : ch.getParents()) {
					getInfoOfParent(conn, parent);// this method adds information of
																				// parents
					getFromDBTelephones(conn, parent);
					getFromDBEmails(conn, parent);
				}
			}
			isAdd = true;
			// ------
		}
		finally {
			if (isAdd == false) {
				conn.rollback(savepoint);
				log.error("Error! Data doesn't save!Problem in getAllChildWithParentsFromDB");
			}
			conn.commit();
			connectionPool.putback(conn);
		}
		return children;
	}

	// H - Get all children
	private List<Child> getFromDBAllChildren(Connection conn)
			throws SQLException, NullPointerException {
		ArrayList<Child> children = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String SQL;
		try {
			SQL = "SELECT `id_children`,`lastName`,`firstName`,`birthday` FROM `Children`";
			ps = conn.prepareStatement(SQL);
			rs = ps.executeQuery(); // выполнение запроса
			children = new ArrayList<Child>();
			while (rs.next()) { // вывод значений полей в виде строк
				Child child = new ChildImpl();
				child.setIdChild(rs.getInt(1));
				child.setLastName(rs.getString(2));
				child.setFirstName(rs.getString(3));
				Date birthday = rs.getDate(4);
				child.setBirthday(String.valueOf(birthday));
				children.add(child);
			}
		}
		catch (SQLException e) {
			log.error("SQLException - Error request!" + e.getMessage());
		}
		finally {
			SQL = null;
			rs.close();
			ps.close();
		}
		return children;
	}

	// H - get info of parent
	private void getInfoOfParent(Connection conn, User parent)
			throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String SQL = null;
		try {
			SQL = "SELECT `id_parents`,`lastName`,`firstName`,`byFather` "
					+ "FROM `Parents` " + "WHERE `id_parents` = ?";
			ps = conn.prepareStatement(SQL);
			ps.setInt(1, parent.getIdAccount());
			rs = ps.executeQuery(); // выполнение запроса

			while (rs.next()) { // вывод значений полей в виде строк
				parent.setLastName(rs.getString(2));
				parent.setFirstName(rs.getString(3));
				parent.setByFather(rs.getString(4));
			}
		}
		catch (SQLException e) {
			log.error("SQLException - Error reques!" + e.getMessage());
		}
		finally {
			SQL = null;
			rs.close();
			ps.close();
		}
	}

	// DB - editChild ----------------------------------------//
	// main - edit Last Name Child
	private boolean editLastNameChild(Child child) throws SQLException {
		Savepoint savepoint = null;
		Connection conn = null;
		boolean isEdit = false;
		String SQL = null;
		PreparedStatement ps = null;
		try {
			conn = connectionPool.retrieve();
			conn.setAutoCommit(false);
			savepoint = conn.setSavepoint("savepoint");
			// ------
			try {
				SQL = "UPDATE Children SET `lastName`=?  WHERE `id_children` = ? ";
				ps = conn.prepareStatement(SQL); // создание запроса ps
				ps.setString(1, child.getLastName());
				ps.setInt(2, child.getIdChild());
				ps.execute(); // выполнение запроса и присвоение rs результата
				isEdit = true;
			}
			catch (SQLException e) {
				log.error("SQLException - Error request!" + e.getMessage());
				isEdit = false;
			}
			finally {
				SQL = null;
				ps.close();
			}
			// ------
		}
		finally {
			if (isEdit == false) {
				conn.rollback(savepoint);
				log.error("Error! Data doesn't save!Problem in editLastNameChild");
			}
			conn.commit();
			connectionPool.putback(conn);
		}
		return isEdit;
	}

	// main - edit First Name Child
	private boolean editFirstNameChild(Child child) throws SQLException {
		Savepoint savepoint = null;
		Connection conn = null;
		boolean isEdit = false;
		String SQL = null;
		PreparedStatement ps = null;
		try {
			conn = connectionPool.retrieve();
			conn.setAutoCommit(false);
			savepoint = conn.setSavepoint("savepoint");
			// ------
			try {
				SQL = "UPDATE Children SET `firstName`=?  WHERE `id_children` = ? ";
				ps = conn.prepareStatement(SQL); // создание запроса ps
				ps.setString(1, child.getFirstName());
				ps.setInt(2, child.getIdChild());
				ps.execute(); // выполнение запроса и присвоение rs результата
				isEdit = true;
			}
			catch (SQLException e) {
				log.error("SQLException - Error request!" + e.getMessage());
				isEdit = false;
			}
			finally {
				SQL = null;
				ps.close();
			}
			// ------
		}
		finally {
			if (isEdit == false) {
				conn.rollback(savepoint);
				log.error("Error! Data doesn't save!Problem in editFirstNameChild");
			}
			conn.commit();
			connectionPool.putback(conn);
		}
		return isEdit;

	}

	// main - edit Birthday Of Child
	private boolean editBirthdayOfChild(Child child) throws SQLException {
		Savepoint savepoint = null;
		Connection conn = null;
		boolean isEdit = false;
		String SQL = null;
		PreparedStatement ps = null;
		try {
			conn = connectionPool.retrieve();
			conn.setAutoCommit(false);
			savepoint = conn.setSavepoint("savepoint");
			// ------
			try {
				java.sql.Date new_child_birthday = java.sql.Date.valueOf(child
						.getBirthday()); // меняем тип переменной с String на Date
				SQL = "UPDATE Children SET `birthday`=?  WHERE `id_children` = ? ";
				ps = conn.prepareStatement(SQL); // создание запроса ps
				ps.setDate(1, new_child_birthday);
				ps.setInt(2, child.getIdChild());
				ps.execute(); // выполнение запроса и присвоение rs результата
				isEdit = true;
			}
			catch (SQLException e) {
				log.error("SQLException - Error request!" + e.getMessage());
				isEdit = false;
			}
			finally {
				SQL = null;
				ps.close();
			}
			// ------
		}
		finally {
			if (isEdit == false) {
				conn.rollback(savepoint);
				log.error("Error! Data doesn't save!Problem in editBirthdayOfChild");
			}
			conn.commit();
			connectionPool.putback(conn);
		}
		return isEdit;

	}

	// main - edit Last Name Parent of Child
	private boolean editLastNameParentOfChild(User parent) throws SQLException {
		Savepoint savepoint = null;
		Connection conn = null;
		boolean isEdit = false;
		String SQL = null;
		PreparedStatement ps = null;
		try {
			conn = connectionPool.retrieve();
			conn.setAutoCommit(false);
			savepoint = conn.setSavepoint("savepoint");
			// ------
			try {
				SQL = "UPDATE Parents SET `lastName`=?  WHERE `id_parents` = ? ";
				ps = conn.prepareStatement(SQL); // создание запроса ps
				ps.setString(1, parent.getLastName());
				ps.setInt(2, parent.getIdAccount());
				ps.execute(); // выполнение запроса и присвоение rs результата
				isEdit = true;
			}
			catch (SQLException e) {
				log.error("SQLException - Error request!" + e.getMessage());
				isEdit = false;
			}
			finally {
				SQL = null;
				ps.close();
			}
			// ------
		}
		finally {
			if (isEdit == false) {
				conn.rollback(savepoint);
				log.error("Error! Data doesn't save!Problem in editLastNameParentOfChild");
			}
			conn.commit();
			connectionPool.putback(conn);
		}
		return isEdit;

	}

	// main - edit First Name Parent of Child
	private boolean editFirstNameParentOfChild(User parent) throws SQLException {
		Savepoint savepoint = null;
		Connection conn = null;
		boolean isEdit = false;
		String SQL = null;
		PreparedStatement ps = null;
		try {
			conn = connectionPool.retrieve();
			conn.setAutoCommit(false);
			savepoint = conn.setSavepoint("savepoint");
			// ------
			try {
				SQL = "UPDATE Parents SET `firstName`=?  WHERE `id_parents` = ? ";
				ps = conn.prepareStatement(SQL); // создание запроса ps
				ps.setString(1, parent.getFirstName());
				ps.setInt(2, parent.getIdAccount());
				ps.execute(); // выполнение запроса и присвоение rs результата
				isEdit = true;
			}
			catch (SQLException e) {
				log.error("SQLException - Error request!" + e.getMessage());
				isEdit = false;
			}
			finally {
				SQL = null;
				ps.close();
			}
			// ------
		}
		finally {
			if (isEdit == false) {
				conn.rollback(savepoint);
				log.error("Error! Data doesn't save!Problem in editFirstNameParentOfChild");
			}
			conn.commit();
			connectionPool.putback(conn);
		}
		return isEdit;

	}

	// main - edit ByFather Parent of Child
	private boolean editByFatherParentOfChild(User parent) throws SQLException {
		Savepoint savepoint = null;
		Connection conn = null;
		boolean isEdit = false;
		String SQL = null;
		PreparedStatement ps = null;
		try {
			conn = connectionPool.retrieve();
			conn.setAutoCommit(false);
			savepoint = conn.setSavepoint("savepoint");
			// ------
			try {
				SQL = "UPDATE Parents SET `byFather`=?  WHERE `id_parents` = ? ";
				ps = conn.prepareStatement(SQL); // создание запроса ps
				ps.setString(1, parent.getByFather());
				ps.setInt(2, parent.getIdAccount());
				ps.execute(); // выполнение запроса и присвоение rs результата
				isEdit = true;
			}
			catch (SQLException e) {
				log.error("SQLException - Error request!" + e.getMessage());
				isEdit = false;
			}
			finally {
				SQL = null;
				ps.close();
			}
			// ------
		}
		finally {
			if (isEdit == false) {
				conn.rollback(savepoint);
				log.error("Error! Data doesn't save!Problem in editByFatherParentOfChild");
			}
			conn.commit();
			connectionPool.putback(conn);
		}
		return isEdit;

	}

	// main - edit Telephone
	private boolean editTelephone(String oldTelephone, String newTelephone)
			throws SQLException {
		Savepoint savepoint = null;
		Connection conn = null;
		boolean isEdit = false;
		String SQL = null;
		PreparedStatement ps = null;
		try {
			conn = connectionPool.retrieve();
			conn.setAutoCommit(false);
			savepoint = conn.setSavepoint("savepoint");
			// ------
			try {
				SQL = "UPDATE Telephones SET `number`=?  WHERE `number` = ? ";
				ps = conn.prepareStatement(SQL); // создание запроса ps
				ps.setString(1, newTelephone);
				ps.setString(2, oldTelephone);
				ps.execute(); // выполнение запроса и присвоение rs результата
				isEdit = true;
			}
			catch (SQLException e) {
				log.error("SQLException - Error request!" + e.getMessage());
				isEdit = false;
			}
			finally {
				SQL = null;
				ps.close();
			}
			// ------
		}
		finally {
			if (isEdit == false) {
				conn.rollback(savepoint);
				log.error("Error! Data doesn't save!Problem in editTelephone");
			}
			conn.commit();
			connectionPool.putback(conn);
		}
		return isEdit;

	}

	// main - edit Email
	private boolean editEmail(String oldEmail, String newEmail)
			throws SQLException {
		Savepoint savepoint = null;
		Connection conn = null;
		boolean isEdit = false;
		String SQL = null;
		PreparedStatement ps = null;
		try {
			conn = connectionPool.retrieve();
			conn.setAutoCommit(false);
			savepoint = conn.setSavepoint("savepoint");
			// ------
			try {
				SQL = "UPDATE Emails SET `email`=?  WHERE `email` = ? ";
				ps = conn.prepareStatement(SQL); // создание запроса ps
				ps.setString(1, newEmail);
				ps.setString(2, oldEmail);
				ps.execute(); // выполнение запроса и присвоение rs результата
				isEdit = true;
			}
			catch (SQLException e) {
				log.error("SQLException - Error request!" + e.getMessage());
				isEdit = false;
			}
			finally {
				SQL = null;
				ps.close();
			}
			// ------
		}
		finally {
			if (isEdit == false) {
				conn.rollback(savepoint);
				log.error("Error! Data doesn't save!Problem in editEmail");
			}
			conn.commit();
			connectionPool.putback(conn);
		}
		return isEdit;

	}

	// Delete-------------------------------//
	// telephone
	public boolean deleteTelephoneFromDB(String tel) throws SQLException {
		Savepoint savepoint = null;
		Connection conn = null;
		boolean isDel = false;
		String SQL = null;
		PreparedStatement ps = null;
		try {
			conn = connectionPool.retrieve();
			conn.setAutoCommit(false);
			savepoint = conn.setSavepoint("savepoint");
			// ------
			try {
				SQL = "DELETE FROM `Telephones` WHERE `number`=?";
				ps = conn.prepareStatement(SQL); // создание запроса ps
				ps.setString(1, tel);
				ps.execute(); // выполнение запроса и присвоение rs результата
				isDel = true;
			}
			catch (SQLException e) {
				log.error("SQLException - Error request!" + e.getMessage());
				isDel = false;
			}
			finally {
				SQL = null;
				ps.close();
			}
			// ------
		}
		finally {
			if (isDel == false) {
				conn.rollback(savepoint);
				log.error("Error! Data doesn't save!Problem in deleteTelephoneFromDB");
			}
			conn.commit();
			connectionPool.putback(conn);
		}
		return isDel;

	};

	// email
	public boolean deleteEmFromDB(String email) throws SQLException {
		Savepoint savepoint = null;
		Connection conn = null;
		boolean isDel = false;
		String SQL = null;
		PreparedStatement ps = null;
		try {
			conn = connectionPool.retrieve();
			conn.setAutoCommit(false);
			savepoint = conn.setSavepoint("savepoint");
			// ------
			try {
				SQL = "DELETE FROM `Emails` WHERE `email`=?";
				ps = conn.prepareStatement(SQL); // создание запроса ps
				ps.setString(1, email);
				ps.execute(); // выполнение запроса и присвоение rs результата
				isDel = true;
			}
			catch (SQLException e) {
				log.error("SQLException - Error request!" + e.getMessage());
				isDel = false;
			}
			finally {
				SQL = null;
				ps.close();
			}
			// ------
		}
		finally {
			if (isDel == false) {
				conn.rollback(savepoint);
				log.error("Error! Data doesn't save!Problem in addTelephoneInDBAfterEdit");
			}
			conn.commit();
			connectionPool.putback(conn);
		}
		return isDel;

	};

	// Object-------------------------------//
	// create new Child
	private Child addNewChildObj(String lastName, String firstName,
			String birthday, String lastNameParent, String firstNameParent,
			String byFatherParent, String parentTelephone1, String parentTelephone2,
			String parentEmail1, String parentEmail2) {

		Child child = new ChildImpl();
		child.setLastName(lastName);
		child.setFirstName(firstName);
		child.setBirthday(birthday);

		addInfoOfParentsForChildObj(child, lastNameParent, firstNameParent,
				byFatherParent, parentTelephone1, parentTelephone2, parentEmail1,
				parentEmail2);
		return child;
	}

	// H - add parent
	private Child addNewParentForChildObj(Child child, String lastNameParent,
			String firstNameParent, String byFatherParent, String parentTelephone1,
			String parentTelephone2, String parentEmail1, String parentEmail2) {
		User parent = new UserImpl();
		parent.setFirstName(firstNameParent);
		parent.setLastName(lastNameParent);
		parent.setByFather(byFatherParent);
		if (parentTelephone1.compareTo("") != 0)
			parent.setTelephones(parentTelephone1);
		if (parentTelephone2.compareTo("") != 0)
			parent.setTelephones(parentTelephone2);
		if (parentEmail1.compareTo("") != 0)
			parent.setEmails(parentEmail1);
		if (parentEmail2.compareTo("") != 0)
			parent.setEmails(parentEmail2);
		parent.setChildren(child);
		child.setParents(parent);

		return child;
	}

	// H - create objects of parents
	private Child addInfoOfParentsForChildObj(Child child, String lastNameParent,
			String firstNameParent, String byFatherParent, String parentTelephone1,
			String parentTelephone2, String parentEmail1, String parentEmail2) {

		if (lastNameParent.compareTo("") != 0)
			addNewParentForChildObj(child, lastNameParent, firstNameParent,
					byFatherParent, parentTelephone1, parentTelephone2, parentEmail1,
					parentEmail2);
		return child;
	}

	// --------------Later----------------------//

	public ReportOfMoney getCommonReport() {
		// TODO Auto-generated method stub
		return null;
	}

	public ReportOfMoney getReportOfMoney() {
		// TODO Auto-generated method stub
		return null;
	}


// main - setInReportOfMoney
	private boolean setInReportOfMoney(Integer idAdmin, String idParent, String inCash, String infoIn) throws SQLException {
		Savepoint savepoint = null;
		Connection conn = null;
		boolean isAdd = false;
		try {
			conn = connectionPool.retrieve();
			conn.setAutoCommit(false);
			savepoint = conn.setSavepoint("savepoint");
			// -----
			setInReportOfMoney(conn,idAdmin,idParent,inCash,infoIn);
			isAdd = true;
			// ------
		}
		finally {
			if (isAdd == false) {
				conn.rollback(savepoint);
				log.error("Error! Data doesn't save!Problem in addNewChildInDB");
			}
			conn.commit();
			connectionPool.putback(conn);
		}
		return isAdd;
	}

//H - setInReportOfMoney
	private boolean setInReportOfMoney(Connection conn, Integer idAdmin, String idParent, String inCash, String infoIn)
			throws SQLException {
		Integer idP = Integer.valueOf( idParent);
		Float in = Float.valueOf( inCash);
		PreparedStatement ps = null;
		String SQL;
		boolean isadd = false;
		try {
			SQL = "INSERT INTO `Cash`(`admin`,`whoGaveCash`,`in`,`info`) VALUE (?,?,?,?)";
			ps = conn.prepareStatement(SQL); // создание запроса ps
			ps.setInt(1,idAdmin );
			ps.setInt(2,idP );
			ps.setFloat(3, in);
			ps.setString(4, infoIn);
			ps.execute(); // выполнение запроса
			isadd = true;

			SQL = null;
			ps.close();
		}
		catch (SQLException e) {
			log.error("SQLException - Error request!" + e.getMessage());
			isadd = false;
		}
		return isadd;
	}

//main - setOUTReportOfMoney
	private boolean setOutReportOfMoney(Integer idAdmin, String inCash, String infoIn) throws SQLException {
		Savepoint savepoint = null;
		Connection conn = null;
		boolean isAdd = false;
		try {
			conn = connectionPool.retrieve();
			conn.setAutoCommit(false);
			savepoint = conn.setSavepoint("savepoint");
			// -----
			setOutReportOfMoney(conn,idAdmin,inCash,infoIn);
			isAdd = true;
			// ------
		}
		finally {
			if (isAdd == false) {
				conn.rollback(savepoint);
				log.error("Error! Data doesn't save!Problem in addNewChildInDB");
			}
			conn.commit();
			connectionPool.putback(conn);
		}
		return isAdd;
	}

//H - setInReportOfMoney
	private boolean setOutReportOfMoney(Connection conn, Integer idAdmin,String inCash, String infoIn)
			throws SQLException {
		Float in = Float.valueOf( inCash);
		PreparedStatement ps = null;
		String SQL;
		boolean isadd = false;
		try {
			SQL = "INSERT INTO `Cash`(`admin`,`out`,`info`) VALUE (?,?,?)";
			ps = conn.prepareStatement(SQL); // создание запроса ps
			ps.setInt(1,idAdmin );
			ps.setFloat(2, in);
			ps.setString(3, infoIn);
			ps.execute(); // выполнение запроса
			isadd = true;
		}
		catch (SQLException e) {
			log.error("SQLException - Error request!" + e.getMessage());
			isadd = false;
		}
		finally {
			SQL = null;
			ps.close();
		}
		return isadd;
	}
	
	
	public ReportOfMoney getIncoming() {
		// TODO Auto-generated method stub
		return null;
	}

}
