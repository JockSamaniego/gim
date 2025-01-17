package org.gob.gim.income.action;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.commercial.action.LocalFeatureHome;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.electronicvoucher.creditnote.action.CreditNoteElectHome;
import org.gob.gim.exception.CreditNoteValueNotValidException;
import org.gob.gim.income.facade.IncomeService;
import org.gob.gim.income.facade.IncomeServiceBean;
import org.gob.gim.revenue.action.AdjunctHome;
import org.gob.gim.revenue.action.SolvencyReportHome;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.common.model.SystemParameter;
import ec.gob.gim.complementvoucher.model.ElectronicVoucher;
import ec.gob.gim.income.model.CreditNote;
import ec.gob.gim.income.model.EndorseCreditNote;
import ec.gob.gim.income.model.LegalStatus;
import ec.gob.gim.income.model.TaxItem;
import ec.gob.gim.income.model.dto.CreditNoteDTO;
import ec.gob.gim.revenue.model.Item;
import ec.gob.gim.revenue.model.MunicipalBond;

@Name("creditNoteHome")
public class CreditNoteHome extends EntityHome<CreditNote> {

	private static final long serialVersionUID = 1L;
	
	@EJB
	private SystemParameterService systemParameterService;

	private static final String PAID_BOND_STATUS_ID = "MUNICIPAL_BOND_STATUS_ID_PAID";

	private LegalStatus legalStatus;

	private String criteria;
	private String identificationNumber;
	private List<Resident> residents;
	private Resident resident;
	
	private Date startDate;
	
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
	
	public void loadValues(){
		Calendar ca = Calendar.getInstance();
		startDate = ca.getTime();
		endDate = ca.getTime();
	}

	private Date endDate;

	private Long municipalBondNumber;

	public void setCreditNoteId(Long id) {
		setId(id);
	}

	public Long getCreditNoteId() {
		return (Long) getId();
	}

	public void wire() {
		getInstance();
		if (getInstance().getResident() != null) {
			setResident(getInstance().getResident());
			setIdentificationNumber(getResident().getIdentificationNumber());
			/*System.out.println("============> "+identificationNumber);
			System.out.println("============> "+resident.getIdentificationNumber());
			System.out.println("============> "+getInstance().getId());*/
		}
		if(isFirstTime){
			chargeParameters();
			isFirstTime = Boolean.FALSE;
		}
	}

	public boolean isWired() {
		return true;
	}

	public CreditNote getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public LegalStatus getLegalStatus() {
		return legalStatus;
	}

	public void setLegalStatus(LegalStatus legalStatus) {
		this.legalStatus = legalStatus;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	@SuppressWarnings("unchecked")
	public void searchByCriteria() {
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			residents = query.getResultList();
		}
	}

