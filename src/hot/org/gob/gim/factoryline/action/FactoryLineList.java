package org.gob.gim.factoryline.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.factoryline.FactoryLine;

/**
 * Línea de Fábrica:
 * 
 * @author Ronald Paladines C
 * @version 1.0
 * @created 02-Dic-2021
 */

@Name("factoryLineList")
public class FactoryLineList extends EntityQuery<FactoryLine> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select factoryLine from FactoryLine factoryLine";

	private static final String[] RESTRICTIONS = {"lower(factoryLine.owner) like lower(concat(#{factoryLineList.name},'%'))",
		"lower(factoryLine.identification) like lower(concat(#{factoryLineList.name}, '%'))",
		"lower(factoryLine.factoryLineNumber) like lower(concat('%', #{factoryLineList.name}, '%'))"
		};

	private FactoryLine factoryLine = new FactoryLine();
	
	private String name;
	private String duplicate;

	public FactoryLineList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("factoryLine.factoryLineNumber");
		setOrderDirection("desc");
		setMaxResults(25);
	}
	
	@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}

	public FactoryLine getFactoryLine() {
		return factoryLine;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the duplicate
	 */
	public String getDuplicate() {
		return duplicate;
	}

	/**
	 * @param duplicate the duplicate to set
	 */
	public void setDuplicate(String duplicate) {
		this.duplicate = duplicate;
	}

}