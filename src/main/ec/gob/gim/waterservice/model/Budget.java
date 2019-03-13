package ec.gob.gim.waterservice.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.MunicipalBond;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:25
 */

@Audited
@Entity
@TableGenerator(name = "BudgetGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "Budget", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "Budget.findByBudgetEntry", query = "select b from Budget b "
				+ "where b.id = :budgetId and "
				+ "EXISTS (select bi from BudgetItem bi "
				+ "left join bi.budgetEntry budgetEntry "
				+ "left join budgetEntry.entry entry "
				+ "where "
				+ "bi.budget.id = :budgetId and entry.id = :entryId)"),
		@NamedQuery(name = "Budget.findMaxCodeByYear", query = "select MAX(b.code) from Budget b where year = :year") })
public class Budget {

	@Id
	@GeneratedValue(generator = "BudgetGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date date;

	private BigDecimal total;
	private BigDecimal subTotalTax;
	private BigDecimal subTotalNoTax;

	@Temporal(TemporalType.DATE)
	private Date inspectionDate;

	private Boolean isServiceOrderGenerate;
	private Boolean isFeePayment;
	private Boolean isResidentOnly;

	/**
	 * codigo secuencial para el año
	 */
	private Integer code;
	private Integer year;

	private String description;
	
	@Column(length = 20)
	private String telephone;
	
	@Column(length = 10)
	private String houseNumber;
	
	@Column(length = 30)
	private String cadastralCode;
	
	
	/**
	 * valor q sera puesto en el titulo de credito -> referencia
	 * la observacion es solo para el presupuesto
	 * @author rfam 2019-01-29
	 */
	private String reference;

	/*
	 * Relationships
	 */

	@OneToMany(mappedBy = "budget", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<BudgetItem> budgetItems;

	@ManyToOne
	@JoinColumn(name = "municipalBond_id")
	private MunicipalBond municipalBond;

	@ManyToOne
	@JoinColumn(name = "resident_id")
	private Resident resident;

	/**
	 * quien lo ingresa al sistema
	 */
	@ManyToOne
	@JoinColumn(name = "register_id")
	private Resident register;

	/**
	 * por quien es hecho el presupuesto
	 */
	@ManyToOne
	@JoinColumn(name = "doneBy_id")
	private Resident doneBy;

	@ManyToOne
	@JoinColumn(name = "property_id")
	private Property property;

	/*
	 * @Transient private BudgetEntry budgetEntry;
	 */

	public Budget() {
		this.budgetItems = new ArrayList<BudgetItem>();
		subTotalTax = new BigDecimal(0);
		subTotalNoTax = new BigDecimal(0);
		total = new BigDecimal(0);
		isFeePayment = new Boolean(false);
		date = new Date();
		inspectionDate = new Date();
		isResidentOnly=new Boolean(false);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<BudgetItem> getBudgetItems() {
		return budgetItems;
	}

	public void setBudgetItems(List<BudgetItem> budgetItems) {
		this.budgetItems = budgetItems;
	}

	/**
	 * @return the inspectionDate
	 */
	public Date getInspectionDate() {
		return inspectionDate;
	}

	/**
	 * @param inspectionDate
	 *            the inspectionDate to set
	 */
	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public void add(BudgetItem budgetItem) {
		if (!budgetItems.contains(budgetItem)) {
			budgetItem.setBudget(this);
			budgetItems.add(budgetItem);
		}
	}

	public void remove(BudgetItem budgetItem) {
		if (budgetItems.remove(budgetItem)) {
			budgetItem.setBudget((Budget) null);
		}
	}

	public MunicipalBond getMunicipalBond() {
		return municipalBond;
	}

	public void setMunicipalBond(MunicipalBond municipalBond) {
		this.municipalBond = municipalBond;
	}

	/**
	 * @return the isServiceOrderGenerate
	 */
	public Boolean getIsServiceOrderGenerate() {
		return isServiceOrderGenerate;
	}

	/**
	 * @param isServiceOrderGenerate
	 *            the isServiceOrderGenerate to set
	 */
	public void setIsServiceOrderGenerate(Boolean isServiceOrderGenerate) {
		this.isServiceOrderGenerate = isServiceOrderGenerate;
	}

	/**
	 * @return the resident
	 */
	public Resident getResident() {
		return resident;
	}

	/**
	 * @param resident
	 *            the resident to set
	 */
	public void setResident(Resident resident) {
		this.resident = resident;
	}

	/**
	 * @return the property
	 */
	public Property getProperty() {
		return property;
	}

	/**
	 * @param property
	 *            the property to set
	 */
	public void setProperty(Property property) {
		this.property = property;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getSubTotalTax() {
		return subTotalTax;
	}

	public void setSubTotalTax(BigDecimal subTotalTax) {
		this.subTotalTax = subTotalTax;
	}

	public BigDecimal getSubTotalNoTax() {
		return subTotalNoTax;
	}

	public void setSubTotalNoTax(BigDecimal subTotalNoTax) {
		this.subTotalNoTax = subTotalNoTax;
	}

	public String getFormatedDate() {
		StringBuilder sb = new StringBuilder();
		sb.append("Loja, ");
		sb.append(new SimpleDateFormat("MMMM dd ").format(this.date));
		sb.append("del ");
		sb.append(new SimpleDateFormat("yyyy").format(this.date));
		return sb.toString();
	}

	public Boolean getIsFeePayment() {
		return isFeePayment;
	}

	public void setIsFeePayment(Boolean isFeePayment) {
		this.isFeePayment = isFeePayment;
	}

	public Resident getRegister() {
		return register;
	}

	public void setRegister(Resident register) {
		this.register = register;
	}

	public Resident getDoneBy() {
		return doneBy;
	}

	public void setDoneBy(Resident doneBy) {
		this.doneBy = doneBy;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Boolean getIsResidentOnly() {
		return isResidentOnly;
	}

	public void setIsResidentOnly(Boolean isResidentOnly) {
		this.isResidentOnly = isResidentOnly;
	}

	public String getCadastralCode() {
		return cadastralCode;
	}

	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
	
	
	/*
	 * public BudgetEntry getBudgetEntry() { return budgetEntry; }
	 * 
	 * public void setBudgetEntry(BudgetEntry budgetEntry) { this.budgetEntry =
	 * budgetEntry; }
	 */

}// end Budget