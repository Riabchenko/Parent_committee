package com.committee.model.dao;

/**
 *
 * @author Aliona Riabchenko
 * @version 1.0 Build 21.06.2015
 *
 *
 */
import java.sql.SQLException;
import java.util.List;
import com.committee.model.action.Action;
import com.committee.model.data.Child;
import com.committee.model.data.ReportOfMoney;
import com.committee.model.data.User;

public interface Service<T> extends Action {

	public int getIdUser();

	public User getMyAccont();

	public Child getChildFromDB(int idChild) throws SQLException;

	public List<ReportOfMoney> getCommonReportOfMoney() throws SQLException;

	public ReportOfMoney getCosts();

	public T getAccountAllChildren();

	public ReportOfMoney getMyReportOfMoney();

	public T editAccount();

	public T getOwesMoney();
}
