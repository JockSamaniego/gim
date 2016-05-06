package ec.gob.gim.income.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Charge;

@Audited
@Entity
@TableGenerator(
		 name="TaxpayerRecordGenerator",
		 table="IdentityGenerator",
		 pkColumnName="name",
		 valueColumnName="value",
		 pkColumnValue="TaxpayerRecord",
		 initialValue=1, allocationSize=1
	 )

@NamedQueries(value = {
		@NamedQuery(name="TaxpayerRecord.findAll", 
				query="select t from TaxpayerRecord t"),
		@NamedQuery(name="TaxpayerRecord.findDefaultTaxpayerRecord", 
				query="select t from TaxpayerRecord t where t.isDefault = true"),
		@NamedQuery(name="TaxpayerRecord.disableDefaultTaxpayerRecord", 
				query="UPDATE TaxpayerRecord t SET t.isDefault = false WHERE t.isDefault = true AND t.id <> :id"),
		@NamedQuery(name="TaxpayerRecord.countTaxPayerRecord", 
				query="select count(t.id) from TaxpayerRecord t")
		}
)

public class TaxpayerRecord {
	@Id
	@GeneratedValue(generator="TaxpayerRecordGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private String name;
	
	private String number;
	
	private String address;
	
	private String phone;
	
	private String fax;
	
	private String slogan;
	
	private Boolean isSpecialTaxpayer;
	
	private String specialTaxpayerResolution;
	
	private Date specialTaxpayerDate;
	
	private Boolean isDefault;
	
	@Column(length = 2147483647)
	@Basic(fetch = FetchType.LAZY)
	private byte[] flag;
	
	@Column(length = 2147483647)
	@Basic(fetch = FetchType.LAZY)
	private byte[] logo;
	
	@OneToMany(mappedBy = "taxpayerRecord", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<ReceiptAuthorization> receiptAuthorizations;
	
	@OneToMany(mappedBy = "institution", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)	
	private List<Charge> charges;
	
	public TaxpayerRecord(){
		receiptAuthorizations = new ArrayList<ReceiptAuthorization>();
		charges = new ArrayList<Charge>();
		isSpecialTaxpayer = Boolean.FALSE;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
	

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public List<ReceiptAuthorization> getReceiptAuthorizations() {
		return receiptAuthorizations;
	}

	public void setReceiptAuthorizations(
			List<ReceiptAuthorization> receiptAuthorizations) {
		this.receiptAuthorizations = receiptAuthorizations;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void add(ReceiptAuthorization r){
		if(r != null && !receiptAuthorizations.contains(r)){
			receiptAuthorizations.add(r);
			r.setTaxpayerRecord(this);
		}
	}
	
	public void remove(ReceiptAuthorization r){
		if(receiptAuthorizations.contains(r)){
			receiptAuthorizations.remove(r);
			r.setTaxpayerRecord(null);
		}
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public List<Charge> getCharges() {
		return charges;
	}

	public void setCharges(List<Charge> charges) {
		this.charges = charges;
	}

	public void add(Charge c){
		if(!charges.contains(c) && c != null){
			charges.add(c);
			c.setInstitution(this);
		}		
	}
	
	public void remove(Charge c){
		if(charges.contains(c)){
			charges.remove(c);
		}		
	}

	public String getSpecialTaxpayerResolution() {
		return specialTaxpayerResolution;
	}

	public void setSpecialTaxpayerResolution(String specialTaxpayerResolution) {
		this.specialTaxpayerResolution = specialTaxpayerResolution;
	}

	public Date getSpecialTaxpayerDate() {
		return specialTaxpayerDate;
	}

	public void setSpecialTaxpayerDate(Date specialTaxpayerDate) {
		this.specialTaxpayerDate = specialTaxpayerDate;
	}
	
	public String getSpecialTaxpayerDateString(){
		DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		return specialTaxpayerDate != null ? df.format(specialTaxpayerDate) : ""; 
	}

	public Boolean getIsSpecialTaxpayer() {
		return isSpecialTaxpayer;
	}

	public void setIsSpecialTaxpayer(Boolean isSpecialTaxpayer) {
		this.isSpecialTaxpayer = isSpecialTaxpayer;
	}

	public byte[] getFlag() {
		return flag;
	}

	public void setFlag(byte[] flag) {
		this.flag = flag;
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}	
}
