package org.gob.gim.revenue.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.income.facade.IncomeService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.common.model.Alert;
import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.EntryType;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.MunicipalBondType;
import ec.gob.gim.revenue.model.SolvencyHistory;
import ec.gob.gim.revenue.model.SolvencyHistoryType;
import ec.gob.gim.revenue.model.SolvencyReportType;

@Name("solvencyReportHome")
public class SolvencyReportHome extends EntityHome<MunicipalBond> {

	@In(create=true)
	UserSession userSession;
	
	@In(create=true)
	SolvencyHistoryHome solvencyHistoryHome;
	
	private static final long serialVersionUID = 1L;

	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	private String entryCode;
	private Entry entry;
	private String criteria;
	private String code;
	private String motivation="Trámites Municipales";
	private String criteriaEntry;
	private String identificationNumber;
	private String applicantIdentificationNumber;
	private List<Resident> residents;
	private List<Entry> entries;
	private SystemParameterService systemParameterService;
	
	private Boolean isReadyForPrint;

	private Resident resident;
	private Resident applicant;

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}
	
	public Resident getApplicant() {
		return applicant;
	}

	public void setApplicant(Resident applicant) {
		this.applicant = applicant;
	}

	@In
	FacesMessages facesMessages;

	private MunicipalBondStatus pendingBondStatus;
	private List<MunicipalBond> municipalBonds;

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getApplicantIdentificationNumber() {
		return applicantIdentificationNumber;
	}

	public void setApplicantIdentificationNumber(
			String applicantIdentificationNumber) {
		this.applicantIdentificationNumber = applicantIdentificationNumber;
	}

	public String getCriteriaEntry() {
		return criteriaEntry;
	}

	public void setCriteriaEntry(String criteriaEntry) {
		this.criteriaEntry = criteriaEntry;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	public Boolean getIsTaxSectionRendered() {
		if (entry != null) {
			return entry.getEntryType() == EntryType.TAX;
		}
		return Boolean.FALSE;
	}

	public String getEntryCode() {
		return entryCode;
	}

	public void setEntryCode(String entryCode) {
		this.entryCode = entryCode;
	}

	public void setMunicipalBondId(Long id) {
		setId(id);
	}

	public Long getMunicipalBondId() {
		return (Long) getId();
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}
	
	public String generateSolvencyCertificate(){
	
		if(resident == null){
			addFacesMessageFromResourceBundle("resident.empty");
			return "failed";
		}
		
		if(motivation.trim().length() <= 0){
			addFacesMessageFromResourceBundle("motivation.empty");
			return "failed";
		}
		
		if(solvencyReportType.equals(SolvencyReportType.SOLVENCY_REPORT_BY_ENTRY) && entry == null){
			addFacesMessageFromResourceBundle("entry.empty");
			return "failed";
		}
		
		if(buttonGenerate==false){
			return "failed";
		}
   		//Para controlar que no tenga alertas
		if(searchAlerts()>0){
			alertsActives=Boolean.TRUE;
			return "failed";
		}else{
			alertsActives=Boolean.FALSE;
		}	 		
		
		if(solvencyReportType.equals(SolvencyReportType.SOLVENCY_REPORT_BY_RESIDENT)){
			entry = null;
			entryCode = null;
			code = null;
		}
		
		if(code == null) code ="";		
		municipalBonds = findPendingBonds();				
		isReadyForPrint = true;
		
		if(municipalBonds.size() > 0) return null;
		certificateCounter();
		
		String finalIdentificationNumber;
		if(applicant!=null){
			finalIdentificationNumber=applicant.getIdentificationNumber();
		}else{
			finalIdentificationNumber=resident.getIdentificationNumber();
		}
		SolvencyHistory solvencyHistory = new SolvencyHistory(resident, motivation, SolvencyHistoryType.GENERAL, entry, null, userSession.getPerson(),certificateNumber, copiesNumber, finalIdentificationNumber);
		if (observation != null) {
			solvencyHistory.setObservation(observation);
		}
		solvencyHistoryHome.setInstance(solvencyHistory);
			
		if (solvencyHistoryHome.save() != "persisted"){
			addFacesMessageFromResourceBundle("history.persisted");
			return "failed";
		}

		if(entry == null){
			return "resident";
		}else{
			return "entry";
		}		
	}
	

	private void calculateTotal(List<MunicipalBond> municipalBonds){
		total = BigDecimal.ZERO;
		for(MunicipalBond m: municipalBonds){			
			total = total.add(m.getValue());
		}		
	}
	
	private BigDecimal total;
	
	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<MunicipalBond> findPendingBonds(){
		SystemParameterService systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
		Long  pendingMunicipalBondStatusId = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_PENDING");
		Long  payment_agreementBondStatusId = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_IN_PAYMENT_AGREEMENT");
		//rfarmijosm 2016-03-20 pedido de alice
		Long  blockedBondStatusId = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_BLOCKED");
		//rfarmijosm 2016-10-18 pedido de alice
		Long futureBondStatusId = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_FUTURE");		
		
		//rfam 2018-05-15 para soportar pagos en abonos
		Long subscriptionBondStatusId = systemParameterService.findParameter(IncomeService.SUBSCRIPTION_BOND_STATUS);
				
		List<Long> statuses = new ArrayList<Long>();
		statuses.add(pendingMunicipalBondStatusId);
		statuses.add(payment_agreementBondStatusId);
		//rfarmijosm 2016-03-20 pedido de alice
		statuses.add(blockedBondStatusId);
		//rfarmijosm 2016-10-18 pedido de alice
		statuses.add(futureBondStatusId);
		
		//rfarmijosm 2018-05-15 soportar pagos en abonos
		statuses.add(subscriptionBondStatusId);
		
		Query query = null;
		if(resident != null && entry == null && (code == null || code.trim().isEmpty()) ){
			query = getEntityManager().createNamedQuery("MunicipalBond.findByResidentIdAndTypeAndStatus");
		}else{
			query = getEntityManager().createNamedQuery("MunicipalBond.findByResidentIdAndTypeAndStatusAndEntryAndGroupingCode");
			query.setParameter("entryId", entry.getId());
			query.setParameter("code", code);
		}	
		
		query.setParameter("residentId", resident.getId());
		query.setParameter("municipalBondType",MunicipalBondType.CREDIT_ORDER);
		query.setParameter("municipalBondStatusIds", statuses);
		return query.getResultList();
	}
	
	public void loadValues(){
		if(pendingBondStatus != null) return;
		if(systemParameterService == null){
			systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);			
		}
		pendingBondStatus = systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PENDING");
		solvencyReportType = SolvencyReportType.SOLVENCY_REPORT_BY_RESIDENT;
		loadCharge();
	}
	
	private SolvencyReportType solvencyReportByEntry = SolvencyReportType.SOLVENCY_REPORT_BY_ENTRY;

	// TODO

	public SolvencyReportType getSolvencyReportByEntry() {
		return solvencyReportByEntry;
	}

	public void setSolvencyReportByEntry(SolvencyReportType solvencyReportByEntry) {
		this.solvencyReportByEntry = solvencyReportByEntry;
	}

	public void searchResident() {			
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			System.out.println("RESIDENT CHOOSER ACTION " + resident.getName());
			setResident(resident);
			isReadyForPrint = false;
			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}
	
	public void searchApplicant() {			
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.applicantIdentificationNumber);
		try {
			Resident applicant = (Resident) query.getSingleResult();
			System.out.println("APPLICANT CHOOSER ACTION " + applicant.getName());
			setApplicant(applicant);
			isReadyForPrint = false;
			if (applicant.getId() == null) {
				addFacesMessageFromResourceBundle("applicant.notFound");
			}

		} catch (Exception e) {
			setApplicant(null);
			addFacesMessageFromResourceBundle("applicant.notFound");
		}
	}
	
	private SolvencyReportType solvencyReportType;

	public SolvencyReportType getSolvencyReportType() {
		return solvencyReportType;
	}

	public void setSolvencyReportType(SolvencyReportType solvencyReportType) {
		this.solvencyReportType = solvencyReportType;
	}

	public void searchEntry() {
		if (entryCode != null) {
			Entry entry = findEntryByCode(entryCode);
			if (entry != null) {
				this.entry = entry;
				this.setEntry(entry);
				if (entry.getAccount() == null) {
					setEntryCode(entry.getCode());
				} else {
					setEntryCode(entry.getAccount().getAccountCode());
				}
			}else{				
				this.entry = null;
				setEntryCode(null);				
			}
		}
		isReadyForPrint = false;
	}
	
	public Entry findEntryByCode(String code) {
		code = this.completeEntryCode(code);
		Query query = getEntityManager().createNamedQuery("Entry.findByCode");
		query.setParameter("code", code);
		List<Entry> results = (List<Entry>)query.getResultList();
		if (! results.isEmpty())
			return results.get(0); 
		return null;
	}
	
	@Logger
	Log logger;
	
	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria() {
		logger.info("SEARCH RESIDENT BY CRITERIA " + this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery(
					"Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			residents = query.getResultList();
		}
	}
	
	private String completeEntryCode(String code) {
		StringBuffer codeBuffer = new StringBuffer(code.trim());
		while (codeBuffer.length() < 5) {
			codeBuffer.insert(0, '0');
		}
		return codeBuffer.toString();
	}

	
	public void searchEntryByCriteria() {
		if (this.criteriaEntry != null && !this.criteriaEntry.isEmpty()) {
			entries = findByCriteria(criteriaEntry);
		}
	}

	public List<Entry> findByCriteria(String criteria) {
		Query query = getEntityManager().createNamedQuery("Entry.findByCriteria");
		query.setParameter("criteria", criteria);
		return query.getResultList();
	}

	public void clearSearchResidentPanel() {
		this.setCriteria(null);
		residents = null;
	}

	public void clearSearchEntryPanel() {
		this.setCriteriaEntry(null);
		entries = null;
	}

	public void residentSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");this.getInstance().setResident(resident);
		this.resident = resident;
		this.setIdentificationNumber(resident.getIdentificationNumber());		
		setEntry(null);
		setEntryCode(null);
		isReadyForPrint = false;
	}

	public void applicantSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident applicant = (Resident) component.getAttributes().get("resident");this.getInstance().setResident(resident);
		this.applicant = applicant;
		this.setApplicantIdentificationNumber(applicant.getIdentificationNumber());		
		setEntry(null);
		setEntryCode(null);
		isReadyForPrint = false;
	}
	
	public void entrySelectedListener(ActionEvent event) {
			UIComponent component = event.getComponent();
		Entry entry = (Entry) component.getAttributes().get("entry");
		this.setEntry(entry);
		if (entry.getAccount() == null) {
			setEntryCode(entry.getCode());
		} else {
			setEntryCode(entry.getAccount().getAccountCode());
		}
		isReadyForPrint = false;
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

	public MunicipalBond getDefinedInstance() {		
		if (isIdDefined()) {
			municipalBonds.add(getInstance());
			return getInstance();
		} else {
			return null;
		}
	}

	public void setSystemParameterService(
			SystemParameterService systemParameterService) {
		this.systemParameterService = systemParameterService;
	}

	public SystemParameterService getSystemParameterService() {
		return systemParameterService;
	}

	public List<MunicipalBond> getMunicipalBonds() {
		return municipalBonds;
	}

	public void setMunicipalBonds(List<MunicipalBond> municipalBonds) {
		this.municipalBonds = municipalBonds;
	}

	private Date findMaximumServiceDate() {
		Query query = this.getEntityManager().createNamedQuery(
				"FiscalPeriod.findMaxDate");
		return (Date) query.getSingleResult();
	}
	
	private Charge revenueCharge;	
	private Charge incomeCharge;	
	private Delegate revenueDelegate;
	private Delegate incomeDelegate;
		
	public Charge getCharge(String systemParameter) {
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		Charge charge = systemParameterService.materialize(Charge.class,systemParameter);
		return charge;
	}
	
	public void loadCharge() {
		revenueCharge = getCharge("DELEGATE_ID_REVENUE");
		if (revenueCharge != null) {
			for (Delegate d : revenueCharge.getDelegates()) {
				if (d.getIsActive())
					revenueDelegate = d;
			}
		}
		incomeCharge = getCharge("DELEGATE_ID_INCOME");
		if (incomeCharge != null) {
			for (Delegate d : incomeCharge.getDelegates()) {
				if (d.getIsActive())
					incomeDelegate = d;
			}
		}		
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	
	}
	
	public Charge getRevenueCharge() {
		return revenueCharge;
	}

	public void setRevenueCharge(Charge revenueCharge) {
		this.revenueCharge = revenueCharge;
	}

	public Delegate getRevenueDelegate() {
		return revenueDelegate;
	}

	public void setRevenueDelegate(Delegate revenueDelegate) {
		this.revenueDelegate = revenueDelegate;
	}

	public Charge getIncomeCharge() {
		return incomeCharge;
	}

	public void setIncomeCharge(Charge incomeCharge) {
		this.incomeCharge = incomeCharge;
	}

	public Delegate getIncomeDelegate() {
		return incomeDelegate;
	}

	public void setIncomeDelegate(Delegate incomeDelegate) {
		this.incomeDelegate = incomeDelegate;
	}

	public Boolean getIsReadyForPrint() {
		return isReadyForPrint;
	}

	public void setIsReadyForPrint(Boolean isReadyForPrint) {
		this.isReadyForPrint = isReadyForPrint;
	}

	public String getMotivation() {
		return motivation;
	}

	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}

	//UserSession.ROLE_NAME_REVENUE_CERTIFICATE
	public Boolean hasRole(String roleKey) {

		if(systemParameterService == null){
			systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);			
		}
		
		String role = systemParameterService.findParameter(roleKey);
		if(role != null){
			return userSession.getUser().hasRole(role);
		}
		return false;
	}
	
