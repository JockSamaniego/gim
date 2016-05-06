package ec.gob.gim.waterservice.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

import ec.gob.gim.cadaster.model.Street;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:28
 */

@Audited
@Entity
@TableGenerator(
	 name="ConnectionSiteGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="ConnectionSite",
	 initialValue=1, allocationSize=1
)
public class ConnectionSite {

	@Id
	@GeneratedValue(generator="ConnectionSiteGenerator",strategy=GenerationType.TABLE)
	private Long id;
	@Column(length=255)
	private String observations;

	@ManyToOne
	@JoinColumn(name="street_id")
	private Street street;

	public ConnectionSite(){

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public Street getStreet() {
		return street;
	}

	public void setStreet(Street street) {
		this.street = street;
	}
}//end ConnectionSite