package org.gob.gim.common.action;

import ec.gob.gim.common.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("checkingRecordList")
public class CheckingRecordList extends EntityQuery<CheckingRecord> {

	private static final String EJBQL = "select checkingRecord from CheckingRecord checkingRecord";

	private static final String[] RESTRICTIONS = {};

	private CheckingRecord checkingRecord = new CheckingRecord();

	public CheckingRecordList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public CheckingRecord getCheckingRecord() {
		return checkingRecord;
	}
}
