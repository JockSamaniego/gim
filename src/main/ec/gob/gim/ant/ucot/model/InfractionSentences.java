package ec.gob.gim.ant.ucot.model;
 

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.ItemCatalog;

/**
 * 
 * @author mack
 * @date 2017-06-28T12:19
 * 
 */
@Audited
@Entity
@TableGenerator(
	 name="InfractionSentencesGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="InfractionSentences",
	 initialValue=1, allocationSize=1
)
@NamedQueries(value = {
		@NamedQuery(name = "sentence.findByInfractionId", query = "Select s from InfractionSentences s WHERE s.infraction.id = :infractionId ORDER BY s.id ASC")})
public class InfractionSentences {

	@Id
	@GeneratedValue(generator="InfractionSentencesGenerator",strategy=GenerationType.TABLE)
	private Long id;

	@Column(length=30)
	private String processNumber;

	@Column(length=100)
	private String judge;
	
	@Column(length=100)
	private String description;
		
	@ManyToOne
	@JoinColumn(name="itemcatalog_id")
	private ItemCatalog type;
	
	@ManyToOne
	@JoinColumn(name="infractions_id")
	private Infractions infraction;
	
	@Temporal(TemporalType.DATE)
	private Date creationDate;
	 
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProcessNumber() {
		return processNumber;
	}

	public void setProcessNumber(String processNumber) {
		this.processNumber = processNumber;
	}

	public String getJudge() {
		return judge;
	}

	public void setJudge(String judge) {
		this.judge = judge;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ItemCatalog getType() {
		return type;
	}

	public void setType(ItemCatalog type) {
		this.type = type;
	}

	public Infractions getInfraction() {
		return infraction;
	}

	public void setInfraction(Infractions infraction) {
		this.infraction = infraction;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	} 
	
}
	

