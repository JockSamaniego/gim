package org.gob.gim.ant.ucot.action;

import ec.gob.gim.ant.ucot.model.*;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;

@Name("fixedRadarList")
public class FixedRadarList extends EntityQuery<Infractions> {
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select infractions from Infractions infractions";

	private static final String[] RESTRICTIONS = {
		"infractions.fixedRadar = #{fixedRadarList.fixedRadar}",
		"infractions.axisNumber = #{fixedRadarList.infractions.axisNumber}",
		"lower(infractions.identification) like lower(concat('%',#{fixedRadarList.infractions.identification},'%'))",
		"lower(infractions.licensePlate) like lower(concat('%',#{fixedRadarList.infractions.licensePlate},'%'))",
		"infractions.citationDate >= #{fixedRadarList.dateFrom}",
		"infractions.citationDate <= #{fixedRadarList.dateUntil}",
		"infractions.creationDate >= #{fixedRadarList.dateFrom2}",
		"infractions.creationDate <= #{fixedRadarList.dateUntil2}",
		"lower(infractions.article) like lower(concat('%',#{fixedRadarList.infractions.article},'%'))",
		"lower(infractions.numeral) like lower(concat('%',#{fixedRadarList.infractions.numeral},'%'))",};

	private Infractions infractions = new Infractions();
	private Date dateFrom;
	private Date dateUntil;
	private Date dateFrom2;
	private Date dateUntil2;
	private Boolean fixedRadar=Boolean.TRUE;

	public FixedRadarList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("infractions.creationDate");
		setOrderDirection("asc");
		setMaxResults(500);
	}
	
	@Override
	public String getRestrictionLogicOperator() {
		return "and";
	}

	public Infractions getInfractions() {
		return infractions;
	}

	public void setInfractions(Infractions infractions) {
		this.infractions = infractions;
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

	public Boolean getFixedRadar() {
		return fixedRadar;
	}

	public void setFixedRadar(Boolean fixedRadar) {
		this.fixedRadar = fixedRadar;
	}

	public Date getDateFrom2() {
		return dateFrom2;
	}

	public void setDateFrom2(Date dateFrom2) {
		this.dateFrom2 = dateFrom2;
	}

	public Date getDateUntil2() {
		return dateUntil2;
	}

	public void setDateUntil2(Date dateUntil2) {
		this.dateUntil2 = dateUntil2;
	}
	
}

import ec.gob.gim.ant.ucot.model.*;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;

@Name("fixedRadarList")
public class FixedRadarList extends EntityQuery<Infractions> {
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select infractions from Infractions infractions";

	private static final String[] RESTRICTIONS = {
		"infractions.fixedRadar = #{fixedRadarList.fixedRadar}",
		"lower(infractions.identification) like lower(concat('%',#{fixedRadarList.infractions.identification},'%'))",
		"infractions.citationNumber = #{fixedRadarList.infractions.citationNumber}",
		"lower(infractions.licensePlate) like lower(concat('%',#{fixedRadarList.infractions.licensePlate},'%'))",
		"infractions.citationDate >= #{fixedRadarList.dateFrom}",
		"infractions.citationDate <= #{fixedRadarList.dateUntil}",
		"infractions.creationDate >= #{fixedRadarList.dateFrom2}",
		"infractions.creationDate <= #{fixedRadarList.dateUntil2}",
		"lower(infractions.article) like lower(concat('%',#{fixedRadarList.infractions.article},'%'))",
		"lower(infractions.numeral) like lower(concat('%',#{fixedRadarList.infractions.numeral},'%'))",};

	private Infractions infractions = new Infractions();
	private Date dateFrom;
	private Date dateUntil;
	private Date dateFrom2;
	private Date dateUntil2;
	private Boolean fixedRadar=Boolean.TRUE;

	public FixedRadarList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("infractions.id");
		setOrderDirection("asc");
		setMaxResults(25);
	}
	
	@Override
	public String getRestrictionLogicOperator() {
		return "and";
	}

	public Infractions getInfractions() {
		return infractions;
	}

	public void setInfractions(Infractions infractions) {
		this.infractions = infractions;
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

	public Boolean getFixedRadar() {
		return fixedRadar;
	}

	public void setFixedRadar(Boolean fixedRadar) {
		this.fixedRadar = fixedRadar;
	}

	public Date getDateFrom2() {
		return dateFrom2;
	}

	public void setDateFrom2(Date dateFrom2) {
		this.dateFrom2 = dateFrom2;
	}

	public Date getDateUntil2() {
		return dateUntil2;
	}

	public void setDateUntil2(Date dateUntil2) {
		this.dateUntil2 = dateUntil2;
	}
	
}
