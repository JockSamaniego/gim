package ec.gob.gim.appraisal.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

/**
 * @author IML
 */
@Audited
@Entity
@TableGenerator(
	 name="AppraisalPeriodGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="AppraisalPeriod",
	 initialValue=1, allocationSize=1
)

@NamedQueries({
	@NamedQuery(name = "AppraisalPeriod.findAll", 
			query = "select aPeriod from AppraisalPeriod aPeriod order by aPeriod.name"),
	@NamedQuery(name = "AppraisalPeriod.findUniqueActiveAndNotForTest", 
		query = "select aPeriod from AppraisalPeriod aPeriod where aPeriod.open=true and aPeriod.forTest = false order by aPeriod.name")
})

public class AppraisalPeriod {

	@Id
	@GeneratedValue(generator="AppraisalPeriodGenerator",strategy=GenerationType.TABLE)
	private Long id;
	@Column(length=15)
	private String code;
	@Column(length=40)
	private String name;
	private boolean open;
	private boolean forTest;
	private BigDecimal factorHasEquipment;
	private BigDecimal factorHasntEquipment;
	private BigDecimal factorHasWater;
	private BigDecimal factorHasntWater;
	private BigDecimal factorHasSewerage;
	private BigDecimal factorHasntSewerage;
	private BigDecimal factorHasEnergy;
	private BigDecimal factorHasntEnergy;
	
	@OneToMany(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "AppraisalPeriod_id")
	@OrderBy("id")
	private List<AppraisalTotalRoof> appraisalTotalRoof;

	@OneToMany(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "AppraisalPeriod_id")
	@OrderBy("id")
	private List<AppraisalTotalStructure> appraisalTotalStructure;

	@OneToMany(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "AppraisalPeriod_id")
	@OrderBy("id")
	private List<AppraisalTotalWall> appraisalTotalWall;

	@OneToMany(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "AppraisalPeriod_id")
	@OrderBy("id")
	private List<AppraisalTotalExternal> appraisalTotalExternal;

