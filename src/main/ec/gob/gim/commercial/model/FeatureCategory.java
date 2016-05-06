package ec.gob.gim.commercial.model;

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
 * @author richard
 * @version 1.0
 * @created 05-nov-2013 08:36:22
 */
@Audited
@Entity
@TableGenerator(name = "FeatureCategoryGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "FeatureCategory", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { @NamedQuery(name = "FeatureCategory.findAllActive", query = "select fc from FeatureCategory fc where fc.isActive = TRUE order by fc.name") })
public class FeatureCategory {

	@Id
	@GeneratedValue(generator = "FeatureCategoryGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length = 80)
	private String name;

	private Boolean isActive;

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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}