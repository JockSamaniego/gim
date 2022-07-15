package org.gob.gim.coercive.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.coercive.model.infractions.InfringementAgreement;


/**
 * Convenio de Pago de infracciones:
 * 
 * @author Ronald Paladines C
 * @version 1.0
 * @created 04-Jul-2022
 */

@Name("infringementAgreementList")
public class InfringementAgreementList extends EntityQuery<InfringementAgreement> {

	private static final long serialVersionUID = -6880509713741244083L;

	private static final String EJBQL = "select infringementAgreement from InfringementAgreement infringementAgreement";

	private static final String[] RESTRICTIONS = {"lower(infringementAgreement.infractorIdentification) like lower(concat('%', #{infringementAgreementList.name},'%'))",
		"lower(infringementAgreement.infractorName) like lower(concat('%', #{infringementAgreementList.name},'%'))"};

	private InfringementAgreement infringementAgreement = new InfringementAgreement();
	
	private String name;

	public InfringementAgreementList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("infringementAgreement.id");
		setOrderDirection("desc");
		setMaxResults(25);
	}
	
	@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}

	public InfringementAgreement getInfringementAgreement() {
		return infringementAgreement;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}