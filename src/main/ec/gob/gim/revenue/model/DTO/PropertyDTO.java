package ec.gob.gim.revenue.model.DTO;

import java.math.BigDecimal;
import java.util.Date;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

import ec.gob.gim.common.model.ItemCatalog;

@NativeQueryResultEntity
public class PropertyDTO {

	@NativeQueryResultColumn(index = 0)
	private Long property_id;

	@NativeQueryResultColumn(index = 1)
	private String cadastralcode;

	@NativeQueryResultColumn(index = 2)
	private String previouscadastralcode;

	@NativeQueryResultColumn(index = 3)
	private String resident_name;

	private Boolean selected = Boolean.FALSE;

	private ItemCatalog treatmentType;

	private BigDecimal amountCreditYear1;

	private BigDecimal amountCreditYear2;

	private BigDecimal amountCreditYear3;

	// @tag descuentoEmprendimiento
	// @author macartuche
	// @date 2016-09-23
	private Date validUntil;

	public Long getProperty_id() {
		return property_id;
	}

	public void setProperty_id(Long property_id) {
		this.property_id = property_id;
	}

	public String getCadastralcode() {
		return cadastralcode;
	}

	public void setCadastralcode(String cadastralcode) {
		this.cadastralcode = cadastralcode;
	}

	public String getPreviouscadastralcode() {
		return previouscadastralcode;
	}

	public void setPreviouscadastralcode(String previouscadastralcode) {
		this.previouscadastralcode = previouscadastralcode;
	}

	public String getResident_name() {
		return resident_name;
	}

	public void setResident_name(String resident_name) {
		this.resident_name = resident_name;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public ItemCatalog getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(ItemCatalog treatmentType) {
		this.treatmentType = treatmentType;
	}

	public BigDecimal getAmountCreditYear1() {
		return amountCreditYear1;
	}

	public void setAmountCreditYear1(BigDecimal amountCreditYear1) {
		this.amountCreditYear1 = amountCreditYear1;
	}

	public BigDecimal getAmountCreditYear2() {
		return amountCreditYear2;
	}

	public void setAmountCreditYear2(BigDecimal amountCreditYear2) {
		this.amountCreditYear2 = amountCreditYear2;
	}

	public BigDecimal getAmountCreditYear3() {
		return amountCreditYear3;
	}

	public void setAmountCreditYear3(BigDecimal amountCreditYear3) {
		this.amountCreditYear3 = amountCreditYear3;
	}

	public Date getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

	@Override
	public String toString() {
		return "PropertyDTO [property_id=" + property_id + ", cadastralcode="
				+ cadastralcode + ", previouscadastralcode="
				+ previouscadastralcode + ", resident_name=" + resident_name
				+ ", selected=" + selected + ", treatmentType=" + treatmentType
				+ "]";
	}

}