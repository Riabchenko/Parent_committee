package com.committee.model.data;

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

public class AccessImpl implements Access {

	String telephone;
	String password;
	TypeOfAccess typeOfAccess;
	int idAccount;

	Connection conn = null;
	PreparedStatement ps;
	ResultSet rs;
	String SQL;

	public TypeOfAccess setTypeOfAccess(String access) {
		if (access.compareToIgnoreCase("admin") == 0)
			typeOfAccess = TypeOfAccess.ADMIN;
		else if (access.compareToIgnoreCase("user") == 0)
			typeOfAccess = TypeOfAccess.USER;
		return typeOfAccess;
	}

	public String setTelephone(String telephone) {
		this.telephone = telephone;
		return telephone;
	}

	public String setPassword(String password) {
		this.password = password;
		return password;
	}

	public String getPassword() {
		return password;
	}

	public int getIdAccount() {
		return idAccount;
	}

	public void setIdAccount(int idAccount) {
		this.idAccount = idAccount;
	}

	public TypeOfAccess getTypeOfAccess() {
		return typeOfAccess;
	}

	public boolean isAdmin() {
		if (typeOfAccess.equals(TypeOfAccess.ADMIN))
			return true;
		return false;
	}

}
