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
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Resident;


@Audited
@Entity
@TableGenerator(
	 name="WorkDealFractionGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="WorkDealFraction",
	 initialValue=1, allocationSize=1
)

public class WorkDealFraction implements Comparable<WorkDealFraction>{

	@Id
	@GeneratedValue(generator="WorkDealFractionGenerator",strategy=GenerationType.TABLE)
	private Long id;	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "property_id")
	private Property property;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resident_id")
	private Resident resident;
	
	@ManyToOne
	private WorkDeal workDeal;
	
	private String address;
	private BigDecimal frontLength;	
	private BigDecimal contributionFront;
	
	//Corresponde al 40%
	private BigDecimal sharedValue;
	
	//Corresponde al 60%
	private BigDecimal differentiatedValue;
	private BigDecimal total;
	
	/**
	 * 2015-07-29
	 * @author mack
	 */
	private BigDecimal waterValue;
	private BigDecimal sewerageValue;
	
	/**
	 * 2016-08-04
	 * @author Rene
	 * @tag cambioCalculoCEM
	 */
	//@Transient
	//campoque almacena el valor seleccionado de avaluo comercial para el calculo de CEM
	private BigDecimal commercialAppraisal;
	
	
	
	public WorkDealFraction() {
		frontLength = BigDecimal.ZERO;		
		contributionFront = BigDecimal.ZERO;
		sharedValue = BigDecimal.ZERO;
		differentiatedValue = BigDecimal.ZERO;
		total = BigDecimal.ZERO;
		
		/**
		 * 2015-07-29
		 * @author mack
		 */
		waterValue = BigDecimal.ZERO;
		sewerageValue = BigDecimal.ZERO;
		
		//@tag cambioCalculoCEM
		//2016-08-04
		commercialAppraisal = BigDecimal.ZERO;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Property getProperty() {
		return property;
	}
	public void setProperty(Property property) {
		this.property = property;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public BigDecimal getFrontLength() {
		return frontLength;
	}
	public void setFrontLength(BigDecimal frontLength) {
		this.frontLength = frontLength;
	}

	public BigDecimal getSharedValue() {
		return sharedValue;
	}
	public void setSharedValue(BigDecimal sharedValue) {
		this.sharedValue = sharedValue;
	}
	public BigDecimal getDifferentiatedValue() {
		return differentiatedValue;
	}
	public void setDifferentiatedValue(BigDecimal differentiatedValue) {
		this.differentiatedValue = differentiatedValue;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public WorkDeal getWorkDeal() {
		return workDeal;
	}
	public void setWorkDeal(WorkDeal workDeal) {
		this.workDeal = workDeal;
	}

	public BigDecimal getContributionFront() {
		return contributionFront;
	}

	public void setContributionFront(BigDecimal contributionFront) {
		this.contributionFront = contributionFront;
	}

	/**
	 * 2015-07-29
	 * @author mack
	 */
	public BigDecimal getWaterValue() {
		return waterValue;
	}

	public void setWaterValue(BigDecimal waterValue) {
		this.waterValue = waterValue;
	}

	public BigDecimal getSewerageValue() {
		return sewerageValue;
	}

	public void setSewerageValue(BigDecimal sewerageValue) {
		this.sewerageValue = sewerageValue;
	}

	@Override
	public int compareTo(WorkDealFraction o) {
		return property.getCadastralCode().compareTo(o.getProperty().getCadastralCode());
	}

	/**
	 * 2016-08-04
	 * @author mack
	 * valor Transiente
	 */

	public BigDecimal getCommercialAppraisal() {
		return commercialAppraisal;
	}

	public void setCommercialAppraisal(BigDecimal commercialAppraisal) {
		this.commercialAppraisal = commercialAppraisal;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}
	
}
