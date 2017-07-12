package ec.gob.gim.income.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

@Audited
@Entity
@TableGenerator(name = "ReplacementPaymentDepositGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "ReplacementPaymentDeposit", initialValue = 1, allocationSize = 1)
public class ReplacementPaymentDeposit {

	@Id
	@GeneratedValue(generator = "ReplacementPaymentDepositGenerator", strategy = GenerationType.TABLE)
	private Long id;

	private BigDecimal value;

	@ManyToOne(fetch = FetchType.LAZY)
	private ReplacementPaymentAgreement replacementPaymentAgreement;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deposit_id")
	private Deposit deposit;

	public ReplacementPaymentDeposit() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public ReplacementPaymentAgreement getReplacementPaymentAgreement() {
		return replacementPaymentAgreement;
	}

	public void setReplacementPaymentAgreement(ReplacementPaymentAgreement replacementPaymentAgreement) {
		this.replacementPaymentAgreement = replacementPaymentAgreement;
	}

	public Deposit getDeposit() {
		return deposit;
	}

	public void setDeposit(Deposit deposit) {
		this.deposit = deposit;
	}

}
