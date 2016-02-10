package com.committee.model.data;

/**
 *
 * @author Aliona Riabchenko
 * @version 1.0 Build 21.06.2015
 *
 *
 */
import java.sql.SQLException;

public interface Access {
	String setTelephone(String login);

	String setPassword(String password);

	TypeOfAccess setTypeOfAccess(String access);

	String getPassword();

	TypeOfAccess getTypeOfAccess() throws SQLException;

	int getIdAccount();

	boolean isAdmin();

	void setIdAccount(int idAccount);
}
