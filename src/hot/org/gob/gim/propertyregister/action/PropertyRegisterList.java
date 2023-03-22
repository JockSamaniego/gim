package org.gob.gim.propertyregister.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.propertyregister.model.PropertyRegister;

@Name("propertyRegisterList")
@Scope(ScopeType.CONVERSATION)
public class PropertyRegisterList extends EntityQuery<PropertyRegister> {

	private static final long serialVersionUID = 6036641475414279729L;

	private static final String EJBQL = "select propertyRegister from PropertyRegister propertyRegister ";

	private static final String[] RESTRICTIONS = {"lower(propertyRegister.identificationNumber) like lower(concat('%',#{propertyRegisterList.resident},'%')) ",
			"lower(propertyRegister.registeredChange) = lower(#{propertyRegisterList.registered})"};

	private PropertyRegister propertyRegister= new PropertyRegister();
	private String registered = "NO";
	private List<String> registeredList = new ArrayList<String>(Arrays.asList("SI", "NO", "PREVIAMENTE"));
	
	public PropertyRegister getPropertyRegister() {
		return propertyRegister;
	}

	private String resident;

	public PropertyRegisterList() {
		setEjbql(EJBQL);
		List<String> list = Arrays.asList(RESTRICTIONS);
		if (registered != null){
			setRestrictionLogicOperator("and");
		} else {
			setRestrictionLogicOperator("or");
		}
		setRestrictionExpressionStrings(list);
		setOrder("id");
		setMaxResults(25);
	}

	public String getResident() {		
		return resident;
	}

	public void setResident(String resident) {
		this.resident = resident;
	}

	/**
	 * @param propertyRegister the propertyRegister to set
	 */
	public void setPropertyRegister(PropertyRegister propertyRegister) {
		this.propertyRegister = propertyRegister;
	}

	/**
	 * @return the registered
	 */
	public String getRegistered() {
		return registered;
	}

	/**
	 * @param registered the registered to set
	 */
	public void setRegistered(String registered) {
		this.registered = registered;
	}

	/**
	 * @return the registeredList
	 */
	public List<String> getRegisteredList() {
		return registeredList;
	}

	/**
	 * @param registeredList the registeredList to set
	 */
	public void setRegisteredList(List<String> registeredList) {
		this.registeredList = registeredList;
	}

}
