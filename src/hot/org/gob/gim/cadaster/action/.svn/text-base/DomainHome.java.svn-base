package org.gob.gim.cadaster.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.action.ResidentHome;
import org.gob.gim.common.action.UserSession;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.cadaster.model.Boundary;
import ec.gob.gim.cadaster.model.DispatchReason;
import ec.gob.gim.cadaster.model.Domain;
import ec.gob.gim.cadaster.model.Mortgage;
import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.cadaster.model.PurchaseType;
import ec.gob.gim.cadaster.model.TerritorialDivision;
import ec.gob.gim.common.model.CheckingRecord;
import ec.gob.gim.common.model.CheckingRecordType;
import ec.gob.gim.common.model.Resident;

@Name("domainHome")
public class DomainHome extends EntityHome<Domain> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@In(create = true)
	PropertyHome propertyHome;
	
	@In(create = true)
	TerritorialDivisionHome territorialDivisionHome;
	
	@In(create = true)
	PurchaseTypeHome purchaseTypeHome;
	
	@In(create = true)
	ResidentHome residentHome;
	
	@Logger
	Log logger;
	
	@In
	UserSession userSession;

	private String criteria;
	private String legalCriteria;
	private String criteriaEntry;
	private String identificationNumber;
	private String legalIdentificationNumber;
	private Domain domain = new Domain();
	private Mortgage mortgage;
	private PurchaseType purchaseType;
	private boolean firstTime=true;	

	private List<Resident> residents;	

	private List<Resident> legalResidents;

	private String changeHistory;

	public void setDomainId(Long id) {
		setId(id);
	}

	public Long getDomainId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
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
		cr.setObservation(propertyHome.getEditObservation());
		this.getInstance().add(cr);
	}
	
	/**
	 * Realiza el update al domain y property
	 * @return String 'updated' en caso de actualizado o 'failed' en caso de error
	 */
	public String preUpdate() {
//		addCheck(CheckingRecordType.CHECKED);
		if(!propertyHome.isUrban()){
			propertyHome.getInstance().setBlock(null);
		}
		String updated = update();
		Domain lastPropertyDomain = propertyHome.getInstance().getCurrentDomain();
//		copyLastAppraisal();
		
		this.getInstance().setChangeOwnerConfirmed(true);
		if (!this.getInstance().getPurchaseType().isChangeName()){
			this.getInstance().setSellName(lastPropertyDomain.getResident().getIdentificationNumber()+" "+lastPropertyDomain.getResident().getName());
			this.getInstance().setResident(lastPropertyDomain.getResident());
		}
		propertyHome.getInstance().setCurrentDomain(this.getInstance());
		//rarmijos 2015-11-23
		propertyHome.getInstance().setNeedConfirmChangeOwner(Boolean.FALSE);
		
		propertyHome.onlyUpdate();
		lastPropertyDomain.setCurrentProperty(null);
		this.setInstance(lastPropertyDomain);
		propertyHome.setEditObservation("Se confirma el TRASPASO DE DOMINIO");
		propertyHome.addCheck(CheckingRecordType.DOMAIN_TRANSFER);
		this.update();
		return updated;
	}
	
