package org.gob.gim.common.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.ResidentType;

@Name("residentList")
public class ResidentList extends EntityQuery<Resident> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String criteria;

	private static final String EJBQL = "select resident from Resident resident left join fetch resident.currentAddress";
	
	private static final String EJBQL_N = "select resident from Person resident left join fetch resident.currentAddress";
	
	private static final String EJBQL_J = "select resident from LegalEntity resident left join fetch resident.currentAddress";

	private static final String[] RESTRICTIONS = {
			"lower(resident.identificationNumber) like lower(concat(#{residentList.criteria},'%'))",			
			"lower(resident.name) like lower(concat(#{residentList.criteria},'%'))"};
	
	private ResidentType residentType;
	
	public ResidentList() {
		setEjbql(EJBQL);		
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("resident.name");
		setOrderDirection("asc");
		setMaxResults(25);
	}
	
	private boolean isFirstTime=true;
	
	@Override
	@Transactional
	public List<Resident> getResultList() {
		// TODO Auto-generated method stub		
		if(isFirstTime && residentType != null && residentType.equals(ResidentType.N)) setEjbql(EJBQL_N);		
		if(isFirstTime && residentType != null && residentType.equals(ResidentType.J)) setEjbql(EJBQL_J);
		isFirstTime = false;
		return super.getResultList();
	}
	
	@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}
	
	public ResidentType getResidentType() {
		return residentType;
	}

	public void setResidentType(ResidentType residentType) {		
		this.residentType = residentType;
	}

}
