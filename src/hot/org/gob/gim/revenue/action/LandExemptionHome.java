package org.gob.gim.revenue.action;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.revenue.model.LandExemption;

@Name("landExemptionHome")
public class LandExemptionHome extends EntityHome<LandExemption> {
	

	@Logger
	Log logger;
	
	@In
	FacesMessages facesMessages;
	
	private String criteriaProperty;
	
	private List<Property> properties;
		
		
	public void setLandExemptionId(Long id) {
		setId(id);
	}

	public Long getLandExemptionId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public boolean isFirsTime = true;
	
	public void wire() {		
		if (!isFirsTime) return;
		getInstance();				
		isFirsTime = false;
	}

	public boolean isWired() {
		return true;
	}
	
	public void searchPropertyByCriteria() {
		String EJBQL = "Property.findByCadastralCode";
		// Query query = getEntityManager().createQuery(EJBQL);
		Query query = getEntityManager().createNamedQuery(EJBQL);
		query.setParameter("criteria", this.criteriaProperty);
		properties = query.getResultList();
	}
	
	public void clearSearchPropertyPanel() {
		this.setCriteriaProperty(null);
		properties = null;
	}
	
	public void propertySelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Property property = (Property) component.getAttributes().get("property");
		this.getInstance().setProperty(property);
	}
	
	public LandExemption existLandExemption() {
		if(this.getInstance().getProperty() == null || this.getInstance().getFiscalPeriod() == null) return null;
		Query query = getEntityManager().createNamedQuery("LandExemption.findByFiscalPeriodAndResident");
		query.setParameter("propertyId", this.getInstance().getProperty().getId());
		query.setParameter("fiscalPeriodId", this.getInstance().getFiscalPeriod().getId());
		List<LandExemption> list = query.getResultList();
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	public LandExemption getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public String save(){
		LandExemption exist = existLandExemption();
		if(exist != null && 
				( this.isManaged() && !exist.getId().equals(this.getInstance().getId())) || (exist != null && !this.isManaged()) ){
			String message = Interpolator.instance().interpolate("#{messages['landExemption.existLandExemption']}", new Object[0]);
			facesMessages.addToControl("",org.jboss.seam.international.StatusMessage.Severity.ERROR,message);
			return "failed";
		}
						
		if(this.isManaged()) return super.update();
		
		return super.persist();
	}
	
	public String removeLandExemption(LandExemption le){
		this.setInstance(le);
		super.remove();		
		return "/revenue/LandExemptionList.xhtml";
	}

	public String getCriteriaProperty() {
		return criteriaProperty;
	}

	public void setCriteriaProperty(String criteriaProperty) {
		this.criteriaProperty = criteriaProperty;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
		
}
