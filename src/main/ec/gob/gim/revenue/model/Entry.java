package ec.gob.gim.revenue.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Identifiable;
import ec.gob.gim.income.model.Account;
import ec.gob.gim.income.model.ReceiptType;
import ec.gob.gim.income.model.Tax;

/**
 * Rubro
 * 
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:33
 */
@Audited
@Entity
@TableGenerator(
		name = "EntryGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "Entry", 
		initialValue = 1, allocationSize = 1
)

@NamedQueries(value = {
		@NamedQuery(name="Entry.findById", 
				query="select entry from Entry entry " +
						"left join fetch entry.timePeriod timePeriod " +
						"where "+
						"entry.id = :entryId)"),
		@NamedQuery(name="Entry.findIdByAccountId", 
				query="select entry.id from Entry entry " +
						"where "+
						"entry.account.id in ( :accountId))"),
		@NamedQuery(name="Entry.findAll", 
				query="select entry from Entry entry " +
						"left join fetch entry.timePeriod timePeriod "),
		@NamedQuery(name="Entry.findByCode", 
				query="select entry from Entry entry " +
						"left join fetch entry.timePeriod timePeriod " +
						"left join fetch entry.account account " +
						"where "+
						"lower(entry.code) like lower(:code)"),
		@NamedQuery(name="Entry.findByCodeAndDirectEmissionAndActive", 
				query="select entry from Entry entry " +
						"left join fetch entry.timePeriod timePeriod " +
						"where "+
						"lower(entry.code) like lower(:code) and " +
						"entry.hasDirectEmission = true and " +
						"entry.isActive = true"),
		@NamedQuery(name="Entry.findByCodeAndDirectEmissionAndActiveAndRole", 
				query="select entry from Entry entry " +
						"left join fetch entry.timePeriod timePeriod " +
						"left join fetch entry.preissuerPermissions preissuer " +
						"left join fetch preissuer.role r " +
						"where "+
						"lower(entry.code) like lower(:code) and " +
						"entry.hasDirectEmission = true and " +
						"entry.isActive = true and " +
						"preissuer.isActive = true and " +
						"r.id in (:roleIds)"),		
		@NamedQuery(name="Entry.findByCriteria", 
				query="select entry from Entry entry " +
						"left join fetch entry.timePeriod timePeriod " +
						"left join fetch entry.account account " +
						"where "+
						"lower(entry.name) like lower(concat(:criteria, '%')) or " +
						"lower(entry.code) like lower(concat(:criteria, '%')) or " +
						"lower(entry.previousCode) like lower(concat(:criteria, '%')) or " +
						"lower(account.budgetCertificateCode) like lower(concat(:criteria, '%')) " +
						"order by entry.name") ,
		@NamedQuery(name="Entry.findByCriteriaAndDirectEmission", 
				query="select entry from Entry entry " +
						"left join fetch entry.timePeriod timePeriod " +
						"where "+
						"(lower(entry.name) like lower(concat(:criteria, '%')) or " +
						"lower(entry.code) like lower(concat(:criteria, '%')) or " +
						"lower(entry.previousCode) like lower(concat(:criteria, '%')) or " +
						"lower(entry.account.budgetCertificateCode) like lower(concat(:criteria, '%')) )" +
						"and entry.hasDirectEmission = true " +
						"order by entry.name") ,
						
		@NamedQuery(name="Entry.findByCriteriaAndDirectEmissionAndRole", 
				query="select distinct (entry) from Entry entry "
					+ "left join fetch entry.timePeriod timePeriod "
					+ "left join fetch entry.preissuerPermissions preissuer "
					+ "left join fetch entry.account ac "
					+ "left join fetch preissuer.role r "
					+ "where "
					+ "(lower(entry.name) like lower(concat(:criteria, '%')) or "
					+ "lower(entry.code) like lower(concat(:criteria, '%')) or "
					+ "lower(entry.previousCode) like lower(concat(:criteria, '%')) or "
					+ "lower(entry.account.budgetCertificateCode) like lower(concat(:criteria, '%')) )"
					+ "and entry.hasDirectEmission = true " 
					+ "and r.id in (:roleIds) and preissuer.isActive = true "
					+ "order by entry.name"),				
					
		@NamedQuery(name="Entry.findLastCode", 
				query="select max(entry.code) from Entry entry") ,
		@NamedQuery(name="Entry.findMaxId", 
				query="select max(entry.id) + 1 from Entry entry") ,
		@NamedQuery(name="Entry.findReceiptTypeByEntryId", 
					query="select entry.receiptType from Entry entry where entry.id = :entryId"),
		@NamedQuery(name="Entry.findByAccountCode", 
				query="select entry from Entry entry " +
						"left join fetch entry.timePeriod timePeriod " +
						"left join fetch entry.account account " +
						"where "+
						"lower(account.accountCode) like lower(:accountCode) " +
						"order by entry.name")			
		}
)

