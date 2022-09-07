package ec.gob.gim.common.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.security.model.User;

/**
 * @author Jock Samaniego
 * @version 1.0
 * @created 2017-05-02
 */
@Audited
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@TableGenerator(
	 name="AlertTypeGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="AlertType",
	 initialValue=1, allocationSize=1
)

@NamedQueries(value = {		
		
	@NamedQuery(name="AlertType.findAll", 
		query = "select alertType from AlertType alertType order by id desc"),
	@NamedQuery(name="AlertType.findAllByActive", 
		query = "select alertType from AlertType alertType where alertType.isActive = true order by id desc")
		
	}
)

public class AlertType {

	@Id
	@GeneratedValue(generator="AlertTypeGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(length=100)
	private String name;
	
	@Column(length=100)
	private String message;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	private String createDetail;
	
	private Boolean isActive;
	
	private Boolean isToEmit;
	private Boolean isToCollect;
	
	/**
	 * Relationships
	 */
	
	@OneToOne
	private User openUser;
	
	public AlertType(){
		isActive = Boolean.TRUE;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateDetail() {
		return createDetail;
	}

	public void setCreateDetail(String createDetail) {
		this.createDetail = createDetail;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public User getOpenUser() {
		return openUser;
	}

	public void setOpenUser(User openUser) {
		this.openUser = openUser;
	}

	public Boolean getIsToEmit() {
		return isToEmit;
	}

	public void setIsToEmit(Boolean isToEmit) {
		this.isToEmit = isToEmit;
	}

	public Boolean getIsToCollect() {
		return isToCollect;
	}

	public void setIsToCollect(Boolean isToCollect) {
		this.isToCollect = isToCollect;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}