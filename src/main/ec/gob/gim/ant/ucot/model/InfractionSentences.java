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
import ec.gob.gim.common.model.Person;

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
	private String description;
		
	@ManyToOne
	@JoinColumn(name="itemcatalog_id")
	private ItemCatalog type;
	
	@ManyToOne
	@JoinColumn(name="infractions_id")
	private Infractions infraction;
	
	@Temporal(TemporalType.TIMESTAMP) 
	private Date creationDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "responsible_id") 
	private Person responsible;
	   
	@JoinColumn(name = "responsible_user")	 
	@Column(length = 100)	 
	private String responsible_user;
	
	//Datos para archivo de documentos............................
	
	@Temporal(TemporalType.TIMESTAMP) 
	private Date archivedDate;
	
	private String archivedHanger;
	
	private String archivedShelf;
	
	private String archivedFolder;
	
	private String archivedContent;
	
	private String archivedDetail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "archived_responsible_id") 
	private Person archivedResponsible;
	   
	@JoinColumn(name = "responsible_user")	 
	@Column(length = 100)	 
	private String archivedResponsible_user;

	//SETTERS AND GETTERS.................................................... 
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

	public Person getResponsible() {
		return responsible;
	}

	public void setResponsible(Person responsible) {
		this.responsible = responsible;
	}

	public String getResponsible_user() {
		return responsible_user;
	}

	public void setResponsible_user(String responsible_user) {
		this.responsible_user = responsible_user;
	}

	public Date getArchivedDate() {
		return archivedDate;
	}

	public void setArchivedDate(Date archivedDate) {
		this.archivedDate = archivedDate;
	}

	public String getArchivedHanger() {
		return archivedHanger;
	}

	public void setArchivedHanger(String archivedHanger) {
		this.archivedHanger = archivedHanger;
	}

	public String getArchivedShelf() {
		return archivedShelf;
	}

	public void setArchivedShelf(String archivedShelf) {
		this.archivedShelf = archivedShelf;
	}

	public String getArchivedFolder() {
		return archivedFolder;
	}

	public void setArchivedFolder(String archivedFolder) {
		this.archivedFolder = archivedFolder;
	}

	public String getArchivedContent() {
		return archivedContent;
	}

	public void setArchivedContent(String archivedContent) {
		this.archivedContent = archivedContent;
	}

	public String getArchivedDetail() {
		return archivedDetail;
	}

	public void setArchivedDetail(String archivedDetail) {
		this.archivedDetail = archivedDetail;
	}

	public Person getArchivedResponsible() {
		return archivedResponsible;
	}

	public void setArchivedResponsible(Person archivedResponsible) {
		this.archivedResponsible = archivedResponsible;
	}

	public String getArchivedResponsible_user() {
		return archivedResponsible_user;
	}

	public void setArchivedResponsible_user(String archivedResponsible_user) {
		this.archivedResponsible_user = archivedResponsible_user;
	}

	
}
	

