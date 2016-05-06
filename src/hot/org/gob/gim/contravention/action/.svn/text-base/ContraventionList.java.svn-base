package org.gob.gim.contravention.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.contravention.model.Contravention;

@Name("contraventionList")
public class ContraventionList extends EntityQuery<Contravention> {

	private static final String EJBQL = "select contravention from Contravention contravention";

	private static final String[] RESTRICTIONS = {
			"lower(contravention.description) like lower(concat(#{contraventionList.criteria},'%'))",
			"lower(contravention.detail) like lower(concat(#{contraventionList.criteria},'%'))",
			"lower(contravention.inactiveReason) like lower(concat(#{contraventionList.criteria},'%'))",
			"lower(contravention.sanctionDetail) like lower(concat(#{contraventionList.criteria},'%'))",
			"contravention.isActive = #{contraventionList.isActive}", };

	private Contravention contravention = new Contravention();

	private String criteria;

	private Boolean isActive = Boolean.TRUE;

	public ContraventionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Contravention getContravention() {
		return contravention;
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}