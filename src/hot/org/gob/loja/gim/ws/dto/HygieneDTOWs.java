/**
 * 
 */
package org.gob.loja.gim.ws.dto;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

/**
 * @author Jock
 *
 */
@NativeQueryResultEntity
public class HygieneDTOWs {
	
	@NativeQueryResultColumn(index = 0)
	private String directionType;
	
	@NativeQueryResultColumn(index = 1)
	private String identificationNumber;
	
	@NativeQueryResultColumn(index = 2)
	private String name;
	
	@NativeQueryResultColumn(index = 3)
	private String street;
	
	@NativeQueryResultColumn(index = 4)
	private String phoneNumber;
	
	@NativeQueryResultColumn(index = 5)
	private String mobileNumber;

	public String getDirectionType() {
		return directionType;
	}

	public void setDirectionType(String directionType) {
		this.directionType = directionType;
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

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	
		
}
