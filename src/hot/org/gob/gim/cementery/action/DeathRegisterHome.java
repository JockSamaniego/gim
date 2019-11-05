package org.gob.gim.cementery.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.cementery.facade.DeathService;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.Gim;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.action.EmissionOrderHome;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.cementery.model.Cementery;
import ec.gob.gim.cementery.model.Death;
import ec.gob.gim.cementery.model.Section;
import ec.gob.gim.cementery.model.Unit;
import ec.gob.gim.cementery.model.UnitStatus;
import ec.gob.gim.cementery.model.UnitType;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Contract;
import ec.gob.gim.revenue.model.ContractType;
import ec.gob.gim.revenue.model.EmissionOrder;

@Name("deathRegisterHome")
public class DeathRegisterHome extends EntityHome<Death> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	public static String DEATH_SERVICE_NAME = "/gim/DeathService/local";

	@In
	Gim gim;

	@In
	FacesMessages facesMessages;

	@In
	UserSession userSession;
	
	@In(create = true)
	private EmissionOrderHome emissionOrderHome;

	@In(create = true)
	UnitDeathHome unitDeathHome;

	Log logger;

	private SystemParameterService systemParameterService;
	private DeathService deathService;
	private EmissionOrder emissionOrder;


	private List<Cementery> cementeryList = new ArrayList<Cementery>();
	private List<Section> sectionList = new ArrayList<Section>();
	private List<UnitType> unitTypeList = new ArrayList<UnitType>();
	private List<Unit> unitList = new ArrayList<Unit>();
	
	private Cementery cementery = null;
	private Section section = null;
	private UnitType unitType = null;
	private Unit unit = null;
	
	private boolean isFirstTime = true;
	
	private boolean viewGeneralButtons = false;// cambia cuando una unidad es seleccionada y visualiza los botones de la unidad seleccionada
	private boolean renderZero = true;//no se renderiza ningún panel solamente el de selección de unidad y habilita/inhabilita la selección
	
	private String criteria;
	private String identificationNumber;

	private boolean selectStatus;
	private UnitStatus unitStatus;
	
	private boolean reserve;
	private boolean exhumation;
	private boolean forExhumation;

	private String responsableIdentification;
	private String responsableCriteria;

	private Person responsable;
	private Resident responsableResident;

	private String deceasedIdentification;
	private String deceasedCriteria;
	private String debtorIdentification;
	private String debtorCriteria;

	private Person deceased;
	private Resident deceasedResident;
	private Resident debtor;

	private List<Resident> residents;

	private boolean registered = true;

	private boolean newContract = false;
	private boolean renewContract;

	private Unit unitLocal;
	private Date dateExhumation;
	private Date dateToExhumation;

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		//System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRR ingresa a wire");
		if (isFirstTime){
			isFirstTime = false;
			getInstance();
			//System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRR ingresa a wire getInstance");
		}
	}

	public boolean isWired() {
		return true;
	}

	public Death getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void setDeathId(Long id) {
		setId(id);
	}

	public Long getDeathId() {
		return (Long) getId();
	}