	public void search() {
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			this.setResident(resident);
		} catch (Exception e) {
			this.setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void clearSearchPanel() {
		this.setCriteria(null);
		residents = null;
	}

	public void residentSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.setResident(resident);
		this.setIdentificationNumber(resident.getIdentificationNumber());
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	@Override
	public String persist() {
		getInstance().setResident(resident);
		IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
		try {
			incomeService.createCreditNote(this.getInstance(), legalStatus);
			if(getInstance().getCreditNoteType().getName().equals("notas de credito")){
				createCreditNotesElect();
			}
			return "persisted";
		} catch (CreditNoteValueNotValidException ex) {
			addFacesMessageFromResourceBundle(ex.getClass().getSimpleName());
			return null;
		}
	}
	
	@Override
	public String update() {
		try {
			if(getInstance().getCreditNoteType().getName().equals("notas de credito")){
				this.mbsForCreditNoteElect = getInstance().getMunicipalBonds();
				createCreditNotesElect();
			}
			super.update();
			return "updated";
		} catch (Exception ex) {
			addFacesMessageFromResourceBundle(ex.getClass().getSimpleName());
			return null;
		}
	}

	public String remove(MunicipalBond municipalBond) {
		this.getInstance().remove(municipalBond);
		return null;
	}

	public String addMunicipalBond() {
		if (municipalBondNumber != null && resident != null) {
			
			Long paidMunicipalBondStatusId = systemParameterService.findParameter(PAID_BOND_STATUS_ID);
			//rarmijos 2015-12-08
			// para crear notas de credito por pagos externos
			Long paidFromExternalBondStatusId = systemParameterService.findParameter(IncomeServiceBean.PAID_FROM_EXTERNAL_CHANNEL_BOND_STATUS);

			List<Long> municipalBondStatusIds = new ArrayList<Long>();
			municipalBondStatusIds.add(paidMunicipalBondStatusId);
			municipalBondStatusIds.add(paidFromExternalBondStatusId);

			Query query = getEntityManager().createNamedQuery("MunicipalBond.findByNumberAndStatusAndResidentId");
			query.setParameter("residentId", resident.getId());
			query.setParameter("municipalBondNumber", municipalBondNumber);
			query.setParameter("municipalBondStatusIds", municipalBondStatusIds);
			try {
				MunicipalBond municipalBond = (MunicipalBond) query.getSingleResult();
				if (municipalBond != null) {
					if (municipalBond.getCreditNote() == null) {
						getInstance().add(municipalBond);
						calculateTotalValue();
						if(municipalBond.getReceipt() != null){
							if(!this.mbsForCreditNoteElect.contains(municipalBond)){
								this.mbsForCreditNoteElect.add(municipalBond);
							}
						}
					} else {
						addFacesMessageFromResourceBundle("creditNote.municipalBondIsInOtherCreditNote", municipalBondNumber, municipalBond.getCreditNote().getId());
					}
				} else {
					addFacesMessageFromResourceBundle("creditNote.municipalBondDoesNotExistAsPaidForResident",municipalBondNumber);
				}
			} catch (Exception e) {
				addFacesMessageFromResourceBundle("creditNote.municipalBondDoesNotExistAsPaidForResident",municipalBondNumber);
			}
		} else {
			addFacesMessageFromResourceBundle("creditNote.enterMunicipalBondNumberFirst");
		}
		return null;
	}
	
	List<CreditNote> creditNotes;
	
	@SuppressWarnings("unchecked")
	public List<CreditNote> findCreditNotesBetweenDates(Date start, Date end){
		Query query = getEntityManager().createNamedQuery("CreditNote.findBetweenDates");
		query.setParameter("startDate", start);
		query.setParameter("endDate", end);
		return query.getResultList();
	}
	
	public boolean isDetailreport() {
		return isDetailreport;
	}

	public void setDetailreport(boolean isDetailreport) {
		this.isDetailreport = isDetailreport;
	}

	private boolean isDetailreport;
	
	private BigDecimal amountAvailable;
	
	public BigDecimal getAmountAvailable() {
		return amountAvailable;
	}

	public void setAmountAvailable(BigDecimal amountAvailable) {
		this.amountAvailable = amountAvailable;
	}

	public BigDecimal getAmountGenerated() {
		return amountGenerated;
	}

	public void setAmountGenerated(BigDecimal amountGenerated) {
		this.amountGenerated = amountGenerated;
	}

	private BigDecimal amountGenerated;
	
	public String generateReport(){
		creditNotes = findCreditNotesBetweenDates(startDate, endDate);
		amountAvailable = BigDecimal.ZERO;
		amountGenerated = BigDecimal.ZERO;
		for(CreditNote cn : creditNotes){			
			amountAvailable = amountAvailable.add(cn.getAvailableAmount());
			amountGenerated = amountGenerated.add(cn.getValue());			
		}
		return "readyForPrint";
	}

	public List<CreditNote> getCreditNotes() {
		return creditNotes;
	}

	public void setCreditNotes(List<CreditNote> creditNotes) {
		this.creditNotes = creditNotes;
	}

	public Long getMunicipalBondNumber() {
		return municipalBondNumber;
	}

	public void setMunicipalBondNumber(Long municipalBondNumber) {
		this.municipalBondNumber = municipalBondNumber;
	}
	
	//para endosar notas de credito
		//Jock Samaniego
	
	private Resident previousResident;
	private Resident currentResident;
	private String identificationNumberPrevious;
	private String identificationNumberCurrent;
	private Long residentId;
	private CreditNote creditNoteSelected;
	private Calendar now;
	private String endorseDetail;
	
	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;
//getters - Setters	
	public Resident getPreviousResident() {
		return previousResident;
	}

	public void setPreviousResident(Resident previousResident) {
		this.previousResident = previousResident;
	}

	public Resident getCurrentResident() {
		return currentResident;
	}

	public void setCurrentResident(Resident currentResident) {
		this.currentResident = currentResident;
	}

	public String getIdentificationNumberPrevious() {
		return identificationNumberPrevious;
	}

	public void setIdentificationNumberPrevious(String identificationNumberPrevious) {
		this.identificationNumberPrevious = identificationNumberPrevious;
	}

	public String getIdentificationNumberCurrent() {
		return identificationNumberCurrent;
	}

	public void setIdentificationNumberCurrent(String identificationNumberCurrent) {
		this.identificationNumberCurrent = identificationNumberCurrent;
	}

	public Long getResidentId() {
		return residentId;
	}

	public void setResidentId(Long residentId) {
		this.residentId = residentId;
	}

	public CreditNote getCreditNoteSelected() {
		return creditNoteSelected;
	}

	public void setCreditNoteSelected(CreditNote creditNoteSelected) {
		this.creditNoteSelected = creditNoteSelected;
	}
	
	public Calendar getNow() {
		return now;
	}

	public void setNow(Calendar now) {
		this.now = now;
	}

	public UserSession getUserSession() {
		return userSession;
	}

	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}