//	/**
//	 * Agrega un nuevo Appraisal en base al último existente
//	 */
//	private void copyLastAppraisal(){
//		if(propertyHome.getInstance().getCurrentDomain().getAppraisals().size() == 0) return;
//		this.getInstance().add(cloneAppraisal(propertyHome.getInstance().getCurrentDomain().getAppraisals().get(propertyHome.getInstance().getCurrentDomain().getAppraisals().size() -1)));
//	}
//	
//	/**
//	 * Clona un el último appraisal existente
//	 * @param appraisal el que será clonado
//	 * @return Appraisal
//	 */
//	private Appraisal cloneAppraisal(Appraisal appraisal){
//		Appraisal rest = new Appraisal();
//		rest.setBuilding(appraisal.getBuilding());
//		rest.setLot(appraisal.getLot());
//		rest.setCommercialAppraisal(appraisal.getCommercialAppraisal());
//		rest.setDate(appraisal.getDate());		
//		return rest;
//	}
//	
	/**
	 * Actualiza el dominio cuando no hay cambio de propietario
	 * @return String 'updated' en caso de actualizado o 'failed' en el caso de algún error
	 */
	public String preUpdateDontChangeOwner() {
		addCheck(CheckingRecordType.CHECKED);
		if(!propertyHome.isUrban()) propertyHome.getInstance().setBlock(null);
		this.getInstance().setChangeOwnerConfirmed(false);
		
		//rarmijos 2015-11-23
		propertyHome.getInstance().setNeedConfirmChangeOwner(Boolean.FALSE);
		
		return this.update();
	}

	public void wire() {
		getInstance();
		Property currentProperty = propertyHome.getDefinedInstance();
		
		
		if(!propertyHome.isUrban()){
			propertyHome.getInstance().setBlock(null);
		}
		
		if (currentProperty != null) {
			getInstance().setCurrentProperty(currentProperty);
		}
		TerritorialDivision notarysCity = territorialDivisionHome.getDefinedInstance();
		if (notarysCity != null) {
			getInstance().setNotarysCity(notarysCity);
		}
		TerritorialDivision notarysProvince = territorialDivisionHome.getDefinedInstance();
		if (notarysProvince != null) {
			getInstance().setNotarysProvince(notarysProvince);
		}
		Property property = propertyHome.getDefinedInstance();
		if (property != null) {
			getInstance().setProperty(property);
		}
		PurchaseType purchaseType = purchaseTypeHome.getDefinedInstance();
		if (purchaseType != null) {
			getInstance().setPurchaseType(purchaseType);
		}
		
		Resident resident = residentHome.getDefinedInstance();
		if (resident != null) {
			getInstance().setResident(resident);
		}
		
		if(this.getInstance().getResident() != null){
			identificationNumber = getInstance().getResident().getIdentificationNumber();
		}

		getInstance().setValueForCalculate(calculateValueTransaction());
	}
	
	public String getChangeHistory() {
		return changeHistory;
	}

	public void setChangeHistory(String changeHistory) {
		this.changeHistory = changeHistory;
	}

	public void addBoundary() {		
		this.getInstance().add(new Boundary());
	}	
	
	public void remove(Boundary boundary) {
		this.getInstance().remove(boundary);
	}
	
	public void addDispatchReason() {		
		this.getInstance().add(new DispatchReason());
	}	
	
	public void remove(DispatchReason dispatchReason) {
		this.getInstance().remove(dispatchReason);
	}

	/**
	 * Devuelve los cantones que pertenecen a una provincia
	 * @return List<TerritorialDivision>
	 */
	@SuppressWarnings("unchecked")
	public List<TerritorialDivision> getCantons() {
		if (getInstance().getNotarysProvince() != null) {
			Query query = getPersistenceContext().createNamedQuery("TerritorialDivision.findByParent");
			query.setParameter("parentId", getInstance().getNotarysProvince().getId());
			return query.getResultList();
		} else {
			return new ArrayList<TerritorialDivision>();
		}
	}
	
	/**
	 * Calcula el avalúo commercial del dominio
	 */
	public void calculateCommercialAppraisal(){
		if(this.getInstance() != null && this.getInstance().getBuildingAppraisal() != null && this.getInstance().getLotAppraisal() != null){
			this.getInstance().setCommercialAppraisal(this.getInstance().getBuildingAppraisal().add(this.getInstance().getLotAppraisal()));
		}else{
			this.getInstance().setCommercialAppraisal(new BigDecimal(0));
		}
	}

	/**
	 * Para editar el dominio
	 */
	public void wireEdit() {		
		getInstance();
		propertyHome.getDefinedInstance();
		if(!propertyHome.isUrban()){
			propertyHome.getInstance().setBlock(null);
		}
		
		if(this.getInstance().getResident() != null){
			identificationNumber = getInstance().getResident().getIdentificationNumber();
		}
		
		calculateCommercialAppraisal();
		
	}
	
	/**
	 * Para cambio de propietario
	 */
	public void wireChangeOwner() {
		getInstance();
		propertyHome.getDefinedInstance();		
		if(!propertyHome.getInstance().getDomains().contains(this.getInstance()))propertyHome.getInstance().add(this.getInstance());
		
		if(firstTime){
			if(this.getInstance().getResident() != null){
				identificationNumber = getInstance().getResident().getIdentificationNumber();
			}
			
			this.getInstance().setValueBySquareMeter(propertyHome.getInstance().getCurrentDomain().getValueBySquareMeter());
			this.getInstance().setBuildingAppraisal(propertyHome.getInstance().getCurrentDomain().getBuildingAppraisal());
			this.getInstance().setLotAppraisal(propertyHome.getInstance().getCurrentDomain().getLotAppraisal());
			
			copyLastBoundaries();
			
			calculateCommercialAppraisal();
						
			firstTime = false;
		}
		
	}
	
	private BigDecimal major(BigDecimal a, BigDecimal b){
		if(a.compareTo(b) == -1){
			return b;
		}
		return a;
	}
	
	/**
	 * Calcula el valor de la transacción en base al valor de la transacción
	 * y el avalúo comercial del dominio
	 * @return BigDecimal
	 */
	public BigDecimal calculateValueTransaction(){
		if(this.getInstance() != null){
			if(this.getInstance().getValueTransaction() == null) this.getInstance().setValueTransaction(new BigDecimal(0));
			if(this.getInstance().getCommercialAppraisal() == null) this.getInstance().setCommercialAppraisal(new BigDecimal(0));
			return major(this.getInstance().getValueTransaction(),this.getInstance().getCommercialAppraisal());
		}
				
		return new BigDecimal(0);
		
	}
	
	/**
	 * Copia los últimos límites del dominio
	 */
	private void copyLastBoundaries(){		
		List<Boundary> boundaries = new ArrayList<Boundary>();
		for(Boundary b: propertyHome.getInstance().getCurrentDomain().getBoundaries()){
			Boundary bo= new Boundary();
			bo.setCompassPoint(b.getCompassPoint());
			bo.setDescription(b.getDescription());
			bo.setLength(b.getLength());
			boundaries.add(bo);
		}
		this.getInstance().setBoundaries(boundaries);
		
	}

	public boolean isWired() {
		return true;
	}

	public Domain getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	/**
	 * Busca un resident en base al criterio de búsqueda
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

	@SuppressWarnings("unchecked")
	public void searchLegalResidentByCriteria() {
		logger.info("SEARCH LEGAL RESIDENT BY CRITERIA " + this.criteria);
		if (this.legalCriteria != null && !this.legalCriteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.legalCriteria);
			setLegalResidents(query.getResultList());
		}
	}

	/**
	 * Busca un resident en base al número de identificación
	 */
	public void searchResident() {
		// logger.info("RESIDENT CHOOSER CRITERIA... "+this.identificationNumber);
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			logger.info("RESIDENT CHOOSER ACTION " + resident.getName());

			// resident.add(this.getInstance());
			this.getInstance().setResident(resident);
			if(resident != null) this.getInstance().setBeneficiaries(resident.getName());

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			this.getInstance().setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	@SuppressWarnings("unused")
	public void searchLegalResident() {
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.legalIdentificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			logger.info("RESIDENT CHOOSER ACTION " + resident.getName());

			this.getInstance().setPersonLegalRevision(resident);
			if(resident != null) 
				this.getInstance().setLegalRevision(resident.getName());
			else 
				this.getInstance().setLegalRevision("");

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			this.getInstance().setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	/**
	 * Selecciona el resident del modalPanel del residentChooser
	 * @param event
	 */
	public void residentSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.getInstance().setResident(resident);
		this.getInstance().setBeneficiaries(resident.getName());
		this.setIdentificationNumber(resident.getIdentificationNumber());
	}

	public void residentLegalSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.getInstance().setPersonLegalRevision(resident);
		this.getInstance().setLegalRevision(resident.getName());
		this.setLegalIdentificationNumber(resident.getIdentificationNumber());
	}

	public void createMortgage() {
		this.mortgage = new Mortgage();
	}

	public void editMortgage(Mortgage m) {
		this.mortgage = m;
	}

	public void removeMortgage(Mortgage m) {		
		if (this.getInstance().getMortgages().contains(m))
			this.getInstance().remove(m);
	}

	public void addMortgage() {
		this.getInstance().add(this.mortgage);
	}

	public void clearSearchResidentPanel() {
		this.setCriteria(null);
		setResidents(null);
	}

	public void clearSearchLegalResidentPanel() {
		this.setLegalCriteria(null);
		setLegalResidents(null);
	}

