package org.gob.gim.cementery.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.cementery.model.Cementery;
import ec.gob.gim.cementery.model.Section;
import ec.gob.gim.cementery.model.Unit;
import ec.gob.gim.cementery.model.UnitStatus;
import ec.gob.gim.common.model.Resident;

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
	
}
