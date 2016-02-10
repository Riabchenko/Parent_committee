package com.committee.model.data;

/**
 *
 * @author Aliona Riabchenko
 * @version 1.0 Build 21.06.2015
 *
 *
 */
import java.util.Date;
import java.util.List;

public interface Child {
	void setIdChild(int idChild);

	int getIdChild();

	String getLastName();

	String getFirstName();

	String getBirthday();

	boolean setFirstName(String firstName);

	boolean setLastName(String lastName);

	boolean setBirthday(String birthday);

	public List<User> getParents();

	public void setParents(User parents);
}
