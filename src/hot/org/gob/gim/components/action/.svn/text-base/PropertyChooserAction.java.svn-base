package org.gob.gim.components.action;

import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityController;
import org.jboss.seam.log.Log;

import ec.gob.gim.cadaster.model.Property;

@Name("propertyChooserAction")
@Scope(ScopeType.CONVERSATION)
public class PropertyChooserAction extends EntityController {

	private static final long serialVersionUID = 1L;

	@Logger
	private Log logger;

	private String criteria;
	private List<Property> properties;
	private Property property;

	@SuppressWarnings("unchecked")
	public void search() {
		String EJBQL = "select property from Property property "
				+ "left join fetch property.currentDomain currentDomain "
				+ "left join fetch property.location "
				+ "left join fetch property.block "
				+ "left join fetch property.propertyType "
				+ "left join currentDomain.resident resident "
				+ "where property.cadastralCode like :criteria";
		Query query = getEntityManager().createQuery(EJBQL);
		query.setParameter("criteria", (this.criteria != null) ? this.criteria
				+ "%" : "");
		properties = query.getResultList();
		logger.info(".................................. en el serch");
	}

	public void selectProperty(Property property) {
		this.property = property;
		logger.info(".................................. en el select");
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

}
