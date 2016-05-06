package org.gob.gim.income.action;

import ec.gob.gim.income.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("taxpayerRecordList")
public class TaxpayerRecordList extends EntityQuery<TaxpayerRecord> {

	private static final String EJBQL = "select t from TaxpayerRecord t";

	private static final String[] RESTRICTIONS = {"lower(t.name) like lower(concat(#{taxpayerRecordList.taxpayerRecord.name},'%'))",
												  "lower(t.number) like lower(concat(#{taxpayerRecordList.taxpayerRecord.number},'%'))"};
	
	private TaxpayerRecord taxpayerRecord = new TaxpayerRecord();
	
	public TaxpayerRecord getTaxpayerRecord() {
		return taxpayerRecord;
	}

	public void setTaxpayerRecord(TaxpayerRecord taxpayerRecord) {
		this.taxpayerRecord = taxpayerRecord;
	}

	public TaxpayerRecordList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

}
