package org.gob.gim.cadaster.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.cadaster.model.CapitalGain;
 

@Name("calculatorList")
public class CalculatorList extends EntityQuery<CapitalGain> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select cg from CapitalGain cg ";

	private static final String[] RESTRICTIONS = {
			"lower(cg.property.previousCadastralCode) like lower(concat(#{calculatorList.cadastralCode},'%'))",
			"lower(cg.property.cadastralCode) like lower(concat(#{calculatorList.cadastralCode},'%'))"
			};
	
	//"lower(limit.street.name) like lower(concat(#{blockList.streetName},'%'))"

	private String cadastralCode;
	
	public CalculatorList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("cg.creationTime");
		setOrderDirection("asc");
		setMaxResults(25);
	}

	public String getCadastralCode() {
		return cadastralCode;
	}

	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}
	
}