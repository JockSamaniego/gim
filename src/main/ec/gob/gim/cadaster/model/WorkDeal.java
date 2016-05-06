package ec.gob.gim.cadaster.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

@Audited
@Entity
@TableGenerator(name = "WorkDealGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "WorkDeal", initialValue = 1, allocationSize = 1)

public class WorkDeal {

	@Id
	@GeneratedValue(generator = "WorkDealGenerator", strategy = GenerationType.TABLE)
	private Long id;

	private String name;

	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private WorkDealType workDealType;

	// La primera vez que lo imprimen
	private Date reportDate;

	private BigDecimal workDealValue;

	@Column(precision = 19, scale = 12)
	private BigDecimal frontFactor;

	@Column(precision = 19, scale = 12)
	private BigDecimal appraisalFactor;

	private BigDecimal totalContributionFront;

	private BigDecimal totalSharedValue;

	private BigDecimal totalAppraisal;

	private BigDecimal totalDifferentiatedValue;

	// Factor de cobro
	private BigDecimal collectFactor;

	private String observation;

	@OneToMany(mappedBy = "workDeal", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<WorkDealFraction> workDealFractions;

	/**
	 * @author macartuche
	 * @date 2015-07-28
	 */
	private Boolean drinkingWater = false;

	private Boolean asphalt = true;//ya existente

	private Boolean sewerage = false; 

	private BigDecimal waterValue;
	private BigDecimal asphaltValue;
	private BigDecimal sewerageValue;

	public WorkDeal() {
		workDealFractions = new ArrayList<WorkDealFraction>();
		workDealValue = BigDecimal.ZERO;
		frontFactor = BigDecimal.ZERO;
		appraisalFactor = BigDecimal.ZERO;
		totalSharedValue = BigDecimal.ZERO;
		totalAppraisal = BigDecimal.ONE;
		totalDifferentiatedValue = BigDecimal.ZERO;
		collectFactor = BigDecimal.ZERO;
		totalContributionFront = BigDecimal.ONE;
		/**
		 * @author mack
		 * @date 2015-07-28
		 */
		waterValue = BigDecimal.ZERO;
		asphaltValue = BigDecimal.ZERO;
		sewerageValue = BigDecimal.ZERO;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public WorkDealType getWorkDealType() {
		return workDealType;
	}

	public void setWorkDealType(WorkDealType workDealType) {
		this.workDealType = workDealType;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public List<WorkDealFraction> getWorkDealFractions() {
		return workDealFractions;
	}

	public void setWorkDealFractions(List<WorkDealFraction> workDealFractions) {
		this.workDealFractions = workDealFractions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void add(WorkDealFraction workDealFraction) {
		if (!this.getWorkDealFractions().contains(workDealFraction) && workDealFraction != null) {
			this.getWorkDealFractions().add(workDealFraction);
			workDealFraction.setWorkDeal(this);
		}
	}

	public void remove(WorkDealFraction workDealFraction) {
		boolean removed = this.workDealFractions.remove(workDealFraction);
		if (removed)
			workDealFraction.setWorkDeal(null);
	}

	public BigDecimal getCollectFactor() {
		return collectFactor;
	}

	public void setCollectFactor(BigDecimal collectFactor) {
		this.collectFactor = collectFactor;
	}

	public BigDecimal getWorkDealValue() {
		return workDealValue;
	}

	public void setWorkDealValue(BigDecimal workDealValue) {
		this.workDealValue = workDealValue;
	}

	public BigDecimal getFrontFactor() {
		return frontFactor;
	}

	public void setFrontFactor(BigDecimal frontFactor) {
		this.frontFactor = frontFactor;
	}

	public BigDecimal getAppraisalFactor() {
		return appraisalFactor;
	}

	public void setAppraisalFactor(BigDecimal appraisalFactor) {
		this.appraisalFactor = appraisalFactor;
	}

	public BigDecimal getTotalSharedValue() {
		return totalSharedValue;
	}

	public void setTotalSharedValue(BigDecimal totalSharedValue) {
		this.totalSharedValue = totalSharedValue;
	}

	public BigDecimal getTotalDifferentiatedValue() {
		return totalDifferentiatedValue;
	}

	public void setTotalDifferentiatedValue(BigDecimal totalDifferentiatedValue) {
		this.totalDifferentiatedValue = totalDifferentiatedValue;
	}

	public BigDecimal getTotalAppraisal() {
		return totalAppraisal;
	}

	public void setTotalAppraisal(BigDecimal totalAppraisal) {
		this.totalAppraisal = totalAppraisal;
	}

	public BigDecimal getTotalContributionFront() {
		return totalContributionFront;
	}

	public void setTotalContributionFront(BigDecimal totalContributionFront) {
		this.totalContributionFront = totalContributionFront;
	}

	/**
	 * @author macartuche
	 * @date 2015-07-28
	 */
	public Boolean getDrinkingWater() {
		return drinkingWater;
	}

	public void setDrinkingWater(Boolean drinkingWater) {
		this.drinkingWater = drinkingWater;
	}

	public Boolean getAsphalt() {
		return asphalt;
	}

	public void setAsphalt(Boolean asphalt) {
		this.asphalt = asphalt;
	}

	public Boolean getSewerage() {
		return sewerage;
	}

	public void setSewerage(Boolean sewerage) {
		this.sewerage = sewerage;
	}

	public BigDecimal getWaterValue() {
		return waterValue;
	}

	public void setWaterValue(BigDecimal waterValue) {
		this.waterValue = waterValue;
	}

	public BigDecimal getAsphaltValue() {
		return asphaltValue;
	}

	public void setAsphaltValue(BigDecimal asphaltValue) {
		this.asphaltValue = asphaltValue;
	}

	public BigDecimal getSewerageValue() {
		return sewerageValue;
	}

	public void setSewerageValue(BigDecimal sewerageValue) {
		this.sewerageValue = sewerageValue;
	}
}