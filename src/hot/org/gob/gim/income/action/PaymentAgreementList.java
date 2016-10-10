package org.gob.gim.income.action;

import ec.gob.gim.income.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("paymentAgreementList")
public class PaymentAgreementList extends EntityQuery<PaymentAgreement> {

	private static final long serialVersionUID = 2200422997707335539L;

	private static final String EJBQL = "select pa from PaymentAgreement pa join fetch pa.resident r";

	private static final String[] RESTRICTIONS = { "(lower(r.identificationNumber) like lower(concat(#{paymentAgreementList.criteria},'%')) OR lower(r.name) like lower(concat(:el1,'%')))",
	// "pa.isActive = #{paymentAgreementList.isActive}"
	};

	private String criteria;

	public PaymentAgreementList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);

		// rfarmijosm 2016-05-23
		setOrderColumn("pa.id");
		setOrderDirection("desc");
	}

	public Boolean getIsActive() {
		return Boolean.TRUE;
	}

	@Override
	public String getRestrictionLogicOperator() {
		return "and";
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	/*
	 * @Override public String getOrder() { return "r.name"; }
	 */
}
