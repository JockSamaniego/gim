package ec.gob.gim.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.gob.gim.common.IdentificationNumberUtil;
import org.gob.gim.common.exception.IdentificationNumberExistsException;
import org.gob.gim.common.exception.IdentificationNumberSizeException;
import org.gob.gim.common.exception.IdentificationNumberWrongException;
import org.gob.gim.common.exception.InvalidIdentificationNumberException;
import org.gob.gim.common.exception.InvalidIdentificationNumberFinishedException;
import org.gob.loja.gim.ws.dto.Taxpayer;
import org.hibernate.envers.Audited;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:37
 */
@Audited
@Entity
@DiscriminatorValue(value="J")
public class LegalEntity extends Resident {

	@Temporal(TemporalType.DATE)
	private Date constitutionDate;
	
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private LegalEntityType legalEntityType;
	
	private String code;

	public LegalEntity(){
	}

	/**
	 * @return the constitutionDate
	 */
	public Date getConstitutionDate() {
		return constitutionDate;
	}

	/**
	 * @param constitutionDate the constitutionDate to set
	 */
	public void setConstitutionDate(Date constitutionDate) {
		this.constitutionDate = constitutionDate;
	}

	/**
	 * @return the legalEntityType
	 */
	public LegalEntityType getLegalEntityType() {
		return legalEntityType;
	}

	/**
	 * @param legalEntityType the legalEntityType to set
	 */
	public void setLegalEntityType(LegalEntityType legalEntityType) {
		this.legalEntityType = legalEntityType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public static LegalEntity createInstance(Taxpayer taxpayer){
		LegalEntity legalEntity = new LegalEntity();
		
		legalEntity.setName(taxpayer.getFirstName());
		legalEntity.setIdentificationNumber(taxpayer.getIdentificationNumber());
		legalEntity.setIdentificationType(IdentificationType.TAXPAYER_DOCUMENT);
		legalEntity.setEmail(taxpayer.getEmail());
		// Actual Direccion
		legalEntity.getCurrentAddress().setStreet(taxpayer.getStreet());
		legalEntity.getCurrentAddress().setPhoneNumber(taxpayer.getPhoneNumber());
		legalEntity.getCurrentAddress().setCity("Loja");
		legalEntity.getCurrentAddress().setCountry("Ecuador");
			
		return legalEntity;
		
	}

	public Boolean isIdentificationNumberValid(String identificationNumber) throws IdentificationNumberSizeException, IdentificationNumberWrongException, 
	InvalidIdentificationNumberException, InvalidIdentificationNumberFinishedException, IdentificationNumberExistsException{
		if(getIdentificationType() == IdentificationType.TAXPAYER_DOCUMENT){
			return IdentificationNumberUtil.getInstance().isTaxpayerNumberValid(identificationNumber);
		}
		return true;
	}
	
}//end LegalEntity