package org.gob.gim.retentioncode.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.complementvoucher.model.RetentionCode; 

@Name("retentionCodeList")
public class RetentionCodeList extends EntityQuery<RetentionCode> {
	/**
	 * 
	 */ 

	private static final String EJBQL = "select retentionCode from RetentionCode retentionCode";

	private static final String[] RESTRICTIONS = {
			"lower(retentionCode.code) like lower(concat(#{retentionCodeList.code},'%'))",
			"lower(retentionCode.name)  like lower(concat(#{retentionCodeList.name},'%'))" };

	private String code;
	private String name;
	private Boolean isActive = Boolean.TRUE;

	public RetentionCodeList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrder("retentionCode.name");
		setMaxResults(25);
	}

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/*@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}*/

}
