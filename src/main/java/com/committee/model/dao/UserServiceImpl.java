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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.committee.model.data.Access;
import com.committee.model.data.TypeOfAccess;
import com.committee.model.data.UserImpl;
import com.committee.model.util.ConnectionPool;
import com.committee.model.util.ConnectionPoolManager;

public class UserServiceImpl extends ServiceImpl<UserDAO> implements UserDAO {
	HttpServletRequest request;
	HttpServletResponse response;
	ConnectionPoolManager dao;

	// Log4j logger
	private static final Logger log = Logger.getLogger(UserServiceImpl.class);

	public UserDAO handleRequest(HttpServletRequest request,
			HttpServletResponse response, ConnectionPool dao)
			throws ServletException, IOException {
		connectionPool = dao;
		Integer id = (Integer) request.getSession().getAttribute("accesUser");
		user = new UserImpl(id);
		user.setTypeOfAccess(TypeOfAccess.USER);
		try {
			createAccount(user);
		}
		catch (SQLException e) {
			log.error("Can't created account of user with id " + id + " : "
					+ e.getMessage());
		}
		UserDAO userdao = this;

		return userdao;
	}

	public int getIdUser() {
		return user.getIdAccount();
	}

}
