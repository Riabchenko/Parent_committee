package com.committee.model.data;

/**
 *
 * @author Aliona Riabchenko
 * @version 1.0 Build 21.06.2015
 *
 *
 */
import java.util.LinkedList;
import java.util.List;

public class UserImpl implements User {
	int idAccount = 0;
	// String password = "";
	TypeOfAccess typeOfAccess;

	public String lastName = "";
	public String firstName = "";
	public String byFather = "";

	List<String> telephones = new LinkedList();
	List<String> emails = new LinkedList();
	List<Child> children = new LinkedList();

	// for new user
	public UserImpl() {
		setTypeOfAccess(TypeOfAccess.USER);
	}

	public UserImpl(int id) {
		setIdAccount(id);
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

	public void setTypeOfAccess(TypeOfAccess typeOfAccess) {
		this.typeOfAccess = typeOfAccess;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getByFather() {
		return byFather;
	}

	public void setByFather(String byFather) {
		this.byFather = byFather;
	}

	public List<String> getTelephones() {
		return telephones;
	}

	public void setTelephones(String telephone) {
		telephones.add(telephone);
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(String email) {
		emails.add(email);
	}

	public List<Child> getChildren() {
		return children;
	}

	public void setChildren(Child child) {
		children.add(child);
	}

}
