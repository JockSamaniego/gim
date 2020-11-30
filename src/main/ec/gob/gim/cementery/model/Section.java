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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.LegalEntityType;

/**
 * @author gerson
 * @version 1.0
 * @created 25-Nov-2011 11:21:43 AM
 */
@Audited
@Entity
@TableGenerator(
	name="SectionGenerator",
	table="IdentityGenerator",
	pkColumnName="name",
	valueColumnName="value",
	pkColumnValue="Section",
	initialValue=1, allocationSize=1
)

@NamedQueries(value = { 
		 @NamedQuery(name = "Section.findAll", 
				 	query = "SELECT section FROM Section section " +
				 			"order by section.name"), 

		 @NamedQuery(name = "Section.findByCementery", 
		 			query = "SELECT section.name FROM Section section " +
		 					"where section.cementery = :cementery ORDER BY section.name ASC") 

})


public class Section {

	@Id
	@GeneratedValue(generator="SectionGenerator",strategy=GenerationType.TABLE)
	private Long id;
	@Column(length = 20)
	private String code; 
	@Column(length = 60)
	private String name;
	private int unitsNumber;
	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private LegalEntityType property;

	@OneToMany(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "section_id")
	private List<Unit> units;
	
	@ManyToOne
	@JoinColumn(name = "cementery_id")
	private Cementery cementery;

	public Section(){
		this.units = new ArrayList<Unit>();
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

	public int getUnitsNumber() {
		return unitsNumber;
	}

	public void setUnitsNumber(int unitsNumber) {
		this.unitsNumber = unitsNumber;
	}

	public LegalEntityType getProperty() {
		return property;
	}

	public void setProperty(LegalEntityType property) {
		this.property = property;
	}

	public List<Unit> getUnits() {
		return units;
	}

	public void setUnits(List<Unit> units) {
		this.units = units;
	}

	public void add(Unit unit){
		if (!this.units.contains(unit)){
			this.units.add(unit);
			unit.setSection(this);
		}
	}
	
	public void remove(Unit unit){
		boolean removed = this.units.remove(unit);
		if (removed) unit.setSection((Section)null);
	}
	
	public Cementery getCementery() {
		return cementery;
	}
	
	public void setCementery(Cementery cementery) {
		this.cementery = cementery;
	}


}//end Section