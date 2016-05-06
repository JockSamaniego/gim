package org.gob.gim.firestation.action;

import java.util.Arrays;
import ec.gob.gim.firestation.model.FireMan;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import ec.gob.gim.commercial.model.Business;

@Name("fireManList")
public class FireManList extends EntityQuery<FireMan> {
	
	private static final long serialVersionUID = -3242997811348537850L;
	private static final String EJBQL = "select fireMan from FireMan fireMan";

	private static final String[] RESTRICTIONS = {"lower(fireMan.fireman.name) like lower(concat(#{fireManList.fireMan.name},'%'))",
												"lower(fireMan.fireman.identificationNumber) like lower(concat(#{fireManList.fireMan.name},'%'))"};

	
	private FireMan fireman = new FireMan();
	private Business business = new Business();

	private String criteria;

	public FireManList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}
	
	public FireMan getFireMan() {
		return fireman;
	}
}

