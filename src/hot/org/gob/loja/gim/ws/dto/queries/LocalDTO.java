/**
 * 
 */
package org.gob.loja.gim.ws.dto.queries;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

/**
 * @author René
 *
 */
@NativeQueryResultEntity
public class LocalDTO {

	@NativeQueryResultColumn(index = 0)
	private Long id;

	@NativeQueryResultColumn(index = 1)
	private String name;

	@NativeQueryResultColumn(index = 2)
	private String reference;

	@NativeQueryResultColumn(index = 3)
	private String address;

	@NativeQueryResultColumn(index = 4)
	private String code;

	@NativeQueryResultColumn(index = 5)
	private String businessType;

	@NativeQueryResultColumn(index = 6)
	private String owner;

	@NativeQueryResultColumn(index = 7)
	private String cedRuc;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference
	 *            the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the businessType
	 */
	public String getBusinessType() {
		return businessType;
	}

	/**
	 * @param businessType
	 *            the businessType to set
	 */
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the cedRuc
	 */
	public String getCedRuc() {
		return cedRuc;
	}

	/**
	 * @param cedRuc
	 *            the cedRuc to set
	 */
	public void setCedRuc(String cedRuc) {
		this.cedRuc = cedRuc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LocalsDTO [id=" + id + ", name=" + name + ", reference="
				+ reference + ", address=" + address + ", code=" + code
				+ ", businessType=" + businessType + ", owner=" + owner
				+ ", cedRuc=" + cedRuc + "]";
	}

}
