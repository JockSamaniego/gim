package ec.gob.gim.revenue.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import org.hibernate.envers.Audited;

@Audited
@Entity
@TableGenerator(name = "FinancialInstitutionGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "FinancialInstitution", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "FinancialInstitution.findAll", query = "select f from FinancialInstitution f order by f.name"),
		@NamedQuery(name = "FinancialInstitution.findByType", query = "select f from FinancialInstitution f where f.financialInstitutionType = :type"),
		@NamedQuery(name = "FinancialInstitution.findById", query = "select f from FinancialInstitution f where f.id = :id")})
public class FinancialInstitution {

	@Id
	@GeneratedValue(generator = "FinancialInstitutionGenerator", strategy = GenerationType.TABLE)
	private Long id;

	private String name;

	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private FinancialInstitutionType financialInstitutionType;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setFinancialInstitutionType(
			FinancialInstitutionType financialInstitutionType) {
		this.financialInstitutionType = financialInstitutionType;
	}

	public FinancialInstitutionType getFinancialInstitutionType() {
		return financialInstitutionType;
	}

    public int hashCode() {
        return (int) id.intValue();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
        	return true;
        }
        
        if (!(obj instanceof FinancialInstitution)) {
        	return false;
        }
        
        FinancialInstitution financialInstitution = (FinancialInstitution) obj;
        return financialInstitution.id.longValue() == id.longValue();
    }
}
