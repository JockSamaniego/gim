package org.gob.gim.ant.ucot.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.gob.gim.common.CatalogConstants;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.revenue.service.ItemCatalogService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.ant.ucot.model.Agent;
import ec.gob.gim.ant.ucot.model.Bulletin;
import ec.gob.gim.ant.ucot.model.Infractions;
import ec.gob.gim.common.model.ItemCatalog;

@Name("bulletinHome")
public class BulletinHome extends EntityHome<Bulletin> {
	
	private List<ItemCatalog> typesBulletin;
	private ItemCatalogService itemCatalogService;

	@In(create = true)
	AgentHome agentHome;
	/*@In(create = true)
	ItemCatalogHome itemCatalogHome;*/

	public void setBulletinId(Long id) {
		setId(id);
	}

	public Long getBulletinId() {
		return (Long) getId();	
	}

	@Override
	protected Bulletin createInstance() {
		Date date = new Date();
		Bulletin bulletin = new Bulletin();
		bulletin.setCreationDate(date);
		bulletin.setCreationTime(date);
		return bulletin;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Agent agent = agentHome.getDefinedInstance();
		if (agent != null) {
			getInstance().setAgent(agent);
		}
		/*ItemCatalog type = itemCatalogHome.getDefinedInstance();
		if (type != null) {
			getInstance().setType(type);
		}*/
		
		initializeService();
		typesBulletin = new ArrayList<ItemCatalog>();
		typesBulletin = itemCatalogService.findItemsForCatalogCode(
						CatalogConstants.CATALOG_TYPES_BULLETIN);
		
		bulletinHaveInfractions();
		
	}

	public boolean isWired() {
		return true;
	}

	public Bulletin getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	

	public List<ItemCatalog> getTypesBulletin() {
		return typesBulletin;
	}

	public void setTypesBulletin(List<ItemCatalog> typesBulletin) {
		this.typesBulletin = typesBulletin;
	}
	
	public void initializeService() {
		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(
					ItemCatalogService.LOCAL_NAME);
		}
	}
	
	public void agentSelectedListener(Agent ag) {
		//UIComponent component = event.getComponent();
		//Agent agent = (Agent) component.getAttributes().get("agent");
		this.instance.setAgent(ag);		 
	}
	
	private String message;
	
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String saveBulletin(){
		message = null;		
		Boolean errorSerial = errorInSerialNumbers();	
		if(errorSerial){
			message = "El numero inicial debe ser menor que el numero final";
			return "failed";
		}
		
		if(startNumberinRank()){
			message = "El numero inicial se encuentra dentro del rango de otro boletin";
			return "failed";
		}
		
		if(endNumberinRank()){
			message = "El numero final se encuentra dentro del rango de otro boletin";
			return "failed";
		}
		if(numbersOutRank()){
			message = "Los numeros inicial y final contienen el rango de otro boletin";
			return "failed";
		}
		
		persist();
		return "persisted";
	}
	
	public Boolean errorInSerialNumbers(){
		if(getInstance().getEndNumber().compareTo(getInstance().getStartNumber())==1){
			return false;
		}	
		return true;
	}
	
	public Boolean startNumberinRank(){
		List<Bulletin> bulletins = new ArrayList();
		Query query = getEntityManager().createNamedQuery(
				"bulletin.findBySerial");
		query.setParameter("serial", getInstance().getStartNumber());
		bulletins = query.getResultList();
		
		if(bulletins.size()>0){
			return true;
		}
		return false;
	}
	
	public Boolean endNumberinRank(){
		List<Bulletin> bulletins = new ArrayList();
		Query query = getEntityManager().createNamedQuery(
				"bulletin.findBySerial");
		query.setParameter("serial", getInstance().getEndNumber());
		bulletins = query.getResultList();
		
		if(bulletins.size()>0){
			return true;
		}
		return false;
	}
	
	public Boolean numbersOutRank(){
		List<Bulletin> bulletins = new ArrayList();
		Query query = getEntityManager().createNamedQuery(
				"bulletin.findByRank");
		query.setParameter("startSerial", getInstance().getStartNumber());
		query.setParameter("endSerial", getInstance().getEndNumber());
		bulletins = query.getResultList();
		
		if(bulletins.size()>0){
			return true;
		}
		return false;
	}
	
	private Boolean haveInfractions;
	
	public Boolean getHaveInfractions() {
		return haveInfractions;
	}

	public void setHaveInfractions(Boolean haveInfractions) {
		this.haveInfractions = haveInfractions;
	}
	

	public void bulletinHaveInfractions(){
		haveInfractions = Boolean.FALSE;
		List<Infractions> infractions = new ArrayList();
		Query query = getEntityManager().createNamedQuery(
				"infractions.findByBulletinId");
		query.setParameter("bulletinId", getInstance().getId());
		infractions = query.getResultList();
		
		if(infractions.size()>0){
			haveInfractions = Boolean.TRUE;
		}
	}
}
