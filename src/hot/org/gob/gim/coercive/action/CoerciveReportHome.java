package org.gob.gim.coercive.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.gob.gim.coercive.view.ExpiredMunicipalBond;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.waterservice.action.RoutePeriodHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.income.model.PaymentAgreement;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.waterservice.model.Route;

@Name("coerciveReportHome")
public class CoerciveReportHome extends EntityHome<Route> {

	@In(create = true)
	RoutePeriodHome routePeriodHome;

	@In
	private FacesMessages facesMessages;

	@In
	UserSession userSession;
	
	private Long quantity = new Long(3);
	private BigDecimal total=BigDecimal.ZERO;

	private Date dateReport = new Date();
	private List <ExpiredMunicipalBond> expiredObligations;
	
	private PaymentAgreement paymentAgreement;
	private List <MunicipalBond> bondsAgreement;
	

	public void setRouteId(Long id) {
		setId(id);
	}

	public Long getRouteId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	boolean isFirstTime = true;

	public void wire() {
		if (!isFirstTime)
			return;
		getInstance();
		isFirstTime = false;
	}

	public void loadValues() {
		if (!isFirstTime)
			return;
		getInstance();
		isFirstTime = false;
	}

	public boolean isWired() {
		return true;
	}

	private boolean newLoad = false;

	public boolean isNewLoad() {
		return newLoad;
	}

	public void setNewLoad(boolean newLoad) {
		this.newLoad = newLoad;
	}

	public void listPendingByQuantity() {
		String sentence = "select NEW org.gob.gim.coercive.view.ExpiredMunicipalBond(resident, count(mb),sum(mb.value)) from MunicipalBond mb "
				+ "inner join mb.resident resident "
				+ "where mb.expirationDate < :date and mb.municipalBondStatus.id=3 "
				+ "group by resident "
				+ "HAVING COUNT(*) >= :quantity "
				+ "order by COUNT(*) desc, sum(mb.value) desc ";
		Query q = this.getEntityManager().createQuery(sentence);
		q.setParameter("date", dateReport);
		q.setParameter("quantity", quantity);
		expiredObligations=q.getResultList();
		for(ExpiredMunicipalBond emb:expiredObligations){
			this.total = total.add(emb.getSubTotal());
			this.quantity = quantity + emb.getAccount();
		}
		System.out.println("la cantidad es "+expiredObligations.size());
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Date getDateReport() {
		return dateReport;
	}

	public void setDateReport(Date dateReport) {
		this.dateReport = dateReport;
	}

	public List<ExpiredMunicipalBond> getExpiredObligations() {
		return expiredObligations;
	}

	public void setExpiredObligations(List<ExpiredMunicipalBond> expiredObligations) {
		this.expiredObligations = expiredObligations;
	}
	
	// reporte para convenios de pago
	
	public List<PaymentAgreement> agreements;
	
	public void listPaymentAgreements(){
		String sentence = "select distinct pa from PaymentAgreement pa "
				+ "left join FETCH pa.municipalBonds mbs "
				//+ "left join FETCH mbs.deposits deposit "
				//+ "left join FETCH deposit.payment payment "
				+ "left join FETCH mbs.resident residentMB "
				+ "LEFT JOIN FETCH mbs.receipt "
				+ "LEFT JOIN FETCH residentMB.currentAddress "
				+ "where pa.isActive = true and pa.firstPaymentDate <= :fpd "
				+ "order by pa.firstPaymentDate";
		Query q = this.getEntityManager().createQuery(sentence);
		q.setParameter("fpd", dateReport);
		agreements=q.getResultList();
		Integer alreadyPayed;
		Integer notPayedYet;
		for (PaymentAgreement pa : agreements) {
			alreadyPayed = new Integer("0");			
			notPayedYet = new Integer("0");
			for (MunicipalBond mb : pa.getMunicipalBonds()) {
				System.out.println("nuevo...");
				if (mb.getMunicipalBondStatus().getId().equals(new Long("6"))) {
					alreadyPayed = alreadyPayed + 1;
				}else{
					if (mb.getMunicipalBondStatus().getId().equals(new Long("4"))) {
						notPayedYet = notPayedYet + 1;
					}
				}
			}
			pa.setAlreadyPayed(alreadyPayed);
			pa.setNotPayedYet(notPayedYet);
		}
		System.out.println("la cantidad es "+agreements.size());
	}
	
	public void selectPaymentAgreement(PaymentAgreement pa){
		this.paymentAgreement=pa;
		String sentence="select mb from MunicipalBond mb "
				+ "left join FETCH mb.deposits deposit "
				+ "left join FETCH deposit.payment payment "
				+ "left join FETCH mb.entry entry "
				+ "where mb.paymentAgreement.id=:paId "
				+ "order by mb.creationDate";
		Query q=this.getEntityManager().createQuery(sentence);
		bondsAgreement = q.setParameter("paId", pa.getId()).getResultList();
	}

	public List<PaymentAgreement> getAgreements() {
		return agreements;
	}

	public void setAgreements(List<PaymentAgreement> agreements) {
		this.agreements = agreements;
	}

	public List<MunicipalBond> getBondsAgreement() {
		return bondsAgreement;
	}

	public void setBondsAgreement(List<MunicipalBond> bondsAgreement) {
		this.bondsAgreement = bondsAgreement;
	}

	public PaymentAgreement getPaymentAgreement() {
		return paymentAgreement;
	}

	public void setPaymentAgreement(PaymentAgreement paymentAgreement) {
		this.paymentAgreement = paymentAgreement;
	}

}
