package org.gob.gim.factoryline.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.factoryline.FactoryLineRoad;

/**
 * Línea de Fábrica: Vías e Hidrografía
 * 
 * @author Ronald Paladines C
 * @version 1.0
 * @created 18-Ene-2022
 */

@Name("factoryLineRoadList")
public class FactoryLineRoadList extends EntityQuery<FactoryLineRoad> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select factoryLineRoad from FactoryLineRoad factoryLineRoad";

	private static final String[] RESTRICTIONS = {"lower(factoryLineRoad.denomination) like lower(concat('%', #{factoryLineRoadList.name},'%'))",
		"lower(factoryLineRoad.location) like lower(concat('%', #{factoryLineRoadList.name}, '%'))"
		};

	private FactoryLineRoad factoryLineRoad = new FactoryLineRoad();
	
	private String name;

	public FactoryLineRoadList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrder("factoryLineRoad.location, factoryLineRoad.denomination");
		setOrderDirection("desc");
		setMaxResults(25);
	}
	
	@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}

	public FactoryLineRoad getFactoryLineRoad() {
		return factoryLineRoad;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}