	public String getEndorseDetail() {
		return endorseDetail;
	}

	public void setEndorseDetail(String endorseDetail) {
		this.endorseDetail = endorseDetail;
	}

	//Metodo de endoso
	public String endorseCreditNote(CreditNote creditNote){
		creditNoteSelected = creditNote;	
		previousResident = creditNoteSelected.getResident();	
		identificationNumberPrevious=previousResident.getIdentificationNumber();
		now = Calendar.getInstance();
		return "/income/CreditNoteEndorse.xhtml";
	}
	
	public String saveCreditNoteEndorse(){
		BigDecimal totalEndorseSave = BigDecimal.ZERO;
		for(CreditNoteDTO cndto: listToEndorse){
			if(cndto.getIsValid() && cndto.getValue()!=null){
				if(cndto.getValue().compareTo(BigDecimal.ZERO)>0){
					totalEndorseSave = totalEndorseSave.add(cndto.getValue());
					
					CreditNote cNote = new CreditNote();
					cNote.setCreditNoteType(this.creditNoteSelected.getCreditNoteType());
					cNote.setDescription(this.creditNoteSelected.getDescription()+" (Endoso)");
					cNote.setDate(now.getTime());
					cNote.setIsActive(Boolean.TRUE);
					cNote.setReference(this.creditNoteSelected.getReference());
					cNote.setResolutionNumber(this.creditNoteSelected.getResolutionNumber());
					cNote.setUseToPay(this.creditNoteSelected.getUseToPay());
										
					cNote.setAvailableAmount(cndto.getValue());
					cNote.setValue(cndto.getValue());
					Resident currentRes = searchResidentCurrent(cndto.getIdentificationNumber()).get(0);
					cNote.setResident(currentRes);
					cNote.setParentCreditNote_id(this.creditNoteSelected.getId());
					EntityManager em = getEntityManager();
					em.persist(cNote);
					
					EndorseCreditNote ecNote = new EndorseCreditNote();
					ecNote.setCurrentResident(currentRes);
					ecNote.setPreviousResident(previousResident);
					ecNote.setEndorseDate(now.getTime());
					ecNote.setEndorseUser(userSession.getUser());
					ecNote.setCreditNote(creditNoteSelected);
					ecNote.setEndorseDetail(endorseDetail);
					em.persist(ecNote);
				}
			}	
		}
		BigDecimal actualAmount = this.creditNoteSelected.getAvailableAmount().subtract(totalEndorseSave);
		setInstance(creditNoteSelected);
		this.instance.setAvailableAmount(actualAmount);
		if(actualAmount.compareTo(BigDecimal.ZERO)<=0){
			this.instance.setIsActive(Boolean.FALSE);
		}
		super.update();
		this.listToEndorse = new ArrayList<CreditNoteDTO>();
		this.isValidEndorse = Boolean.FALSE;
		this.endorseDetail =  null;
		return "/income/CreditNoteList.xhtml";
	}
	
	public void createCreditNote(){
		
	}
	
	public void createCreditNoteEndorse(){
		
	}
		
	//metodos necesarios

	public List<Resident> searchResidentCurrent(String numIde) {
		List<Resident> residents = new ArrayList();
        Query query = getEntityManager().createNamedQuery(
                "Resident.findByIdentificationNumber");
        query.setParameter("identificationNumber",
        		numIde);
            residents = query.getResultList();
            return residents;
    }
	
//	public void currentSelectedListener(ActionEvent event) {
//        UIComponent component = event.getComponent();
//        Resident resident = (Resident) component.getAttributes()
//                .get("resident");
//        this.setCurrentResident(resident);
//        this.setIdentificationNumberCurrent(resident.getIdentificationNumber());
//    }
	
	public void previousResidentSelectedListener(ActionEvent event) {
        UIComponent component = event.getComponent();
        Resident resident = (Resident) component.getAttributes()
                .get("resident");
        this.setPreviousResident(resident);
        this.setIdentificationNumberPrevious(resident.getIdentificationNumber());
    }
	
