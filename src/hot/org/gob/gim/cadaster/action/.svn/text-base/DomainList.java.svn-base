package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("domainList")
public class DomainList extends EntityQuery<Domain> {

	private static final String EJBQL = "select domain from Domain domain";

	private static final String[] RESTRICTIONS = {
			"lower(domain.observations) like lower(concat(#{domainList.domain.observations},'%'))",
			"lower(domain.realStateNumber) like lower(concat(#{domainList.domain.realStateNumber},'%'))",};

	private Domain domain = new Domain();

	public DomainList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Domain getDomain() {
		return domain;
	}
}
