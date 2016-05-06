package org.gob.gim.concession.action;

import ec.gob.gim.consession.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("concessionItemList")
public class ConcessionItemList extends EntityQuery<ConcessionItem> {

	private static final String EJBQL = "select concessionItem from ConcessionItem concessionItem";

	private static final String[] RESTRICTIONS = {
			"lower(concessionItem.concept) like lower(concat(#{concessionItemList.concessionItem.concept},'%'))",
			"lower(concessionItem.explanation) like lower(concat(#{concessionItemList.concessionItem.explanation},'%'))",};

	private ConcessionItem concessionItem = new ConcessionItem();

	public ConcessionItemList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public ConcessionItem getConcessionItem() {
		return concessionItem;
	}
}
