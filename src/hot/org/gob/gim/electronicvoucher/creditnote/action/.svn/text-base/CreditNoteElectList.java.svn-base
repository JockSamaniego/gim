package org.gob.gim.electronicvoucher.creditnote.action;

import java.util.Arrays;
import java.util.Date;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.complementvoucher.model.ElectronicVoucher; 

@Name("creditNoteElectList")
public class CreditNoteElectList extends EntityQuery<ElectronicVoucher> {
 
	private static final String EJBQL = "select electronicvoucher from ElectronicVoucher electronicvoucher "
			+ "where electronicvoucher.typeEmissionPoint.complementVoucherType.code = #{creditNoteElectList.typeVoucher}";
	private static final String[] RESTRICTIONS = { 
			"electronicvoucher.sequentialNumber like concat(#{creditNoteElectList.sequentialNumber},'%')",
			"electronicvoucher.emissionDate >=   #{creditNoteElectList.emissionDate}",
			"electronicvoucher.typeEmissionPoint.complementVoucherType.code = #{creditNoteElectList.typeVoucher}"}; //notas de credito

	
	
	private String sequentialNumber;
	private Date emissionDate; 
	private String typeVoucher ="04";
	

	public CreditNoteElectList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrder("electronicvoucher.emissionDate, electronicvoucher.sequentialNumber");
		setMaxResults(25);
	}

	public String getSequentialNumber() {
		return sequentialNumber;
	}

	public void setSequentialNumber(String sequentialNumber) {
		this.sequentialNumber = sequentialNumber;
	}

	public Date getEmissionDate() {
		return emissionDate;
	}

	public void setEmissionDate(Date emissionDate) {
		this.emissionDate = emissionDate;
	}

	public String getTypeVoucher() {
		return typeVoucher;
	}

	public void setTypeVoucher(String typeVoucher) {
		this.typeVoucher = typeVoucher;
	}
	/*@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}*/
}
