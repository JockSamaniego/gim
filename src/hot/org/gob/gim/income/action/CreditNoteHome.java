package org.gob.gim.income.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.exception.CreditNoteValueNotValidException;
import org.gob.gim.income.facade.IncomeService;
import org.gob.gim.income.facade.IncomeServiceBean;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.income.model.CreditNote;
import ec.gob.gim.income.model.LegalStatus;
import ec.gob.gim.revenue.model.MunicipalBond;

@Name("creditNoteHome")
public class CreditNoteHome extends EntityHome<CreditNote> {

	private static final long serialVersionUID = 1L;

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
			return "persisted";
		} catch (CreditNoteValueNotValidException ex) {
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
			SystemParameterService systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
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
}
