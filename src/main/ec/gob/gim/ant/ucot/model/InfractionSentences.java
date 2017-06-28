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

	public ItemCatalogx getType() {
		return type;
	}

	public void setType(ItemCatalogx type) {
		this.type = type;
	} 
	
	
}
	
	
	
	
	