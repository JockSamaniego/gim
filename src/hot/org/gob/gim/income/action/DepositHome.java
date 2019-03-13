package org.gob.gim.income.action;
	
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.income.facade.IncomeService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.common.model.FinancialStatus;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.income.model.Deposit;
import ec.gob.gim.income.model.TillPermission;

@Name("depositHome")
public class DepositHome extends EntityHome<Deposit> {

	private static final long serialVersionUID = 1L;
	
	@In
	UserSession userSession;

	private List<Deposit> deposits;
	
	private Deposit deposit;
	
	private Boolean allDepositsSelected;
	
	private Resident resident;
	
	private boolean isTillOpened;
	
	public boolean isTillOpened() {
		return isTillOpened;
	}

	public void setTillOpened(boolean isTillOpened) {
		this.isTillOpened = isTillOpened;
	}

	private Person cashier;
	
	public Person getCashier() {		
		return cashier;
	}

	public void setCashier(Person cashier) {		
		this.cashier = cashier;
	}

	private List<Resident> residents;
	
	private String identificationNumber;
	
	private String criteria;
	
	private String concept;
	
	@Logger
	Log logger;

	public void setDepositId(Long id) {
		setId(id);
	}

	public Long getDepositId() {
		return (Long) getId();
	}

	public boolean isWired() {
		return true;
	}

	public Deposit getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	@Override
	public String persist() {
		try{
			List<Long> selected = getSelectedDepositIds();
			IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);

			incomeService.reverse(selected, concept, userSession.getUser().getResident());
			
			//@author
			//@tag recaudacionCoactivas
			//@date 2016-07-06T12:23
			//al realizar un reverso de abono, anular en la nueva tabla de municipalbondaux 
			incomeService.reversePaymentAgreements(selected);

			return "persisted";
		} catch (Exception e){
			addFacesMessageFromResourceBundle(e.getClass().getSimpleName());
			//e.printStackTrace();
			return null;
		}
	}
	
	public List<Deposit> getSelectedDeposits(){
		List<Deposit> selected = new ArrayList<Deposit>();
		if(deposits != null){
			for(Deposit d : deposits){
				if(d.getIsSelected()){
					selected.add(d);
				}
			}
		}
		return selected;
	}
	
	public List<Long> getSelectedDepositIds(){
		List<Long> selected = new ArrayList<Long>();
		for(Deposit d : deposits){
			if(d.getIsSelected() && d.getStatus() == FinancialStatus.VALID){
				selected.add(d.getId());
			}
		}
		return selected;
	}
	
	public void search() {
		logger.info("RESIDENT CHOOSER CRITERIA "+this.criteria);
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try{
			Resident resident = (Resident) query.getSingleResult();
			this.setResident(resident);
		}
		catch(Exception e){
			this.setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}
	
	@SuppressWarnings("unchecked")
	public void searchByCriteria(){
		logger.info("SEARCH BY CRITERIA "+this.criteria);
		if(this.criteria != null && !this.criteria.isEmpty()){
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			residents = query.getResultList();
		}
	}
	
	public void clearSearchPanel(){
		this.setCriteria(null);
		residents = null;
	}
	
	public void resetDialog(){
		this.concept = null;
	}
	
	public void residentSelectedListener(ActionEvent event){
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.setResident(resident);
		this.setIdentificationNumber(resident.getIdentificationNumber());		
	}

	public void findDeposits(){		
		if(resident == null || cashier == null){
			deposits.clear();
			return; 
		}
		IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
		deposits = incomeService.findDepositsForReverse(resident.getId(), cashier.getId());
		isTillOpened = findIsTillOpened(cashier.getId()); 
	}
	
	public boolean findIsTillOpened(Long cashierId){
		Query query = getEntityManager().createNamedQuery("TillPermission.findByCashierAndWorkdayDate");
		query.setParameter("date", Calendar.getInstance().getTime());
		query.setParameter("cashierId", cashierId);
		TillPermission tp = (TillPermission) query.getSingleResult();
		if(tp != null && tp.getClosingTime() == null && tp.getTill().isActive()){
			return true;
		}
		return false;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public void selectAllDeposits(){
		for(Deposit deposit : deposits){
			if(deposit.getStatus() == FinancialStatus.VALID){
				deposit.setIsSelected(allDepositsSelected);
			}
		}
	}
	
	public void synchronizeAllDepositsSelected(Boolean isSelected){
		if(!isSelected){
			allDepositsSelected = Boolean.FALSE;
		}
	}
	
	public String eliminateReverse(){
		if(deposit != null){
			System.out.println("REHABILITAR ---> "+deposit.getId());
			IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
			incomeService.eliminateReverse(deposit.getId(), userSession.getUser().getResident());
			return "reverted";
		}
		return null;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getCriteria() {
		return criteria;
	}

	public List<Deposit> getDeposits() {
		return deposits;
	}

	public void setDeposits(List<Deposit> deposits) {
		this.deposits = deposits;
	}

	public Boolean getAllDepositsSelected() {
		return allDepositsSelected;
	}

	public void setAllDepositsSelected(Boolean allDepositsSelected) {
		this.allDepositsSelected = allDepositsSelected;
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public Deposit getDeposit() {
		return deposit;
	}

	public void setDeposit(Deposit deposit) {
		this.deposit = deposit;
	}
	
}
