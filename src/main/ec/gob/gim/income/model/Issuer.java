package ec.gob.gim.income.model;

import java.util.Date;

import javax.persistence.Entity;
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

import ec.gob.gim.revenue.model.Entry;

@Audited
@Entity
@TableGenerator(
		 name="IssuerGenerator",
		 table="IdentityGenerator",
		 pkColumnName="name",
		 valueColumnName="value",
		 pkColumnValue="Issuer",
		 initialValue=1, allocationSize=1
	 )
@NamedQueries(value={
	@NamedQuery(name="Issuer.findTaxPayerRecordByEntryId",
				query="select o.taxpayerRecord from Issuer o where o.entry.id = :entryId and o.isActive = true"),
	@NamedQuery(name="Issuer.findByEntry", query="select i from Issuer i where i.entry.id=:id"),
	@NamedQuery(name="Issuer.findActiveTaxpayerRecordByEntryId", query="select i.taxpayerRecord from Issuer i where i.entry.id = :entryId and i.isActive = true")
})
public class Issuer {
	@Id
	@GeneratedValue(generator="IssuerGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	private Boolean isActive;
	
	@ManyToOne
	@JoinColumn(name = "taxpayerRecord_id")
	private TaxpayerRecord taxpayerRecord;	
	
	@ManyToOne
	@JoinColumn(name = "entry_id")
	private Entry entry;	
	
	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public TaxpayerRecord getTaxpayerRecord() {
		return taxpayerRecord;
	}

	public void setTaxpayerRecord(TaxpayerRecord taxpayerRecord) {
		this.taxpayerRecord = taxpayerRecord;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
