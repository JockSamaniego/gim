package org.gob.gim.cementery.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.cementery.model.Cementery;
import ec.gob.gim.cementery.model.Section;

@Name("sectionCementeryList")
public class SectionCementeryList extends EntityQuery<Section> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select section from Section section";

	private static final String[] RESTRICTIONS = {
//			"lower(resident.identificationNumber) like lower(concat(#{cmementeryList.criteriaIdentificationNumber},'%'))",
//		"lower(resident.name) like lower(concat(#{cementeryList.criteriaManagerName},'%'))" 
		};

	String criteriaName;
	Cementery cementery;

	public SectionCementeryList() {
		
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(3);
	}

	public String getCriteriaName() {
		return criteriaName;
	}

	public void setCriteriaName(String criteriaName) {
		this.criteriaName = criteriaName;
	}

	public Cementery getCementery() {
		return cementery;
	}

	public void setCementery(Cementery cementery) {
		this.cementery = cementery;
	}


}
