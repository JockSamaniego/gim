package org.gob.gim.ws.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.gob.gim.common.NativeQueryResultColumn;

public class BondSendMail {

	@NativeQueryResultColumn(index = 0)
	private Long id;
	@NativeQueryResultColumn(index = 1)
	private String identificationnumber;
	@NativeQueryResultColumn(index = 2)
	private String name;
	
	@NativeQueryResultColumn(index = 3)
	private String email;
	
	
	@NativeQueryResultColumn(index = 4)
	private Long number;
	@NativeQueryResultColumn(index = 5)
	private Date emisiondate;
	@NativeQueryResultColumn(index = 6)
	private Date servicedate;
	@NativeQueryResultColumn(index = 7)
	private Date liquidationdate;	
	@NativeQueryResultColumn(index = 8)
	private Date liquidationtime;
	@NativeQueryResultColumn(index = 9)
	private String description;
	@NativeQueryResultColumn(index = 10)
	private String reference;
	@NativeQueryResultColumn(index = 11)
	private String address;
	@NativeQueryResultColumn(index = 12)
	private BigDecimal lotappraisal;
	@NativeQueryResultColumn(index = 13)
	private BigDecimal buildingappraisal;
	@NativeQueryResultColumn(index = 14)
	private BigDecimal commercialappraisal;
	@NativeQueryResultColumn(index = 15)
	private BigDecimal exemptionvalue;
	@NativeQueryResultColumn(index = 16)
	private Date expirationdate;
	@NativeQueryResultColumn(index = 17)
	private BigDecimal nontaxabletotal;
	@NativeQueryResultColumn(index = 18)
	private BigDecimal discount;
	@NativeQueryResultColumn(index = 19)
	private BigDecimal interest;
	@NativeQueryResultColumn(index = 20)
	private BigDecimal surcharge;
	@NativeQueryResultColumn(index = 21)
	private BigDecimal paidtotal;
	@NativeQueryResultColumn(index = 22)
	private String groupingcode;
	@NativeQueryResultColumn(index = 23)
	private String street;
	@NativeQueryResultColumn(index = 24)
	private Long entryid;
	@NativeQueryResultColumn(index = 25)
	private String territorialCode;
	@NativeQueryResultColumn(index = 26)
	private String catastralKey;
	@NativeQueryResultColumn(index = 27)
	private String items;
	@NativeQueryResultColumn(index = 28)
	private String rubro;
	
	@NativeQueryResultColumn(index = 29)
	private Integer printingsNumber;	
	@NativeQueryResultColumn(index = 30)
	private String emailsendit;	
	@NativeQueryResultColumn(index = 31)
	private Date sendmaildate;			     

	private List<BondItemPrint> itemList =new ArrayList<BondItemPrint>();
	
	// private List<BondEntryPrint> itemEntry =new ArrayList<BondEntryPrint>();
	private BondEntryPrint entry = new BondEntryPrint();

	/*public List<BondEntryPrint> getItemEntry() {
		return itemEntry;
	}

	public void setItemEntry(List<BondEntryPrint> itemEntry) {
		this.itemEntry = itemEntry;
	}*/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdentificationnumber() {
		return identificationnumber;
	}

	public void setIdentificationnumber(String identificationnumber) {
		this.identificationnumber = identificationnumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public Date getEmisiondate() {
		return emisiondate;
	}

	public void setEmisiondate(Date emisiondate) {
		this.emisiondate = emisiondate;
	}

	public Date getServicedate() {
		return servicedate;
	}

	public void setServicedate(Date servicedate) {
		this.servicedate = servicedate;
	}

	public Date getLiquidationdate() {
		return liquidationdate;
	}

	public void setLiquidationdate(Date liquidationdate) {
		this.liquidationdate = liquidationdate;
	}

	public Date getLiquidationtime() {
		return liquidationtime;
	}

	public void setLiquidationtime(Date liquidationtime) {
		this.liquidationtime = liquidationtime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getLotappraisal() {
		return lotappraisal;
	}

	public void setLotappraisal(BigDecimal lotappraisal) {
		this.lotappraisal = lotappraisal;
	}

	public BigDecimal getBuildingappraisal() {
		return buildingappraisal;
	}

	public void setBuildingappraisal(BigDecimal buildingappraisal) {
		this.buildingappraisal = buildingappraisal;
	}

	public BigDecimal getCommercialappraisal() {
		return commercialappraisal;
	}

	public void setCommercialappraisal(BigDecimal commercialappraisal) {
		this.commercialappraisal = commercialappraisal;
	}

	public BigDecimal getExemptionvalue() {
		return exemptionvalue;
	}

	public void setExemptionvalue(BigDecimal exemptionvalue) {
		this.exemptionvalue = exemptionvalue;
	}

	public Date getExpirationdate() {
		return expirationdate;
	}

	public void setExpirationdate(Date expirationdate) {
		this.expirationdate = expirationdate;
	}

	public BigDecimal getNontaxabletotal() {
		return nontaxabletotal;
	}

	public void setNontaxabletotal(BigDecimal nontaxabletotal) {
		this.nontaxabletotal = nontaxabletotal;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public BigDecimal getSurcharge() {
		return surcharge;
	}

	public void setSurcharge(BigDecimal surcharge) {
		this.surcharge = surcharge;
	}

	public BigDecimal getPaidtotal() {
		return paidtotal;
	}

	public void setPaidtotal(BigDecimal paidtotal) {
		this.paidtotal = paidtotal;
	}

	public String getGroupingcode() {
		return groupingcode;
	}

	public void setGroupingcode(String groupingcode) {
		this.groupingcode = groupingcode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Long getEntryid() {
		return entryid;
	}

	public void setEntryid(Long entryid) {
		this.entryid = entryid;
	}

	public String getTerritorialCode() {
		return territorialCode;
	}

	public void setTerritorialCode(String territorialCode) {
		this.territorialCode = territorialCode;
	}

	public String getCatastralKey() {
		return catastralKey;
	}

	public void setCatastralKey(String catastralKey) {
		this.catastralKey = catastralKey;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public String getRubro() {
		return rubro;
	}

	public void setRubro(String rubro) {
		this.rubro = rubro;
	}

	public List<BondItemPrint> getItemList() {
		return itemList;
	}

	public void setItemList(List<BondItemPrint> itemList) {
		this.itemList = itemList;
	}
	
	public Integer getPrintingsNumber() {
		return printingsNumber;
	}

	public void setPrintingsNumber(Integer printingsNumber) {
		this.printingsNumber = printingsNumber;
	}

	public BondEntryPrint getEntry() {
		return entry;
	}

	public void setEntry(BondEntryPrint entry) {
		this.entry = entry;
	}

}
