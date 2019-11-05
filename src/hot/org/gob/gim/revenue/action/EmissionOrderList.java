package org.gob.gim.revenue.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.revenue.model.EmissionOrder;

@Name("emissionOrderList")
public class EmissionOrderList extends EntityQuery<EmissionOrder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private static final String EJBQL = "select e from EmissionOrder e "
	+"left join fetch e.municipalBonds m "
	+"left join fetch m.receipt "
	+"left join fetch m.resident "
	+"left join fetch e.emisor "
	+"left join fetch m.entry entry ";
	
	
	private static final String[] RESTRICTIONS= {
		"(lower(m.resident.name) like lower(concat(#{emissionOrderList.resident},'%'))) ", 
		"(lower(m.resident.identificationNumber) like lower(concat(#{emissionOrderList.identificationNumber},'%'))) ",
		"(lower(m.entry.code) like lower(concat('%',#{emissionOrderList.entry},'%')))",
		"(lower(m.entry.department) like lower(concat('%',#{emissionOrderList.department},'%')))",
		"e.isDispatched=#{emissionOrderList.isDispatched}",
		"entry.id not in (#{emissionOrderList.photoPenalty})"};
	
	/*private static final String[] RESTRICTIONS= {
			"e.isDispatched=#{emissionOrderList.isDispatched}",
			};*/

	
	private String resident;
	private String identificationNumber;
	
	private Boolean isDispatched = Boolean.FALSE;
	
	private List<Long> photoPenalty = new ArrayList<Long>(Arrays.asList(new Long("643"), new Long("644")));
	
	public Boolean getIsDispatched() {
		return isDispatched;
	}
	
	/*@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}*/

	public void setIsDispatched(Boolean isDispatched) {
		this.isDispatched = isDispatched;
	}

	private String department;

	private String entry;
	
	public EmissionOrderList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrder("e.id");
		setOrderDirection("desc");
		setMaxResults(25);
	}


	public void setResident(String resident) {		
		this.resident = resident;
	}


	public String getResident() {
		return resident;
	}


	public void setEntry(String entry) {		
		this.entry = entry;
	}


	public String getEntry() {
		return entry;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}
	
	public void changeSelectedEmissionOrder(EmissionOrder eo, boolean selected){
		eo.setIsSelected(!selected);
	}
	
	public void emitSelectedEmissionOrder(){
		int i =0;
		for(EmissionOrder eo :this.getResultList()){
			if(eo.getIsSelected()){
				//System.out.println("------------------------- ok "+eo.getId());
			}
			i++;
		}
		//System.out.println("------------------------- la cantidad "+i);
	}

	public List<Long> getPhotoPenalty() {
		return photoPenalty;
	}

	public void setPhotoPenalty(List<Long> photoPenalty) {
		this.photoPenalty = photoPenalty;
	}
	
}
