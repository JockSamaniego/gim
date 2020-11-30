
package org.gob.gim.cementery.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.cadaster.action.DomainHome;
import org.gob.gim.common.NativeQueryResultsMapper;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.action.ContractHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.cadaster.model.dto.DomainTransferDTO;
import ec.gob.gim.cementery.model.Cementery;
import ec.gob.gim.cementery.model.Section;
import ec.gob.gim.cementery.model.Unit;
import ec.gob.gim.cementery.model.UnitStatus;
import ec.gob.gim.cementery.model.UnitType;
import ec.gob.gim.cementery.model.dto.MunicipalBondByCementeryDTO;
import ec.gob.gim.cementery.model.dto.UnitContractExpiredDTO;
import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Contract;

import org.jboss.seam.faces.Renderer;

@Name("cementeryHome")
public class CementeryHome extends EntityHome<Cementery> implements
		Serializable {

	private static final long serialVersionUID = 1L;

	@In(create = true)
	SectionCementeryHome sectionCementeryHome;

	Log logger;
	private Section section;
	private Unit unit;	

	private List<Resident> residents;
	
	public Cementery cementery;
	private String criteria;
	private String identificationNumber;

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();

		if(this.getInstance().getManager() != null){
			this.identificationNumber = this.getInstance().getManager().getIdentificationNumber();
		} else {
			identificationNumber = null;
		}
	}

	public boolean isWired() {
		return true;
	}

	public Cementery getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void setCementeryId(Long id) {
		setId(id);
	}

	public Long getCementeryId() {
		return (Long) getId();
	}

	public List<Section> getSections() {
		return getInstance() == null ? null : new ArrayList<Section>(
				getInstance().getSections());
	}

	public void createSection() {
		this.section = new Section();
	}

	public void editSection(Section section) {
		this.section = section;
	}

	public void addSection() {
		this.getInstance().add(this.section);		
		logger.info(this.getInstance().getSections().size());		
	}
	
	public Cementery getCementery() {
		return cementery;
	}

	public void setCementery(Cementery cementery) {
		this.cementery = cementery;
	}

	public void removeSection(Section section) {
		this.getInstance().remove(section);
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public void createSectionUnit() {
		this.unit = new Unit();
		this.unit.setUnitStatus(UnitStatus.FREE);
		this.section.add(unit);
		btnAddUnit();
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
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

	@SuppressWarnings("unchecked")
	public void searchManagerCementeryByCriteria() {
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			setResidents(query.getResultList());
		}
	}

	public void searchManagerCementery() throws NullPointerException {
		try {
			Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
			query.setParameter("identificationNumber", this.identificationNumber);
			Resident resident = (Resident) query.getSingleResult();

			this.getInstance().setManager(resident);
			// resident.add(this.getInstance());
			// this.getInstance().getCurrentDomain().setResident(resident);

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			this.getInstance().setManager(null);
//			this.identificationNumber = null;
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void residentSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.getInstance().setManager(resident);
	}

	public void clearSearchResidentPanel() {
		this.setCriteria(null);
		setResidents(null);
	}
	
	private boolean buttons;
	private boolean btnDisabled=Boolean.TRUE;
	private boolean btnAddUnit=Boolean.TRUE;
	private long idUnit;

	public boolean isButtons() {
		return buttons;
	}

	public void setButtons(boolean buttons) {
		this.buttons = buttons;
	}

	public boolean isBtnDisabled() {
		return btnDisabled;
	}

	public void setBtnDisabled(boolean btnDisabled) {
		this.btnDisabled = btnDisabled;
	}

	public long getIdUnit() {
		return idUnit;
	}

	public void setIdUnit(long idUnit) {
		this.idUnit = idUnit;
	}

	public boolean isBtnAddUnit() {
		return btnAddUnit;
	}

	public void setBtnAddUnit(boolean btnAddUnit) {
		this.btnAddUnit = btnAddUnit;
	}

	public void directCreation(){		
		update();
		buttons=Boolean.TRUE;
		btnAddUnit=Boolean.TRUE;
		idUnit=this.unit.getId();
	}
	
	public void btnSave(){
		if((this.unit.getCode()!=null && this.unit.getCode()!="") && this.unit.getUnitType()!=null){
			btnDisabled=Boolean.FALSE;
			btnAddUnit=Boolean.TRUE;
		}else{
			btnDisabled=Boolean.TRUE;
			btnAddUnit=Boolean.FALSE;
		}
	}
	
	public void btnAddUnit(){
		buttons=Boolean.FALSE;
		btnAddUnit=Boolean.FALSE;
		btnDisabled=Boolean.TRUE;	
	}
	
	public void print(){
		//System.out.println("=======estado de la unidad"+this.unit.getId());
	}
	
	
	//Para reporte de Contratos culminados (expirados)
	// Jock Samaniego
	
	private Cementery cementerySelected;
	private UnitType unitTypeSelected;
	private List<UnitContractExpiredDTO> unitsDeathsExpired;
	private Date expiredUntil;
	private UnitContractExpiredDTO unitSelected;
	private String subscriberCriteria;

	public Cementery getCementerySelected() {
		return cementerySelected;
	}

	public void setCementerySelected(Cementery cementerySelected) {
		this.cementerySelected = cementerySelected;
	}

	public List<UnitContractExpiredDTO> getUnitsDeathsExpired() {
		return unitsDeathsExpired;
	}

	public void setUnitsDeathsExpired(List<UnitContractExpiredDTO> unitsDeathsExpired) {
		this.unitsDeathsExpired = unitsDeathsExpired;
	}

	public Date getExpiredUntil() {
		return expiredUntil;
	}

	public void setExpiredUntil(Date expiredUntil) {
		this.expiredUntil = expiredUntil;
	}
	
	public UnitType getUnitTypeSelected() {
		return unitTypeSelected;
	}

	public void setUnitTypeSelected(UnitType unitTypeSelected) {
		this.unitTypeSelected = unitTypeSelected;
	}

	public UnitContractExpiredDTO getUnitSelected() {
		return unitSelected;
	}

	public void setUnitSelected(UnitContractExpiredDTO unitSelected) {
		this.unitSelected = unitSelected;
	}
	
	public String getSubscriberCriteria() {
		return subscriberCriteria;
	}

	public void setSubscriberCriteria(String subscriberCriteria) {
		this.subscriberCriteria = subscriberCriteria;
	}

	public void findUnitsExpiredContracts(){
		unitsDeathsExpired = new ArrayList();
		if(subscriberCriteria != null && subscriberCriteria != ""){
			String query = "SELECT unit.id as id_unit, "
					 + "cont.subscriptiondate as subscription_date, " 
					 + "cast(cont.expirationdate AS date) as expiration_date, "
					 + "cement.name as cementery, "
					 + "type.name as type, "
					 + "death.deathname as deceased, "
					 + "subs.name as subscriber_name, "
					 + "subs.identificationnumber as subscriber_identification, "
					 + "subs.email as subscriber_email, "
					 + "cont.id as contractId, "
					 + "cont.lastNotificationDate as last_notification "
					 + "FROM gimprod.unit unit "
					 + "LEFT JOIN gimprod.death death ON death.id = unit.death_id "
					 + "LEFT JOIN gimprod.contract cont On cont.id = death.currentcontract_id "
					 + "LEFT JOIN gimprod.section sect on sect.id = unit.section_id "
					 + "LEFT JOIN gimprod.cementery cement ON cement.id = sect.cementery_id "
					 + "LEFT JOIN gimprod.unittype type On type.id = unit.unittype_id "
					 + "LEFT JOIN gimprod.resident subs On subs.id = cont.subscriber_id "
					 + "WHERE "
					 + "subs.identificationNumber like upper(concat(:criteria,'%')) or "
					 + "lower(subs.name) like lower(concat(:criteria, '%')) "
					 + "ORDER BY cont.expirationdate ASC";
			Query q = getEntityManager().createNativeQuery(query);
			q.setParameter("criteria", this.subscriberCriteria);
			unitsDeathsExpired = NativeQueryResultsMapper.map(q.getResultList(), UnitContractExpiredDTO.class);	
		}else{
			String query = "SELECT unit.id as id_unit, "
						 + "cont.subscriptiondate as subscription_date, " 
						 + "cast(cont.expirationdate AS date) as expiration_date, "
						 + "cement.name as cementery, "
						 + "type.name as type, "
						 + "death.deathname as deceased, "
						 + "subs.name as subscriber_name, "
						 + "subs.identificationnumber as subscriber_identification, "
						 + "subs.email as subscriber_email, "
						 + "cont.id as contractId, "
						 + "cont.lastNotificationDate as last_notification "
						 + "FROM gimprod.unit unit "
						 + "LEFT JOIN gimprod.death death ON death.id = unit.death_id "
						 + "LEFT JOIN gimprod.contract cont On cont.id = death.currentcontract_id "
						 + "LEFT JOIN gimprod.section sect on sect.id = unit.section_id "
						 + "LEFT JOIN gimprod.cementery cement ON cement.id = sect.cementery_id "
						 + "LEFT JOIN gimprod.unittype type On type.id = unit.unittype_id "
						 + "LEFT JOIN gimprod.resident subs On subs.id = cont.subscriber_id "
						 + "WHERE cont.expirationdate <= :dateUntil "
						 + "and unit.unitstatus = 'RENTED' "
						 + "and cement.id = :cementery "
						 + "and type.id = :unitType "
						 + "ORDER BY subs.name ASC";
			Query q = getEntityManager().createNativeQuery(query);
			q.setParameter("dateUntil", this.expiredUntil);
			q.setParameter("cementery", this.cementerySelected.getId());
			q.setParameter("unitType", this.unitTypeSelected.getId());
			unitsDeathsExpired = NativeQueryResultsMapper.map(q.getResultList(), UnitContractExpiredDTO.class);
		}
	}
	
	@In(create = true)
	private Renderer renderer;
	
	@In(create = true)
	ContractHome contractHome;
	
	public void sendMailNotification(UnitContractExpiredDTO unit){
		EntityManager em = getEntityManager();
		Contract currentContract= getEntityManager().find(Contract.class, unit.getContractId());
		currentContract.setLastNotificationDate(new Date());
		contractHome.setInstance(currentContract);
		contractHome.persist();
		this.unitSelected = unit;	
		this.sendMail(renderer);
		findUnitsExpiredContracts();
	}
	
	@Asynchronous
	private void sendMail(Renderer r){
		try {
			// logger.info("Renderer "+r);
			r.render("/cementery/ContractExpiredMail.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Para reporte de recaudación por cementerios
	// Jock Samaniego M.
	
	private Date collectionFromDate;
	private Date collectionUntilDate;
	private List<MunicipalBondByCementeryDTO> cementeryBonds;
	private BigDecimal totalCollected;

	public Date getCollectionFromDate() {
		return collectionFromDate;
	}

	public void setCollectionFromDate(Date collectionFromDate) {
		this.collectionFromDate = collectionFromDate;
	}

	public Date getCollectionUntilDate() {
		return collectionUntilDate;
	}

	public void setCollectionUntilDate(Date collectionUntilDate) {
		this.collectionUntilDate = collectionUntilDate;
	}
	
	public List<MunicipalBondByCementeryDTO> getCementeryBonds() {
		return cementeryBonds;
	}

	public void setCementeryBonds(List<MunicipalBondByCementeryDTO> cementeryBonds) {
		this.cementeryBonds = cementeryBonds;
	}

	public BigDecimal getTotalCollected() {
		return totalCollected;
	}

	public void setTotalCollected(BigDecimal totalCollected) {
		this.totalCollected = totalCollected;
	}

	
	public void findMunicipalBondsByCementeries(){
		cementeryBonds = new ArrayList();
		String query = "SELECT mb.number, res.identificationnumber, res.name as nombre_resident, "
				+ "mb.description, ent.name as nombre_entry, i.total, mbs.name, mb.liquidationdate "
				+ "from gimprod.item i "
				+ "LEFT JOIN gimprod.municipalbond mb On mb.id = i.municipalbond_id "
				+ "LEFT JOIN gimprod.municipalbondstatus mbs On mbs.id = mb.municipalbondstatus_id "
				+ "LEFT JOIN gimprod.resident res ON res.id = mb.resident_id "
				+ "LEFT JOIN gimprod.entry ent On ent.id = i.entry_id "
				+ "WHERE i.entry_id = 24 "
				+ "AND mbs.id in (6,11)"
				+ "and mb.liquidationdate BETWEEN :fromDate and :untilDate "
				+ "ORDER BY mb.liquidationdate, res.name ASC ";
		Query q = getEntityManager().createNativeQuery(query);
		q.setParameter("fromDate", this.collectionFromDate);
		q.setParameter("untilDate", this.collectionUntilDate);
		cementeryBonds = NativeQueryResultsMapper.map(q.getResultList(), MunicipalBondByCementeryDTO.class);
		findTotalCollectedByCementeries();
	}
	
	public void findTotalCollectedByCementeries(){
		totalCollected = new BigDecimal(0);
		String query = "SELECT sum(i.total) "
				+ "from gimprod.item i "
				+ "LEFT JOIN gimprod.municipalbond mb On mb.id = i.municipalbond_id "
				+ "LEFT JOIN gimprod.municipalbondstatus mbs On mbs.id = mb.municipalbondstatus_id "
				+ "LEFT JOIN gimprod.resident res ON res.id = mb.resident_id "
				+ "LEFT JOIN gimprod.entry ent On ent.id = i.entry_id "
				+ "WHERE i.entry_id = 24 "
				+ "AND mbs.id in (6,11)"
				+ "and mb.liquidationdate BETWEEN :fromDate and :untilDate ";
		Query q = getEntityManager().createNativeQuery(query);
		q.setParameter("fromDate", this.collectionFromDate);
		q.setParameter("untilDate", this.collectionUntilDate);
		totalCollected = (BigDecimal) q.getSingleResult();
	}	
}
