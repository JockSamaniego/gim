package org.gob.gim.income.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.action.MunicipalBondHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.income.model.Deposit;
import ec.gob.gim.income.model.PaymentAgreement;
import ec.gob.gim.income.model.ReplacementPaymentAgreement;
import ec.gob.gim.income.model.ReplacementPaymentDeposit;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.StatusChange;

@Name("replacementPaymentAgreementHome")
public class ReplacementPaymentAgreementHome extends EntityHome<ReplacementPaymentAgreement> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@In(create = true)
	MunicipalBondHome municipalBondHome;
	@In(create = true)
	PaymentAgreementHome paymentAgreementHome;
	
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	private SystemParameterService systemParameterService;
	
	@In
    EntityManager entityManager;
	
	@In
    UserSession userSession;

	private Long number;

	public void setReplacementPaymentAgreementId(Long id) {
		setId(id);
	}

	public Long getReplacementPaymentAgreementId() {
		return (Long) getId();
	}

	@Override
	protected ReplacementPaymentAgreement createInstance() {
		ReplacementPaymentAgreement replacementPaymentAgreement = new ReplacementPaymentAgreement();
		return replacementPaymentAgreement;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		MunicipalBond municipalBond = municipalBondHome.getDefinedInstance();
		if (municipalBond != null) {
			getInstance().setMunicipalBond(municipalBond);
		}
		PaymentAgreement paymentAgreement = paymentAgreementHome.getDefinedInstance();
		if (paymentAgreement != null) {
			getInstance().setPaymentAgreement(paymentAgreement);
		}
	}

	public boolean isWired() {
		return true;
	}

	public ReplacementPaymentAgreement getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public void addMunicipalbond() {
		
		this.getInstance().setTotal(BigDecimal.ZERO);
		
		String sql = "SELECT mb " + "FROM MunicipalBond mb " + "LEFT JOIN FETCH mb.entry "
				+ "LEFT JOIN FETCH mb.creditNote " + "WHERE mb.number = :municipalBondNumber AND "
				+ "mb.municipalBondStatus.id IN (:municipalBondStatusIds)";

		Query q = this.getEntityManager().createQuery(sql);
		q.setParameter("municipalBondNumber", this.number);
		q.setParameter("municipalBondStatusIds", loadMunicipalBondStatuses());

		if (q.getResultList().size() > 0) {
			this.getInstance().setMunicipalBond((MunicipalBond) q.getSingleResult());
			this.getInstance().setReplacementPaymentDeposits(new ArrayList<ReplacementPaymentDeposit>());

			String sqlDeposits = "SELECT d FROM Deposit d " 
			+ "LEFT JOIN FETCH d.municipalBond mb "
			+ "LEFT JOIN FETCH mb.receipt r " 
			+ "LEFT JOIN FETCH d.payment p "
			+ "WHERE ";
			// rfam 2021-08-31 ML-JR-2021-741-M
			if (this.getInstance().getMunicipalBond().getMunicipalBondStatus().getId().intValue() == 4) {
				sqlDeposits = sqlDeposits + "mb.paymentAgreement is not null and "; 
			}
			sqlDeposits = sqlDeposits + "mb.id = :municipalBondId "
			+ "AND d.status = 'VALID' AND p.status = 'VALID' ORDER BY d.id ";

			Query qDeposits = this.getEntityManager().createQuery(sqlDeposits);
			qDeposits.setParameter("municipalBondId", this.getInstance().getMunicipalBond().getId());
			List<Deposit> deposits = qDeposits.getResultList();
			
			ReplacementPaymentDeposit rd;
			for (Deposit dep : deposits) {
				rd = new ReplacementPaymentDeposit();
				rd.setDeposit(dep);
				rd.setValue(dep.getValue());
				this.getInstance().setTotal(this.getInstance().getTotal().add(dep.getValue()));
				this.getInstance().add(rd);
			}
		}
	}

	public List<Long> loadMunicipalBondStatuses() {
		List<Long> status = new ArrayList<Long>();
		status.add(new Long("4"));
		// rfam 2021-08-31 ML-JR-2021-741-M
		status.add(new Long("14"));
		return status;
	}
	
	@Override
	public String persist() {
		
		//primero dar la baja del titulo
		//entityManager.persist(createInstance());
		
		//luego hacer el registro del mismo en la devoluvion
		this.getInstance().setStatusChange(createStatusChange());
		this.getInstance().getMunicipalBond().setMunicipalBondStatus(findReversedStatus());
		this.getInstance().getMunicipalBond().setReversedResolution(this.getInstance().getDetail());
		this.getInstance().getMunicipalBond().setReversedDate(new Date());
		this.getInstance().getMunicipalBond().setReversedTime(new Date());
		// rfam 2021-08-31 ML-JR-2021-741-M
		if (this.getInstance().getMunicipalBond().getMunicipalBondStatus().getId().intValue() == 4) {
			this.getInstance().setPaymentAgreement(this.getInstance().getMunicipalBond().getPaymentAgreement());	
		}
		
		
		return super.persist();
	}
	
	
	public MunicipalBondStatus findReversedStatus() {
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		return systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_REVERSED");
	}
	
	public StatusChange createStatusChange() {
		StatusChange scNew = new StatusChange();
		scNew.setExplanation(this.getInstance().getDetail());
		scNew.setUser(userSession.getUser());
		scNew.setPreviousBondStatus(this.getInstance().getMunicipalBond().getMunicipalBondStatus());
		scNew.setMunicipalBondStatus(findReversedStatus());
		scNew.setMunicipalBond(this.getInstance().getMunicipalBond());
		return scNew;

	}
	
	

}