//	public List<Appraisal> getAppraisals() {
//		return getInstance() == null ? null : new ArrayList<Appraisal>(
//				getInstance().getAppraisals());
//	}
	/*
	public List<MunicipalBond> getMunicipalBonds() {
		return getInstance() == null ? null : new ArrayList<MunicipalBond>(
				getInstance().getMunicipalBonds());
	}
	*/
	public List<Mortgage> getMortgages() {
		return getInstance() == null ? null : new ArrayList<Mortgage>(
				getInstance().getMortgages());
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteriaEntry(String criteriaEntry) {
		this.criteriaEntry = criteriaEntry;
	}

	public String getCriteriaEntry() {
		return criteriaEntry;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setMortgage(Mortgage mortgage) {
		this.mortgage = mortgage;
	}

	public Mortgage getMortgage() {
		return mortgage;
	}

	public void setPurchaseType(PurchaseType purchaseType) {
		this.purchaseType = purchaseType;
	}

	public PurchaseType getPurchaseType() {
		return purchaseType;
	}

	public void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
	}

	public boolean isFirstTime() {
		return firstTime;
	}
	
	public List<Boundary> getBoundaries(){		
		return this.getInstance().getBoundaries();
	}
	
	public List<DispatchReason> getDispatchReasons(){
		return this.getInstance().getDispatchReasons();
	}

	public String getLegalCriteria() {
		return legalCriteria;
	}

	public void setLegalCriteria(String legalCriteria) {
		this.legalCriteria = legalCriteria;
	}

	public String getLegalIdentificationNumber() {
		return legalIdentificationNumber;
	}

	public void setLegalIdentificationNumber(String legalIdentificationNumber) {
		this.legalIdentificationNumber = legalIdentificationNumber;
	}

	public List<Resident> getLegalResidents() {
		return legalResidents;
	}

	public void setLegalResidents(List<Resident> legalResidents) {
		this.legalResidents = legalResidents;
	}
	
}
