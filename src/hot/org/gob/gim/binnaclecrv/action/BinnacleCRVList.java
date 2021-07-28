package org.gob.gim.binnaclecrv.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.binnaclecrv.model.BinnacleCRV;

/**
 * Bitácora Digital:
 * 
 * @author Ronald Paladines C
 * @version 1.0
 * @created 10-May-2021
 */

@Name("binnacleCRVList")
public class BinnacleCRVList extends EntityQuery<BinnacleCRV> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select binnacleCRV from BinnacleCRV binnacleCRV";

	private static final String[] RESTRICTIONS = {"lower(binnacleCRV.licensePlate) like lower(concat(#{binnacleCRVList.name},'%'))",
			"lower(binnacleCRV.serialNumber) = lower(concat(#{binnacleCRVList.name}))",
			"lower(binnacleCRV.chassis) = lower(concat(#{binnacleCRVList.name}))",
			"lower(binnacleCRV.motor) = lower(concat(#{binnacleCRVList.name}))",
			"lower(binnacleCRV.admissionResponsableIdentification) like lower(concat('%', #{binnacleCRVList.name},'%'))",
			"lower(binnacleCRV.admissionResponsableName) like lower(concat('%', #{binnacleCRVList.name},'%'))"};

	private BinnacleCRV binnacleCRV = new BinnacleCRV();
	
	private String name;

	public BinnacleCRVList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("binnacleCRV.serialNumber");
		setOrderDirection("desc");
		setMaxResults(25);
	}
	
	@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}

	public BinnacleCRV getBinnacleCRV() {
		return binnacleCRV;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}