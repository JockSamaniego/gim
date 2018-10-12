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
		"lower(infractions.identification) like lower(concat('%',#{fixedRadarList.infractions.identification},'%'))",
		"infractions.radarCode = #{fixedRadarList.infractions.radarCode}",
		"lower(infractions.licensePlate) like lower(concat('%',#{fixedRadarList.infractions.licensePlate},'%'))",
		"infractions.citationDate >= #{fixedRadarList.dateFrom}",
		"infractions.citationDate <= #{fixedRadarList.dateUntil}",
		"lower(infractions.article) like lower(concat('%',#{fixedRadarList.infractions.article},'%'))",
		"lower(infractions.numeral) like lower(concat('%',#{fixedRadarList.infractions.numeral},'%'))",
		"lower(infractions.partNumber) like lower(concat('%',#{fixedRadarList.infractions.partNumber},'%'))",};

	private Infractions infractions = new Infractions();
	private Date dateFrom;
	private Date dateUntil;
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
	
}
