package ec.gob.gim.cementery.model;

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
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;

import org.hibernate.envers.Audited;

/**
 * @author gerson
 * @version 1.0
 * @created 25-Nov-2011 11:21:43 AM
 */
@Audited
@Entity
@TableGenerator(
	 name="UnitGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Unit",
	 initialValue=1, allocationSize=1
)

@NamedQueries(value = { 
		 @NamedQuery(name = "Unit.findAll", 
				 	query = "SELECT unit FROM Unit unit " +
				 			"order by unit.code"), 

		 @NamedQuery(name = "Unit.findBySectionAndUnitType", 
		 			query = "SELECT unit FROM Unit unit " +
		 					"where unit.section = :section and unit.unitType = :unitType order by unit.code"), 

		 @NamedQuery(name = "Unit.findUnitsInSection", 
		 			query = "SELECT distinct unit.unitType FROM Unit unit " +
				 			"where unit.section = :section")
})


public class Unit {

	@Id
	@GeneratedValue(generator="UnitGenerator",strategy=GenerationType.TABLE)
	private Long id;
	@Column(length = 5)
	private String code;
	@ManyToOne
	private Section section;
	@OneToOne
	private UnitType unitType;
	@Enumerated(EnumType.STRING)
	private UnitStatus unitStatus;

	@OneToOne(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL)
	@JoinColumn(name="death_id")
	private Death currentDeath;
	
	@OneToMany(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL)
	@JoinColumn(name="unit_id")
	private List<Death> deathsHistory;
	
	public Unit(){

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

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public UnitType getUnitType() {
		return unitType;
	}

	public void setUnitType(UnitType unitType) {
		this.unitType = unitType;
	}

	public UnitStatus getUnitStatus() {
		return unitStatus;
	}

	public void setUnitStatus(UnitStatus unitStatus) {
		this.unitStatus = unitStatus;
	}

	public void setDeath(Death currentDeath) {
		this.currentDeath = currentDeath;
	}

	public Death getCurrentDeath() {
		return currentDeath;
	}

	public void setCurrentDeath(Death currentDeath) {
		this.currentDeath = currentDeath;
	}

	public List<Death> getDeathsHistory() {
		return deathsHistory;
	}

	public void setDeathsHistory(List<Death> deathsHistory) {
		this.deathsHistory = deathsHistory;
	}

}//end Unit