package ec.gob.gim.firestation.model;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;
import org.hibernate.annotations.Cascade;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;


/**
 * @author gerson
 * @version 1.0
 * @created 12-Dic-2011 10:14:56
 */
@Audited
@Entity
@TableGenerator(name = "FireManGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "FireMan", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {@NamedQuery(name = "FireMan.findAll", query = "SELECT f FROM FireMan f order by f.name")})
public class FireMan {

	@Id
	@GeneratedValue(generator = "FireManGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length = 50, nullable = false)
	private String name;
	
	@ManyToOne
	private Resident fireman;
	
	public FireMan() {
		
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
		this.name = name.toUpperCase();
	}
	
		
	public Resident getFireMan() {
		return fireman;
	}

	public void setFireMan(Resident fireman) {
		this.fireman = fireman;
	}
	
}// end GroupStores