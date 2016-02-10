package com.committee.model.data;

/**
 *
 * @author Aliona Riabchenko
 * @version 1.0 Build 21.06.2015
 *
 *
 */
import java.util.List;

public interface User {
	public int getIdAccount();

	public void setIdAccount(int idAccount);

	public TypeOfAccess getTypeOfAccess();

	public void setTypeOfAccess(TypeOfAccess typeOfAccess);

	public String getLastName();

	public void setLastName(String lastName);

	public String getFirstName();

	public void setFirstName(String firstName);

	public String getByFather();

	public void setByFather(String byFather);

	public List<String> getTelephones();

	public void setTelephones(String telephone);

	public List<String> getEmails();

	public void setEmails(String email);

	public List<Child> getChildren();

	public void setChildren(Child child);
}