	public void searchResidentPrevious() {
        Query query = getEntityManager().createNamedQuery(
                "Resident.findByIdentificationNumber");
        query.setParameter("identificationNumber",
                this.identificationNumberPrevious);
        try {
            Resident resident = (Resident) query.getSingleResult();
            this.setPreviousResident(resident);

            if (resident.getId() == null) {
                addFacesMessageFromResourceBundle("resident.notFound");
            }
        } catch (Exception e) {
            this.setPreviousResident(null);
            addFacesMessageFromResourceBundle("resident.notFound");
        }
    }
	
	//para endoso multiple de notas de credito
	//Jock Samaniego
	//04-12-2018
	
	private List<CreditNoteDTO> listToEndorse = new ArrayList();
	private Boolean isValidEndorse = Boolean.FALSE;
	
	public List<CreditNoteDTO> getListToEndorse() {
		return listToEndorse;
	}

	public void setListToEndorse(List<CreditNoteDTO> listToEndorse) {
		this.listToEndorse = listToEndorse;
	}
	
	public Boolean getIsValidEndorse() {
		return isValidEndorse;
	}

	public void setIsValidEndorse(Boolean isValidEndorse) {
		this.isValidEndorse = isValidEndorse;
	}

	
	public BigDecimal calculateTotalEndorse(){
		BigDecimal totalEndorse = BigDecimal.ZERO;
		for(CreditNoteDTO cn: listToEndorse){
			if(cn.getIsValid() && cn.getValue()!=null){
				if(cn.getValue().compareTo(BigDecimal.ZERO)<=0){
					cn.setColor("red");
				}else{
					totalEndorse = totalEndorse.add(cn.getValue());
					cn.setColor("white");
				}
			}		
		}
		if(totalEndorse.compareTo(BigDecimal.ZERO)<=0 || totalEndorse.compareTo(this.creditNoteSelected.getAvailableAmount())>0){
			this.isValidEndorse = Boolean.FALSE;
		}else{
			this.isValidEndorse = Boolean.TRUE;
		}
		return totalEndorse;
	}
	
	public void addCreditNoteEndorse() {
		CreditNoteDTO creditNoteItem = new CreditNoteDTO();
		creditNoteItem.setIsValid(Boolean.FALSE);
		creditNoteItem.setColor("white");
		listToEndorse.add(creditNoteItem);
	}
	
	public void remove(CreditNoteDTO creditNote) {
		listToEndorse.remove(creditNote);
		this.calculateTotalEndorse();
	}
	
	public void searchResidentName(CreditNoteDTO _creditNoteEndorse){
		List<Resident> rdts = searchResidentCurrent(_creditNoteEndorse.getIdentificationNumber());
		if(rdts.size()>0){
			_creditNoteEndorse.setName(rdts.get(0).getName());
			_creditNoteEndorse.setIsValid(Boolean.TRUE);
		}else{
			_creditNoteEndorse.setName("No existe el contribuyente");
		}

	}
	
		//Para imprimir reporte de la consulta completa
		//Jock Samaniego
		
		private List<CreditNote> creditnoteList;
		private Date dateFrom;
		private Date dateUntil;

		public List<CreditNote> getCreditnoteList() {
			return creditnoteList;
		}

		public void setCreditnoteList(List<CreditNote> creditnoteList) {
			this.creditnoteList = creditnoteList;
		}
				
		public Date getDateFrom() {
			return dateFrom;
		}

		public void setDateFrom(Date dateFrom) {
			this.dateFrom = dateFrom;
		}

		public Date getDateUntil() {
			return dateUntil;
		}

		public void setDateUntil(Date dateUntil) {
			this.dateUntil = dateUntil;
		}

		public void searhCreditNotesByCriteria(String name, String ident, Date from, Date until){
			creditnoteList = new ArrayList();

			if(from == null){
				from = new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime();
			}else{
				this.dateFrom = from;
			}
			if(until == null){
				until = new Date();		
			}else{
				this.dateUntil = until;
			}
			
			
			Query q = getEntityManager().createNamedQuery("CreditNote.findByResidentOrCreationDate");	
			q.setParameter("criteriaName", name);
			q.setParameter("criteriaIdentification", ident);
			q.setParameter("dateFrom", from);
			q.setParameter("dateUntil", until);
			creditnoteList = q.getResultList();
		}
		
		// para crear notas de credito electrónicas desde este Home
		// Jock Samaniego
		// 23-03-2021
		
