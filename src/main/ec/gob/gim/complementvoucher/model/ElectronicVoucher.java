package ec.gob.gim.complementvoucher.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Contract;
import ec.gob.gim.revenue.model.Item;
import ec.gob.gim.revenue.model.MunicipalBond;

/**
 * @author manuel
 * @version 1.0
 * @created 17-abril-2015 12:32:33
 */
@Audited
@Entity
@TableGenerator(name = "ElectronicVoucherGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "ElectronicVoucher", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { @NamedQuery(name = "ElectronicVoucher.findAll", query = "SELECT ev FROM ElectronicVoucher ev order by ev.creationDate"),
						@NamedQuery(name = "ElectronicVoucher.findCreditNote", query = "SELECT cn FROM ElectronicVoucher cn where cn.municipalBond.number = :mbNumber and cn.documentType = '04' and cn.active = true ")})
public class ElectronicVoucher {

	@Id
	@GeneratedValue(generator = "ElectronicVoucherGenerator", strategy = GenerationType.TABLE)
	private Long id;

 
	@Column(nullable = false)
	private Date creationDate;
	
	@Column( nullable = false)
	private Date creationTime;

	@Temporal(TemporalType.DATE)
	private Date emissionDate;
	
	@Temporal(TemporalType.TIME)
	private Date emisionTime;

	private Date authorizationDate;
	
	@Column( nullable = true)
	@Temporal(TemporalType.TIME)
	private Date authorizationTime;
	
	@Column(length = 50, nullable = true)
	private String authorizationCode;
	
	@Column(length = 50, nullable = true)
	private String accessCode;
 