	public AppraisalPeriod() {
		this.appraisalTotalStructure = new ArrayList<AppraisalTotalStructure>();
		this.appraisalTotalWall = new ArrayList<AppraisalTotalWall>();
		this.appraisalTotalRoof = new ArrayList<AppraisalTotalRoof>();
		this.appraisalTotalExternal = new ArrayList<AppraisalTotalExternal>();
		this.forTest = true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isForTest() {
		return forTest;
	}

	public void setForTest(boolean forTest) {
		this.forTest = forTest;
	}

	public BigDecimal getFactorHasEquipment() {
		return factorHasEquipment;
	}

	public void setFactorHasEquipment(BigDecimal factorHasEquipment) {
		this.factorHasEquipment = factorHasEquipment;
	}

	public BigDecimal getFactorHasntEquipment() {
		return factorHasntEquipment;
	}

	public void setFactorHasntEquipment(BigDecimal factorHasntEquipment) {
		this.factorHasntEquipment = factorHasntEquipment;
	}

	public BigDecimal getFactorHasWater() {
		return factorHasWater;
	}

	public void setFactorHasWater(BigDecimal factorHasWater) {
		this.factorHasWater = factorHasWater;
	}

	public BigDecimal getFactorHasntWater() {
		return factorHasntWater;
	}

	public void setFactorHasntWater(BigDecimal factorHasntWater) {
		this.factorHasntWater = factorHasntWater;
	}

	public BigDecimal getFactorHasSewerage() {
		return factorHasSewerage;
	}

	public void setFactorHasSewerage(BigDecimal factorHasSewerage) {
		this.factorHasSewerage = factorHasSewerage;
	}

	public BigDecimal getFactorHasntSewerage() {
		return factorHasntSewerage;
	}

	public void setFactorHasntSewerage(BigDecimal factorHasntSewerage) {
		this.factorHasntSewerage = factorHasntSewerage;
	}

	public BigDecimal getFactorHasEnergy() {
		return factorHasEnergy;
	}

	public void setFactorHasEnergy(BigDecimal factorHasEnergy) {
		this.factorHasEnergy = factorHasEnergy;
	}

	public BigDecimal getFactorHasntEnergy() {
		return factorHasntEnergy;
	}

	public void setFactorHasntEnergy(BigDecimal factorHasntEnergy) {
		this.factorHasntEnergy = factorHasntEnergy;
	}

	public List<AppraisalTotalRoof> getAppraisalTotalRoof() {
		return appraisalTotalRoof;
	}

	public void setAppraisalTotalRoof(List<AppraisalTotalRoof> appraisalTotalRoof) {
		this.appraisalTotalRoof = appraisalTotalRoof;
	}

	public List<AppraisalTotalStructure> getAppraisalTotalStructure() {
		return appraisalTotalStructure;
	}

	public void setAppraisalTotalStructure(
			List<AppraisalTotalStructure> appraisalTotalStructure) {
		this.appraisalTotalStructure = appraisalTotalStructure;
	}

	public List<AppraisalTotalWall> getAppraisalTotalWall() {
		return appraisalTotalWall;
	}

	public void setAppraisalTotalWall(List<AppraisalTotalWall> appraisalTotalWall) {
		this.appraisalTotalWall = appraisalTotalWall;
	}

	public List<AppraisalTotalExternal> getAppraisalTotalExternal() {
		return appraisalTotalExternal;
	}

	public void setAppraisalTotalExternal(
			List<AppraisalTotalExternal> appraisalTotalExternal) {
		this.appraisalTotalExternal = appraisalTotalExternal;
	}
	
	public void add(AppraisalTotalStructure appraisalTotalStructure){
		if (!this.appraisalTotalStructure.contains(appraisalTotalStructure)){
			this.appraisalTotalStructure.add(appraisalTotalStructure);
//			appraisalTotalStructure.setAppraisalTotalStructure(this);
		}
	}
	
	public void remove(AppraisalTotalStructure appraisalTotalStructure){
		this.appraisalTotalStructure.remove(appraisalTotalStructure);
//		if (removed) appraisalTotalStructure.setAppraisalTotalStructure((AppraisalTotalStructure)null);
	}
	
	public void add(AppraisalTotalWall appraisalTotalWall){
		if (!this.appraisalTotalWall.contains(appraisalTotalWall)){
			this.appraisalTotalWall.add(appraisalTotalWall);
//			appraisalTotalWall.setAppraisalTotalWall(this);
		}
	}
	
	public void remove(AppraisalTotalWall appraisalTotalWall){
		this.appraisalTotalWall.remove(appraisalTotalWall);
//		if (removed) appraisalTotalWall.setAppraisalTotalWall((AppraisalTotalWall)null);
	}
	
	public void add(AppraisalTotalRoof appraisalTotalRoof){
		if (!this.appraisalTotalRoof.contains(appraisalTotalRoof)){
			this.appraisalTotalRoof.add(appraisalTotalRoof);
//			appraisalTotalRoof.setAppraisalTotalRoof(this);
		}
	}
	
	public void remove(AppraisalTotalRoof appraisalTotalRoof){
		this.appraisalTotalRoof.remove(appraisalTotalRoof);
//		if (removed) appraisalTotalRoof.setAppraisalTotalRoof((AppraisalTotalRoof)null);
	}
	
	public void add(AppraisalTotalExternal appraisalTotalExternal){
		if (!this.appraisalTotalExternal.contains(appraisalTotalExternal)){
			this.appraisalTotalExternal.add(appraisalTotalExternal);
//			appraisalTotalExternal.setAppraisalTotalExternal(this);
		}
	}
	
	public void remove(AppraisalTotalExternal appraisalTotalExternal){
		this.appraisalTotalExternal.remove(appraisalTotalExternal);
//		if (removed) appraisalTotalExternal.setAppraisalTotalExternal((AppraisalTotalExternal)null);
	}
	
}
