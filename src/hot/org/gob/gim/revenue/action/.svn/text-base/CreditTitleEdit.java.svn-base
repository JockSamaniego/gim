package org.gob.gim.revenue.action;

/**
 * @author wilman
 */

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.Controller;
import org.jboss.seam.log.Log;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.EntryDefinition;
import ec.gob.gim.revenue.model.Item;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondType;

@Name("creditTitleEdit")
@Scope(ScopeType.CONVERSATION)
@Deprecated
public class CreditTitleEdit extends Controller {
	
	private static final long serialVersionUID = 1L;
	
	Boolean isDateEditable;
	
	MunicipalBond municipalBond;
	Resident resident;
	Entry entry;
	
	//List<Item> items;
	
	@Logger 
	Log logger;
	
	@In(create = true)
	MunicipalBondHome municipalBondHome;
	
	@In(create=true)
	EntityManager entityManager;

	public CreditTitleEdit() {
		
		System.out.printf("Ingresando a Constructor....\n", this);
		
		isDateEditable = Boolean.FALSE;
		
		resident = new Person();
		entry = new Entry();
		
	}
	
	public void wire() {
		logger.info("--Ingreso a wire", this);
		municipalBond = municipalBondHome.getInstance();
		logger.info("--Ingreso a wire:: id MunicipalBond: #0", municipalBond.getId());
		if (municipalBond != null && municipalBond.getId() != null){
			resident = municipalBond.getResident();
		}else{
			municipalBond.setMunicipalBondType(MunicipalBondType.CREDIT_ORDER);
		}
	}
	
	public void setResidentToMunicipalBond(Resident resident){
		logger.info("Agregando residente con idNumber: #0....", resident.getIdentificationNumber());
		this.resident = resident;
		resident.add(municipalBond);
		
	}
	
	@SuppressWarnings("unchecked")
	private List<EntryDefinition> findEntryDefinitions(Entry entry){
		Query query = entityManager.createNamedQuery("EntryDefinition.findByMaxStarDateAndEntry");
		query.setParameter("entry", entry);
		query.setParameter("startDate", municipalBond.getEmisionDate());
		return query.getResultList();
	}
	
	private void buildItems(List<EntryDefinition> entryDefinitionList){
		this.municipalBond.getItems().clear();
		for (EntryDefinition ed : entryDefinitionList){
			Item item = new Item ();
			item.setEntry(ed.getEntry());
			
			if (ed.getValue() == null){
				ed.setValue(new BigDecimal(0));
			}
			item.setValue(ed.getValue().divide(new BigDecimal(ed.getFactor())));
			municipalBond.add(item);
		}
	}
	
	public void populateEntry(Entry entry){
		logger.info("=====> Ingreso a fijar entry #0", entry.getCode());
		if (entry != null){
			List<EntryDefinition> entryDefinitionList = findEntryDefinitions(entry);
			buildItems(entryDefinitionList);
		}
		
	}

	/**
	 * @return the isDateEditable
	 */
	public Boolean getIsDateEditable() {
		return isDateEditable;
	}

	/**
	 * @param isDateEditable the isDateEditable to set
	 */
	public void setIsDateEditable(Boolean isDateEditable) {
		this.isDateEditable = isDateEditable;
	}

	/**
	 * @return the municipalBond
	 */
	public MunicipalBond getMunicipalBond() {
		return municipalBond;
	}

	/**
	 * @param municipalBond the municipalBond to set
	 */
	public void setMunicipalBond(MunicipalBond municipalBond) {
		this.municipalBond = municipalBond;
	}

	/**
	 * @return the resident
	 */
	public Resident getResident() {
		return resident;
	}

	/**
	 * @param resident the resident to set
	 */
	public void setResident(Resident resident) {
		this.resident = resident;
	}
	
	
	
	/**
	 * @return the entry
	 */
	public Entry getEntry() {
		return entry;
	}

	/**
	 * @param entry the entry to set
	 */
	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	@SuppressWarnings("unchecked")
	private FiscalPeriod findCurrentFiscalPeriod(){
		Query query = entityManager.createNamedQuery("FiscalPeriod.findCurrent");
		query.setParameter("currentDate", municipalBond.getEmisionDate());
		List<FiscalPeriod> result = (List<FiscalPeriod>)query.getResultList();
		return !result.isEmpty() ? result.get(0) : null; 
	}

	public String persist(){
		try{
			FiscalPeriod fiscalPeriod = findCurrentFiscalPeriod();
			municipalBond.setFiscalPeriod(fiscalPeriod);
			logger.info("Inserted satisfactoriamente #0, #1, #2....", municipalBond.getId(), municipalBond.getMunicipalBondType().name(), municipalBond.getResident().getIdentificationNumber());
			entityManager.persist(municipalBond);
			entityManager.flush();
		}catch(Exception e){
			logger.info("Error: Inserted satisfactoriamente #0, #1....", e.getMessage());
		}
		
		return "persisted";
	}
	
	

}
