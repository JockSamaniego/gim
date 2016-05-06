package ec.gob.gim.income.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import ec.gob.gim.common.model.FinancialStatus;
import ec.gob.gim.revenue.model.MunicipalBond;

@Audited
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"receiptType_id", "authorizationNumber", "branch", "till", "sequential", "versionReceipt"})) 
@TableGenerator(name = "ReceiptGenerator", 
				table = "IdentityGenerator", 
				pkColumnName = "name", 
				valueColumnName = "value", 
				pkColumnValue = "Receipt", 
				initialValue = 1, 
				allocationSize = 1)

@NamedQueries({
	@NamedQuery(name="Receipt.setAsValid",
			    query="UPDATE Receipt r SET r.municipalBond=r.reversedMunicipalBond, r.reversedMunicipalBond=null WHERE r.reversedMunicipalBond.id = :municipalBondId"),
    @NamedQuery(name="Receipt.setAsVoid",
		    query="UPDATE Receipt r SET r.reversedMunicipalBond=r.municipalBond, r.municipalBond=null WHERE r.municipalBond.id = :municipalBondId"),
	@NamedQuery(name  = "Receipt.findById", 
				query = "SELECT r FROM Receipt r " +
						" LEFT JOIN FETCH r.receiptType rt " +
						" LEFT JOIN FETCH r.receiptAuthorization ra " +
						" LEFT JOIN FETCH ra.taxpayerRecord tr " +
						" WHERE r.id = :receiptId "),
	@NamedQuery(name="Receipt.findById", query="SELECT r FROM Receipt r WHERE r.id = :receiptId "),
	@NamedQuery(name="Receipt.findByMunicipalBondId", query="SELECT r FROM Receipt r WHERE r.municipalBond.id = :municipalBondId "),
	@NamedQuery(name="Receipt.findReversedByMunicipalBondId", query="SELECT r FROM Receipt r WHERE r.reversedMunicipalBond.id = :municipalBondId ORDER BY r.id DESC"),
	@NamedQuery(name="Receipt.findByDateAndStatus", 
				query = "SELECT DISTINCT r FROM Receipt r " +
						" LEFT JOIN FETCH r.receiptType rt " +
						" LEFT JOIN FETCH r.municipalBond mb " +
						" LEFT JOIN FETCH mb.adjunct a " +
						" LEFT JOIN FETCH mb.items i " +
						" LEFT JOIN FETCH i.entry e " +
						" LEFT JOIN FETCH mb.resident re " +
						" LEFT JOIN FETCH r.receiptAuthorization ra " +
						" LEFT JOIN FETCH ra.taxpayerRecord tr " +
						" WHERE r.status = :status " +
						" AND r.date BETWEEN :startDate AND :endDate " +
						" ORDER BY r.id"),
			@NamedQuery(name="Receipt.findReversedByDateAndStatus", 
				query = "SELECT DISTINCT r FROM Receipt r " +
						" LEFT JOIN FETCH r.receiptType rt " +
						" LEFT JOIN FETCH r.reversedMunicipalBond mb " +
						" LEFT JOIN FETCH mb.items i " +
						" LEFT JOIN FETCH mb.adjunct a " +
						" LEFT JOIN FETCH i.entry e " +
						" LEFT JOIN FETCH mb.resident re " +
						" LEFT JOIN FETCH r.receiptAuthorization ra " +
						" LEFT JOIN FETCH ra.taxpayerRecord tr " +
						" WHERE r.status = :status " +
						" AND r.date BETWEEN :startDate AND :endDate " +
						" ORDER BY r.id"),
			@NamedQuery(name="Receipt.findBetweenDatesAndStatusElectronicReceiptOriginal", 
			query = "SELECT DISTINCT r FROM Receipt r " +
					" LEFT JOIN FETCH r.receiptType rt " +
					" LEFT JOIN FETCH r.reversedMunicipalBond mb " +
					" LEFT JOIN FETCH mb.items i " +
					" LEFT JOIN FETCH mb.adjunct a " +
					" LEFT JOIN FETCH i.entry e " +
					" LEFT JOIN FETCH mb.resident re " +
					" LEFT JOIN FETCH r.receiptAuthorization ra " +
					" LEFT JOIN FETCH ra.taxpayerRecord tr " +
					" WHERE r.statusElectronicReceipt = :statusElectronicReceipt " +
					" AND r.date >= :startDate AND r.date <= :endDate " +
					" ORDER BY r.id"),
			@NamedQuery(name="Receipt.findBetweenDatesAndStatusElectronicReceipt", 
			query = "SELECT r FROM Receipt r " +
					" WHERE r.statusElectronicReceipt = :statusElectronicReceipt " +
					" AND r.date >= :startDate AND r.date <= :endDate " +
					" ORDER BY r.id"),
			@NamedQuery(name="Receipt.findFullByIds", 
			query = "SELECT DISTINCT r FROM Receipt r " +
					" LEFT JOIN FETCH r.receiptType rt " +
					" LEFT JOIN FETCH r.municipalBond mb " +
					" LEFT JOIN FETCH mb.adjunct a " +
					" LEFT JOIN FETCH mb.items i " +
					" LEFT JOIN FETCH i.entry e " +
					" LEFT JOIN FETCH mb.resident re " +
					" LEFT JOIN FETCH r.receiptAuthorization ra " +
					" LEFT JOIN FETCH ra.taxpayerRecord tr " +
					" WHERE r.id in (:receiptIds) " +
					" ORDER BY r.id"),
			@NamedQuery(name="Receipt.updateElectronicReceipt", 
			query = "UPDATE Receipt r "
					+ " SET r.sriAccessKey = :sriAccessKey, "
					+ " r.authorizationNumber = :authorizationNumber,"
					+ " r.sriAuthorizationDate = :sriAuthorizationDate,"
					+ " r.sriContingencyEnvironment = :sriContingencyEnvironment,"
					+ " r.statusElectronicReceipt = :statusElectronicReceipt "
					+ " WHERE r.id = :receiptId ")
					
})
public class Receipt {
	@Id
	@GeneratedValue(generator = "ReceiptGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@Version
	private Long version = 0L;

	@Column(length=40)
	private String authorizationNumber;
	
	private Integer branch;
	
	private Integer till;
	
	private Long sequential;
	
	private Date date; 
	
	private Date sriAuthorizationDate; 
	
	private Long versionReceipt;
	
	private boolean electronicReceipt;
	
	private boolean sriContingencyEnvironment;
	
	//agregado macartuche
	private boolean isCompensation;
	
	@Column(length=50)
	private String sriAccessKey;
	
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private StatusElectronicReceipt statusElectronicReceipt;
	
	@Column(length=20)
	private String receiptNumber;
	
	@NotAudited
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="receiptAuthorization_id")
	private ReceiptAuthorization receiptAuthorization; 
	
	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	private FinancialStatus status;
	
