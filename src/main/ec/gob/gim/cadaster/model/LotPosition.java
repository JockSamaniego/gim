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
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:38
 */
@Audited
@Entity
@TableGenerator(
	 name="LotPositionGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="LotPosition",
	 initialValue=1, allocationSize=1
)
@NamedQueries({
	@NamedQuery(name="LotPosition.findAll",
			query="select lp from LotPosition lp " +
				"order by lp.name"
	),
	@NamedQuery(name="LotPosition.findByName",
			query="select lp from LotPosition lp where " +
				"lower(lp.name) like lower(concat(:name, '%')) order by lp.name"
	)
	,
	@NamedQuery(name="LotPosition.findAllOrderById",
			query="select lp from LotPosition lp order by lp.id"
	)
})
public class LotPosition {

	@Id
	@GeneratedValue(generator="LotPositionGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(length=25, nullable=false)
	private String name;

	@Column(nullable=false)
	private BigDecimal factor;

	public LotPosition(){
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