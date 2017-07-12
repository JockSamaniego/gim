package org.gob.gim.ant.ucot.action;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.action.ResidentHome;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.ant.ucot.model.Agent;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.complementvoucher.model.Provider;

@Name("agentHome")
@Scope(ScopeType.CONVERSATION)
public class AgentHome extends EntityHome<Agent> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private String identificationNumber;
	private String criteria;
	private List<Resident> residents;
	
	@In(create = true)
	ResidentHome residentHome;

	public void setAgentId(Long id) {
		setId(id);
	}

	public Long getAgentId() {
		return (Long) getId();
	}

	@Override
	protected Agent createInstance() {
		Agent agent = new Agent();
		return agent;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		//Resident resident = residentHome.getDefinedInstance();
		if (getInstance().getId() == null) {
			//getInstance().setResident(resident);
			this.instance.setActive(Boolean.TRUE);
		}
	}

 
	
	
	public boolean isWired() {
		return true;
	}

	public Agent getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	
	/*** Para buscador*****/
	
	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria() {
		System.out.println("SEARCH RESIDENT BY CRITERIA " + this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			setResidents(query.getResultList());
		}
	}
	
	public void searchResident() {
		System.out.println("RESIDENT CHOOSER CRITERIA... " + this.identificationNumber);
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();						
			this.instance.setResident(resident);

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			this.instance.setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}	 
	}
	
	
	public String persist(){
		//validaciones
		
		//verificar que el mismo codigo de agente no esté asignado
		String code = this.instance.getAgentCode();
		boolean repeated  = agentCodeExist(code);
		if(repeated){
			return "";
		}
		
		//verificar que el resident no este asignado
		Resident resident = this.instance.getResident();
		boolean assigned  = residentAssigned(resident);
		if(assigned){
			return "";
		}
		//guardado en BD
		
		
		return "";
	}
	
	/**
	 * 
	 * @param resident
	 * @return
	 */
	public boolean residentAssigned(Resident resident){
		Query q = getEntityManager().createNamedQuery("Agent.FindByResident");
		q.setParameter("residentid", resident.getId());
		return (q.getResultList().size() > 0)? true: false;		
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public boolean agentCodeExist(String code){
		if(code.trim().isEmpty()) return true;
		Query q = getEntityManager().createNamedQuery("Agent.FindByCode");
		q.setParameter("code", code);
		return (q.getResultList().size() > 0)? true: false;		
	}

	
	public void clearSearchResidentPanel() {
		this.setCriteria(null);
		setResidents(null);
	}
	
	public void residentSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.instance.setResident(resident);
		this.setIdentificationNumber(resident.getIdentificationNumber());		
		 
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
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
	
}