public class Entry extends Identifiable{

	@Id
	@GeneratedValue(generator = "EntryGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length = 10, unique=true)
	private String code;
	
	@Column(length = 10)
	private String previousCode;
	
	@Column(length = 120, nullable=false)
	private String name;
	
	@Temporal(TemporalType.DATE)
	private Date creationDate;
	
	private String description;
	
	private String department;

	private TimeToCalculate timeToCalculate;
	
	public TimeToCalculate getTimeToCalculate() {
		return timeToCalculate;
	}

	public void setTimeToCalculate(TimeToCalculate timeToCalculate) {
		this.timeToCalculate = timeToCalculate;
	}

	private String reason;
	
	@Column(length = 50)
	private String amountLabel;
	
	@Column(length = 50)
	private String groupingCodeLabel;
	
	private Boolean isAmountEditable;
	
	private Boolean isValueEditable;
	
	private Boolean emitOnInternal;
	
	private Boolean isPreviousPaymentFieldEnabled;

	public Boolean getIsValueEditable() {
		return isValueEditable;
	}

	public void setIsValueEditable(Boolean isValueEditable) {
		this.isValueEditable = isValueEditable;
	}

	private Boolean hasDirectEmission;

	@Column(length = 40)
	private String datePattern;
	
	@Column(length = 250)
	private String adjunctClassName;
	
	private Boolean isTaxable;
	
	private Boolean isActive;
	
	private Boolean hasMultipleEmission;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="entryTypeIncome_id")
	private EntryTypeIncome entryTypeIncome;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Account account;	

