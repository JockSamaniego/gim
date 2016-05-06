package org.gob.gim.appraisal.action;

import ec.gob.gim.appraisal.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("appraisalItemTypeList")
public class AppraisalItemTypeList extends EntityQuery<AppraisalItemType> {

	private static final String EJBQL = "select appraisalItemType from AppraisalItemType appraisalItemType";

	private static final String[] RESTRICTIONS = {
			"lower(appraisalItemType.name) like lower(concat(#{appraisalItemTypeList.appraisalItemType.name},'%'))",};

	
	
	private AppraisalItemType appraisalItemType = new AppraisalItemType();

	public AppraisalItemTypeList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	

	public void setAppraisalItemType(AppraisalItemType appraisalItemType) {
		this.appraisalItemType = appraisalItemType;
	}

	public AppraisalItemType getAppraisalItemType() {
		return appraisalItemType;
	}
}
