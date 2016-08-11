package ec.gob.gim.cadaster.model.dto;

import java.math.BigDecimal;
import java.util.List;

import ec.gob.gim.cadaster.model.WorkDealFraction;

public class WorkDealFull {

	private Long id;
	private String address;
	private BigDecimal contributionfront;
	private BigDecimal differentiatedvalue;
	private BigDecimal frontlength;
	private BigDecimal sharedvalue;
	private BigDecimal total;
	private Long propertyId;
	private Long workdealId;
	private BigDecimal sewerageValue;
	private BigDecimal watervalue;
	private BigDecimal commercialappraisal;
	private String identificationnumber;
	private String name;
	private String previouscadastralcode;
	private BigDecimal lotaliquot;
	
	private List<WorkDealFraction> workdealfractions;
	
	public WorkDealFull(){
	}
	
	public WorkDealFull(Long id, String address, BigDecimal contributionfront, BigDecimal differentiatedvalue,
			BigDecimal frontlength, BigDecimal sharedvalue, BigDecimal total, Long propertyId, Long workdealId,
			BigDecimal sewerageValue, BigDecimal watervalue, BigDecimal commercialappraisal,
			String identificationnumber, String name, String previouscadastralcode, BigDecimal lotaliquot,
			List<WorkDealFraction> workdealfractions) {
		this.id = id;
		this.address = address;
		this.contributionfront = contributionfront;
		this.differentiatedvalue = differentiatedvalue;
		this.frontlength = frontlength;
		this.sharedvalue = sharedvalue;
		this.total = total;
		this.propertyId = propertyId;
		this.workdealId = workdealId;
		this.sewerageValue = sewerageValue;
		this.watervalue = watervalue;
		this.commercialappraisal = commercialappraisal;
		this.identificationnumber = identificationnumber;
		this.name = name;
		this.previouscadastralcode = previouscadastralcode;
		this.lotaliquot = lotaliquot;
		this.workdealfractions = workdealfractions;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public BigDecimal getContributionfront() {
		return contributionfront;
	}
	public void setContributionfront(BigDecimal contributionfront) {
		this.contributionfront = contributionfront;
	}
	public BigDecimal getDifferentiatedvalue() {
		return differentiatedvalue;
	}
	public void setDifferentiatedvalue(BigDecimal differentiatedvalue) {
		this.differentiatedvalue = differentiatedvalue;
	}
	public BigDecimal getFrontlength() {
		return frontlength;
	}
	public void setFrontlength(BigDecimal frontlength) {
		this.frontlength = frontlength;
	}
	public BigDecimal getSharedvalue() {
		return sharedvalue;
	}
	public void setSharedvalue(BigDecimal sharedvalue) {
		this.sharedvalue = sharedvalue;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public Long getPropertyId() {
		return propertyId;
	}
	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
	}
	public Long getWorkdealId() {
		return workdealId;
	}
	public void setWorkdealId(Long workdealId) {
		this.workdealId = workdealId;
	}
	public BigDecimal getSewerageValue() {
		return sewerageValue;
	}
	public void setSewerageValue(BigDecimal sewerageValue) {
		this.sewerageValue = sewerageValue;
	}
	public BigDecimal getWatervalue() {
		return watervalue;
	}
	public void setWatervalue(BigDecimal watervalue) {
		this.watervalue = watervalue;
	}
	public BigDecimal getCommercialappraisal() {
		return commercialappraisal;
	}
	public void setCommercialappraisal(BigDecimal commercialappraisal) {
		this.commercialappraisal = commercialappraisal;
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
	public String getPreviouscadastralcode() {
		return previouscadastralcode;
	}
	public void setPreviouscadastralcode(String previouscadastralcode) {
		this.previouscadastralcode = previouscadastralcode;
	}
	public BigDecimal getLotaliquot() {
		return lotaliquot;
	}
	public void setLotaliquot(BigDecimal lotaliquot) {
		this.lotaliquot = lotaliquot;
	}

	public List<WorkDealFraction> getWorkdealfractions() {
		return workdealfractions;
	}

	public void setWorkdealfractions(List<WorkDealFraction> workdealfractions) {
		this.workdealfractions = workdealfractions;
	}
	
	
}
