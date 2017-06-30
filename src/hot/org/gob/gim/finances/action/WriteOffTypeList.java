package org.gob.gim.finances.action;

import ec.gob.gim.finances.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("writeOffTypeList")
public class WriteOffTypeList extends EntityQuery<WriteOffType> {

	private static final String EJBQL = "select writeOffType from WriteOffType writeOffType";

	private static final String[] RESTRICTIONS = {
			"lower(writeOffType.detail) like lower(concat(#{writeOffTypeList.writeOffType.detail},'%'))",
			"lower(writeOffType.name) like lower(concat(#{writeOffTypeList.writeOffType.name},'%'))",};

	private WriteOffType writeOffType = new WriteOffType();

	public WriteOffTypeList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public WriteOffType getWriteOffType() {
		return writeOffType;
	}
}
