package org.gob.gim.concession.action;

import ec.gob.gim.consession.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("concessionClasificationList")
public class ConcessionClasificationList
		extends
			EntityQuery<ConcessionClasification> {

	private static final String EJBQL = "select concessionClasification from ConcessionClasification concessionClasification";

	private static final String[] RESTRICTIONS = {"lower(concessionClasification.name) like lower(concat(#{concessionClasificationList.concessionClasification.name},'%'))",};

	private ConcessionClasification concessionClasification = new ConcessionClasification();

	public ConcessionClasificationList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public ConcessionClasification getConcessionClasification() {
		return concessionClasification;
	}
}