	@Column(length = 50, nullable = true)
	private String versionVoucher;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="typeEmissionPoint_id")
	private TypeEmissionPoint typeEmissionPoint;
	

	@Column(length = 50, nullable = false)
	private Long voucherNumber;
	
	@Column(length = 50, nullable = false)
	private String sequentialNumber;
	
	@Column(nullable = false)
	private BigDecimal total;
	
	@Column(length = 3, nullable = false)
	private String documentType;
	
	@Column(length = 20, nullable = false)
	private String documentNumber;
	
	@Column( nullable = true)
	@Temporal(TemporalType.DATE)
	private Date documentDate;
	
	@Column(length = 20, nullable = false)
	private String electronicStatus;
	
	@Column(length = 2, nullable = true)
	private String monthFiscal;
	
	
	@Column(nullable = true)
	private Boolean active;
	
	@ManyToOne
	private FiscalPeriod fiscalPeriod;
	 
	/**
	 * agregado macartuche
	 * 2015-05-04
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="resident_id")
	private Resident resident;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emitter_id")
	private Person emitter;
	
	@OneToOne(fetch=FetchType.LAZY)
	private Provider provider;
	
	@OneToMany(mappedBy="electronicVoucher", fetch= FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}) 
	private List<ElectronicVoucherDetail> details;
	
	@OneToMany(mappedBy = "electronicVoucher", fetch= FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private  List<AditionalField> fields;
	  
	
	/** para notas de credito **/	
	@OneToOne( fetch = FetchType.LAZY)
	private MunicipalBond municipalBond;
	

	@OneToMany(mappedBy = "electronicVoucher", cascade = { CascadeType.MERGE,CascadeType.PERSIST })
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<ElectronicItem> items;
	
	@Column(length = 200, nullable = true)
	private String reason;
	
	private BigDecimal taxableTotal;

	private BigDecimal nonTaxableTotal;
	
	private BigDecimal totalTaxes;
	
	private BigDecimal totalPaid;
	
	@Transient
	private String documentModify;
	
	@Transient
	private Boolean selectToPrint;
	
	public ElectronicVoucher() {
		details = new ArrayList<ElectronicVoucherDetail>();
		fields = new ArrayList<AditionalField>();
		items = new ArrayList<ElectronicItem>();
	}

	public void finalize() throws Throwable {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getEmissionDate() {
		return emissionDate;
	}

	public void setEmissionDate(Date emissionDate) {
		this.emissionDate = emissionDate;
	}

	public Date getAuthorizationDate() {
		return authorizationDate;
	}

	public void setAuthorizationDate(Date authorizatinDate) {
		this.authorizationDate = authorizatinDate;
	}

	public Date getAuthorizationTime() {
		return authorizationTime;
	}

	public void setAuthorizationTime(Date authorizationTime) {
		this.authorizationTime = authorizationTime;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public String getSequentialNumber() {
		return sequentialNumber;
	}

	public void setSequentialNumber(String sequentialNumber) {
		this.sequentialNumber = sequentialNumber;
	}


	public String getVersionVoucher() {
		return versionVoucher;
	}

	public void setVersionVoucher(String versionVoucher) {
		this.versionVoucher = versionVoucher;
	}

	public Long getVoucherNumber() {
		return voucherNumber;
	}

	public void setVoucherNumber(Long voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	
	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public Date getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	/**
	 * agregado macartuche
	 * 2015-05-04
	 */
	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public List<ElectronicVoucherDetail> getDetails() {
		return details;
	}

	public void setDetails(List<ElectronicVoucherDetail> details) {
		this.details = details;
	}

	public TypeEmissionPoint getTypeEmissionPoint() {
		return typeEmissionPoint;
	}

	public void setTypeEmissionPoint(TypeEmissionPoint typeEmissionPoint) {
		this.typeEmissionPoint = typeEmissionPoint;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getElectronicStatus() {
		return electronicStatus;
	}

	public void setElectronicStatus(String electronicStatus) {
		this.electronicStatus = electronicStatus;
	}

	public String getMonthFiscal() {
		return monthFiscal;
	}

	public void setMonthFiscal(String monthFiscal) {
		this.monthFiscal = monthFiscal;
	}

	public FiscalPeriod getFiscalPeriod() {
		return fiscalPeriod;
	}

	public void setFiscalPeriod(FiscalPeriod fiscalPeriod) {
		this.fiscalPeriod = fiscalPeriod;
	}

	public Person getEmitter() {
		return emitter;
	}

	public void setEmitter(Person emitter) {
		this.emitter = emitter;
	}

	public Date getEmisionTime() {
		return emisionTime;
	}

	public void setEmisionTime(Date emisionTime) {
		this.emisionTime = emisionTime;
	}

	public List<AditionalField> getFields() {
		return fields;
	}

	public void setFields(List<AditionalField> fields) {
		this.fields = fields;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public MunicipalBond getMunicipalBond() {
		return municipalBond;
	}

	public void setMunicipalBond(MunicipalBond municipalBond) {
		this.municipalBond = municipalBond;
	}

	public List<ElectronicItem> getItems() {
		return items;
	}

	public void setItems(List<ElectronicItem> items) {
		this.items = items;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public String getDocumentModify() {
		return documentModify;
	}

	public void setDocumentModify(String documentModify) {
		this.documentModify = documentModify;
	}

	public BigDecimal getTaxableTotal() {
		return taxableTotal;
	}

	public void setTaxableTotal(BigDecimal taxableTotal) {
		this.taxableTotal = taxableTotal;
	}

	public BigDecimal getNonTaxableTotal() {
		return nonTaxableTotal;
	}

	public void setNonTaxableTotal(BigDecimal nonTaxableTotal) {
		this.nonTaxableTotal = nonTaxableTotal;
	}

	public BigDecimal getTotalTaxes() {
		return totalTaxes;
	}

	public void setTotalTaxes(BigDecimal totalTaxes) {
		this.totalTaxes = totalTaxes;
	}

	public BigDecimal getTotalPaid() {
		return totalPaid;
	}

	public void setTotalPaid(BigDecimal totalPaid) {
		this.totalPaid = totalPaid;
	}

	public Boolean getSelectToPrint() {
		return selectToPrint;
	}

	public void setSelectToPrint(Boolean selectToPrint) {
		this.selectToPrint = selectToPrint;
	}

}// end ComplementVoucher