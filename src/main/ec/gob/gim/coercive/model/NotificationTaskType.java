package ec.gob.gim.coercive.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * Tipo de tareas de notificación
 * @author jlgranda
 * @version 1.0
 * @created 23-Nov-2011 12:56:42
 */
@Audited
@Entity
@TableGenerator(
	 name="NotificationTaskTypeGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="NotificationTaskType",
	 initialValue=1, allocationSize=1
)
@NamedQueries(value = {
		@NamedQuery(name = "NotificationTaskType.findByName", query = "SELECT ntt FROM NotificationTaskType ntt "
				+ "WHERE ntt.name=:name "
				+ "ORDER BY ntt.id"),
		@NamedQuery(name = "NotificationTaskType.findAll", query = "SELECT ntt FROM NotificationTaskType ntt "
				+ "ORDER BY ntt.name"),
		@NamedQuery(name = "NotificationTaskType.findBySequence", query = "SELECT ntt FROM NotificationTaskType ntt "
				+ "WHERE ntt.sequence = :sequence " 
				+ "ORDER BY ntt.name")
		})
public class NotificationTaskType {
	
	public static final Long FIRST = 1L;

	@Id
	@GeneratedValue(generator="NotificationTaskTypeGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private Long sequence;
	
	@Column(length=255, nullable=false)
	private String name;
	private String description;
	private String reports;
	
	/**
	 * Relationships
	 */
	
	
	public NotificationTaskType(){

	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	public String getReports() {
		return reports;
	}

	public void setReports(String reports) {
		this.reports = reports;
	}
	
	public String toString(){
		return this.getName().toUpperCase();
	}
	
	
}//end NotificationTaskType