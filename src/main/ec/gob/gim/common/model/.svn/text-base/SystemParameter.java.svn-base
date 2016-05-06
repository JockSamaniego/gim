package ec.gob.gim.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.envers.Audited;

/**
 *
 * @author gerson
 */
@Audited
@Entity
@NamedQueries(value={
    @NamedQuery(name = "SystemParameter.findAll", query = "select o from SystemParameter o"),
    @NamedQuery(name="SystemParameter.findByName", query="select o from SystemParameter o where o.name = :name")
})
public class SystemParameter {
    @Id
    private String name;
    @Column(nullable=false)
    private String value;
    @Column(nullable=false)
    private String className;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