/*
	@Factory("unitStatuses")
	public List<UnitStatus> loadUnitStatus() {
		return Arrays.asList(UnitStatus.values());
	}

	@SuppressWarnings("unchecked")
	@Factory("unitTypes")
	public List<UnitType> loadUnitTypes() {
		Query query = this.getEntityManager().createNamedQuery(
		"UnitType.findAll");
		return (List<UnitType>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Factory("unitTypesName")
	public List<UnitType> loadUnitTypesName() {
		Query query = this.getEntityManager().createNamedQuery(
		"UnitType.findAllNames");
		return (List<UnitType>) query.getResultList();
	}
*/
	public List<Cementery> getCementeryList() {
		return cementeryList;
	}

	public void setCementeryList(List<Cementery> cementeryList) {
		this.cementeryList = cementeryList;
	}

	public List<Section> getSectionList() {
		return sectionList;
	}

	public void setSectionList(List<Section> sectionList) {
		this.sectionList = sectionList;
	}

	public List<UnitType> getUnitTypeList() {
		return unitTypeList;
	}

	public void setUnitTypeList(List<UnitType> unitTypeList) {
		this.unitTypeList = unitTypeList;
	}

	public List<Unit> getUnitList() {
		return unitList;
	}

	public void setUnitList(List<Unit> unitList) {
		this.unitList = unitList;
	}

	public Cementery getCementery() {
		return cementery;
	}

	public void setCementery(Cementery cementery) {
		this.cementery = cementery;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public UnitType getUnitType() {
		return unitType;
	}

	public void setUnitType(UnitType unitType) {
		this.unitType = unitType;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public boolean isViewGeneralButtons() {
		return viewGeneralButtons;
	}

	public void setViewGeneralButtons(boolean viewGeneralButtons) {
		this.viewGeneralButtons = viewGeneralButtons;
	}

	public boolean isRenderZero() {
		return renderZero;
	}

	public void setRenderZero(boolean renderZero) {
		this.renderZero = renderZero;
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

	public boolean isSelectStatus() {
		return selectStatus;
	}

	public void setSelectStatus(boolean selectStatus) {
		this.selectStatus = selectStatus;
	}

	public UnitStatus getUnitStatus() {
		return unitStatus;
	}

	public void setUnitStatus(UnitStatus unitStatus) {
		this.unitStatus = unitStatus;
	}

	public boolean isReserve() {
		return reserve;
	}

	public void setReserve(boolean reserve) {
		this.reserve = reserve;
	}

	public boolean isExhumation() {
		return exhumation;
	}

	public void setExhumation(boolean exhumation) {
		this.exhumation = exhumation;
	}

	public boolean isForExhumation() {
		return forExhumation;
	}

	public void setForExhumation(boolean forExhumation) {
		this.forExhumation = forExhumation;
	}

	public String getResponsableIdentification() {
		return responsableIdentification;
	}

	public void setResponsableIdentification(String responsableIdentification) {
		this.responsableIdentification = responsableIdentification;
	}

	public String getResponsableCriteria() {
		return responsableCriteria;
	}

	public void setResponsableCriteria(String responsableCriteria) {
		this.responsableCriteria = responsableCriteria;
	}

	public Person getResponsable() {
		return responsable;
	}

	public void setResponsable(Person responsable) {
		this.responsable = responsable;
	}

	public Resident getResponsableResident() {
		return responsableResident;
	}

	public void setResponsableResident(Resident responsableResident) {
		this.responsableResident = responsableResident;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	public Unit getUnitLocal() {
		return unitLocal;
	}

	public void setUnitLocal(Unit unitLocal) {
		this.unitLocal = unitLocal;
	}

	public Date getDateExhumation() {
		return dateExhumation;
	}

	public void setDateExhumation(Date dateExhumation) {
		this.dateExhumation = dateExhumation;
	}

	public Date getDateToExhumation() {
		return dateToExhumation;
	}

	public void setDateToExhumation(Date dateToExhumation) {
		this.dateToExhumation = dateToExhumation;
	}

	public String getDeceasedIdentification() {
		return deceasedIdentification;
	}

	public void setDeceasedIdentification(String deceasedIdentification) {
		this.deceasedIdentification = deceasedIdentification;
	}

	public String getDeceasedCriteria() {
		return deceasedCriteria;
	}

	public void setDeceasedCriteria(String deceasedCriteria) {
		this.deceasedCriteria = deceasedCriteria;
	}

	public String getDebtorIdentification() {
		return debtorIdentification;
	}

	public void setDebtorIdentification(String debtorIdentification) {
		this.debtorIdentification = debtorIdentification;
	}

	public String getDebtorCriteria() {
		return debtorCriteria;
	}

	public void setDebtorCriteria(String debtorCriteria) {
		this.debtorCriteria = debtorCriteria;
	}

	public Person getDeceased() {
		return deceased;
	}

	public void setDeceased(Person deceased) {
		this.deceased = deceased;
	}

	public Resident getDeceasedResident() {
		return deceasedResident;
	}

	public void setDeceasedResident(Resident deceasedResident) {
		this.deceasedResident = deceasedResident;
	}

	public Resident getDebtor() {
		return debtor;
	}

	public void setDebtor(Resident debtor) {
		this.debtor = debtor;
	}

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public boolean isNewContract() {
		return newContract;
	}

	public void setNewContract(boolean newContract) {
		this.newContract = newContract;
	}

	public boolean isRenewContract() {
		return renewContract;
	}

	public void setRenewContract(boolean renewContract) {
		this.renewContract = renewContract;
	}

	public void changeViewButtons(boolean viewButtons){
		this.viewGeneralButtons = viewButtons;
	}

	@SuppressWarnings("unchecked")
	public List<Cementery> findCementeries(){
		Query query = getPersistenceContext().createNamedQuery(
				"Cementery.findAll");
		cementery = null;
		findSectionsByCementery();
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Section> findSectionsByCementery(){
		if (cementery == null){
			section=null;
			if (!sectionList.isEmpty()) sectionList.clear();
			findUnitsTypeInSection();
			return null;
		}
		section=null;
		unitType = null;
		unit = null;
		Query query = getPersistenceContext().createNamedQuery(
				"Section.findByCementery");
		query.setParameter("cementery", cementery);
		sectionList.clear();
		sectionList = query.getResultList();
		if (!sectionList.isEmpty()) findUnitsTypeInSection();
		return sectionList;
	}
	
	@SuppressWarnings("unchecked")
	public List<UnitType> findUnitsTypeInSection(){
		if (section == null){
			unitType = null;
			if (!unitTypeList.isEmpty()) unitTypeList.clear();
			findUnitsBySectionAndType();
			return null;
		}
		unitType = null;
		unit = null;
		unitTypeList.clear();
		Query query = getPersistenceContext().createNamedQuery(
				"Unit.findUnitsInSection");
		query.setParameter("section", section);
		unitTypeList = query.getResultList();
		if (!unitTypeList.isEmpty()) findUnitsBySectionAndType();
		return unitTypeList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Unit> findUnitsBySectionAndType(){
		if ((section == null) || (unitType == null)) {
			unit=null;
			if (!unitList.isEmpty()) unitList.clear();
			return null;
		}
		Query query = getPersistenceContext().createNamedQuery(
				"Unit.findBySectionAndUnitType");
		query.setParameter("section", section);
		query.setParameter("unitType", unitType);
		return query.getResultList();
	}

	public void setNullDeceased() {
		if (!isIdDefined()){
			if (!this.registered) {
				this.setDeceasedIdentification(null);
				this.setDeceased(null);
				setDeceasedResident(null);
				clearSearchResidentPanel();
			}
		}
	}


	@SuppressWarnings("unchecked")
	public void searchDeceasedByCriteria() {
		if (this.deceasedCriteria != null && !this.deceasedCriteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery(
					"Resident.findByCriteria");
			query.setParameter("criteria", this.deceasedCriteria);
			setResidents(query.getResultList());
		}
	}

	@SuppressWarnings("unchecked")
	public void searchDebtorByCriteria() {
		if (this.debtorCriteria != null && !this.debtorCriteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery(
					"Resident.findByCriteria");
			query.setParameter("criteria", this.debtorCriteria);
			setResidents(query.getResultList());
		}
	}

	public void searchPersonDeceased() {
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.deceasedIdentification);
		try {
			Resident resident = (Resident) query.getSingleResult();

			this.getInstance().setDeceased((Person) resident);
			this.getInstance().setDeathName(resident.getName());
			this.setDeceasedResident(resident);
			this.setDeceasedIdentification(resident.getIdentificationNumber());

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			this.getInstance().setDeceased(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void searchResidentDebtor() {
		// logger.info("RESIDENT CHOOSER CRITERIA... "+this.identificationNumber);
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.debtorIdentification);
		try {
			Resident resident = (Resident) query.getSingleResult();

//			this.getInstance().getCurrentDomain().setResident(resident);
			this.setDebtor(resident);
			if (this.instance.getCurrentContract() != null)
				this.instance.getCurrentContract().setSubscriber(resident);
			this.setDebtorIdentification(resident.getIdentificationNumber());

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
//			this.getInstance().getCurrentDomain().setResident(null);
			this.setDebtor(null);
			this.instance.getCurrentContract().setSubscriber(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void deceasedSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes()
				.get("resident");
//		this.getInstance().getCurrentDomain().setResident(resident);
		this.getInstance().setDeceased((Person) resident);
		this.setDeceasedResident(resident);
		this.setDeceasedIdentification(resident.getIdentificationNumber());
	}

	public void debtorSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes()
				.get("resident");
//		this.getInstance().getCurrentDomain().setResident(resident);
		this.setDebtor(resident);
		this.instance.getCurrentContract().setSubscriber(resident);
		this.setDebtorIdentification(resident.getIdentificationNumber());
	}

	public void clearSearchResidentPanel() {
		this.setDeceasedCriteria(null);
		this.setDebtorCriteria(null);
		setResidents(null);
	}

	public void newDeathRegister(){
		//System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRR newDeathRegister");
		this.newContract = true;
		this.instance = new Death();
		this.unit.setCurrentDeath(this.getInstance());
		this.setRegistered(false);
		this.setDeceasedCriteria(null);
		this.getInstance().setCementery(this.cementery);
		this.getInstance().setUnit(this.unit);
		this.setRenderZero(false);
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		ContractType contractType = systemParameterService.materialize(ContractType.class, "CONTRACT_TYPE_ID_DEATH");

		if (this.getInstance().getCurrentContract() == null){
			this.getInstance().setCurrentContract(new Contract());
			this.getInstance().getCurrentContract().setSubscriptionDate(new Date());
		}
		this.getInstance().getCurrentContract().setCreationDate(new Date());
		this.getInstance().getCurrentContract().setExpirationDate(new Date());
		this.getInstance().getCurrentContract().setContractType(contractType);
		this.getInstance().getCurrentContract().setDeath(this.instance);
		this.getInstance().getCurrentContract().setTotalMonths(new Long(0));
		if(this.getInstance().getCurrentContract() != null)this.setDebtor(this.getInstance().getCurrentContract().getSubscriber());
		if(this.getDebtor() != null)this.setDebtorIdentification(this.getDebtor().getIdentificationNumber());
		if(this.getInstance().getDeceased() != null)this.setDeceasedResident(this.getInstance().getDeceased());
		if(this.getDeceasedResident() != null)this.setDeceasedIdentification(this.getDeceasedResident().getIdentificationNumber());

//		if (unitDeathHome.isIdDefined()){
//			System.out.println("================Ingresa a unitDeathHome en wire");
//			if (unitDeathHome.getInstance().getCurrentDeath() == null){
//				this.setRegistered(false);
//				this.setDeceasedCriteria(null);
//			} else {
//				if (unitDeathHome.getInstance().getCurrentDeath().getDeceased() == null){
//					this.setRegistered(false);
//					this.setDeceasedCriteria(null);
//				} else {
//	renew				this.setRegistered(true);
//					this.setDeceasedIdentification(unitDeathHome.getInstance().getCurrentDeath().getDeceased().getIdentificationNumber());
////					this.setDeceased(unitDeathHome.getInstance().getCurrentDeath().getDeceased());
//					searchPersonDeceased();
//				}
//			}
//			unitDeathHome.getInstance().setCurrentDeath(instance);
//			this.instance.setUnit(unitDeathHome.getInstance());
//		}
//		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
//		ContractType contractType = systemParameterService.materialize(ContractType.class, "CONTRACT_TYPE_ID_DEATH");
//		if (isRenewContract()) {
//			Contract lastContract = null;
//			lastContract = this.instance.getCurrentContract();
//			if (lastContract != null){
//				this.instance.setCurrentContract(new Contract());
//				this.instance.getCurrentContract().setSubscriptionDate(lastContract.getExpirationDate());
//				this.instance.getCurrentContract().setSubscriber(lastContract.getSubscriber());
//				
//			} 
//			
//		}
//		if (this.instance.getCurrentContract() == null){
//			this.instance.setCurrentContract(new Contract());
//			this.instance.getCurrentContract().setSubscriptionDate(new Date());
//		}
//		this.instance.getCurrentContract().setCreationDate(new Date());
//		this.instance.getCurrentContract().setExpirationDate(new Date());
//		this.instance.getCurrentContract().setContractType(contractType);
//		this.instance.getCurrentContract().setDeath(this.instance);
//		this.instance.getCurrentContract().setTotalMonths(new Long(0));
//		if(this.getInstance().getCurrentContract() != null)this.setDebtor(this.getInstance().getCurrentContract().getSubscriber());
//		if(this.getDebtor() != null)this.setDebtorIdentification(this.getDebtor().getIdentificationNumber());
//		if(this.getInstance().getDeceased() != null)this.setDeceasedResident(this.getInstance().getDeceased());
//		if(this.getDeceasedResident() != null)this.setDeceasedIdentification(this.getDeceasedResident().getIdentificationNumber());
//
	}
	
	public void saveNewDeathRegister(){
		Cementery newCementery = this.cementery;
		Section newSection = this.section;
		
		this.unit.setUnitStatus(UnitStatus.RENTED);
		this.setRenderZero(true);
		this.setNewContract(false);
		super.persist();
		this.unit = null;
		findUnitsBySectionAndType();
	}
	
}