package ec.gob.gim.consession.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

/**
 * @version 1.0
 * @created 02-ago-2013 16:54:33
 */
@Audited
@Entity
@TableGenerator(name = "ConcessionGroupGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "ConcessionGroup", initialValue = 1, allocationSize = 1)
public class ConcessionGroup {

	@Id
	@GeneratedValue(generator = "ConcessionGroupGenerator", strategy = GenerationType.TABLE)
	private Long id;	
	
	@Column(length = 50, nullable = false)
	private String name;
	
	private Boolean isActive;
		
	@OneToMany(mappedBy = "concessionGroup", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<ConcessionPlace> places;
	
	@ManyToOne
	@JoinColumn(name = "clasification_id")
	private ConcessionClasification clasification;

	public ConcessionGroup(){
		places=new ArrayList<ConcessionPlace>();
	}

	public void finalize() throws Throwable {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.toUpperCase();
	}

	public ConcessionClasification getClasification() {
		return clasification;
	}

	public void setClasification(ConcessionClasification clasification) {
		this.clasification = clasification;
	}

	public List<ConcessionPlace> getPlaces() {
		return places;
	}

	public void setPlaces(List<ConcessionPlace> places) {
		this.places = places;
	}
	
	
	public void add(ConcessionPlace cp) {
		if (!places.contains(cp)) {
			cp.setConcessionGroup(this);
			places.add(cp);
		}
	}

	public void remove(ConcessionPlace cp) {
		if (places.remove(cp)) {
			cp.setConcessionGroup((ConcessionGroup) null);
		}
	}
			
}//end ConcessionGroup