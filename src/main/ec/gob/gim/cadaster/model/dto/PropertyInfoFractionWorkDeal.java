/**
 * 
 */
package ec.gob.gim.cadaster.model.dto;

import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

/**
 * @author rene
 *
 */
@NativeQueryResultEntity
public class PropertyInfoFractionWorkDeal {
	
	@NativeQueryResultColumn(index = 0)	
	private Long property_id;
	
	@NativeQueryResultColumn(index = 1)
	private String identificationNumber;
	
	@NativeQueryResultColumn(index = 2)
	private String name;
	
	@NativeQueryResultColumn(index = 3)
	private String previouscadastralcode;
	
	@NativeQueryResultColumn(index = 4)
	private String cadastralCode;
	
	@NativeQueryResultColumn(index = 5)
	private BigDecimal lotalicuot;

	public Long getProperty_id() {
		return property_id;
	}

	public void setProperty_id(Long property_id) {
		this.property_id = property_id;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
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

	public String getCadastralCode() {
		return cadastralCode;
	}

	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}

	public BigDecimal getLotalicuot() {
		return lotalicuot;
	}

	public void setLotalicuot(BigDecimal lotalicuot) {
		this.lotalicuot = lotalicuot;
	}
	
}