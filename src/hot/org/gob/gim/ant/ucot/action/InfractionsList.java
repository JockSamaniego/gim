package org.gob.gim.ant.ucot.action;

import ec.gob.gim.ant.ucot.model.*;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Name("infractionsList")
public class InfractionsList extends EntityQuery<Infractions> {
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select infractions from Infractions infractions";

	private static final String[] RESTRICTIONS = {
		"infractions.fixedRadar = #{infractionsList.fixedRadar}",
		"lower(infractions.identification) like lower(concat('%',#{infractionsList.infractions.identification},'%'))",
		"infractions.serial >= #{infractionsList.startSerial}",
		"infractions.serial <= #{infractionsList.endSerial}",
		"infractions.serial = #{infractionsList.infractions.serial}",
		"lower(infractions.licensePlate) like lower(concat('%',#{infractionsList.infractions.licensePlate},'%'))",
		"infractions.citationDate >= #{infractionsList.dateFrom}",
		"infractions.citationDate <= #{infractionsList.dateUntil}",
		"DATE(infractions.creationDate) >= #{infractionsList.dateFrom2}",
		"DATE(infractions.creationDate) <= #{infractionsList.dateUntil2}",
		"lower(infractions.bulletin.agent.resident.identificationNumber) like lower(concat('%',#{infractionsList.agent},'%'))",
		"lower(infractions.article) like lower(concat('%',#{infractionsList.infractions.article},'%'))",
		"lower(infractions.numeral) like lower(concat('%',#{infractionsList.infractions.numeral},'%'))",
		"lower(infractions.partNumber) like lower(concat('%',#{infractionsList.infractions.partNumber},'%'))",
		"infractions.inconsistent = #{infractionsList.infractions.inconsistent}",
		"infractions.nullified = #{infractionsList.infractions.nullified}",
		"infractions.photoFine = #{infractionsList.infractions.photoFine}",};

	private Infractions infractions = new Infractions();
	private BigInteger startSerial;
	private BigInteger endSerial;
	private Date dateFrom;
	private Date dateUntil;
	private Date dateFrom2;
	private Date dateUntil2;
	private String agent;
	private Boolean fixedRadar=Boolean.FALSE;

	public InfractionsList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("infractions.serial");
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

	public BigInteger getStartSerial() {
		return startSerial;
	}

	public void setStartSerial(BigInteger startSerial) {
		this.startSerial = startSerial;
	}

	public BigInteger getEndSerial() {
		return endSerial;
	}

	public void setEndSerial(BigInteger endSerial) {
		this.endSerial = endSerial;
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

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
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
