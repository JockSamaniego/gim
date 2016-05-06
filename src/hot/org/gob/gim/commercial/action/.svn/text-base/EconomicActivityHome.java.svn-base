package org.gob.gim.commercial.action;

import java.util.ArrayList;
import java.util.List;

import org.gob.gim.revenue.action.EntryHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.commercial.model.EconomicActivity;
import ec.gob.gim.commercial.model.EconomicActivityType;
import ec.gob.gim.revenue.model.Entry;

@Name("economicActivityHome")
public class EconomicActivityHome extends EntityHome<EconomicActivity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@In(create = true)
	EntryHome entryHome;
	
	@In(create = true, value = "economicActivityTypes")
	List<EconomicActivityType> economicActivityTypes;
	
	private EconomicActivity economicActivityChild = null; 

	public void setEconomicActivityId(Long id) {
		setId(id);
	}

	public Long getEconomicActivityId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}
	
	private EconomicActivity createEconomicActivityChild(){
		EconomicActivity economicActivity = null;
		int economicActivityTypeIndex = economicActivityTypes.indexOf(getInstance().getEconomicActivityType());
		
		if (economicActivityTypeIndex < economicActivityTypes.size()-1){
		
			economicActivity = new EconomicActivity();	
			if(getInstance().getCode() != null) economicActivity.setCode(getInstance().getCode()+"##");
			economicActivity.setEconomicActivityType(economicActivityTypes.get(economicActivityTypeIndex+1));
		
		}
		return economicActivity;
		
	}
	
	public void updateCode(){
		if(getInstance().getCode() != null) economicActivityChild.setCode(getInstance().getCode()+"???");
	}
	
	private boolean isFirstTime = true;	
	
	public void wire() {
		if(!isFirstTime) return;
		isFirstTime = false;
		getInstance();
		if (!isManaged()){
			getInstance().setEconomicActivityType(economicActivityTypes.get(0));
		}
		Entry entry = entryHome.getDefinedInstance();
		if (entryHome != null) {
			getInstance().setEntry(entry);
		}
	}

	public boolean isWired() {
		return true;
	}

	public EconomicActivity getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public List<EconomicActivity> getChildren() {
		return getInstance() == null ? null : new ArrayList<EconomicActivity>(
				getInstance().getChildren());
	}

	/**
	 * @return the economicActivityChild
	 */
	public EconomicActivity getEconomicActivityChild() {
		if (economicActivityChild == null){
			economicActivityChild = createEconomicActivityChild();
		}
		return economicActivityChild;
	}

	/**
	 * @param economicActivityChild the economicActivityChild to set
	 */
	public void setEconomicActivityChild(EconomicActivity economicActivityChild) {
		this.economicActivityChild = economicActivityChild;
	}
	
	
	public void saveEconomicActivityChild(){
		if (this.economicActivityChild.getId() == null){
			getInstance().add(economicActivityChild);
		}
		setEconomicActivityChild(createEconomicActivityChild());
	}
	
	public void remove(EconomicActivity economicActivity){
		getInstance().remove(economicActivity);
	}

}