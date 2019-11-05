package org.gob.gim.coercive.action;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.gob.gim.coercive.view.ExpiredMunicipalBond;
import org.gob.gim.common.NativeQueryResultsMapper;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.income.action.PaymentHome;
import org.gob.gim.waterservice.action.RoutePeriodHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.common.model.FinancialStatus;
import ec.gob.gim.income.model.Payment;
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
	
	private Date startDate;
	private Date endDate;
	private List<CoerciveReportDto> expiredList;
	private CoerciveReportDto selected;
	private BigDecimal canceledValue = BigDecimal.ZERO;
	private BigDecimal balanceForPay  = BigDecimal.ZERO;
	private BigDecimal payedValue = BigDecimal.ZERO;
	private BigInteger totalPayedQuotes = BigInteger.ZERO;
	private BigInteger totalQuotes = BigInteger.ZERO;
	
	private List<Payment> payments = new ArrayList<Payment>();
	

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
		//System.out.println("la cantidad es "+expiredObligations.size());
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
	
	@SuppressWarnings("unchecked")
	public void listExpired(){
		
		if(this.startDate == null && this.endDate==null) {
			return;
		}
		//@macartuche
		//cambio de consulta para convenios expirados.
		this.canceledValue = BigDecimal.ZERO;
		this.payedValue = BigDecimal.ZERO;
		this.balanceForPay = BigDecimal.ZERO;
		totalPayedQuotes = BigInteger.ZERO;
		
		this.expiredList = new ArrayList<CoerciveReportDto>();	
		String sentence = 
				"select pa.id as paymentAgreementID, "
				+ "		max(d.date) as expiration, " 
				+"		r.identificationnumber as identification, "  
				+"		r.name as names, "  
				+"		(select sum(dep.value) "  
				+"			from deposit dep join municipalbond mb on mb.id = dep.municipalbond_id "  
				+"			where mb.paymentagreement_id=pa.id) as payed, "
				+" 		pa.description " 
				+"	from dividend d " 
				+"	join paymentagreement pa on pa.id = d.paymentagreement_id "  
				+"	join resident r on r.id = pa.resident_id " 
				+"where pa.isactive=true " 
				+"group by pa.id, r.identificationnumber, r.name "
				+"having max(d.date) between ?0 and ?1 "
				+ "order by 2,3";
		
		Query q = this.getEntityManager().createNativeQuery(sentence);
		System.out.println(sentence);
		q.setParameter(0, this.startDate);
		q.setParameter(1, this.endDate);
		this.expiredList = NativeQueryResultsMapper.map(q.getResultList(), CoerciveReportDto.class);
	}
	
	public void clean() {
		this.canceledValue = BigDecimal.ZERO; 
		this.totalPayedQuotes = BigInteger.ZERO;
		this.totalQuotes = BigInteger.ZERO;
		this.payedValue = BigDecimal.ZERO;
		this.balanceForPay = BigDecimal.ZERO;
		this.canceledValue = BigDecimal.ZERO;
		this.startDate = null;
		this.endDate = null;
		this.expiredList = new ArrayList<CoerciveReportDto>();
		
	}
	

	public void selectPaymentAgreement(CoerciveReportDto dto){				
		this.selected = dto;
		this.payedValue = dto.getPayed();
		
		calculateCanceledBonds(dto);
		calculateBalanceForPay(dto);
		loadTotalQuotes(dto);
		loadTotalQuotasPayed(dto);
	}

	private void loadTotalQuotes(CoerciveReportDto dto) {
		
		this.totalQuotes = BigInteger.ZERO;
		
		if(dto.getPaymentAgreementID()!=null) { 
			Query q = getEntityManager().createNativeQuery("select count(*) from dividend where paymentagreement_id = ?0 ");
			q.setParameter(0, dto.getPaymentAgreementID());
			this.totalQuotes = (BigInteger)q.getSingleResult();	 
		}
	}
	
	
	public void calculateCanceledBonds(CoerciveReportDto dto) {
		if(dto.getPaymentAgreementID()!=null) { 
			Query q = getEntityManager().createNativeQuery("select coalesce(sum(m.value),0) from municipalbond m "
					+ "where m.paymentagreement_id=:agreement and m.municipalbondstatus_id=:canceled");
			q.setParameter("canceled", 9L); //anulados o dados de baja
			q.setParameter("agreement", dto.getPaymentAgreementID());
			this.canceledValue = (BigDecimal)q.getSingleResult();	 
		}
	}
	
	@In(create=true)
	PaymentHome paymentHome;
	public void calculateBalanceForPay(CoerciveReportDto dto) {
		balanceForPay = BigDecimal.ZERO;		
		PaymentAgreement instance  = getEntityManager().find(PaymentAgreement.class, dto.getPaymentAgreementID());
		paymentHome.setPaymentAgreement(instance);
		paymentHome.calculateTotals();
		for(MunicipalBond bond: paymentHome.getMunicipalBonds()) {
			balanceForPay = balanceForPay.add(bond.getPaidTotal());
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void loadTotalQuotasPayed(CoerciveReportDto dto) {
		totalPayedQuotes = BigInteger.ZERO;
		if(dto.getPaymentAgreementID() !=null ) {
			Query q = getEntityManager().createNativeQuery("select count(distinct(p.id)) "
					+ "from payment  p join deposit d on d.payment_id= p.id  "
					+ "join municipalbond m on m.id= d.municipalbond_id "
					+ "where m.paymentagreement_id=:id and p.status='VALID' and d.status='VALID' ");
			q.setParameter("id", dto.getPaymentAgreementID());
			
			List<Object> data = q.getResultList();
			if(data.size()>0) {
				totalPayedQuotes = (BigInteger)data.get(0);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadDeposits(CoerciveReportDto dto) {
		if(dto.getPaymentAgreementID()!=null) {
			 
			Query q = getEntityManager().createQuery("Select DISTINCT(p) from Payment p "
					+ "JOIN p.deposits d "
					+ "JOIN d.municipalBond m "
					+ "WHERE m.paymentAgreement.id=:id and d.status=:status "
					+ "and m.municipalBondStatus.id !=:canceled "
					+ "ORDER by p.date, p.time ");
			q.setParameter("id", dto.getPaymentAgreementID() );
			q.setParameter("status", FinancialStatus.VALID);
			q.setParameter("canceled", new Long(9));
			payments = (List<Payment>)q.getResultList(); 
		}
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<CoerciveReportDto> getExpiredList() {
		return expiredList;
	}

	public void setExpiredList(List<CoerciveReportDto> expiredList) {
		this.expiredList = expiredList;
	}

	public CoerciveReportDto getSelected() {
		return selected;
	}

	public void setSelected(CoerciveReportDto selected) {
		this.selected = selected;
	}

	public BigDecimal getCanceledValue() {
		return canceledValue;
	}

	public void setCanceledValue(BigDecimal canceledValue) {
		this.canceledValue = canceledValue;
	}

	public BigDecimal getBalanceForPay() {
		return balanceForPay;
	}

	public void setBalanceForPay(BigDecimal balanceForPay) {
		this.balanceForPay = balanceForPay;
	}

	public BigDecimal getPayedValue() {
		return payedValue;
	}

	public void setPayedValue(BigDecimal payedValue) {
		this.payedValue = payedValue;
	}

	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	public BigInteger getTotalPayedQuotes() {
		return totalPayedQuotes;
	}

	public void setTotalPayedQuotes(BigInteger totalPayedQuotes) {
		this.totalPayedQuotes = totalPayedQuotes;
	}

	public BigInteger getTotalQuotes() {
		return totalQuotes;
	}

	public void setTotalQuotes(BigInteger totalQuotes) {
		this.totalQuotes = totalQuotes;
	} 
}
