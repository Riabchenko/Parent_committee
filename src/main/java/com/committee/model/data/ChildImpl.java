package com.committee.model.data;

/**
 *
 * @author Aliona Riabchenko
 * @version 1.0 Build 21.06.2015
 *
 *
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChildImpl implements Child {

	public int idChild = 0;
	public String lastName = "";
	public String firstName = "";
	public String birthday = "";
	public List<User> parents;

	public ChildImpl() {
		parents = new ArrayList<User>();
	};

	public ChildImpl(int idChild, String lastName, String firstName,
			String birthday) {
		this.idChild = idChild;
		this.lastName = lastName;
		this.firstName = firstName;
		this.birthday = birthday;
		parents = new ArrayList<User>();
	}

	public int getIdChild() {
		return idChild;
	}

	public void setIdChild(int idChild) {
		this.idChild = idChild;
	}

	public boolean setLastName(String lastName) {
		this.lastName = lastName;
		return true;
	}

	public boolean setFirstName(String firstName) {
		this.firstName = firstName;
		return true;
	}

	public boolean setBirthday(String birthday) {
		this.birthday = birthday;
		return true;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getBirthday() {
		return birthday;
	}

	public List<User> getParents() {
		return parents;
	}

	public void setParents(User parent) {
		parents.add(parent);
	}

}
