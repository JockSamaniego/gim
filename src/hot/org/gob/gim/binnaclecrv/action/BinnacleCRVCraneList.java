package org.gob.gim.binnaclecrv.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.binnaclecrv.model.BinnacleCRVCrane;

/**
 * Bitácora Digital:
 * 
 * @author Ronald Paladines C
 * @version 1.0
 * @created 22-Jul-2021
 */

@Name("binnacleCRVCraneList")
public class BinnacleCRVCraneList extends EntityQuery<BinnacleCRVCrane> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select binnacleCRVCrane from BinnacleCRVCrane binnacleCRVCrane";

	private static final String[] RESTRICTIONS = {"lower(binnacleCRVCrane.name) like lower(concat('%', #{binnacleCRVCraneList.name},'%'))"};

	private BinnacleCRVCrane binnacleCRVCrane = new BinnacleCRVCrane();
	
	private String name;

	public BinnacleCRVCraneList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("binnacleCRVCrane.name");
		setOrderDirection("asc");
		setMaxResults(25);
	}
	
	@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}

	public BinnacleCRVCrane getBinnacleCRVCrane() {
		return binnacleCRVCrane;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}