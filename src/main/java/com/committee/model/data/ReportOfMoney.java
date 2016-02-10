package com.committee.model.data;

/**
 *
 * @author Aliona Riabchenko
 * @version 1.0 Build 21.06.2015
 *
 *
 */
public interface ReportOfMoney {
	ReportOfMoney getReportOfMoney();

	ReportOfMoney setReportOfMoney();

	ReportOfMoney getIncoming();

	ReportOfMoney getMyReportOfMoney();

	ReportOfMoney getCommonReport();

	ReportOfMoney getCosts();

	public int getId_cash();

	public void setId_cash(int id_cash);

	public String getDateTime();

	public void setDateTime(String dateTime);

	public float getIn();

	public void setIn(float in);

	public float getOut();

	public void setOut(float out);

	public String getInfo();

	public void setInfo(String info);

	public String getWhoGaveCash();

	public void setWhoGaveCash(String whoGaveCash);

	public String getAdmin();

	public void setAdmin(String admin);

	public Integer getWhoGaveCashInt();

	public void setWhoGaveCashInt(Integer whoGaveCash);

	public Integer getAdminInt();

	public void setAdminInt(Integer admin);
}
