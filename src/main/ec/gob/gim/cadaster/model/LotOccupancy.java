package ec.gob.gim.cadaster.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * @author jock
 * @version 1.0
 * @created 15-Sep-2022
 */
@Audited
@Entity
@TableGenerator(
	 name="LotOccupancyGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="LotOccupancy",
	 initialValue=1, allocationSize=1
)
@NamedQueries({
	@NamedQuery(name="LotOccupancy.findAll",
			query="select lo from LotOccupancy lo " +
				"order by lo.name"
	),
	@NamedQuery(name="LotOccupancy.findByName",
			query="select lo from LotOccupancy lo where " +
				"lower(lo.name) like lower(concat(:name, '%')) order by lo.name"
	)
	,
	@NamedQuery(name="LotOccupancy.findAllOrderById",
			query="select lo from LotOccupancy lo order by lo.id"
	)
})
public class LotOccupancy {

	@Id
	@GeneratedValue(generator="LotOccupancyGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(length=25, nullable=false)
	private String name;

	@Column(nullable=false)
	private BigDecimal factor;

	public LotOccupancy(){
		if (factor == null)
			factor = BigDecimal.ZERO;
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

	public BigDecimal getFactor() {
		return factor;
	}

	public void setFactor(BigDecimal factor) {
		this.factor = factor;
	}

}