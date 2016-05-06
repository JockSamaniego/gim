package ec.gob.gim.appraisal.model;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * @author IML
 */
@Audited
@Entity
@TableGenerator(
	 name="AppraisalItemBaseGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="AppraisalItemBase",
	 initialValue=1, allocationSize=1
)

@NamedQueries({
		@NamedQuery(name = "AppraisalItemBase.findAll", 
				query = "select aIBase from AppraisalItemBase aIBase " +
						"WHERE lower(aIBase.name) LIKE lower(concat(:aIBaseName,'%')) order by aIBase.name "),
		@NamedQuery(name="AppraisalItemBase.findByName", 
				query="select appraisalItemBase from AppraisalItemBase appraisalItemBase " +
						"where lower(appraisalItemBase.name) like lower(concat(:name,'%')) order by appraisalItemBase.name")
		})
public class AppraisalItemBase {

	@Id
	@GeneratedValue(generator="AppraisalItemBaseGenerator",strategy=GenerationType.TABLE)
	private Long id;
	@Column(length=100)
	private String name;
	@Column(length=10)
	private String unitMed;
	private BigDecimal coste;

	@ManyToOne
	@JoinColumn(name="appraisalItemType_id")
	private AppraisalItemType appraisalItemType;

	public AppraisalItemBase(){

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnitMed() {
		return unitMed;
	}

	public void setUnitMed(String unitMed) {
		this.unitMed = unitMed;
	}

	public BigDecimal getCoste() {
		return coste;
	}

	public void setCoste(BigDecimal coste) {
		this.coste = coste;
	}

	public AppraisalItemType getAppraisalItemType() {
		return appraisalItemType;
	}

	public void setAppraisalItemType(AppraisalItemType appraisalItemType) {
		this.appraisalItemType = appraisalItemType;
	}

}