//Autor: Jock Samaniego M.
//Para resetear el contador de certificados cada anio.
	private String certificateNumber;
	
	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

	public void certificateCounter(){
		SolvencyHistory sH=lastCertificate();
		if(sH.getCertificateNumber()==""||sH.getCertificateNumber()==null){
			certificateNumber=this.userSession.getFiscalPeriod().getFiscalYear().substring(1, 5)+"-000001";
		}else if(Integer.parseInt(sH.getCertificateNumber().substring(0, 4))<Integer.parseInt(this.userSession.getFiscalPeriod().getFiscalYear().substring(1, 5))){
			certificateNumber=this.userSession.getFiscalPeriod().getFiscalYear().substring(1, 5)+"-000001";
		}else{
			String s=sH.getCertificateNumber();
			String number=Integer.toString(Integer.parseInt(s.substring(5, s.length()))+1);
			String complement="";
			for(int i=number.length();i<6;i++){complement=complement+"0";}		
			certificateNumber=this.userSession.getFiscalPeriod().getFiscalYear().substring(1, 5)+"-"+complement+number;
		}
	}
	
//para obtener el ultimo registro guardado en la tabla.
	public SolvencyHistory lastCertificate(){
		Query query2 = getEntityManager().createNamedQuery("SolvencyHistory.findLastId");
		Long n=(Long)query2.getSingleResult();
		Query query = getEntityManager().createNamedQuery("SolvencyHistory.findLastCertificate");
		query.setParameter("id", n);
		SolvencyHistory solvencyHistory2=(SolvencyHistory) query.getSingleResult();
		return solvencyHistory2;
	}	
	
	//Autor: Jock Samaniego M.
	//Para controlar al emitir el certificado de solvencia que el resident no tenga alertas activas.
	private boolean alertsActives=Boolean.FALSE;
	
	public boolean isAlertsActives() {
		return alertsActives;
	}

	public void setAlertsActives(boolean alertsActives) {
		this.alertsActives = alertsActives;
	}
	
	private List<Alert> alerts;
	
	public List<Alert> getAlerts() {
		return alerts;
	}

	public void setAlerts(List<Alert> alerts) {
		this.alerts = alerts;
	}

	public int searchAlerts(){
		int numAlerts=0;
		alerts = new ArrayList<Alert>();
		if(resident.getIdentificationNumber()!=null){
			Query query = getEntityManager().createNamedQuery("Alert.findPendingAlertsByResidentIdNum");
			query.setParameter("residentIdNum", resident.getIdentificationNumber());
			alerts = query.getResultList();
		}else{
			Query query = getEntityManager().createNamedQuery("Alert.findPendingAlertsByResidentName");
			query.setParameter("residentName", resident.getName());
			alerts = query.getResultList();
		}	
		numAlerts=alerts.size();
		return numAlerts;
	}
	
	//Autor: Jock Samaniego M.
	//Para controlar que no impriman el certificado de forma incorrecta.
	private boolean buttonGenerate=Boolean.TRUE;
	
	public boolean isButtonGenerate() {
		return buttonGenerate;
	}

	public void setButtonGenerate(boolean buttonGenerate) {
		this.buttonGenerate = buttonGenerate;
	}

	public void enableButton(){
		buttonGenerate=Boolean.TRUE;		
	}
	
	public void disableButton(){
		buttonGenerate=Boolean.FALSE;	
	}

	//Autor: Jock Samaniego M.
	//Para guardar el numero de copias emitidas por certificado.

	private String copiesNumber="1";

	public String getCopiesNumber() {
		return copiesNumber;
	}

	public void setCopiesNumber(String copiesNumber) {
		this.copiesNumber = copiesNumber;
	}
	
	private String observation;

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}
		
}