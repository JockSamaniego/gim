package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("appraisalList")
public class AppraisalList extends EntityQuery<Appraisal> {

	private static final String EJBQL = "select appraisal from Appraisal appraisal";

	private static final String[] RESTRICTIONS = {};

	private Appraisal appraisal = new Appraisal();

	public AppraisalList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Appraisal getAppraisal() {
		return appraisal;
	}
}
