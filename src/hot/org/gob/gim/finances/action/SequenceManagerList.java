package org.gob.gim.finances.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.finances.model.SequenceManager;

@Name("sequenceManagerList")
public class SequenceManagerList extends EntityQuery<SequenceManager> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String criteria;

	private static final String EJBQL = "select sm from SequenceManager sm left join fetch sm.sequenceManagerType smt left join fetch sm.takenBy taker ";
	
	private static final String ORDER = "sm.date desc, sm.code desc";  

	private static final String[] RESTRICTIONS = {
			// "sm.code = #{sequenceManagerList.code} ",
			"lower(taker.name) like lower(concat('%',#{sequenceManagerList.criteria},'%'))",
			"lower(taker.identificationNumber) like lower(concat('%',#{sequenceManagerList.criteria},'%'))", };

	
	// private Integer code;

	public SequenceManagerList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setRestrictionLogicOperator("or");
		//setOrderColumn("sm.date");
		setOrder(ORDER);
		//setOrderDirection("desc");
		setMaxResults(25);
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

}
