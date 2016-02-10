package com.committee.model.dao;

/**
 *
 * @author Aliona Riabchenko
 * @version 1.0 Build 21.06.2015
 *
 *
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import com.committee.model.data.Child;
import com.committee.model.data.ChildImpl;
import com.committee.model.data.ReportOfMoney;
import com.committee.model.data.ReportOfMoneyImpl;
import com.committee.model.data.User;
import com.committee.model.data.UserImpl;
import com.committee.model.util.ConnectionPool;

public abstract class ServiceImpl<T> implements Service<T> {
	User user;
	ConnectionPool connectionPool;

	List<Child> child;
	List<ReportOfMoney> reportOfCash;

	// Log4j logger
	private static final Logger log = Logger.getLogger(ServiceImpl.class);

	// ==================Method for create======================//
	// create profile of user
	public void createAccount(User user) throws SQLException {
		Connection conn = null;
		try {
			conn = connectionPool.retrieve();
			conn.setAutoCommit(false);
			// -----
			getFromDBInfoUser(conn, user);
			getFromBDChildrenForUser(conn, user);
			getFromDBTelephones(conn, user);
			getFromDBEmails(conn, user);
			// ------
		}
		finally {
			conn.commit();
			connectionPool.putback(conn);
		}
	}

	// Helper ---------------Methods for created Account------------//

	// H - get info for user
	public void getFromDBInfoUser(Connection conn, User user)
			throws SQLException, NullPointerException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String SQL;
		try {
			SQL = "SELECT `lastName`,`firstName`,`byFather` FROM `Parents` "
					+ "WHERE `id_parents` = ?";
			ps = conn.prepareStatement(SQL);
			ps.setInt(1, user.getIdAccount());
			rs = ps.executeQuery(); // выполнение запроса

			while (rs.next()) { // вывод значений полей в виде строк
				user.setLastName(rs.getString(1));
				user.setFirstName(rs.getString(2));
				user.setByFather(rs.getString(3));
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

	// H - get telephone for user
	public void getFromDBTelephones(Connection conn, User user)
			throws SQLException, NullPointerException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String SQL;
		try {
			SQL = "SELECT `number` " + "FROM `Telephones` "
					+ "WHERE `Parents_id_parents` = ? ";
			ps = conn.prepareStatement(SQL);
			ps.setInt(1, user.getIdAccount());
			rs = ps.executeQuery(); // выполнение запроса

			rs.last();
			int rowsOfTelephones = rs.getRow();
			rs.beforeFirst();

			if (rowsOfTelephones == 0) {
				log.warn("Doesn't find any telephone numbers!");
			}
			else {
				while (rs.next()) { // вывод значений полей в виде строк
					user.setTelephones(rs.getString(1));
				}
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

	// H - get email for user
	public void getFromDBEmails(Connection conn, User user) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String SQL;
		try {
			SQL = "SELECT `email` FROM `Emails` " + "WHERE `Parents_id_parents` = ? ";
			ps = conn.prepareStatement(SQL);
			ps.setInt(1, user.getIdAccount());
			rs = ps.executeQuery(); // выполнение запроса

			rs.last();
			int rowsOfEmails = rs.getRow();
			rs.beforeFirst();
			if (rowsOfEmails > 0) {
				List<String> em = new LinkedList<String>();
				while (rs.next()) { // вывод значений полей в виде строк
					user.setEmails(rs.getString(1));
				}
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

	// get all information of children for this user
	public void getFromBDChildrenForUser(Connection conn, User user)
			throws NullPointerException, SQLException {
		getFromDBChildren(conn, user);
		getFromDBIdParentsForChild(conn, user);
		for (Child ch : user.getChildren()) {
			for (User u : ch.getParents()) {
				getFromDBInfoOfParentsForChild(conn, user);
			}
		}
	}

	// H - get id parents for child
	public void getFromDBIdParentsForChild(Connection conn, User user)
			throws SQLException, NullPointerException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String SQL;
		try {
			for (Child ch : user.getChildren()) {
				SQL = "SELECT `Parents_id_parents` FROM `ChildrenAndParents` "
						+ "WHERE `Children_id_children` = ?";
				ps = conn.prepareStatement(SQL);
				ps.setInt(1, ch.getIdChild());
				rs = ps.executeQuery(); // выполнение запроса

				while (rs.next()) { // вывод значений полей в виде строк
					ch.setParents(new UserImpl(rs.getInt(1)));
				}
				SQL = null;
				rs.close();
				ps.close();
			}
		}
		catch (SQLException e) {
			log.error("SQLException - Error reques!" + e.getMessage());
		}
	}

	// H - Get children for this user
	public void getFromDBChildren(Connection conn, User user)
			throws SQLException, NullPointerException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String SQL;
		try {
			SQL = "SELECT `id_children`,`lastName`,`firstName`,`birthday` "
					+ "FROM `Children` " + "WHERE `id_children` = "
					+ "(SELECT `Children_id_children` " + "FROM `ChildrenAndParents` "
					+ "WHERE `Parents_id_parents` = ? )";
			ps = conn.prepareStatement(SQL);
			ps.setInt(1, user.getIdAccount());
			rs = ps.executeQuery(); // выполнение запроса
			while (rs.next()) { // вывод значений полей в виде строк
				int idChild = rs.getInt(1);
				String lastNameChild = rs.getString(2);
				String firstNameChild = rs.getString(3);
				Date birthday = rs.getDate(4);
				String birthdayChild = String.valueOf(birthday);
				user.setChildren(new ChildImpl(idChild, lastNameChild, firstNameChild,
						birthdayChild));
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

	// H - get info of parents for child
	public void getFromDBInfoOfParentsForChild(Connection conn, User u)
			throws SQLException, NullPointerException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String SQL;
		try {
			SQL = "SELECT `lastName`,`firstName`,`byFather` " + "FROM `Parents` "
					+ "WHERE `id_parents` = ?";
			ps = conn.prepareStatement(SQL);
			ps.setInt(1, u.getIdAccount());
			rs = ps.executeQuery(); // выполнение запроса

			while (rs.next()) { // вывод значений полей в виде строк
				u.setLastName(rs.getString(1));
				u.setFirstName(rs.getString(2));
				u.setByFather(rs.getString(3));
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

	// ================================================//

	// DB - getChild ----------------------------------------//
	// main - get Child from DB
	public Child getChildFromDB(int idChild) throws SQLException {
		Child child = null;
		Connection conn = null;
		try {
			conn = connectionPool.retrieve();
			System.out.print("  ...>>>>"+conn);
			conn.setAutoCommit(false);
			// -----
			child = getChildFromDBHelper(conn, idChild);
			if (child != null) {
				getParentsByIdChild(conn, child);
				for (User parent : child.getParents()) {
					getFromDBInfoOfParentsForChild(conn, parent);
					getFromDBTelephones(conn, parent);
					getFromDBEmails(conn, parent);
				}
			}
			conn.commit();
			// ------
		}
		finally {
			connectionPool.putback(conn);
		}
		return child;
	}

	// H - get from DB and create Child from DB
	protected Child getChildFromDBHelper(Connection conn, int idChild)
			throws SQLException {
		Child child = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String SQL;
		try {
			SQL = "SELECT `lastName`,`firstName`,`birthday` " + "FROM `Children` "
					+ "WHERE `id_children` = ? ";
			ps = conn.prepareStatement(SQL);
			ps.setInt(1, idChild);
			rs = ps.executeQuery(); // выполнение запроса
			while (rs.next()) { // вывод значений полей в виде строк
				child = new ChildImpl();
				child.setIdChild(idChild);
				child.setFirstName(rs.getString(2));
				child.setLastName(rs.getString(1));
				if (rs.getDate(3) != null) {
					Date birthday = rs.getDate(3);
					child.setBirthday(String.valueOf(birthday));
				}
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
		return child;
	}

	// H - get from DB and create Parents by idChild
	protected void getParentsByIdChild(Connection conn, Child child)
			throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String SQL;
		try {
			SQL = "SELECT `Parents_id_parents` " + "FROM `ChildrenAndParents` "
					+ "WHERE `Children_id_children` = ?";
			ps = conn.prepareStatement(SQL);
			ps.setInt(1, child.getIdChild());
			rs = ps.executeQuery(); // выполнение запроса
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			while (rs.next()) { // вывод значений полей в виде строк
				User parent = new UserImpl();
				parent.setIdAccount(rs.getInt(1));
				parent.setChildren(child);
				child.setParents(parent);
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

	public User getMyAccont() {
		return user;
	}

	// ----------------Reports---------------------------------------//
	public List<ReportOfMoney> getCommonReportOfMoney() throws SQLException {
		Connection conn = null;
		List<ReportOfMoney> listOfReport = new ArrayList<ReportOfMoney>();
		try {
			conn = connectionPool.retrieve();

			// -----
			listOfReport = getReports(conn);
			for (ReportOfMoney r : listOfReport) {
				getAdminsForReports(conn, r);
			}
			for (ReportOfMoney r : listOfReport) {
				if (r.getWhoGaveCashInt() != 0)
					getWhoGaveForReports(conn, r);
			}
			// ------
		}
		finally {
			connectionPool.putback(conn);
		}
		return listOfReport;
	}

	// H - get Reports
	protected List<ReportOfMoney> getReports(Connection conn) throws SQLException {
		ArrayList<ReportOfMoney> listReport = new ArrayList<ReportOfMoney>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String SQL;
		try {
			SQL = "SELECT `id_cash`,`dateTime`,`in`,`out`,`info`,`whoGaveCash`,`admin` FROM `Cash`";
			ps = conn.prepareStatement(SQL);
			rs = ps.executeQuery(); // выполнение запроса
			while (rs.next()) { // вывод значений полей в виде строк
				ReportOfMoney report = new ReportOfMoneyImpl();
				report.setId_cash(rs.getInt(1));
				report.setDateTime(rs.getDate(2).toString());
				report.setIn(rs.getFloat(3));
				report.setOut(rs.getFloat(4));
				report.setInfo(rs.getString(5));
				report.setWhoGaveCashInt(rs.getInt(6));
				report.setAdminInt(rs.getInt(7));
				listReport.add(report);
			}
			SQL = null;
			ps.close();
			rs.close();
		}
		catch (SQLException e) {
			log.error("SQLException - Error reques!" + e.getMessage());
		}
		return listReport;
	}

	// H - get name of admin for Reports
	protected void getAdminsForReports(Connection conn, ReportOfMoney report)
			throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String SQL;
		try {
			SQL = "SELECT `lastName`,`firstName`,`byFather` " + "FROM `Parents` "
					+ "WHERE `id_parents` = ?";
			ps = conn.prepareStatement(SQL);
			ps.setInt(1, report.getAdminInt());
			rs = ps.executeQuery(); // выполнение запроса
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			while (rs.next()) { // вывод значений полей в виде строк
				if (rs.getString(1) != null)
					report.setAdmin(rs.getString(1));
				if (rs.getString(1) != null)
					report.setAdmin(rs.getString(2));
				if (rs.getString(1) != null)
					report.setAdmin(rs.getString(3));
			}
			SQL = null;
			ps.close();
			rs.close();
		}
		catch (SQLException e) {
			log.error("SQLException - Error reques!" + e.getMessage());
		}
	}

	// H - get name of who gave cash for Reports
	protected void getWhoGaveForReports(Connection conn, ReportOfMoney report)
			throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String SQL;
		try {
			SQL = "SELECT `lastName` FROM `Parents` WHERE `id_parents` = ?";
			;
			ps = conn.prepareStatement(SQL);
			ps.setInt(1, report.getWhoGaveCashInt());
			rs = ps.executeQuery(); // выполнение запроса
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			while (rs.next()) { // вывод значений полей в виде строк
				report.setWhoGaveCash(rs.getString(1));
			}
		}
		catch (SQLException e) {
			log.error("SQLException - Error reques!" + e.getMessage());
		}
		finally {
			SQL = null;
			ps.close();
			rs.close();
		}
	}

	public ReportOfMoney getCosts() {
		// TODO Auto-generated method stub
		return null;
	}

	public T getAccountAllChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	public ReportOfMoney getMyReportOfMoney() {
		// TODO Auto-generated method stub
		return null;
	}

	public T getOwesMoney() {
		// TODO Auto-generated method stub
		return null;
	}

	public T editAccount() {
		// TODO Auto-generated method stub
		return null;
	}
}
