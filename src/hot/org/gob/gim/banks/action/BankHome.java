package org.gob.gim.banks.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.common.action.UserSession;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.bank.model.BankingEntityLog;

@Name("bankHome")
@Scope(ScopeType.CONVERSATION)
public class BankHome extends EntityHome<Object>implements Serializable {

	private static final long serialVersionUID = 1L;

	Log logger;

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	private Date startDate;
	private Date endDate;
	private List<ReportBank> reports;

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void getReportData() {
		 
		if (userSession.getUser().getCashierid() != null) {

			EntityManager em = getEntityManager();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String start = format.format(startDate);
			String end = format.format(endDate);
			String query = "SELECT d.date,mb.liquidationTime,MB.ID,MB.NUMBER,resident.identificationNumber, "
					+ " d.value, mb.entry_id ,mb.groupingcode, wsr.watermeternumber " + " from gimprod.Deposit d"
					+ " inner join gimprod.municipalbond mb on d.municipalbond_id= mb.id"
					+ " inner join gimprod.payment on d.payment_id=gimprod.payment.id"
					+ " inner join gimprod.resident on mb.resident_id = gimprod.resident.id"
					+ " LEFT OUTER JOIN gimprod.waterservicereference wsr on mb.adjunct_id=wsr.id"
					+ " WHERE d.status = 'VALID' AND" + " d.date BETWEEN '" + start + "' and '" + end + "' "
					+ " and payment.cashier_id= " + userSession.getUser().getCashierid() + " order by d.date";

			// 248608
			Query q = em.createNativeQuery(query); 

			List<Object[]> list = q.getResultList();
			this.reports = new ArrayList<ReportBank>();

			for (Object[] object : list) {
				  
				Date date = (Date) object[0];
				String dateliquidationTime =  object[1].toString();
				Long id = Long.valueOf(object[2] + "");
				Long number = Long.valueOf(object[3] + "");
				String identificationNumber = object[4] + "";
				BigDecimal value = new BigDecimal(object[5] + "");
				Long entry_id = Long.valueOf(object[6] + "");
				String groupingcode = object[7] + "";
				String watermeternumber = object[8] + "";

				ReportBank report = new ReportBank();
				report.setDate(date);
				report.setLiquidationTime(dateliquidationTime);
				report.setId(id);
				report.setNumber(number);
				report.setIdentificationNumber(identificationNumber);
				report.setValue(value);
				report.setEntry_id(entry_id);
				report.setGroupingcode(groupingcode);
				report.setWatermeternumber(watermeternumber);

				reports.add(report);
			} 
		} 
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public Object getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<ReportBank> getReports() {
		return reports;
	}

	public void setReports(List<ReportBank> reports) {
		this.reports = reports;
	}

}
