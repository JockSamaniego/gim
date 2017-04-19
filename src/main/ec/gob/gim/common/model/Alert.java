package ec.gob.gim.common.model;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.security.model.User;

/**
 * @author GADM-Loja
 * @version 1.0
 * @created 2013-05-05
 */
@Audited
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@TableGenerator(
	 name="AlertGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Alert",
	 initialValue=1, allocationSize=1
)

@NamedQueries(value = {
	@NamedQuery(name="Alert.findAll", 
		query = "select alert from Alert alert order by id desc"),
		
	@NamedQuery(name="Alert.findAllByActive", 
		query = "select alert from Alert alert where alert.isActive = :isActive order by id desc"),
		
	@NamedQuery(name="Alert.findPendingAlertsByResidentId", 
		query = "select alert from Alert alert where alert.isActive = true and alert.resident.id = :residentId order by id desc"),
	
	@NamedQuery(name="Alert.findPendingAlertsByResidentIdNum", 
	query = "select alert from Alert alert where alert.isActive = true and alert.resident.identificationNumber = :residentIdNum"),

	@NamedQuery(name="Alert.findPendingAlertsByResidentName", 
	query = "select alert from Alert alert where alert.isActive = true and alert.resident.name = :residentName")
	}
)

public class Alert {

	@Id
	@GeneratedValue(generator="AlertGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(length=30)
	private String department;
	
	@Column(length=30)
	private String closeDepartment;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date openDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date closeDate;

	private String openDetail;
	
	private String closeDetail;
	
	private Boolean isActive;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	private AlertPriority priority;

	
	/**
	 * Relationships
	 */
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY)
	private Resident resident;
	
	@OneToOne
	private User openUser;
	
	@OneToOne
	private User closeUser;
	
	public Alert(){
		isActive = Boolean.TRUE;
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
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * @return the closeDepartment
	 */
	public String getCloseDepartment() {
		return closeDepartment;
	}

	/**
	 * @param closeDepartment the closeDepartment to set
	 */
	public void setCloseDepartment(String closeDepartment) {
		this.closeDepartment = closeDepartment;
	}

	/**
	 * @return the openDate
	 */
	public Date getOpenDate() {
		return openDate;
	}

	/**
	 * @param openDate the openDate to set
	 */
	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	/**
	 * @return the closeDate
	 */
	public Date getCloseDate() {
		return closeDate;
	}

	/**
	 * @param closeDate the closeDate to set
	 */
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	/**
	 * @return the openDetail
	 */
	public String getOpenDetail() {
		return openDetail;
	}

	/**
	 * @param openDetail the openDetail to set
	 */
	public void setOpenDetail(String openDetail) {
		this.openDetail = openDetail;
	}

	/**
	 * @return the closeDetail
	 */
	public String getCloseDetail() {
		return closeDetail;
	}

	/**
	 * @param closeDetail the closeDetail to set
	 */
	public void setCloseDetail(String closeDetail) {
		this.closeDetail = closeDetail;
	}

	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the resident
	 */
	public Resident getResident() {
		return resident;
	}

	/**
	 * @param resident the resident to set
	 */
	public void setResident(Resident resident) {
		this.resident = resident;
	}

	/**
	 * @return the openUser
	 */
	public User getOpenUser() {
		return openUser;
	}

	/**
	 * @param openUser the openUser to set
	 */
	public void setOpenUser(User openUser) {
		this.openUser = openUser;
	}

	/**
	 * @return the closeUser
	 */
	public User getCloseUser() {
		return closeUser;
	}

	/**
	 * @param closeUser the closeUser to set
	 */
	public void setCloseUser(User closeUser) {
		this.closeUser = closeUser;
	}

	public AlertPriority getPriority() {
		return priority;
	}

	public void setPriority(AlertPriority priority) {
		this.priority = priority;
	}

}