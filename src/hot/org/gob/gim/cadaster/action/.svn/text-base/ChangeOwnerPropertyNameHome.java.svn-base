package org.gob.gim.cadaster.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.action.UserSession;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.cadaster.model.Appraisal;
import ec.gob.gim.cadaster.model.Boundary;
import ec.gob.gim.cadaster.model.Domain;
import ec.gob.gim.cadaster.model.Mortgage;
import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.common.model.CheckingRecord;
import ec.gob.gim.common.model.CheckingRecordType;
import ec.gob.gim.common.model.Resident;

@Name("changeOwnerPropertyName")
public class ChangeOwnerPropertyNameHome extends EntityHome<Property> {

	private static final long serialVersionUID = 1L;

	@In
	FacesMessages facesMessages;
	
	@In
	UserSession userSession;

	@Logger
	Log logger;
	
	private String criteria;	
	private List<Resident> residents;	
	private String identificationNumber;
	
	private Resident resident;
	
	private String observation;

	public void setPropertyId(Long id) {
		setId(id);
	}

	public Long getPropertyId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire(){
		getInstance();
	}
	
	/**
	 * Busca residents por un criterio
	 */
	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria() {
		logger.info("SEARCH RESIDENT BY CRITERIA " + this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			setResidents(query.getResultList());
		}
	}
	
	/**
	 * Busca resident por número de identificación
	 */
	public void searchResident() {
		logger.info("RESIDENT CHOOSER CRITERIA... " + this.identificationNumber);
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			resident = (Resident) query.getSingleResult();
			logger.info("RESIDENT CHOOSER ACTION " + resident.getName());
			
			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			resident = null;
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}
	
	/**
	 * Selecciona el resident del modalPanel del residentChooser
	 * @param event
	 */
	public void residentSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		resident = (Resident) component.getAttributes().get("resident");		
		this.setIdentificationNumber(resident.getIdentificationNumber());
	}

	/**
	 * Limpia panel de búsqueda de los resident
	 */
	public void clearSearchResidentPanel() {
		this.setCriteria(null);
		setResidents(null);
	}
	
	public String changeOwnerName(){
		this.getInstance().getCurrentDomain().setCurrentProperty(null);
		Domain d = copyDomain(this.getInstance().getCurrentDomain());		
		d.setObservations(observation);
		this.getInstance().add(d);
		this.getInstance().setCurrentDomain(d);
		addCheck(CheckingRecordType.NAME_CHANGE);
		return this.update();
	}
	
	/**
	 * Agrega un checkingRecordType al dominio
	 * @param checkingRecordType
	 */
	public void addCheck(CheckingRecordType checkingRecordType){
		CheckingRecord cr = new CheckingRecord();
		cr.setChecker(userSession.getPerson());
		cr.setCheckingRecordType(checkingRecordType);
		cr.setDate(new Date());
		cr.setTime(new Date());
		cr.setObservation(observation);
		this.getInstance().getCurrentDomain().getCurrentProperty().add(cr);
	}
	
	private Domain copyDomain(Domain domain){
		Domain d = new Domain();
//		copyLastAppraisals(domain, d);
		d.setBeneficiaries(domain.getBeneficiaries());
		copyLastBoundaries(domain, d);		
		d.setBuildingAppraisal(domain.getBuildingAppraisal());
		d.setCurrentProperty(domain.getCurrentProperty());
		d.setChangeOwnerConfirmed(Boolean.TRUE);
		d.setCommercialAppraisal(domain.getCommercialAppraisal());
		d.setCreationDate(new Date());
		if (domain.getDate() != null) d.setDate(domain.getDate());
		d.setHasDeed(domain.getHasDeed());
		d.setIsActive(domain.getIsActive());
		d.setLotAppraisal(domain.getLotAppraisal());
		d.setNewBuildingValue(domain.getNewBuildingValue());
		d.setNotaryNumber(domain.getNotaryNumber());
		d.setDescription(domain.getDescription());
		copyLastMortgages(domain, d);
		d.setNotarysCity(domain.getNotarysCity());
		d.setNotarysProvince(domain.getNotarysProvince());
		d.setObservations(domain.getObservations());
		d.setPropertyUse(domain.getPropertyUse());
		d.setPurchaseType(domain.getPurchaseType());
		d.setRealStateNumber(domain.getRealStateNumber());
		d.setResident(resident);
		d.setSpecialContribution(domain.getSpecialContribution());
		d.setTotalAreaConstruction(domain.getTotalAreaConstruction());
		if (domain.getTramitNumber() != null) d.setTramitNumber(domain.getTramitNumber());
		d.setValueBySquareMeter(domain.getValueBySquareMeter());
		d.setValueForCalculate(domain.getValueForCalculate());
		d.setValueTransaction(domain.getValueTransaction());
		d.setUserRegister(userSession.getUser());
		return d;
	}
	
//	/**
//	 * Copia los últimos appraisal
//	 */
//	private void copyLastAppraisals(Domain d, Domain newDomain){		
//		for(Appraisal a: d.getAppraisals()){
//			Appraisal ap= new Appraisal();
//			ap.setBuilding(a.getBuilding());
//			ap.setCommercialAppraisal(a.getCommercialAppraisal());
//			ap.setDate(a.getDate());
//			ap.setLot(a.getLot());
//			newDomain.add(ap);			
//		}
//		
//	}
//	
	/**
	 * Copia las últimas hipotecas
	 */
	private void copyLastMortgages(Domain d, Domain newDomain){		
		for(Mortgage m: d.getMortgages()){
			Mortgage aux= new Mortgage();
			aux.setAmount(m.getAmount());
			aux.setEndDate(m.getEndDate());
			aux.setFinancialInstitution(m.getFinancialInstitution());
			aux.setIsActive(m.getIsActive());
			aux.setMortgageType(m.getMortgageType());
			aux.setStartDate(m.getStartDate());
			newDomain.add(aux);			
		}
		
	}
	
	/**
	 * Copia los últimos límites del dominio
	 */
	private void copyLastBoundaries(Domain d, Domain newDomain){		
		for(Boundary b: d.getBoundaries()){
			Boundary bo= new Boundary();
			bo.setCompassPoint(b.getCompassPoint());
			bo.setDescription(b.getDescription());
			bo.setLength(b.getLength());
			newDomain.add(bo);
		}		
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}
	
}