package ec.gob.gim.cadaster.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:38
 */
@Audited
@Entity
@TableGenerator(
	 name="LocationGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Location",
	 initialValue=1, allocationSize=1
 )
 public class Location {

	@Id
	@GeneratedValue(generator="LocationGenerator",strategy=GenerationType.TABLE)
	private Long id;

	@Column(length=15)
	private String houseNumber;
	
	@ManyToOne
	@JoinColumn(name="mainStreet_id")
	private BlockLimit mainBlockLimit;
	
	@ManyToOne
	@JoinColumn(name="neighborhood_id")
	private Neighborhood neighborhood;

	public Location(){

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BlockLimit getMainBlockLimit() {
		return mainBlockLimit;
	}

	public void setMainBlockLimit(BlockLimit mainBlockLimit) {
		this.mainBlockLimit = mainBlockLimit;
	}
	
	public Neighborhood getNeighborhood() {		
		return neighborhood;
	}

	public void setNeighborhood(Neighborhood neighborhood) {		
		this.neighborhood = neighborhood;
	}

	/**
	 * @return the houseNumber
	 */
	public String getHouseNumber() {
		return houseNumber;
	}

	/**
	 * @param houseNumber the houseNumber to set
	 */
	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}
	
}//end Location