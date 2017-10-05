package org.gob.gim.ant.ucot.action;

import ec.gob.gim.ant.ucot.model.*;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;

@Name("infractionsList")
public class InfractionsList extends EntityQuery<Infractions> {
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select infractions from Infractions infractions";

	private static final String[] RESTRICTIONS = {
		"lower(infractions.identification) like lower(concat('%',#{infractionsList.infractions.identification},'%'))",
		"infractions.serial >= #{infractionsList.startSerial}",
		"infractions.serial <= #{infractionsList.endSerial}",
		"infractions.serial = #{infractionsList.infractions.serial}",
		"lower(infractions.licensePlate) like lower(concat('%',#{infractionsList.infractions.licensePlate},'%'))",
		"infractions.citationDate >= #{infractionsList.dateFrom}",
		"infractions.citationDate <= #{infractionsList.dateUntil}",
		"lower(infractions.bulletin.agent.resident.identificationNumber) like lower(concat('%',#{infractionsList.agent},'%'))",
		"lower(infractions.article) like lower(concat('%',#{infractionsList.infractions.article},'%'))",
		"lower(infractions.numeral) like lower(concat('%',#{infractionsList.infractions.numeral},'%'))",
		"lower(infractions.partNumber) like lower(concat('%',#{infractionsList.infractions.partNumber},'%'))",};

	private Infractions infractions = new Infractions();
	private BigInteger startSerial;
	private BigInteger endSerial;
	private Date dateFrom;
	private Date dateUntil;
	private String agent;

	public InfractionsList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("infractions.serial");
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

	
}
