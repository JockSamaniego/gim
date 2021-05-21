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
public class EntryDTO {

	@NativeQueryResultColumn(index = 0)
	private Long id;

	@NativeQueryResultColumn(index = 1)
	private String code;

	@NativeQueryResultColumn(index = 2)
	private String department;

	@NativeQueryResultColumn(index = 3)
	private String description;

	@NativeQueryResultColumn(index = 4)
	private String name;

	@NativeQueryResultColumn(index = 5)
	private String reason;

	@NativeQueryResultColumn(index = 6)
	private String completeName;

	@NativeQueryResultColumn(index = 7)
	private String accountCode;

	@NativeQueryResultColumn(index = 8)
	private String accountName;

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
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * @param department
	 *            the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason
	 *            the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * @return the completeName
	 */
	public String getCompleteName() {
		return completeName;
	}

	/**
	 * @param completeName
	 *            the completeName to set
	 */
	public void setCompleteName(String completeName) {
		this.completeName = completeName;
	}

	/**
	 * @return the accountCode
	 */
	public String getAccountCode() {
		return accountCode;
	}

	/**
	 * @param accountCode
	 *            the accountCode to set
	 */
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName
	 *            the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EntryDTO [id=" + id + ", code=" + code + ", department="
				+ department + ", description=" + description + ", name="
				+ name + ", reason=" + reason + ", completeName="
				+ completeName + ", accountCode=" + accountCode
				+ ", accountName=" + accountName + "]";
	}

}
