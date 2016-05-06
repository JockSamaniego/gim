package ec.gob.gim.firestation.model;


import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.commercial.model.Local;
import ec.gob.gim.common.model.Resident;


/**
 * @author gerson
 * @version 1.0
 * @created 12-Dic-2011 10:14:56
 */
@Audited
@Entity
@TableGenerator(name = "InspectionsGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "Inspections", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {@NamedQuery(name = "Inspections.findAll", query = "SELECT inspections FROM Inspections inspections order by inspections.id")})
public class Inspections {

	@Id
	@GeneratedValue(generator = "InspectionsGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="local_id")
	private Local local;
	
	@OneToMany(mappedBy = "inspections", cascade = CascadeType.ALL)
	private List<TechnicalInformation> technicalInformations;
	
	@OneToMany(mappedBy = "inspections", cascade = CascadeType.ALL)
	private List<TransportTechnicalInformation> transportTechnicalInformations;
	
	@ManyToOne
	@JoinColumn(name="inspector_id")
	private Resident inspector;
	
	@Temporal(TemporalType.DATE)
	private Date inspectionsDate;
		
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private InspectionsState inspectionsState;

	public Inspections() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Local getLocal() {
		return local;
	}
	
	public void setLocal(Local local) {
		this.local = local;
	}
	
	public Date getInspectionsDate() {
		return inspectionsDate;
	}
	public void setInspectionsDate(Date inspectionsDate) {
		this.inspectionsDate = inspectionsDate;
	}
	
	//public Boolean getIsActive() {
	//	return isActive;
	//}

	//public void setIsActive(Boolean isActive) {
	//	this.isActive = isActive;
	//}
	
	public Resident getInspector() {
		return inspector;
	}

	public void setInspector(Resident inspector) {
		this.inspector = inspector;
	}

	//public String getName() {
	//	return name;
	//}

	//public void setName(String name) {
	//	this.name = name.toUpperCase();
	//}

	public InspectionsState getInspectionsState() {
		return inspectionsState;
	}

	public void setInspectionsState(InspectionsState inspectionsState) {
		this.inspectionsState = inspectionsState;
	}


	
}// end Inspections