	@ManyToOne(fetch=FetchType.LAZY)
	private Account subLineAccount;

	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private EntryType entryType;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "receiptType_id")
	private ReceiptType receiptType;	
	
	/**
	 * @author macartuche
	 * @date 2015-07-27
	 * @return
	 */
	@Column(length = 250)
	private String completeName;
	
	/**
	 * @author rfam
	 * @date 2017-12-01
	 * nota aclaratoria ML-JRM-2017-884-M
	 * @return
	 */
	@Column(length = 100)
	private String explanatoryNote;
	
	public ReceiptType getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(ReceiptType receiptType) {
		this.receiptType = receiptType;
	}

	/**
	 * Relationships
	 */

	//@OneToMany(mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	//@ManyToMany(mappedBy = "parents", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	//private List<Entry> children;
	@OneToMany(mappedBy = "parent", cascade = {CascadeType.ALL})
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OrderBy("order")
	private List<EntryStructure> children;
	
	@OneToMany(mappedBy = "child", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@OrderBy("order")
	private List<EntryStructure> parents;
	
	@OneToMany(mappedBy = "entry", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<PreissuerPermission> preissuerPermissions;
	
	@OneToMany(mappedBy = "entry", cascade = {CascadeType.ALL})
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<EntryDefinition> entryDefinitions;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private TimePeriod timePeriod;
	
	//@OneToMany(cascade = CascadeType.ALL)
	@ManyToMany
	@JoinTable(name="entry_tax",
			joinColumns=@JoinColumn(name="entry_id"),
			inverseJoinColumns=@JoinColumn(name="tax_id"))
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<Tax> taxes;

	public Entry() {
		isTaxable = Boolean.FALSE;
		emitOnInternal = Boolean.TRUE;
		isAmountEditable = Boolean.FALSE;
		isValueEditable = Boolean.FALSE;
		this.creationDate = java.util.Calendar.getInstance().getTime();
		entryDefinitions = new ArrayList<EntryDefinition>();	
		children = new ArrayList<EntryStructure>();
		parents = new ArrayList<EntryStructure>();
		taxes = new ArrayList<Tax>();
		preissuerPermissions = new ArrayList<PreissuerPermission>();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code.toUpperCase();
	}

	/**
	 * @return the previousCode
	 */
	public String getPreviousCode() {
		return previousCode;
	}

	/**
	 * @param previousCode the previousCode to set
	 */
	public void setPreviousCode(String previousCode) {
		this.previousCode = previousCode;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.toUpperCase();
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	
	public List<EntryDefinition> getEntryDefinitions() {
		return entryDefinitions;
	}

	public void setEntryDefinitions(List<EntryDefinition> entryDefinitions) {
		this.entryDefinitions = entryDefinitions;
	}

	public EntryType getEntryType() {
		return entryType;
	}

	public void setEntryType(EntryType entryType) {
		this.entryType = entryType;
	}

	/**
	 * @return the children
	 */
	public List<EntryStructure> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<EntryStructure> children) {
		this.children = children;
	}

	/**
	 * @return the parents
	 */
	public List<EntryStructure> getParents() {
		return parents;
	}

	/**
	 * @param parents the parents to set
	 */
	public void setParents(List<EntryStructure> parents) {
		this.parents = parents;
	}

	/**
	 * @return the isAmountEditable
	 */
	public Boolean getIsAmountEditable() {
		return isAmountEditable;
	}

	/**
	 * @param isAmountEditable the isAmountEditable to set
	 */
	public void setIsAmountEditable(Boolean isAmountEditable) {
		this.isAmountEditable = isAmountEditable;
	}

	public void addChild(EntryStructure entryStructure) {
		if (!this.children.contains(entryStructure)) {
			this.children.add(entryStructure);
			entryStructure.setParent(this);
		}
	}
	
	public void removeChild(EntryStructure entryStructure) {
		boolean removed = this.children.remove(entryStructure);
		if (removed){
			entryStructure.setParent(null);
		}	
	}
	
	
	public void addParent(EntryStructure entryStructure) {
		if (!this.parents.contains(entryStructure)) {
			this.parents.add(entryStructure);
			entryStructure.setChild(this);
		}
	}

	
	public void removeParent(EntryStructure entryStructure) {
		boolean removed = this.parents.remove(entryStructure);
		if (removed){
			entryStructure.setChild(null);
			
		}
	}

	public void add(EntryDefinition entryDefinition) {
		if (!this.entryDefinitions.contains(entryDefinition)) {
			this.entryDefinitions.add(entryDefinition);
			entryDefinition.setEntry(this);
		}
	}

	public void remove(EntryDefinition entryDefinition) {
		boolean removed = this.entryDefinitions.remove(entryDefinition);
		if (removed)
			entryDefinition.setEntry(null);
	}
	
	/**
	 * Actualiza el atributo isCurrent de todos los EntryDefinition que se
	 * encuentran en el rubro; 
	 * @param value
	 */
	public void updateAllIsCurrentToEntryDefinitions(Boolean value){
		for (EntryDefinition ed : this.entryDefinitions){
			if (ed.getIsCurrent() != value)
				ed.setIsCurrent(value);
		}
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	/**
	 * @return the timePeriod
	 */
	public TimePeriod getTimePeriod() {
		return timePeriod;
	}

	/**
	 * @param timePeriod the timePeriod to set
	 */
	public void setTimePeriod(TimePeriod timePeriod) {
		this.timePeriod = timePeriod;
	}

	/**
	 * @return the amountLabel
	 */
	public String getAmountLabel() {
		return amountLabel;
	}

	/**
	 * @param amountLabel the amountLabel to set
	 */
	public void setAmountLabel(String amountLabel) {
		this.amountLabel = amountLabel;
	}

	public String getAdjunctClassName() {
		return adjunctClassName;
	}

	public void setAdjunctClassName(String adjunctClassName) {
		this.adjunctClassName = adjunctClassName;
	}

	public String getGroupingCodeLabel() {
		return groupingCodeLabel;
	}

	public void setGroupingCodeLabel(String groupingCodeLabel) {
		this.groupingCodeLabel = groupingCodeLabel;
	}
	
	public List<Tax> getTaxes() {
		return taxes;
	}

	public void setTaxes(List<Tax> taxes) {
		this.taxes = taxes;
	}
	
	public void add(Tax tax) {
		if (!this.taxes.contains(tax)) {
			this.taxes.add(tax);
		}
	}

	public void remove(Tax tax) {
		this.taxes.remove(tax);
	}

	public Boolean getIsTaxable() {
		return isTaxable;
	}

	public void setIsTaxable(Boolean isTaxable) {
		this.isTaxable = isTaxable;
	}
	
	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public EntryTypeIncome getEntryTypeIncome() {
		return entryTypeIncome;
	}

	public void setEntryTypeIncome(EntryTypeIncome entryTypeIncome) {
		this.entryTypeIncome = entryTypeIncome;
	}

	@Override
	public boolean equals(Object object) {		
		if (!(object instanceof Entry)) {
			return false;
		}
		Entry other = (Entry) object;
					
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}	
	
	public Account getSubLineAccount() {
		return subLineAccount;
	}

	public void setSubLineAccount(Account subLineAccount) {
		this.subLineAccount = subLineAccount;
	}

	public Boolean getHasDirectEmission() {
		return hasDirectEmission;
	}

	public void setHasDirectEmission(Boolean hasDirectEmission) {
		this.hasDirectEmission = hasDirectEmission;
	}

	public Boolean getHasMultipleEmission() {
		if (hasMultipleEmission == null) return Boolean.FALSE;
		return hasMultipleEmission;
	}

	public void setHasMultipleEmission(Boolean hasMultipleEmission) {
		this.hasMultipleEmission = hasMultipleEmission;
	}

	public List<PreissuerPermission> getPreissuerPermissions() {
		return preissuerPermissions;
	}

	public void setPreissuerPermissions(
			List<PreissuerPermission> preissuerPermissions) {
		this.preissuerPermissions = preissuerPermissions;
	}
	
	public void add(PreissuerPermission preissuerPermission) {
		if (!this.preissuerPermissions.contains(preissuerPermission)) {
			this.preissuerPermissions.add(preissuerPermission);
			preissuerPermission.setEntry(this);
		}
	}
	
	public void remove(PreissuerPermission preissuerPermission) {
		boolean removed = this.preissuerPermissions.remove(preissuerPermission);
		if (removed){
			preissuerPermission.setEntry(null);
		}	
	}	
	
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Boolean getEmitOnInternal() {
		return emitOnInternal;
	}

	public void setEmitOnInternal(Boolean emitOnInternal) {
		this.emitOnInternal = emitOnInternal;
	}

	public Boolean getIsPreviousPaymentFieldEnabled() {
		return isPreviousPaymentFieldEnabled;
	}

	public void setIsPreviousPaymentFieldEnabled(
			Boolean isPreviousPaymentFieldEnabled) {
		this.isPreviousPaymentFieldEnabled = isPreviousPaymentFieldEnabled;
	}

	/**
	 * @author macartuche
	 * @date 2015-07-27
	 * @return
	 */
	public String getCompleteName() {
		return completeName;
	}

	public void setCompleteName(String completeName) {
		this.completeName = completeName;
	}

	public String getExplanatoryNote() {
		return explanatoryNote;
	}

	public void setExplanatoryNote(String explanatoryNote) {
		this.explanatoryNote = explanatoryNote;
	}
		
	
}// end Entry