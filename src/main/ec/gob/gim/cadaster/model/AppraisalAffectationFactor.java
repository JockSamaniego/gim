package ec.gob.gim.cadaster.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import ec.gob.gim.appraisal.model.AppraisalPeriod;

@Entity
@TableGenerator(name = "AppraisalAffectationFactorGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "AppraisalAffectationFactor", initialValue = 1, allocationSize = 1)
public class AppraisalAffectationFactor {

	@Id
	@GeneratedValue(generator = "AppraisalAffectationFactorGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "affectationFactor_id", nullable = false, referencedColumnName = "id")
	private AffectationFactor affectationFactor;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "appraisalPeriod_id", nullable = false, referencedColumnName = "id")
	private AppraisalPeriod appraisalPeriod;

	private BigDecimal coefficient;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AffectationFactor getAffectationFactor() {
		return affectationFactor;
	}

	public void setAffectationFactor(AffectationFactor affectationFactor) {
		this.affectationFactor = affectationFactor;
	}

	public AppraisalPeriod getAppraisalPeriod() {
		return appraisalPeriod;
	}

	public void setAppraisalPeriod(AppraisalPeriod appraisalPeriod) {
		this.appraisalPeriod = appraisalPeriod;
	}

	public BigDecimal getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(BigDecimal coefficient) {
		this.coefficient = coefficient;
	}

	@Override
	public String toString() {
		return "AppraisalAffectationFactor [id=" + id + ", affectationFactor="
				+ affectationFactor + ", appraisalPeriod=" + appraisalPeriod
				+ ", coefficient=" + coefficient + "]";
	}

}
