package org.gob.gim.finances.action;

import ec.gob.gim.finances.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("writeOffDetailList")
public class WriteOffDetailList extends EntityQuery<WriteOffDetail> {

	private static final String EJBQL = "select writeOffDetail from WriteOffDetail writeOffDetail";

	private static final String[] RESTRICTIONS = {};

	private WriteOffDetail writeOffDetail = new WriteOffDetail();

	public WriteOffDetailList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public WriteOffDetail getWriteOffDetail() {
		return writeOffDetail;
	}
}
