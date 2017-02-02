/**
 * 
 */
package org.gob.gim.income.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.facade.RevenueService;
import org.gob.gim.revenue.service.EmissionService;
import org.gob.gim.revenue.service.ImpugnmentService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityController;

import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.DTO.ReportEmissionDTO;
import ec.gob.gim.revenue.model.criteria.ReportEmissionCriteria;


/**
 * @author Rene
 *
 */
@Name("emissionReportHome")
@Scope(ScopeType.CONVERSATION)
public class EmissionReportHome extends EntityController {
	
	private static final long serialVersionUID = 1L;
	/*
	 * INYENCCIONES
	 */
	@In
	FacesMessages facesMessages;
	
	/*
	 * ATRIBUTOS
	 */
	
	private boolean isFirstTime = true;
	
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	
	private SystemParameterService systemParameterService;
	
	private EmissionService emissionService;
	
	public static String REVENUE_SERVICE_NAME = "/gim/RevenueService/local";
	private final static String ENTRIES_EMAALEP_LIST = "ENTRIES_EMAALEP_LIST";
	
	private MunicipalBondStatus inPaymentAgreementStatus;
	private MunicipalBondStatus externalChannelStatus;
	private MunicipalBondStatus paidMunicipalBondStatus;
	private MunicipalBondStatus compensationMunicipalBondStatus;
	private MunicipalBondStatus blockedMunicipalBondStatus;
	private MunicipalBondStatus reversedMunicipalBondStatus;
	private MunicipalBondStatus pendingStatus;
	private MunicipalBondStatus cancelledBondStatus;
	private MunicipalBondStatus futureBondStatus;
	
	private MunicipalBondStatus municipalBondStatus;
	
	private Date startDate;
	private Date endDate;
	
	private Charge incomeCharge;
	private Charge revenueCharge;
	private Delegate incomeDelegate;
	private Delegate revenueDelegate;
	
	private String explanation;
	private String explanationFormalize;
	
	private String criteriaEntry;
	private Entry entry;
	private String entryCode;
	private List<Entry> entries;
	
	private List<ReportEmissionDTO> allResults = new ArrayList<ReportEmissionDTO>();
	
	public EmissionReportHome() {
		
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

	public String getCriteriaEntry() {
		return criteriaEntry;
	}

	public void setCriteriaEntry(String criteriaEntry) {
		this.criteriaEntry = criteriaEntry;
	}

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	public String getEntryCode() {
		return entryCode;
	}

	public void setEntryCode(String entryCode) {
		this.entryCode = entryCode;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
	
	public MunicipalBondStatus getMunicipalBondStatus() {
		return municipalBondStatus;
	}

	public void setMunicipalBondStatus(MunicipalBondStatus municipalBondStatus) {
		this.municipalBondStatus = municipalBondStatus;
	}

	public List<ReportEmissionDTO> getAllResults() {
		return allResults;
	}

	public void setAllResults(List<ReportEmissionDTO> allResults) {
		this.allResults = allResults;
	}

	public void loadDefaultDates() {
		if (isFirstTime) {
			initializeServices();
			loadMunicipalBondStatus();
			loadDates();
			loadCharge();
			isFirstTime = false;
			explanation = systemParameterService
					.findParameter("STATUS_CHANGE_FUTURE_EMISSION_EXPLANATION");

			explanationFormalize = systemParameterService
					.findParameter("STATUS_CHANGE_FOMALIZE_EMISSION_EXPLANATION");
		}
	}
	
	private void initializeServices() {
		systemParameterService = ServiceLocator.getInstance().findResource(
				SystemParameterService.LOCAL_NAME);
		
		if (emissionService == null) {
			emissionService = ServiceLocator.getInstance().findResource(
					emissionService.LOCAL_NAME);
		}
	}
	
	private void loadMunicipalBondStatus() {
		if (pendingStatus != null)
			return;
		pendingStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PENDING");
		paidMunicipalBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PAID");
		compensationMunicipalBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class,
				"MUNICIPAL_BOND_STATUS_ID_COMPENSATION");
		inPaymentAgreementStatus = systemParameterService.materialize(
				MunicipalBondStatus.class,
				"MUNICIPAL_BOND_STATUS_ID_IN_PAYMENT_AGREEMENT");
		externalChannelStatus = systemParameterService.materialize(
				MunicipalBondStatus.class,
				"MUNICIPAL_BOND_STATUS_ID_PAID_FROM_EXTERNAL_CHANNEL");
		blockedMunicipalBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_BLOCKED");
		reversedMunicipalBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_REVERSED");
		futureBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class,
				"MUNICIPAL_BOND_STATUS_ID_FUTURE_ISSUANCE");
	}
	
	public void loadDates() {
		if (isFirstTime) {
			Calendar c = Calendar.getInstance();
			startDate = c.getTime();
			endDate = c.getTime();
			isFirstTime = false;
			loadReversedStatus();
		}

	}
	
	private void loadCharge() {
		incomeCharge = getCharge("DELEGATE_ID_INCOME");
		if (incomeCharge != null) {
			for (Delegate d : incomeCharge.getDelegates()) {
				if (d.getIsActive())
					incomeDelegate = d;
			}
		}
		revenueCharge = getCharge("DELEGATE_ID_REVENUE");
		if (revenueCharge != null) {
			for (Delegate d : revenueCharge.getDelegates()) {
				if (d.getIsActive())
					revenueDelegate = d;
			}
		}
	}
	
	private Charge getCharge(String systemParameter) {
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		Charge charge = systemParameterService.materialize(Charge.class,
				systemParameter);
		return charge;
	}
	
	public void loadReversedStatus() {
		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		}
		cancelledBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_VOID");
	}
	
	public void searchEntry() {
		if (entryCode != null) {
			RevenueService revenueService = ServiceLocator.getInstance()
					.findResource(REVENUE_SERVICE_NAME);
			Entry entry = revenueService.findEntryByCode(entryCode);
			if (entry != null) {
				this.entry = entry;
				this.setEntry(entry);
				if (entry.getAccount() != null) {
					setEntryCode(entry.getAccount().getAccountCode());
				} else {
					setEntryCode(entry.getCode());
				}
			} else {
				setEntry(null);
			}
		}
	}
	
	public void searchEntryByCriteria() {
		// logger.info("SEARCH Entry BY CRITERIA "+this.criteriaEntry);
		if (this.criteriaEntry != null && !this.criteriaEntry.isEmpty()) {
			RevenueService revenueService = (RevenueService) ServiceLocator
					.getInstance().findResource(REVENUE_SERVICE_NAME);
			entries = revenueService.findEntryByCriteria(criteriaEntry);
		}
	}
	
	public void clearSearchEntryPanel() {
		this.setCriteriaEntry(null);
		entries = null;
	}
	
	public void entrySelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Entry entry = (Entry) component.getAttributes().get("entry");
		this.setEntry(entry);
		if (entry.getAccount() != null) {
			setEntryCode(entry.getAccount().getAccountCode());
		} else {
			setEntryCode(entry.getCode());
		}
	}
	
	public void generateReport(){
		
		ReportEmissionCriteria criteria = new ReportEmissionCriteria();
		criteria.setStartDate(this.startDate);
		criteria.setEndDate(this.endDate);
		criteria.setAccount_id(this.entry == null ? null: this.entry.getAccount().getId());
		criteria.setStatus_ids("3, 4, 5, 6, 7, 11");
		
		this.allResults = this.emissionService.findEmissionReport(criteria);
		
		System.out.println(allResults);
		
		
	}
	
}