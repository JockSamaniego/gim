package org.gob.gim.waterservice.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.sql.ForUpdateFragment;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.waterservice.model.MaintenanceEntry;
import ec.gob.gim.waterservice.model.WaterSupply;

@Name("maintenanceEntryHome")
public class MaintenanceEntryHome extends EntityHome<MaintenanceEntry> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@In
	private FacesMessages facesMessages;
	
	private WaterSupply waterSupply;
	
	
	
	private List<MaintenanceEntry> maintenanceEntries;
	
	public List<MaintenanceEntry> getMaintenanceEntries() {
		return maintenanceEntries;
	}

	public void setMaintenanceEntries(List<MaintenanceEntry> maintenanceEntries) {
		this.maintenanceEntries = maintenanceEntries;
	}

	public WaterSupply getWaterSupply() {
		return waterSupply;
	}

	public void setWaterSupply(WaterSupply waterSupply) {
		this.waterSupply = waterSupply;
	}

	public void setMaintenanceEntryId(Long id) {
		setId(id);
	}

	public Long getMaintenanceEntryId() {
		return (Long) getId();
	}
	
	private void loadWaterSupply(Long id){
		Query query = getEntityManager().createNamedQuery("WaterSupply.findById");
		query.setParameter("id", id);
		waterSupply = (WaterSupply) query.getSingleResult();
	}
	
	private void loadMaintenanceEntries(Long waterSupplyId){
		Query query = getEntityManager().createNamedQuery("MaintenanceEntry.findByWaterSupply");
		query.setParameter("waterSupplyId", waterSupplyId);
		maintenanceEntries = query.getResultList();
	}
	
	public void loadValues(Long waterSupplyId){
		loadWaterSupply(waterSupplyId);
		loadMaintenanceEntries(waterSupplyId);
		setValues();
	}
	
	public void invalidMaintenanceEntry(MaintenanceEntry maintenanceEntry){
		maintenanceEntry.setIsValid(false);
		if(maintenanceEntry.getId() != null)idsForUpdate.add(maintenanceEntry.getId());
	}
	
	private void setValues(){
		Calendar ca = Calendar.getInstance();
		this.getInstance().setDate(ca.getTime());
		this.getInstance().setTime(ca.getTime());
	}
	
	public void addNewMaintenance(){
		maintenanceEntries.add(0,this.getInstance());
		this.setInstance(new MaintenanceEntry());
		setValues();
	}
	
	List<Long> idsForUpdate = new ArrayList<Long>();
	
	public String saveAll(){
		for(MaintenanceEntry me: maintenanceEntries){
			if(me.getId() == null){
				me.setWaterSupply(waterSupply);
				this.setInstance(me);
				this.persist();
			}else if(idsForUpdate.contains(me.getId())){
				this.setInstance(me);
				this.update();
			}
		}
		this.setInstance(new MaintenanceEntry());
		idsForUpdate = new ArrayList<Long>();
		return "persisted";
	}
	

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

	public MaintenanceEntry getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
}
