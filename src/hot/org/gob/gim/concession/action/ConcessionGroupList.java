package org.gob.gim.concession.action;

import ec.gob.gim.consession.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("concessionGroupList")
public class ConcessionGroupList extends EntityQuery<ConcessionGroup> {

	private static final String EJBQL = "select concessionGroup from ConcessionGroup concessionGroup";

	private static final String[] RESTRICTIONS = {"lower(concessionGroup.name) like lower(concat(#{concessionGroupList.concessionGroup.name},'%'))",};

	private ConcessionGroup concessionGroup = new ConcessionGroup();

	public ConcessionGroupList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public ConcessionGroup getConcessionGroup() {
		return concessionGroup;
	}
}
