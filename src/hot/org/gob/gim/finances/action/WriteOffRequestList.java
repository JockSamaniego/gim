package org.gob.gim.finances.action;

import ec.gob.gim.finances.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("writeOffRequestList")
public class WriteOffRequestList extends EntityQuery<WriteOffRequest> {

	private static final String EJBQL = "select writeOffRequest from WriteOffRequest writeOffRequest";

	private static final String[] RESTRICTIONS = {
			"lower(writeOffRequest.detail) like lower(concat(#{writeOffRequestList.writeOffRequest.detail},'%'))",
			"lower(writeOffRequest.detailEmission) like lower(concat(#{writeOffRequestList.writeOffRequest.detailEmission},'%'))",
			"lower(writeOffRequest.internalProcessNumber) like lower(concat(#{writeOffRequestList.writeOffRequest.internalProcessNumber},'%'))",};

	private WriteOffRequest writeOffRequest = new WriteOffRequest();

	public WriteOffRequestList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public WriteOffRequest getWriteOffRequest() {
		return writeOffRequest;
	}
}
