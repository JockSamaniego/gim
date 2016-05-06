package ec.gob.gim.income.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

@Audited
@Entity
@TableGenerator(
		 name="ReceiptAuthorizationGenerator",
		 table="IdentityGenerator",
		 pkColumnName="name",
		 valueColumnName="value",
		 pkColumnValue="ReceiptAuthorization",
		 initialValue=1, allocationSize=1
	 )
@NamedQueries(value={
		@NamedQuery(name="ReceiptAuthorization.findActiveByTaxpayerRecordId",
					query="SELECT o FROM ReceiptAuthorization o " +
						  "   WHERE " +
						  "	  o.taxpayerRecord.id = :taxpayerRecordId AND " +
						 // "   o.receiptType.id = :receiptTypeId AND " +
						  "   :date BETWEEN o.startDate AND o.endDate"),
						  
		@NamedQuery(name="ReceiptAuthorization.findByNumber",
		 			query="SELECT o FROM ReceiptAuthorization o WHERE o.authorizationNumber = :number"),
		 			
		@NamedQuery(name="ReceiptAuthorization.findById",
					query="SELECT o FROM ReceiptAuthorization o WHERE o.id = :id") 
})
public class ReceiptAuthorization {
	
	@Id
	@GeneratedValue(generator="ReceiptAuthorizationGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(length=20, nullable=false)
	private String authorizationNumber;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date startDate;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date endDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private TaxpayerRecord taxpayerRecord;
		
	public String getAuthorizationNumber() {
		return authorizationNumber;
	}

	public void setAuthorizationNumber(String authorizationNumber) {
		this.authorizationNumber = authorizationNumber;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTaxpayerRecord(TaxpayerRecord taxpayerRecord) {
		this.taxpayerRecord = taxpayerRecord;
	}

	public TaxpayerRecord getTaxpayerRecord() {
		return taxpayerRecord;
	}
	
}