	/**
	 * Relationships
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "receiptType_id") 
	private ReceiptType receiptType;
	
	@OneToOne(fetch=FetchType.LAZY)
	private MunicipalBond municipalBond;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="reversedMunicipalBond_id")
	private MunicipalBond reversedMunicipalBond;
	
	@OneToMany(mappedBy = "receipt")
	private List<ReceiptMessageSRI> messages;
	
	public MunicipalBond getMunicipalBond() {
		return municipalBond;
	}

	public void setMunicipalBond(MunicipalBond municipalBond) {
		this.municipalBond = municipalBond;
	}

	public ReceiptType getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(ReceiptType receiptType) {
		this.receiptType = receiptType;
	}


	public Integer getBranch() {
		return branch;
	}

	public void setBranch(Integer branch) {
		this.branch = branch;
	}

	public Integer getTill() {
		return till;
	}

	public void setTill(Integer till) {
		this.till = till;
	}
		
	public String toString(){
		return getBranchLabel()+"-"+getTillLabel()+"-"+getNumberLabel();
	}

	public String getBranchLabel(){
		return String.format("%03d", branch != null ? branch : 0);
	}
	
	public String getTillLabel(){
		return String.format("%03d", till != null ? till : 0);
	}
	
	public String getNumberLabel(){
		return String.format("%09d", sequential != null ? sequential : 0);
	}

	public MunicipalBond getReversedMunicipalBond() {
		return reversedMunicipalBond;
	}

	public void setReversedMunicipalBond(MunicipalBond reversedMunicipalBond) {
		this.reversedMunicipalBond = reversedMunicipalBond;
	}

	public FinancialStatus getStatus() {
		return status;
	}

	public void setStatus(FinancialStatus status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthorizationNumber() {
		return authorizationNumber;
	}

	public void setAuthorizationNumber(String authorizationNumber) {
		this.authorizationNumber = authorizationNumber;
	}

	public Long getSequential() {
		return sequential;
	}

	public void setSequential(Long sequential) {
		this.sequential = sequential;
	}

	public ReceiptAuthorization getReceiptAuthorization() {
		return receiptAuthorization;
	}

	public void setReceiptAuthorization(ReceiptAuthorization receiptAuthorization) {
		this.receiptAuthorization = receiptAuthorization;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getSriAuthorizationDate() {
		return sriAuthorizationDate;
	}

	public void setSriAuthorizationDate(Date sriAuthorizationDate) {
		this.sriAuthorizationDate = sriAuthorizationDate;
	}

	public Long getVersionReceipt() {
		return versionReceipt;
	}

	public void setVersionReceipt(Long versionReceipt) {
		this.versionReceipt = versionReceipt;
	}

	public boolean isElectronicReceipt() {
		return electronicReceipt;
	}

	public void setElectronicReceipt(boolean electronicReceipt) {
		this.electronicReceipt = electronicReceipt;
	}

	public String getSriAccessKey() {
		return sriAccessKey;
	}

	public void setSriAccessKey(String sriAccessKey) {
		this.sriAccessKey = sriAccessKey;
	}

	public boolean isSriContingencyEnvironment() {
		return sriContingencyEnvironment;
	}

	public void setSriContingencyEnvironment(boolean sriContingencyEnvironment) {
		this.sriContingencyEnvironment = sriContingencyEnvironment;
	}

	public StatusElectronicReceipt getStatusElectronicReceipt() {
		return statusElectronicReceipt;
	}

	public void setStatusElectronicReceipt(
			StatusElectronicReceipt statusElectronicReceipt) {
		this.statusElectronicReceipt = statusElectronicReceipt;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public List<ReceiptMessageSRI> getMessages() {
		return messages;
	}

	public void setMessages(List<ReceiptMessageSRI> messages) {
		this.messages = messages;
	}
	
	public boolean isCompensation() {
		return isCompensation;
	}

	public void setCompensation(boolean isCompensation) {
		this.isCompensation = isCompensation;
	}
	
	

}