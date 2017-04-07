package ec.gob.gim.income.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.StatusChange;

@Audited
@Entity
@TableGenerator(name = "ReplacementPaymentAgreementGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "ReplacementPaymentAgreement", initialValue = 1, allocationSize = 1)
public class ReplacementPaymentAgreement {

	@Id
	@GeneratedValue(generator = "ReplacementPaymentAgreementGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	private java.util.Date date;

	@Temporal(TemporalType.TIME)
	private java.util.Date time;

	private Boolean isReversed;
	private String reversedExplanation;

	// detalle de porq se anula
	private String detail;

	// es la suma del value de los deposit q existan
	private BigDecimal total;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "municipalBond_id")
	private MunicipalBond municipalBond;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "paymentAgreement_id")
	private PaymentAgreement paymentAgreement;

	@NotAudited
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "statusChange_id")
	private StatusChange statusChange;

	@OneToMany(mappedBy = "replacementPaymentAgreement", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@OrderBy("id")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<ReplacementPaymentDeposit> replacementPaymentDeposits;

	public ReplacementPaymentAgreement() {
		this.date = new Date();
		this.time = new Date();
		this.isReversed = Boolean.FALSE;
		this.replacementPaymentDeposits = new ArrayList<ReplacementPaymentDeposit>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public java.util.Date getTime() {
		return time;
	}

	public void setTime(java.util.Date time) {
		this.time = time;
	}

	public Boolean getIsReversed() {
		return isReversed;
	}

	public void setIsReversed(Boolean isReversed) {
		this.isReversed = isReversed;
	}

	public String getReversedExplanation() {
		return reversedExplanation;
	}

	public void setReversedExplanation(String reversedExplanation) {
		this.reversedExplanation = reversedExplanation;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public MunicipalBond getMunicipalBond() {
		return municipalBond;
	}

	public void setMunicipalBond(MunicipalBond municipalBond) {
		this.municipalBond = municipalBond;
	}

	public PaymentAgreement getPaymentAgreement() {
		return paymentAgreement;
	}

	public void setPaymentAgreement(PaymentAgreement paymentAgreement) {
		this.paymentAgreement = paymentAgreement;
	}

	public List<ReplacementPaymentDeposit> getReplacementPaymentDeposits() {
		return replacementPaymentDeposits;
	}

	public void setReplacementPaymentDeposits(List<ReplacementPaymentDeposit> replacementPaymentDeposits) {
		this.replacementPaymentDeposits = replacementPaymentDeposits;
	}

	public void add(ReplacementPaymentDeposit rpd) {
		if (!this.replacementPaymentDeposits.contains(rpd)) {
			this.replacementPaymentDeposits.add(rpd);
			rpd.setReplacementPaymentAgreement(this);
		}
	}

	public void remove(ReplacementPaymentDeposit rpd) {
		boolean removed = this.replacementPaymentDeposits.remove(rpd);
		if (removed) {
			rpd.setReplacementPaymentAgreement(null);
		}

	}

	public StatusChange getStatusChange() {
		return statusChange;
	}

	public void setStatusChange(StatusChange statusChange) {
		this.statusChange = statusChange;
	}

	public BigDecimal sumTotalPaid() {
		BigDecimal paidValue = BigDecimal.ZERO;
		for (ReplacementPaymentDeposit deposit : replacementPaymentDeposits) {
			paidValue = paidValue.add(deposit.getValue());
		}
		return paidValue;
	}

}