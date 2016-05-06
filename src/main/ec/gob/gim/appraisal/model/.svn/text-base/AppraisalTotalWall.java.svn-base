package ec.gob.gim.appraisal.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.cadaster.model.WallMaterial;

/**
 * @author IML
 */
@Audited
@Entity
@TableGenerator(
	 name="AppraisalTotalWallGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="AppraisalTotalWall",
	 initialValue=1, allocationSize=1
)

public class AppraisalTotalWall {

	@Id
	@GeneratedValue(generator="AppraisalTotalWallGenerator",strategy=GenerationType.TABLE)
	private Long id;
	private BigDecimal total;
	
	@Enumerated(EnumType.STRING)
	@Column(length=25)
	private WallMaterial wallMaterial;
	
	@OneToMany(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "AppraisalTotalWall_id")
	@OrderBy("id")
	private List<AppraisalItemWall> appraisalItemsWall;

	@ManyToOne
	private AppraisalPeriod appraisalPeriod;
	
	public AppraisalTotalWall() {
		this.appraisalItemsWall = new ArrayList<AppraisalItemWall>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public WallMaterial getWallMaterial() {
		return wallMaterial;
	}

	public void setWallMaterial(WallMaterial wallMaterial) {
		this.wallMaterial = wallMaterial;
	}

	public List<AppraisalItemWall> getAppraisalItemsWall() {
		return appraisalItemsWall;
	}

	public void setAppraisalItemsWall(List<AppraisalItemWall> appraisalItemsWall) {
		this.appraisalItemsWall = appraisalItemsWall;
	}

	public AppraisalPeriod getAppraisalPeriod() {
		return appraisalPeriod;
	}

	public void setAppraisalPeriod(AppraisalPeriod appraisalPeriod) {
		this.appraisalPeriod = appraisalPeriod;
	}

	public void add(AppraisalItemWall appraisalItemWall){
		if (!this.appraisalItemsWall.contains(appraisalItemWall)){
			this.appraisalItemsWall.add(appraisalItemWall);
			appraisalItemWall.setAppraisalTotalWall(this);
		}
	}
	
	public void remove(AppraisalItemWall appraisalItemWall){
		boolean removed = this.appraisalItemsWall.remove(appraisalItemWall);
		if (removed) appraisalItemWall.setAppraisalTotalWall((AppraisalTotalWall)null);
	}
	
}
