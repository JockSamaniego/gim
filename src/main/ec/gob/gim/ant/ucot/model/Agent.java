
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
import ec.gob.gim.common.model.Resident;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

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
@NamedQueries(value = {
		@NamedQuery(name = "Agent.FindByCode", query = "Select a from Agent a where a.agentCode=:code"),
		@NamedQuery(name = "Agent.FindByResident", query = "Select a from Agent a where a.resident.id=:residentid")})

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
	

	private boolean active;

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


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}


	public List<Bulletin> getBulletins() {
		return bulletins;
	}


	public void setBulletins(List<Bulletin> bulletins) {
		this.bulletins = bulletins;
	} 
	
	
}
