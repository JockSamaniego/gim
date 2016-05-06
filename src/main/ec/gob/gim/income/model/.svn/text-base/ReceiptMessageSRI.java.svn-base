package ec.gob.gim.income.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

@Audited
@Entity
@TableGenerator(name = "ReceiptMessageSRIGenerator", 
				table = "IdentityGenerator", 
				pkColumnName = "name", 
				valueColumnName = "value", 
				pkColumnValue = "ReceiptMessageSRI", 
				initialValue = 1, 
				allocationSize = 1)

public class ReceiptMessageSRI {
	@Id
	@GeneratedValue(generator = "ReceiptMessageSRIGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	private Date date;

	@Column(length=150)
	private String message;
	
	@Column(length=200)
	private String aditionalInfo;
	
	@Column(length=10)
	private String sriCode;
	
	@ManyToOne
	@JoinColumn(name = "receipt_id")
	private Receipt receipt;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAditionalInfo() {
		return aditionalInfo;
	}

	public void setAditionalInfo(String aditionalInfo) {
		this.aditionalInfo = aditionalInfo;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Receipt getReceipt() {
		return receipt;
	}

	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
	}

}