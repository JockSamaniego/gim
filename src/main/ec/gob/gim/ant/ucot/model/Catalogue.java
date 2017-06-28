package ec.gob.gim.ant.ucot.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;

/**
 * 
 * @author mack
 * @date 2017-06-28T09:32:22
 */
@Entity
@TableGenerator(name = "CatalogueGenerator", table = "IdentityGenerator", 
pkColumnName = "name", valueColumnName = "value", pkColumnValue = "Catalogue", initialValue = 1, allocationSize = 1)
public class Catalogue {

	@Id
	@GeneratedValue(generator = "CatalogueGenerator", strategy = GenerationType.TABLE)
	private Long id; 
	
	@Column(length = 120)
	private String name;

	@Column(length = 255)
	private String description;

	public Catalogue() {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
