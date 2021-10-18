package ec.gob.gim.revenue.model.bankDebit;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.revenue.model.MunicipalBondForBankDebit;

@Audited
@Entity
@TableGenerator(name = "BankDebitForLiquidationGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "BankDebitForLiquidation", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "debitForLiquidation.findPending", query = "SELECT bdl from BankDebitForLiquidation bdl Where bdl.isActive = true ORDER BY bdl.bankCount ASC "),})
public class BankDebitForLiquidation implements Serializable{
	
	private static final long serialVersionUID = 18386387333339876L;
	
	@Id
	@GeneratedValue(generator = "BankDebitForLiquidationGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Version
	private Long version = 0L;
	
	private Long residentId;
	
	private String residentIdentification;
	
    private String residentName;
	
    private String accountType;

    private String accountNumber;

    private String accountHolder;

    private Integer service;

    private Integer amount;

    private BigDecimal value;
    
    private Boolean isLiquidated;
    
    private Boolean isActive;
    
    private Boolean isSelected;
    
    private Boolean hasProblem;

	@Temporal(TemporalType.DATE)
	private Date creationDate;

	@Temporal(TemporalType.TIME)
	private Date creationTime;
	
	@Temporal(TemporalType.DATE)
	private Date liquidationDate;

	@Temporal(TemporalType.TIME)
	private Date liquidationTime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creation_responsible_id")
	private Person creationResponsible;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "liquidation_responsible_id")
	private Person liquidationResponsible;
	
	@OneToMany(fetch = FetchType.LAZY)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "bankdebit_id")
	private List<MunicipalBondForBankDebit> mbsForBankDebit;
	
	private String observation;
	
	@Column(nullable=true)
	private int bankCount;
	
	public BankDebitForLiquidation(){
		mbsForBankDebit = new ArrayList<MunicipalBondForBankDebit>();
		isLiquidated = Boolean.FALSE;
		isActive = Boolean.TRUE;
		isSelected = Boolean.FALSE;
		hasProblem = Boolean.FALSE;
		observation = "";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getResidentIdentification() {
		return residentIdentification;
	}

	public void setResidentIdentification(String residentIdentification) {
		this.residentIdentification = residentIdentification;
	}

	public String getResidentName() {
		return residentName;
	}

	public void setResidentName(String residentName) {
		this.residentName = residentName;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}

	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Person getCreationResponsible() {
		return creationResponsible;
	}

	public void setCreationResponsible(Person creationResponsible) {
		this.creationResponsible = creationResponsible;
	}

	public List<MunicipalBondForBankDebit> getMbsForBankDebit() {
		return mbsForBankDebit;
	}

	public void setMbsForBankDebit(List<MunicipalBondForBankDebit> mbsForBankDebit) {
		this.mbsForBankDebit = mbsForBankDebit;
	}
	
	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Boolean getIsLiquidated() {
		return isLiquidated;
	}

	public void setIsLiquidated(Boolean isLiquidated) {
		this.isLiquidated = isLiquidated;
	}

	public Date getLiquidationDate() {
		return liquidationDate;
	}

	public void setLiquidationDate(Date liquidationDate) {
		this.liquidationDate = liquidationDate;
	}

	public Date getLiquidationTime() {
		return liquidationTime;
	}

	public void setLiquidationTime(Date liquidationTime) {
		this.liquidationTime = liquidationTime;
	}

	public Person getLiquidationResponsible() {
		return liquidationResponsible;
	}

	public void setLiquidationResponsible(Person liquidationResponsible) {
		this.liquidationResponsible = liquidationResponsible;
	}
	
	public Long getResidentId() {
		return residentId;
	}

	public void setResidentId(Long residentId) {
		this.residentId = residentId;
	}
	
	public Boolean getHasProblem() {
		return hasProblem;
	}

	public void setHasProblem(Boolean hasProblem) {
		this.hasProblem = hasProblem;
	}
	
	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public int getBankCount() {
		return bankCount;
	}

	public void setBankCount(int bankCount) {
		this.bankCount = bankCount;
	}

	public void add(MunicipalBondForBankDebit municipalBondForBankDebit) {
		if (!mbsForBankDebit.contains(municipalBondForBankDebit)
				&& municipalBondForBankDebit != null) {
			mbsForBankDebit.add(municipalBondForBankDebit);
		}
	}

}
