package ec.gob.gim.revenue.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

import ec.gob.gim.revenue.model.adjunct.ValuePair;

@Audited
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="residentType", discriminatorType=DiscriminatorType.STRING, length=3)
@TableGenerator(
		name = "AdjunctGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "Adjunct", 
		initialValue = 1, allocationSize = 1
)
@NamedQueries(value={
		@NamedQuery(name="Adjunct.findById",
					query="select o from Adjunct o where o.id = :id"),
		@NamedQuery(name="Adjunct.findByCode",
				query="select o from Adjunct o where o.code = :code")
})
public abstract class Adjunct {
	@Id
	@GeneratedValue(generator = "AdjunctGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@Column(length=50)
	private String code;

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

	public abstract List<ValuePair> getDetails();
	
}
