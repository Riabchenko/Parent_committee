package com.committee.model.data;

/**
 *
 * @author Aliona Riabchenko
 * @version 1.0 Build 21.06.2015
 *
 *
 */
public class ReportOfMoneyImpl implements ReportOfMoney {

	int id_cash;
	String dateTime;
	float in, out;
	String info;

	Integer whoGaveCashInt, adminInt;

	StringBuilder bsAdmin = new StringBuilder();
	StringBuilder bsWhoGave = new StringBuilder();

	public ReportOfMoneyImpl() {
	}

	public int getId_cash() {
		return id_cash;
	}

	public void setId_cash(int id_cash) {
		this.id_cash = id_cash;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public float getIn() {
		return in;
	}

	public void setIn(float in) {
		this.in = in;
	}

	public float getOut() {
		return out;
	}

	public void setOut(float out) {
		this.out = out;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getWhoGaveCash() {
		return bsWhoGave.toString();
	}

	public void setWhoGaveCash(String whoGaveCash) {
		bsWhoGave.append(whoGaveCash);
	}

	public String getAdmin() {
		return bsAdmin.toString();
	}

	public void setAdmin(String admin) {
		if (admin != null)
			bsAdmin.append(admin + " ");

	}

	public ReportOfMoneyImpl getReportOfMoney() {
		// TODO Auto-generated method stub
		return null;
	}

	public ReportOfMoneyImpl setReportOfMoney() {
		// TODO Auto-generated method stub
		return null;
	}

	public ReportOfMoneyImpl getIncoming() {
		// TODO Auto-generated method stub
		return null;
	}

	public ReportOfMoneyImpl getMyReportOfMoney() {
		// TODO Auto-generated method stub
		return null;
	}

	public ReportOfMoney getCommonReport() {
		// TODO Auto-generated method stub
		return null;
	}

	public ReportOfMoneyImpl getCosts() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getWhoGaveCashInt() {
		return whoGaveCashInt;
	}

	public void setWhoGaveCashInt(Integer whoGaveCash) {
		this.whoGaveCashInt = whoGaveCash;
	}

	public Integer getAdminInt() {
		return adminInt;
	}

	public void setAdminInt(Integer admin) {
		this.adminInt = admin;
	}

}
