package org.gob.gim.commercial.action;

import java.util.Arrays;
import java.util.Calendar;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.commercial.model.Local;

@Name("localLicenseList")
public class LocalLicenseList extends EntityQuery<Local> {

	private static final long serialVersionUID = 1L;
	
	static Calendar cal4 = Calendar.getInstance();	
	private static String fiscalYear=Integer.toString(cal4.get(Calendar.YEAR));

	private static final String EJBQL = "SELECT local from Local local WHERE "+
			"local.isActive = true AND local.licenseyear = '"+fiscalYear+"')";

	private static final String[] RESTRICTIONS = {
			"lower(local.id) = lower(#{localLicenseList.idLocal})",
			"lower(local.business.name) = lower(#{localLicenseList.commercialLocal})",
			"lower(local.name) = lower(#{localLicenseList.localName})",
			"lower(local.isActive) = lower(#{localLicenseList.isActive})",
			"lower(local.business.owner.name) = lower(#{localLicenseList.ownerName})",
			"lower(local.address.street) = lower(#{localLicenseList.localAddress})",
			"lower(local.cedruc) = lower(#{localLicenseList.localRuc})",
			"lower(local.licenseyear) = lower(#{localLicenseList.licenseYear})",
	};

	private Long idLocal;
	private String commercialLocal;
	private String localName;
	private Boolean isActive;
	private String ownerName;
	private String localAddress;
	private Boolean localRuc;
	private String licenseYear;

	public LocalLicenseList() {
		setOrder("local.business.name, local.name, local.business.owner.name");
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(100);		
	}


	public Long getIdLocal() {
		return idLocal;
	}


	public void setIdLocal(Long idLocal) {
		this.idLocal = idLocal;
	}


	public String getCommercialLocal() {
		return commercialLocal;
	}


	public void setCommercialLocal(String commercialLocal) {
		this.commercialLocal = commercialLocal;
	}


	public String getLocalName() {
		return localName;
	}


	public void setLocalName(String localName) {
		this.localName = localName;
	}


	public Boolean getIsActive() {
		return isActive;
	}


	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}


	public String getOwnerName() {
		return ownerName;
	}


	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}


	public String getLocalAddress() {
		return localAddress;
	}


	public void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
	}


	public Boolean getLocalRuc() {
		return localRuc;
	}


	public void setLocalRuc(Boolean localRuc) {
		this.localRuc = localRuc;
	}


	public String getLicenseYear() {
		return licenseYear;
	}


	public void setLicenseYear(String licenseYear) {
		this.licenseYear = licenseYear;
	}

}
