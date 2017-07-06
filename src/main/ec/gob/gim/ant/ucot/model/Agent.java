package ec.gob.gim.ant.ucot.model;
 

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Alert;
import ec.gob.gim.common.model.Resident;

@Audited
@Entity
@TableGenerator(
	 name="AgentGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Agent",
	 initialValue=1, allocationSize=1
)
public class Agent {

	@Id
	@GeneratedValue(generator="AgentGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(nullable=false, length=30)
	private String agentCode;
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY)
	@JoinColumn(name="resident_id")
	private Resident resident	;
	
	@OneToMany(mappedBy = "agent", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<Bulletin> bulletins;
	private Boolean isActive;

	public Long getId() {
		return id;
	}


	public List<Bulletin> getBulletins() {
		return bulletins;
	}


	public void setBulletins(List<Bulletin> bulletins) {
		this.bulletins = bulletins;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getAgentCode() {
		return agentCode;
	}


	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}


	public Resident getResident() {
		return resident;
	}


	public void setResident(Resident resident) {
		this.resident = resident;
	}


	public Boolean getIsActive() {
		return isActive;
	}


	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	} 
	
	
}