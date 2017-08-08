package org.gob.gim.income.action;

import ec.gob.gim.income.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("replacementPaymentAgreementList")
public class ReplacementPaymentAgreementList extends EntityQuery<ReplacementPaymentAgreement> {

	private static final String EJBQL = "select replacementPaymentAgreement from ReplacementPaymentAgreement replacementPaymentAgreement";

	private static final String[] RESTRICTIONS = {
			"lower(replacementPaymentAgreement.detail) like lower(concat(#{replacementPaymentAgreementList.replacementPaymentAgreement.detail},'%'))",
			"lower(replacementPaymentAgreement.reversedExplanation) like lower(concat(#{replacementPaymentAgreementList.replacementPaymentAgreement.reversedExplanation},'%'))", };

	private ReplacementPaymentAgreement replacementPaymentAgreement = new ReplacementPaymentAgreement();

	public ReplacementPaymentAgreementList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public ReplacementPaymentAgreement getReplacementPaymentAgreement() {
		return replacementPaymentAgreement;
	}
}
