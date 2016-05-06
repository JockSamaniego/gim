package ec.gob.gim.cementery.model;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.LegalEntityType;
import ec.gob.gim.common.model.Resident;

/**
 * @author gerson
 * @version 1.0
 * @created 25-Nov-2011 11:21:42 AM
 */
@Audited
@Entity
@TableGenerator(
	 name="CementeryGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Cementery",
	 initialValue=1, allocationSize=1
)

@NamedQueries(value = { 
		 @NamedQuery(name = "Cementery.findAll", 
				 	query = "SELECT cementery FROM Cementery cementery " +
				 			"order by cementery.name"), 

		 @NamedQuery(name = "Cementery.findById", 
		 			query = "SELECT cementery FROM Cementery cementery " +
		 					"where cementery.id = :id") 
 
})

public class Cementery {

	@Id
	@GeneratedValue(generator="CementeryGenerator",strategy=GenerationType.TABLE)
	private Long id;
	@Column(length = 5)
	private String code;
	@Column(length = 40)
	private String name;
	@Column(length = 60)
	private String address;
	@OneToMany(cascade = CascadeType.ALL)
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "cementery_id")
	private List<Section> sections;
	@OneToMany(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "cementery_id")
	private List<Death> deaths;
	@OneToOne(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "manager_id")
	private Resident manager;
	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private LegalEntityType property;

	public Cementery(){
		this.sections = new ArrayList<Section>();
		
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	public void add(Section section){
		if (!this.sections.contains(section)){
			this.sections.add(section);
			section.setCementery(this);
		}
	}
	
	public void remove(Section section){
		boolean removed = this.sections.remove(section);
		if (removed) section.setCementery((Cementery)null);
	}
	
	public List<Death> getDeaths() {
		return deaths;
	}

	public void setDeaths(List<Death> deaths) {
		this.deaths = deaths;
	}

	public Resident getManager() {
		return manager;
	}

	public void setManager(Resident manager) {
		this.manager = manager;
	}

	public LegalEntityType getProperty() {
		return property;
	}

	public void setProperty(LegalEntityType property) {
		this.property = property;
	}
}//end Cementery