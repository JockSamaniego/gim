package org.gob.gim.electronicvoucher.creditnote.action;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.gob.gim.revenue.action.SolvencyReportHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.complementvoucher.model.ElectronicVoucher; 

@Name("creditNoteElectList")
public class CreditNoteElectList extends EntityQuery<ElectronicVoucher> {
 
	private static final String EJBQL = "select electronicvoucher from ElectronicVoucher electronicvoucher "
			+ "where electronicvoucher.typeEmissionPoint.complementVoucherType.code = #{creditNoteElectList.typeVoucher}";
	private static final String[] RESTRICTIONS = { 
			"electronicvoucher.sequentialNumber like concat(#{creditNoteElectList.sequentialNumber},'%')",
			"electronicvoucher.emissionDate >=   #{creditNoteElectList.emissionDateFrom}",
			"electronicvoucher.emissionDate <=   #{creditNoteElectList.emissionDateUntil}",
			"electronicvoucher.typeEmissionPoint.complementVoucherType.code = #{creditNoteElectList.typeVoucher}",
			"electronicvoucher.resident.identificationNumber like concat(#{creditNoteElectList.criteriaIdentification},'%')"}; //notas de credito

	
	private String criteriaIdentification;
	private String sequentialNumber;
	private Date emissionDateFrom; 
	private Date emissionDateUntil; 
	private String typeVoucher ="04";
	

	public CreditNoteElectList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrder("electronicvoucher.sequentialNumber DESC, electronicvoucher.emissionDate DESC");
		setMaxResults(25);
	}

	public String getSequentialNumber() {
		return sequentialNumber;
	}

	public void setSequentialNumber(String sequentialNumber) {
		this.sequentialNumber = sequentialNumber;
	}

	public Date getEmissionDateFrom() {
		return emissionDateFrom;
	}

	public void setEmissionDateFrom(Date emissionDateFrom) {
		this.emissionDateFrom = emissionDateFrom;
	}

	public Date getEmissionDateUntil() {
		return emissionDateUntil;
	}

	public void setEmissionDateUntil(Date emissionDateUntil) {
		this.emissionDateUntil = emissionDateUntil;
	}

	public String getTypeVoucher() {
		return typeVoucher;
	}

	public void setTypeVoucher(String typeVoucher) {
		this.typeVoucher = typeVoucher;
	}

	public String getCriteriaIdentification() {
		return criteriaIdentification;
	}

	public void setCriteriaIdentification(String criteriaIdentification) {
		this.criteriaIdentification = criteriaIdentification;
	}
	
	
	@In(create = true)
	CreditNoteElectHome creditNoteElectHome;
	
	public void selectAllCreditNote(){
		String query = "select electronicvoucher.id from ElectronicVoucher electronicvoucher "
				+ "LEFT JOIN gimprod.typeemissionpoint tye ON tye.id = electronicvoucher.typeemissionpoint_id "
				+ "LEFT JOIN gimprod.complementvouchertype cvt ON cvt.id = tye.complementvouchertype_id "
				+ "LEFT JOIN gimprod.resident res ON res.id = electronicvoucher.resident_id "
				+ "where cvt.code =:typeVoucher ";
		if(sequentialNumber != null){
			query = query + " and electronicvoucher.sequentialNumber like concat(:sequentialNumber,'%') ";
		}
		if(emissionDateFrom != null){
			query = query + " and electronicvoucher.emissionDate >= :emissionDateFrom ";
		}
		if(emissionDateUntil != null){
			query = query + " and electronicvoucher.emissionDate <= :emissionDateUntil ";
		}
		if(criteriaIdentification != null){
			query = query + " and res.identificationNumber like concat(:criteriaIdentification,'%') ";
		}
		
		query = query + " ORDER BY electronicvoucher.sequentialNumber, electronicvoucher.emissionDate ASC ";
		
		Query q = getEntityManager().createNativeQuery(query);
		q.setParameter("typeVoucher", typeVoucher);
		
		if(sequentialNumber != null){
			q.setParameter("sequentialNumber", sequentialNumber);
		}
		if(emissionDateFrom != null){
			q.setParameter("emissionDateFrom", emissionDateFrom);
		}
		if(emissionDateUntil != null){
			q.setParameter("emissionDateUntil", emissionDateUntil);
		}
		if(criteriaIdentification != null){
			q.setParameter("criteriaIdentification", criteriaIdentification);
		}
		
		List<BigInteger> evs = q.getResultList();
		CreditNoteElectHome creditNoteElectHome = (CreditNoteElectHome) Contexts.getConversationContext().get(CreditNoteElectHome.class);
		creditNoteElectHome.creditNoteAllSelect(evs);
		creditNoteElectHome.setDateReportFrom(emissionDateFrom);
		creditNoteElectHome.setDateReportUntil(emissionDateUntil);
		
	}
	
	/*@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}*/
}
