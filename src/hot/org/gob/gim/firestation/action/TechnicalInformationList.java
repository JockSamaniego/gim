package org.gob.gim.firestation.action;

import java.util.Arrays;
import ec.gob.gim.firestation.model.TechnicalInformation;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import ec.gob.gim.commercial.model.Business;

@Name("technicalInformationList")
public class TechnicalInformationList extends EntityQuery<TechnicalInformation> {
	
	private static final long serialVersionUID = -3242997811348537850L;
	private static final String EJBQL = "select technicalInformation from TechnicalInformation technicalInformation";

	private static final String[] RESTRICTIONS = {"lower(technicalInformation.id) like lower(concat(#{technicalInformationList.id},'%'))"};

	
	private TechnicalInformation technicalInformation = new TechnicalInformation();

	private String criteria;

	public TechnicalInformationList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}
	
	

}

