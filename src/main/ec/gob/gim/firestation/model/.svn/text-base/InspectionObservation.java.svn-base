package ec.gob.gim.firestation.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

@Audited
@Entity
@TableGenerator(name = "InspectionObservationGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "InspectionObservation", initialValue = 1, allocationSize = 1)
public class InspectionObservation {

	@Id
	@GeneratedValue(generator = "InspectionObservationGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date date;

	private String detail;

	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY)
	private TechnicalInformation technicalInformation;

	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY)
	private TransportTechnicalInformation transportTechnicalInformation;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public InspectionObservation() {
		date = new Date();
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public TechnicalInformation getTechnicalInformation() {
		return technicalInformation;
	}

	public void setTechnicalInformation(
			TechnicalInformation technicalInformation) {
		this.technicalInformation = technicalInformation;
	}

	public TransportTechnicalInformation getTransportTechnicalInformation() {
		return transportTechnicalInformation;
	}

	public void setTransportTechnicalInformation(
			TransportTechnicalInformation transportTechnicalInformation) {
		this.transportTechnicalInformation = transportTechnicalInformation;
	}

}
