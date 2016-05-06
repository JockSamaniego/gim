package org.gob.gim.emoney.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

import org.gob.gim.accounting.dto.ReportType;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import javax.persistence.Query;

import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;

import ec.gob.gim.emoney.ConciliationEmoney;
import ec.gob.gim.firestation.model.FireMan;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.commercial.model.Business;


@Name("conciliationEmoneyHome")
public class ConciliationEmoneyHome extends EntityHome<ConciliationEmoney> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Logger 
	Log logger;

	
	private Date reportDate;
	private EmoneyReportType reportType;
	private List<ConciliationEmoney> dataList;
	private BigDecimal totalReport=BigDecimal.ZERO;
	
	
	
	public void setConciliationEmoneyId(Long id) {
		setId(id);
	}

	public Long getConciliationEmoneyId() {
		return (Long) getId();
	}

	public void load() {
//		if (isIdDefined()) {
////			wire();
//		}
	}
	

	@Factory("reportTypesEM")
	public List<EmoneyReportType> loadReportTypes() {
	
		List<EmoneyReportType> list = new ArrayList<EmoneyReportType>();
		
		for (EmoneyReportType emoneyReportType : EmoneyReportType.values()) {
			list.add(emoneyReportType);
		}
		
		return list;
	}

	@SuppressWarnings("unchecked")
	public void generateReport(){
		Calendar start = Calendar.getInstance();
		start.setTime(reportDate);
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND,0);
		
		Calendar end = Calendar.getInstance();
		end.setTime(reportDate);
		end.set(Calendar.HOUR_OF_DAY, 23);
		end.set(Calendar.MINUTE, 59);
		end.set(Calendar.SECOND,0);
		
		String account = reportType.getAccount();
		Query q = getEntityManager().createNamedQuery("ConciliationEmoney.findByDateAndAccount");
		q.setParameter("startDate", start.getTime());
		q.setParameter("endDate", end.getTime());		
		q.setParameter("account", "%"+account+"%");
		dataList = q.getResultList();
		totalReport = BigDecimal.ZERO;
		for (ConciliationEmoney conciliationEmoney : dataList) {
			totalReport  = totalReport.add(conciliationEmoney.getAmount());
		}
	}
	
	
	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public EmoneyReportType getReportType() {
		return reportType;
	}

	public void setReportType(EmoneyReportType reportType) {
		this.reportType = reportType;
	}

	public List<ConciliationEmoney> getDataList() {
		return dataList;
	}

	public void setDataList(List<ConciliationEmoney> dataList) {
		this.dataList = dataList;
	}

	public BigDecimal getTotalReport() {
		return totalReport;
	}

	public void setTotalReport(BigDecimal totalReport) {
		this.totalReport = totalReport;
	}
	
	
	 
}
