package org.gob.gim.cementery.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.cementery.model.Section;
import ec.gob.gim.cementery.model.Unit;

@Name("sectionCementeryHome")
public class SectionCementeryHome extends EntityHome<Section> implements Serializable {

	private static final long serialVersionUID = 1L;

	Log logger;
	
	@In(create=true)
	UnitHome unitHome;
	
	private Unit unit;
	
	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public Section getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void setSectionCementeryId(Long id) {
		setId(id);
	}

	public Long getSectionCementeryId() {
		return (Long) getId();
	}

	public List<Unit> getUnits() {
		return getInstance() == null ? null : new ArrayList<Unit>(
				getInstance().getUnits());
	}
	
	public void createUnit(){
		this.unit = new Unit();
	}
	
	public void editUnit(Unit unit){
		this.unit = unit;
	}
	
	public void addUnit(){
		this.getInstance().add(this.unit);
		logger.info(this.getInstance().getUnits().size());
	}
	
	public void removeUnit(Unit unit){
		this.getInstance().remove(unit);
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	 

}
