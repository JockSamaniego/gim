package org.gob.gim.income.action;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityController;

import ec.gob.gim.income.model.Branch;
import ec.gob.gim.income.model.Till;
import ec.gob.gim.income.model.TillPermission;
import ec.gob.gim.income.model.TillPermissionDetail;

@Name("tillReportHome")
@Scope(ScopeType.CONVERSATION)
public class TillReportHome extends EntityController {

	private static final long serialVersionUID = 1L;

	@In
	UserSession userSession;
	

	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	
	private SystemParameterService systemParameterService;
	private Date startDate;
	private Date endDate;
	private Branch branch;
	private Till till;
	private Long paidStatus;
	private Long compensationStatus;
	private Long externalPaidStatus;
	private Long agreementStatus;
	private TillPermission tillPermission;
	
	private List<Branch> branches;
	private List<TillPermissionDetail> tillPermissionsDetails;
	
	@SuppressWarnings("unchecked")
	@Observer("org.jboss.seam.postCreate.tillReportHome")
	public void checkSettings(){
		Calendar c = Calendar.getInstance();			
		startDate = c.getTime();
		endDate = c.getTime();
		systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);		
		paidStatus = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_PAID");
		compensationStatus = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_COMPENSATION");
	}
	
	public List<Branch> findBranches() {
		if (branches == null) {
			Query query = getEntityManager().createNamedQuery("Branch.findAll");
			branches = query.getResultList();			
		}
		return branches != null ? branches : new ArrayList<Branch>();
	}
	
	public List<Till> loadTills() {
		if (branch == null) {
			return new ArrayList<Till>();
		}
		return orderTillsByNumber(branch.getTills());
	}
	
	/**
	 * Ordena por número de caja
	 * @param list
	 * @return List<Till>
	 */
	private List<Till> orderTillsByNumber(List<Till> list){
		List<Integer> numbers = new ArrayList<Integer>(); 
		for(Till t: list){
			numbers.add(t.getNumber());
		}
		Collections.sort(numbers);
		List<Till> result = new ArrayList<Till>();
		for(Integer n : numbers){
			for(Till t: list){
				if(t.getNumber() == n){
					result.add(t);
				}
			}
		}
		return result; 
	}
	
	/**
	 * Generar reporte por tillPermission entre fechas
	 */
	public void generateCashiersTillReport() {
		tillPermissionsDetails = null;
		Query query = null;
		if(branch == null){
			query = getEntityManager().createNamedQuery("TillPermission.findBetweenDates");
		}else {
			if (till == null) {		
				query = getEntityManager().createNamedQuery("TillPermission.findByBranchBetweenDates");
			} else {
				query = getEntityManager().createNamedQuery("TillPermission.findByBranchTillBetweenDates");
				query.setParameter("tillId", till.getId());
			}
			query.setParameter("branchId", branch.getId());
		}
		loadStatuses();
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		
		List<TillPermission> tillPermissions = query.getResultList();		
				
		findTotalCollected(tillPermissions);
				
		if(tillPermissions.size() == 1){
			tillPermission = tillPermissions.get(0);
		}		
	}
	
	/**
	 * EL Total de los municipalBonds entre fechas, cajero y municipalBonsStatus 
	 * y q no están o han estado en acuerdos de pago
	 * @param startDate
	 * @param endDate
	 * @param cashierId
	 * @param statusIds
	 * @return BigDecimal
	 */
	private BigDecimal findTotalCollectedByCashier(Date startDate, Date endDate, Long cashierId, List<Long> statusIds){		
		String sql = "select sum(m.paidTotal) from Payment p " +
				"left join p.deposits d " +
				"left join d.municipalBond m " +				
				"where p.date Between :startDate and :endDate " +
				"and p.cashier.id = :cashierId " +
				"and m is not null " +
				"AND m.paymentAgreement is null " +
				"and m.municipalBondStatus.id in (:statusIds)";
		Query query = getEntityManager().createQuery(sql);		
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("cashierId", cashierId);
		query.setParameter("statusIds", statusIds);
		BigDecimal res = (BigDecimal) query.getSingleResult();
		return res == null ? BigDecimal.ZERO : res;		
	}
	
	/**
	 * EL Total de los municipalBonds en acuerdos de pago entre fechas, cajero y municipalBonsStatus 
	 * @param startDate
	 * @param endDate
	 * @param cashierId
	 * @return BigDecimal
	 */
	private BigDecimal findTotalCollectedInAgreementByCashier(Date startDate, Date endDate, Long cashierId){		
		String sql = "select sum(d.value) from Payment p " +
				"left join p.deposits d " +
				"left join d.municipalBond m " +				
				"where p.date Between :startDate and :endDate " +
				"and d.date Between :startDate and :endDate " +
				"and p.cashier.id = :cashierId " +
				"and m is not null " +
				"AND m.paymentAgreement is  not null";
		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("cashierId", cashierId);					
		BigDecimal res = (BigDecimal) query.getSingleResult();
		return res == null ? BigDecimal.ZERO : res;
	}
	
	private void loadStatuses(){
		paidStatus = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_PAID");
		externalPaidStatus = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_PAID_FROM_EXTERNAL_CHANNEL");
		agreementStatus= systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_IN_PAYMENT_AGREEMENT");
		compensationStatus = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_COMPENSATION");
	}
	
	private List<Long> getPaidStatuses(){
		List<Long> list = new ArrayList<Long>();
		list.add(paidStatus);
		list.add(externalPaidStatus);
		return list;
	}
	
	private List<Long> getCompensationStatuses(){
		List<Long> list = new ArrayList<Long>();
		list.add(compensationStatus);		
		return list;
	}
	
	/**
	 * Total recaudado por tillPermissions
	 * @param tillPermissions
	 */
	private void findTotalCollected(List<TillPermission> tillPermissions) {
		tillPermissionsDetails = new ArrayList<TillPermissionDetail>();		
		List<Long> paidStatuses = getPaidStatuses();
		List<Long> compensationStatuses = getCompensationStatuses();
		for (TillPermission t : tillPermissions) {
			TillPermissionDetail td = createTillPermissionDetail(t);			
			BigDecimal totalCompensation = (BigDecimal) findTotalCollectedByCashier(t.getWorkday().getDate(), t.getWorkday().getDate(), t.getPerson().getId(), compensationStatuses);
			BigDecimal totalEffective = (BigDecimal) findTotalCollectedByCashier(t.getWorkday().getDate(), t.getWorkday().getDate(), t.getPerson().getId(), paidStatuses);
			BigDecimal totalPaymentAgreement = (BigDecimal) findTotalCollectedInAgreementByCashier(t.getWorkday().getDate(), t.getWorkday().getDate(), t.getPerson().getId());			
			td.setTillPermission(t);			
			td.setTotalCollected(totalEffective.add(totalPaymentAgreement));
			td.setTotalCompensationCollected(totalCompensation);					
			td.setTillNumber(t.getTill().getNumber());
			td.setBranch(t.getTill().getBranch().getName());
			td.setTotal(globalTotal(td));
			if(td.getTotal().compareTo(BigDecimal.ZERO) == 1)tillPermissionsDetails.add(td);
		}
	}
	
	private TillPermissionDetail createTillPermissionDetail(TillPermission t) {
		TillPermissionDetail td = new TillPermissionDetail();
		td.setDate(t.getWorkday().getDate());
		td.setInitialBalance(t.getInitialBalance());
		td.setInChargeName(t.getPerson().getName());
		td.setOpened(false);		
		if(t.getClosingTime() == null && t.getOpeningTime() != null) td.setOpened(true);	
		return td;
	}
	
	public BigDecimal totalTillPermissionDetailCollected() {
		BigDecimal total = BigDecimal.ZERO;
		if (tillPermissionsDetails == null) return total;
		for (TillPermissionDetail t : tillPermissionsDetails) {			
			total = total.add(t.getTotal());
		}
		return total;
	}
	
	private BigDecimal globalTotal(TillPermissionDetail t) {
		BigDecimal total = BigDecimal.ZERO;
		if (t == null) return total;
		total = total.add(t.getTotalCollected()).add(t.getTotalCompensationCollected());
		return total;
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

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public List<Branch> getBranches() {
		return branches;
	}

	public void setBranches(List<Branch> branches) {
		this.branches = branches;
	}

	public Till getTill() {
		return till;
	}

	public void setTill(Till till) {
		this.till = till;
	}

	public List<TillPermissionDetail> getTillPermissionsDetails() {
		return tillPermissionsDetails;
	}

	public void setTillPermissionsDetails(
			List<TillPermissionDetail> tillPermissionsDetails) {
		this.tillPermissionsDetails = tillPermissionsDetails;
	}

	public Long getPaidStatus() {
		return paidStatus;
	}

	public void setPaidStatus(Long paidStatus) {
		this.paidStatus = paidStatus;
	}

	public Long getCompensationStatus() {
		return compensationStatus;
	}

	public void setCompensationStatus(Long compensationStatus) {
		this.compensationStatus = compensationStatus;
	}

	public TillPermission getTillPermission() {
		return tillPermission;
	}

	public void setTillPermission(TillPermission tillPermission) {
		this.tillPermission = tillPermission;
	}

	public Long getExternalPaidStatus() {
		return externalPaidStatus;
	}

	public void setExternalPaidStatus(Long externalPaidStatus) {
		this.externalPaidStatus = externalPaidStatus;
	}

	public Long getAgreementStatus() {
		return agreementStatus;
	}

	public void setAgreementStatus(Long agreementStatus) {
		this.agreementStatus = agreementStatus;
	}
	
	
}
