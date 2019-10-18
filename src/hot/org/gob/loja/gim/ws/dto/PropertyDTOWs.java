/**
 * 
 */
package org.gob.loja.gim.ws.dto;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

/**
 * @author Rene
 *
 */
@NativeQueryResultEntity
public class PropertyDTOWs {
	
	@NativeQueryResultColumn(index = 0)
	private Integer propertyId;
	
	@NativeQueryResultColumn(index = 1)
	private String residentName;
	
	@NativeQueryResultColumn(index = 2)
	private String cadastralCode;
	
	@NativeQueryResultColumn(index = 3)
	private String previousCadastralCode;
	
	/**
	 * @return the propertyId
	 */
	public Integer getPropertyId() {
		return propertyId;
	}
	/**
	 * @param propertyId the propertyId to set
	 */
	public void setPropertyId(Integer propertyId) {
		this.propertyId = propertyId;
	}
	/**
	 * @return the residentName
	 */
	public String getResidentName() {
		return residentName;
	}
	/**
	 * @param residentName the residentName to set
	 */
	public void setResidentName(String residentName) {
		this.residentName = residentName;
	}
	/**
	 * @return the cadastralCode
	 */
	public String getCadastralCode() {
		return cadastralCode;
	}
	/**
	 * @param cadastralCode the cadastralCode to set
	 */
	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}
	/**
	 * @return the previousCadastralCode
	 */
	public String getPreviousCadastralCode() {
		return previousCadastralCode;
	}
	/**
	 * @param previousCadastralCode the previousCadastralCode to set
	 */
	public void setPreviousCadastralCode(String previousCadastralCode) {
		this.previousCadastralCode = previousCadastralCode;
	}
		
}
