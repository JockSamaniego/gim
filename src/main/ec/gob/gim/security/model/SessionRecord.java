package ec.gob.gim.security.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

//@Audited
@Entity
@TableGenerator(
		 name="SessionRecordGenerator",
		 table="IdentityGenerator",
		 pkColumnName="name",
		 valueColumnName="value",
		 pkColumnValue="SessionRecord",
		 initialValue=1, 
		 allocationSize=1)
public class SessionRecord {
	@Id
	@GeneratedValue(generator = "SessionRecordGenerator", strategy = GenerationType.TABLE)
	private Long id;
	private String userName;
	@Temporal(TemporalType.DATE)
	private Date date;
	@Temporal(TemporalType.TIME)
	private Date time;
	private String ipAddress;
	
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private SessionRecordType sessionRecordType;
	public Long getId() { 
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public SessionRecordType getSessionRecordType() {
		return sessionRecordType;
	}
	public void setSessionRecordType(SessionRecordType sessionRecordType) {
		this.sessionRecordType = sessionRecordType;
	}
}
