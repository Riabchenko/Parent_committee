package com.committee.model.dao;

/**
 *
 * @author Aliona Riabchenko
 * @version 1.0 Build 21.06.2015
 *
 *
 */
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.committee.model.data.Child;
import com.committee.model.data.ReportOfMoney;
import com.committee.model.util.ConnectionPool;

public interface AdminDAO extends Service<AdminDAO> {
	boolean addNewChild(HttpServletRequest request, HttpServletResponse response,
			ConnectionPool dao) throws ServletException, IOException, SQLException;

	boolean editChild(int idChild, HttpServletRequest request,
			HttpServletResponse response, ConnectionPool dao)
			throws ServletException, IOException, SQLException;

	boolean changeTypeOfAccess(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			SQLException;

	List<Child> getAllChildWithParentsFromDB() throws SQLException;

	boolean editPassword(int idParent, String password) throws SQLException;

	boolean editAccessForParent(int idParent, String access) throws SQLException;
	
	boolean inReport(HttpServletRequest request,
			HttpServletResponse response, ConnectionPool dao)
			throws ServletException, IOException, SQLException;
	
  boolean outReport(HttpServletRequest request,
			HttpServletResponse response, ConnectionPool dao)
			throws ServletException, IOException, SQLException;

	// ReportOfMoney getCommonReport();
	// ReportOfMoney getCosts();
	// ReportOfMoney getMyReportOfMoney();

	// <T> T addNewAccount();
	// ReportOfMoney getReportOfMoney();
	// ReportOfMoney setReportOfMoney();
	// ReportOfMoney getIncoming();
}
