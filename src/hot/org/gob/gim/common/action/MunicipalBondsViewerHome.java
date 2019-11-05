package org.gob.gim.common.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.income.facade.IncomeService;
import org.gob.gim.revenue.exception.EntryDefinitionNotFoundException;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityController;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.income.model.Deposit;
import ec.gob.gim.revenue.model.MunicipalBond;

@Name("municipalBondsViewerHome")
@Scope(ScopeType.CONVERSATION)
public class MunicipalBondsViewerHome extends EntityController{
	private static final long serialVersionUID = 1L;
	
	@In(create = true)
	UserSession userSession;
	
	private Resident resident;
	
	private List<MunicipalBond> municipalBonds;
	
	private List<Deposit> deposits;
	
	private Long inAgreementStatus;
	
	private Long inCompensationStatus;
	
	private Long pendingStatus;
	
	public Long getInAgreementStatusId() {
		if(inAgreementStatus != null) return inAgreementStatus;
		systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		inAgreementStatus = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_IN_PAYMENT_AGREEMENT");
		return inAgreementStatus;
	}
	
	public Long getInCompensationStatusId() {
		if(inCompensationStatus != null) return inCompensationStatus;
		systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		inCompensationStatus = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_IN_PAYMENT_AGREEMENT");
		return inCompensationStatus;
	}
	
	public Long getPendingStatusId() {
		if(pendingStatus != null) return pendingStatus;
		systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		pendingStatus = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_PENDING");
		return pendingStatus;
	}
	
	private List<Long> getStatusesForCalculate(){
		List<Long> ids = new ArrayList<Long>();
		ids.add(getInAgreementStatusId());
		ids.add(getInCompensationStatusId());
		ids.add(getPendingStatusId());
		return ids;
	}
		
	public void loadMunicipalBond(Long municipalBondId) throws EntryDefinitionNotFoundException {		
		municipalBonds = new ArrayList<MunicipalBond>();		
		if (municipalBondId != null){
			IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);			
			MunicipalBond municipalBond = incomeService.loadMunicipalBond(municipalBondId);
			if(municipalBond != null && getStatusesForCalculate().contains(municipalBond.getMunicipalBondStatus().getId())) incomeService.calculatePayment(municipalBond, true, true);
			municipalBonds.add(municipalBond);
			resident = municipalBond.getResident();
			deposits = loadDeposits(municipalBondId);
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<Deposit> loadDeposits(Long municipalBondId) {			
		Query query = getEntityManager().createNamedQuery("Deposit.findByMunicipalBondId");
		query.setParameter("municipalBondId", municipalBondId);
		return query.getResultList();
	}
	
	public void loadMunicipalBond(MunicipalBond municipalBond) {		
		municipalBonds = new ArrayList<MunicipalBond>();		
		if(municipalBond != null) municipalBonds.add(municipalBond);		
		if(municipalBond != null) resident = municipalBond.getResident();	
		if(municipalBond != null) deposits = loadDeposits(municipalBond.getId());
	}
	
	private SystemParameterService systemParameterService;
	
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	
	private Boolean hasDetailCheckerRole;
	
	public Boolean getHasDetailCheckerRole() {
		if(hasDetailCheckerRole != null) return hasDetailCheckerRole;
		if(systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		String role = systemParameterService.findParameter(UserSession.ROLE_NAME_DETAIL_CHECKER);
		if(role == null){
			hasDetailCheckerRole = false;
		}else{
			hasDetailCheckerRole = userSession.getUser().hasRole(role);
		}	
		return hasDetailCheckerRole; 
	}
	
	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}
	

	public List<MunicipalBond> getMunicipalBonds() {
		return municipalBonds;
	}

	public void setMunicipalBonds(List<MunicipalBond> municipalBonds) {
		this.municipalBonds = municipalBonds;
	}

	public Long getInAgreementStatus() {
		return inAgreementStatus;
	}

	public void setInAgreementStatus(Long inAgreementStatus) {
		this.inAgreementStatus = inAgreementStatus;
	}

	public Long getPendingStatus() {
		return pendingStatus;
	}

	public void setPendingStatus(Long pendingStatus) {
		this.pendingStatus = pendingStatus;
	}

	public Long getInCompensationStatus() {
		return inCompensationStatus;
	}

	public void setInCompensationStatus(Long inCompensationStatus) {
		this.inCompensationStatus = inCompensationStatus;
	}

	public List<Deposit> getDeposits() {
		return deposits;
	}

	public void setDeposits(List<Deposit> deposits) {
		this.deposits = deposits;
	}
	
	//para notificaciones de fotomultas
	// Jock Samaniego  06/07/2018
	
	String URLnotification;
	
	public String getURLnotification() {
		return URLnotification;
	}

	public void setURLnotification(String uRLnotification) {
		URLnotification = uRLnotification;
	}

	public void loadInfractionNotification(String ANTnotification){
		//System.out.println("======ANTnotification===> "+ANTnotification);
		this.URLnotification = ANTnotification;
	}
	
}
