package ec.gob.gim.ant.ucot.model;
 

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

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
	@GeneratedValue(generator="BulletinGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(nullable=false, length=30)
	private String agentCode;
	
	@ManyToOne
	@JoinColumn(name="resident_id")
	private Resident resident	;


	public Long getId() {
		return id;
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
}