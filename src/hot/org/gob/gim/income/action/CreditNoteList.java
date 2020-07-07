package org.gob.gim.income.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.income.model.CreditNote;

@Name("creditNoteList")
public class CreditNoteList extends EntityQuery<CreditNote> {
	
	private static final long serialVersionUID = 1L;

	private String criteriaName;
	private String criteriaIdentification;
	private Date dateFrom;
	private Date dateUntil;


	private static final String EJBQL = "SELECT o FROM CreditNote o";

	private static final String[] RESTRICTIONS = {
			"lower(o.resident.identificationNumber) like lower(concat(#{creditNoteList.criteriaIdentification},'%'))",
			"lower(o.resident.name) like lower(concat(#{creditNoteList.criteriaName},'%'))",
			"o.creationDate >= #{creditNoteList.dateFrom}",
			"o.creationDate <= #{creditNoteList.dateUntil}",};
	
	public CreditNoteList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("o.resident.name");
		setOrderDirection("asc");
		setMaxResults(25);
	}
	
	@Override
	public String getRestrictionLogicOperator() {
		return "and";
	}
	
	public String getCriteriaName() {
		return criteriaName;
	}

	public void setCriteriaName(String criteriaName) {
		this.criteriaName = criteriaName;
	}

	public String getCriteriaIdentification() {
		return criteriaIdentification;
	}

	public void setCriteriaIdentification(String criteriaIdentification) {
		this.criteriaIdentification = criteriaIdentification;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateUntil() {
		return dateUntil;
	}

	public void setDateUntil(Date dateUntil) {
		this.dateUntil = dateUntil;
	}

}
