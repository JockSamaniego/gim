package org.gob.gim.electronicvoucher.action;

import java.util.Arrays;
import java.util.Date;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.complementvoucher.model.ElectronicVoucher;

@Name("electronicVoucherList") 
public class ElectronicVoucherList extends EntityQuery<ElectronicVoucher> {

	private static final String EJBQL = "select electronicvoucher from ElectronicVoucher electronicvoucher "
			+ "where electronicvoucher.typeEmissionPoint.complementVoucherType.code = #{electronicVoucherList.typeVoucher}";

	private static final String[] RESTRICTIONS = { 
			"electronicvoucher.sequentialNumber like concat(#{electronicVoucherList.sequentialNumber},'%')",
			"electronicvoucher.emissionDate >= #{electronicVoucherList.emissionDate}",
			"electronicvoucher.typeEmissionPoint.complementVoucherType.code = #{electronicVoucherList.typeVoucher}" };

	private String sequentialNumber;
	private Date emissionDate; 
	private String typeVoucher ="07";
	  
	public ElectronicVoucherList() { 
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
