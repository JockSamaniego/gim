package org.gob.gim.parking.action;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.ResidentHome;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.revenue.facade.RevenueService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.parking.model.Journal;
import ec.gob.gim.parking.model.ParkingLot;
import ec.gob.gim.revenue.model.Entry;

@Name("parkingLotHome")
public class ParkingLotHome extends EntityHome<ParkingLot> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3488019640553145387L;

	public static String REVENUE_SERVICE_NAME = "/gim/RevenueService/local";
	
	@Logger 
	Log logger;
	
	@In
	FacesMessages facesMessages;
	
	private String criteria;
	
	private String identificationNumber;
	
	private List<Resident> residents;
	
	private String entryCode;
	private Entry entry;
	private String criteriaEntry;
	private List<Entry> entries;
	
	private String cashClosingEntryCode;
	private Entry cashClosingEntry;
	
	@In(create = true)
	ResidentHome residentHome;
	
	
	private ParkingLot parkingLot = null;
	
	@In(scope=ScopeType.SESSION, value="userSession")
	UserSession userSession; //Fuente del operator
	
	private Journal journal;
	
	public ParkingLotHome() {
		journal = new Journal();
	}
	

	public void setParkingLotId(Long id) {
		setId(id);
	}

	public Long getParkingLotId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();			
		}
	}

	public void wire() {
		getInstance();		
		loadEntriesValues();
	}
	
	private void loadEntriesValues(){
		setEntry(this.getInstance().getEntry());
		if(getEntry() != null){
			if(getEntry().getAccount() != null){
				setEntryCode(getEntry().getAccount().getAccountCode());
			}else{
				setEntryCode(getEntry().getCode());
			}			
		}
		setCashClosingEntry(this.getInstance().getCashClosingEntry());
		if(getCashClosingEntry() != null){
			if(getCashClosingEntry().getAccount() != null){
				setCashClosingEntryCode(getCashClosingEntry().getAccount().getAccountCode());
			}else{
				setCashClosingEntryCode(getCashClosingEntry().getCode());
			}
		}
	}

	public boolean isWired() {
		return true;
	}

	public ParkingLot getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public ParkingLot getParkingLot() {
		return parkingLot;
	}

	public void setParkingLot(ParkingLot parkingLot) {
		this.parkingLot = parkingLot;
	}

	public void setJournal(Journal journal) {
		this.journal = journal;
		if(this.journal != null && this.journal.getOperator() != null)identificationNumber=this.journal.getOperator().getIdentificationNumber();
	}

	public Journal getJournal() {
		return journal;
	}
	
	@In(create=true)
	private JournalHome journalHome;
	
	public String addJournal(){		
		if (this.getJournal().getOperator() == null){
			String message = Interpolator.instance().interpolate(
					"#{messages['resident.required']}", new Object[0]);
			facesMessages.addToControl("",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			return "failed";
		}
		if (this.getJournal().getId() == null){
			//Agregar
			this.getJournal().setParkingLot(this.getInstance());
			this.journalHome.setInstance(this.getJournal());
			this.journalHome.persist();
			this.getInstance().addJournal(this.getJournal());
		} else {
			this.journalHome.setInstance(this.getJournal());
			this.journalHome.update();
		}
		this.update();
		
		this.setJournal(this.journalHome.getInstance());
		identificationNumber = null;
		return "updated";
	}
	
	
	public void removeJournal(Journal journal){
		this.getInstance().getJournals().remove(journal);
		this.update();
		this.setJournal(null);
	}


	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}


	public String getCriteria() {
		return criteria;
	}


	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}


	public String getIdentificationNumber() {
		return identificationNumber;
	}
	
	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}
	
	public void searchOwner() {
		logger.info("looking for............ {0}", this.getIdentificationNumber());
		Query query = getEntityManager().createNamedQuery("Person.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.getIdentificationNumber());
		try{
			Person resident = (Person) query.getSingleResult();
			logger.info("RESIDENT CHOOSER ACTION "+resident.getName());
			
			//resident.add(this.getInstance());			
			this.getJournal().setOperator(resident);
			
			if (resident.getId() == null){
				addFacesMessageFromResourceBundle("resident.notFound");
				return;
			}			
		}
		catch(Exception e){
			this.getJournal().setOperator(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	
	@SuppressWarnings("unchecked")
	public void searchOperatorByCriteria(){
		if (this.criteria != null && !this.criteria.isEmpty()){
			Query query = getEntityManager().createNamedQuery("Person.findByCriteria");
			query.setParameter("criteria", this.criteria);
			residents = query.getResultList();
		}
	}
	
	public void clearSearchPanel(){
		this.setCriteria(null);
		residents = null;
	}
	
	public void operatorSelectedListener(ActionEvent event){
		UIComponent component = event.getComponent();
		Person resident = (Person) component.getAttributes().get("resident");
		this.getJournal().setOperator(resident);
		identificationNumber = resident.getIdentificationNumber();
	}


	public Entry getEntry() {
		return entry;
	}


	public void setEntry(Entry entry) {
		this.entry = entry;
	}


	public String getCashClosingEntryCode() {
		return cashClosingEntryCode;
	}


	public void setCashClosingEntryCode(String cashClosingEntryCode) {
		this.cashClosingEntryCode = cashClosingEntryCode;
	}


	public Entry getCashClosingEntry() {
		return cashClosingEntry;
	}


	public void setCashClosingEntry(Entry cashClosingEntry) {		
		this.cashClosingEntry = cashClosingEntry;
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
	
	public void entrySelectedListener(ActionEvent event){
		UIComponent component = event.getComponent();
		Entry entry = (Entry) component.getAttributes().get("entry");
		this.setEntry(entry); //para vista
		this.getInstance().setEntry(entry); //para persistencia		
		if(entry.getAccount() == null){
			setEntryCode(entry.getCode());
		}else{
			setEntryCode(entry.getAccount().getAccountCode());
		}
	}
	
	public void clearSearchEntryPanel(){
		this.setCriteriaEntry(null);
		entries = null;
	}
	
	public void searchEntry() {
		if (entryCode != null) {
			RevenueService revenueService = ServiceLocator.getInstance().findResource(REVENUE_SERVICE_NAME); 
			Entry entry = revenueService.findEntryByCode(entryCode);
			if (entry != null){
				logger.info("Entry found... "+ entry.getName());
				this.entry = entry;
				this.instance.setEntry(entry);				
				if(entry.getAccount() != null){					
					setEntryCode(entry.getAccount().getAccountCode());
				}
			}
		}
	}
	
	public void searchEntryByCriteria(){
		logger.info("SEARCH Entry BY CRITERIA "+this.criteriaEntry);
		if (this.criteriaEntry != null && !this.criteriaEntry.isEmpty()){
			RevenueService revenueService = (RevenueService)ServiceLocator.getInstance().findResource(REVENUE_SERVICE_NAME); 
			entries = revenueService.findEntryByCriteria(criteriaEntry);			
		}
	}

	public void cashClosingEntrySelectedListener(ActionEvent event){
		UIComponent component = event.getComponent();
		Entry entry = (Entry) component.getAttributes().get("entry");		
		this.setCashClosingEntry(entry); //para vista
		this.getInstance().setCashClosingEntry(entry); //para persistencia		
		if(entry.getAccount() == null){
			setCashClosingEntryCode(entry.getCode());
		}else{
			setCashClosingEntryCode(entry.getAccount().getAccountCode());
		}
	}
		
		
	
	public void searchCashClosingEntry() {
		if (entryCode != null) {
			RevenueService revenueService = ServiceLocator.getInstance().findResource(REVENUE_SERVICE_NAME); 
			Entry entry = revenueService.findEntryByCode(cashClosingEntryCode);
			if (entry != null){
				logger.info("Entry found... "+ entry.getName());
				this.cashClosingEntry = entry;
				this.instance.setCashClosingEntry(cashClosingEntry);				
				if(entry.getAccount() != null){					
					setCashClosingEntryCode(entry.getAccount().getAccountCode());
				}
			}
		}
	}

	public String getEntryCode() {
		return entryCode;
	}


	public void setEntryCode(String entryCode) {		
		this.entryCode = entryCode;		
	}
	
	@Override
	public String persist(){
		this.getInstance().setManager(userSession.getPerson());
		return super.persist();
	}
	
	
}
