package org.gob.gim.appraisal.action;

import ec.gob.gim.appraisal.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("appraisalItemBaseList")
public class AppraisalItemBaseList extends EntityQuery<AppraisalItemBase> {

	private static final String EJBQL = "select appraisalItemBase from AppraisalItemBase appraisalItemBase";

	private static final String[] RESTRICTIONS = {
			"lower(appraisalItemBase.id) like lower(concat(#{appraisalItemBaseList.appraisalItemBase.id},'%'))",
			"lower(appraisalItemBase.name) like lower(concat(#{appraisalItemBaseList.appraisalItemBase.name},'%'))",
			"lower(appraisalItemBase.appraisalItemType.name) like lower(concat(#{appraisalItemBaseList.appraisalItemType.name},'%'))",};

	private AppraisalItemBase appraisalItemBase = new AppraisalItemBase();
	
	private AppraisalItemType appraisalItemType = new AppraisalItemType();

	public AppraisalItemBaseList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("appraisalItemBase.name");
		setOrderDirection("asc");
		setMaxResults(25);
	}

	public AppraisalItemBase getAppraisalItemBase() {
		return appraisalItemBase;
	}

	public void setAppraisalItemType(AppraisalItemType appraisalItemType) {
		this.appraisalItemType = appraisalItemType;
	}

	public AppraisalItemType getAppraisalItemType() {
		return appraisalItemType;
	}
}