		private List<MunicipalBond> mbsForCreditNoteElect = new ArrayList();
		
		@In(create = true)
		CreditNoteElectHome creditNoteElectHome;
		
		public void createCreditNotesElect(){
			creditNoteElectHome.wire();
			FiscalPeriod fiscalPeriod = null;
			Query q = getEntityManager().createNamedQuery("FiscalPeriod.findCurrent");
			q.setParameter("currentDate", getInstance().getDate());
			try{
				fiscalPeriod = (FiscalPeriod) q.getSingleResult();
			}catch (Exception e){
				fiscalPeriod = null;
				addFacesMessage("Periodo fiscal no encontrado para la fecha", getInstance().getDate());
			}
			creditNoteElectHome.setMbsForCreditNotes(this.mbsForCreditNoteElect);
			creditNoteElectHome.setEmissionReason(getInstance().getDescription().trim());
			creditNoteElectHome.setEmissionDate(getInstance().getDate());
			creditNoteElectHome.setFiscalPeriod(fiscalPeriod);
			Calendar cal = Calendar.getInstance();
			cal.setTime(getInstance().getDate());
			String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
			if(month.length() > 1){
				creditNoteElectHome.setMonthFiscal(month);
			}else{
				creditNoteElectHome.setMonthFiscal("0" + month);
			}
			//creditNoteElectHome.chargeValues();
			creditNoteElectHome.createFromCreditNoteHome();
		}
		
		public void calculateTotalValue(){
			BigDecimal total = BigDecimal.ZERO;
			for(MunicipalBond mb : getInstance().getMunicipalBonds()){
				for(Item item : mb.getItems()){
					if(!this.entries.contains(item.getEntry().getId())){
						total = total.add(item.getTotal());
					}
				}
				total = total.add(mb.getInterest().add(mb.getSurcharge()));
				total = total.subtract(mb.getDiscount());
				Query query = getEntityManager().createNamedQuery("TaxItem.findTaxItemByMunicipalBondId");
				query.setParameter("municipalBondId", mb.getId());
				try {
					TaxItem taxItem = (TaxItem) query.getSingleResult();
					if (taxItem != null) {
						total = total.add(taxItem.getValue());
					}
				} catch (Exception e) {
					addFacesMessageFromResourceBundle("No existe registro de impuestos", municipalBondNumber);
				}
			}
			getInstance().setValue(total);
			getInstance().setAvailableAmount(total);
		}
		
		private List<Long> entries;
		private Boolean isFirstTime = Boolean.TRUE;
		
		public void chargeParameters(){
			systemParameterService = ServiceLocator.getInstance().findResource(
					SystemParameterService.LOCAL_NAME);
			entries = new ArrayList();
			Query query = getEntityManager().createNamedQuery("SystemParameter.findByName");
			query.setParameter("name", "MB_ENTRIES_ELECTRONIC_CREDIT_NOTE");
			SystemParameter controlEntries = (SystemParameter) query.getSingleResult();
			String[] entriesStr = controlEntries.getValue().trim().split(",");
			for(String st : entriesStr){
				entries.add(Long.parseLong(st));
			}
		}
		
		public Boolean hasRole(String roleKey) {
			systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
			String role = systemParameterService.findParameter(roleKey);
			if (role != null) {
				return userSession.getUser().hasRole(role);
			}
			return false;
		}
		
		public void setAvailableAmount(){
			getInstance().setAvailableAmount(getInstance().getValue());
		}
		
		public void disabledCreditNote(){
			if(this.selectedToDisabled.getAvailableAmount().compareTo(this.selectedToDisabled.getValue()) == 0){
				this.selectedToDisabled.setIsActive(Boolean.FALSE);
				this.selectedToDisabled.setDescription(this.reasonToDisabled);
				for(MunicipalBond mb : this.selectedToDisabled.getMunicipalBonds()){
					mb.setCreditNote(null);
				}
				super.update();
			}
		}
				
		public void prepareToDisabled(CreditNote _creditNote){
			this.selectedToDisabled = _creditNote;
		}
		
		private String reasonToDisabled = "";
		private CreditNote selectedToDisabled;

		public String getReasonToDisabled() {
			return reasonToDisabled;
		}

		public void setReasonToDisabled(String reasonToDisabled) {
			this.reasonToDisabled = reasonToDisabled;
		}

		public CreditNote getSelectedToDisabled() {
			return selectedToDisabled;
		}

		public void setSelectedToDisabled(CreditNote selectedToDisabled) {
			this.selectedToDisabled = selectedToDisabled;
		}
		
}
