package ec.gob.gim.revenue.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;


@Audited
@Entity
@TableGenerator(name="ExemptionTypeGenerator",
				pkColumnName="name",
				pkColumnValue="ExemptionType",
				table="IdentityGenerator",
				valueColumnName="value",
				allocationSize = 1, initialValue = 1)
@NamedQueries({
	@NamedQuery(name = "ExemptionType.findAll", 
			query = "select e from ExemptionType e " +
					"order by e.id"),
	@NamedQuery(name = "ExemptionType.findByFiscalPeriod", 
			query = "select e from Exemption e " +
					"where e.fiscalPeriod.id =:fiscalPeriodId")
	})
public class ExemptionType {
	
	@Id
	@GeneratedValue(generator="ExemptionTypeGenerator", strategy=GenerationType.TABLE)
	private Long id;

	private String name;
	
	public ExemptionType(){

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

}
