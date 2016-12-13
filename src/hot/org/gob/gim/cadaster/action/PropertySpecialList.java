package org.gob.gim.cadaster.action;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.cadaster.model.PropertyLocationType;
import ec.gob.gim.common.model.SystemParameter;

@Name("propertySpecialList")
@Scope(ScopeType.CONVERSATION)
public class PropertySpecialList extends EntityQuery<Property> {
	
	private static final long serialVersionUID = 1L;
	public Enum propertyLocationType = PropertyLocationType.SPECIAL;


	public Enum getPropertyLocationType() { 
		return propertyLocationType;
	}

	public void setPropertyLocationType(Enum propertyLocationType) {
		this.propertyLocationType = propertyLocationType;
	}

	private static final String EJBQL = "select property from Property property " +
		"left join fetch property.currentDomain currentDomain " +
		"left join fetch currentDomain.notarysProvince np " +
		"left join fetch np.territorialDivisionType " +
		"left join fetch currentDomain.notarysCity nc " +
		"left join fetch nc.territorialDivisionType " +
		"left join fetch currentDomain.purchaseType purchaseType " +		
		"left join fetch property.location " +
		"left join fetch property.block block " +
		"left join fetch block.sector sector " +
		"left join fetch property.propertyType pt " +
		"left join fetch pt.entry entry " +
		"left join fetch currentDomain.resident resident";

	private static final String[] RESTRICTIONS = {
			"(lower(resident.identificationNumber) like lower(concat(#{propertySpecialList.residentCriteria},'%')) or " +
			"lower(resident.name) like lower(concat(:el1,'%')))",
			"(lower(property.previousCadastralCode) like lower(concat(#{propertySpecialList.cadastralCodeCriteria},'%')) or " +
			"lower(property.cadastralCode) like lower(concat('%',:el2,'%')))",
			"pt.id = #{propertySpecialList.propertyTypeId}  ",
			"property.propertyLocationType = #{propertySpecialList.propertyLocationType} ",};

	private String residentCriteria;
	
	private String criteriaProperty;
	
	public String getCriteriaProperty() {
		return criteriaProperty;
	}

	public void setCriteriaProperty(String criteriaProperty) {
		this.criteriaProperty = criteriaProperty;
	}
	
	private List<Property> properties;
	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	
	/**
	 * Busca propiedades por clave catastral
	 */
	public void searchPropertyByCriteria() {
		String EJBQL = "Property.findByCadastralCode";
		// Query query = getEntityManager().createQuery(EJBQL);
		Query query = getEntityManager().createNamedQuery(EJBQL);		
		query.setParameter("criteria", this.criteriaProperty);
		properties = query.getResultList();
	}
	
	private String cadastralCodeCriteria;
	
	@Out(scope=ScopeType.SESSION, required=false)
	@In(scope=ScopeType.SESSION, required=false)
	private Long propertyTypeId;
	
	private boolean isSpecial = true;

	public PropertySpecialList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("property.cadastralCode");
		setOrderDirection("asc");
		setMaxResults(25);
		
		System.out.println(EJBQL);
		System.out.println(propertyTypeId);
	}

	/**
	 * @return the residentCriteria
	 */
	public String getResidentCriteria() {
		return residentCriteria;
	}

	/**
	 * @param residentCriteria the residentCriteria to set
	 */
	public void setResidentCriteria(String residentCriteria) {
		this.residentCriteria = residentCriteria;
	}

	/**
	 * @return the cadastralCodeCriteria
	 */
	public String getCadastralCodeCriteria() {
		return cadastralCodeCriteria;
	}

	/**
	 * @param cadastralCodeCriteria the cadastralCodeCriteria to set
	 */
	public void setCadastralCodeCriteria(String cadastralCodeCriteria) {
		this.cadastralCodeCriteria = cadastralCodeCriteria;
	}

	public void setPropertyTypeId(Long propertyTypeId) {
		this.propertyTypeId = propertyTypeId;
	}

	public Long getPropertyTypeId() {
		return propertyTypeId;
	}
	
	private boolean isFirstTime = true;
	
	/**
	 * Carga el tipo de propiedad: urbana o rústica
	 */
	public void chargePropertyType(){ //cacpe
		if(!isFirstTime) return;
		isFirstTime = false;
		String name="PROPERTY_TYPE_ID_URBAN";
//		if(isSpecial) name ="PROPERTY_TYPE_ID_URBAN";
		setPropertyTypeId(Long.parseLong(findPropertyType(name)));
	}
	
	public void setSpecial(boolean isSpecial) {		
		this.isSpecial = isSpecial;
	}
	
	/**
	 * Devuelve el parámetro del sistema del tipo de propiedad 
	 * @param name
	 * @return String
	 */
	public String findPropertyType(String name) {	
		List<?> list = getPersistenceContext().createNamedQuery("SystemParameter.findByName").setParameter("name", name).getResultList();
		if (list != null && list.size() > 0) {
			SystemParameter sp = (SystemParameter) list.get(0);
			return sp.getValue();
		}
		return null;
	}

	public boolean isSpecial() {
		return isSpecial;
	}
	
}
