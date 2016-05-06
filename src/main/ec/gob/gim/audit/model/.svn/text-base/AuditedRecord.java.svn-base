package ec.gob.gim.audit.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AuditedRecord {
	private Long id;
	private Date date;
	private Date time;
	private Integer transactionType;
	private String username;
	
	private Map<String, Object> values;

	public AuditedRecord() {
		values = new HashMap<String, Object>();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Integer getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Object getValue(String fieldName){
		return values.get(fieldName);
	}
	public Object addValue(String fieldName, Object value){
		return values.put(fieldName, value);
	}
}
