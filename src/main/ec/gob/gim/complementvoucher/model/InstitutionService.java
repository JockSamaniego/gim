package ec.gob.gim.complementvoucher.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.revenue.model.Item;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.security.model.Action;

/**
 * @author manuel
 * @version 1.0
 * @created 02-ago-2013 16:54:33
 */
@Audited
@Entity
@TableGenerator(name = "InstitutionServiceGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "InstitutionService", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { 
		@NamedQuery(name = "InstitutionService.findAll", query = "SELECT isv FROM InstitutionService isv order by isv.name"),
		@NamedQuery(name = "InstitutionService.findMatrix", query = "SELECT isv FROM InstitutionService isv where isv.isMatrix=:isMatrix order by isv.name")})
public class InstitutionService {

	@Id
	@GeneratedValue(generator = "InstitutionServiceGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length = 50, nullable = false)
	private String name;
	
	@Column(length = 50, nullable = false)
	private String address;
	
	@Column(length = 50, nullable = true)
	private String resolution;
	
	@Column(length = 50, nullable = true)
	private String email;
	
	@Column(length = 50, nullable = true)
	private String phone;
	
	@Column( nullable = false)
	private Boolean isMatrix;
	
	@Column( nullable = true)
	private String ruc;
	
	@Column( nullable = true)
	private Boolean accountingRecords;
	
	
	@OneToMany(mappedBy = "institutionService", cascade = { CascadeType.MERGE,CascadeType.PERSIST })
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<ComplementVoucher> complementVoucherList;
	 
	 
	@OneToMany(mappedBy="parent")
	private List<InstitutionService> institutions;
	
	@ManyToOne
	private InstitutionService parent;
	
	@Transient
	private String hasaccountingRecords;
	
	public InstitutionService() { 
		complementVoucherList = new ArrayList<ComplementVoucher>();
	}

	public void finalize() throws Throwable {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.toUpperCase();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public Boolean getIsMatrix() {
		return isMatrix;
	}

	public void setIsMatrix(Boolean isMatrix) {
		this.isMatrix = isMatrix;
	} 

	public Boolean getAccountingRecords() {
		return accountingRecords;
	}

	public void setAccountingRecords(Boolean accountingRecords) {
		this.accountingRecords = accountingRecords;
	}

	public List<InstitutionService> getInstitutions() {
		return institutions;
	}

	public void setInstitutions(List<InstitutionService> institutions) {
		this.institutions = institutions;
	}

	public InstitutionService getParent() {
		return parent;
	}

	public void setParent(InstitutionService parent) {
		this.parent = parent;
	}

	public String getHasaccountingRecords() {
		return (getAccountingRecords())? "Si": "No";
	}

	public void setHasaccountingRecords(String hasaccountingRecords) {
		this.hasaccountingRecords = hasaccountingRecords;
	}

	public List<ComplementVoucher> getComplementVoucherList() {
		return complementVoucherList;
	}

	public void setComplementVoucherList(
			List<ComplementVoucher> complementVoucherList) {
		this.complementVoucherList = complementVoucherList;
	}
}// end InstitutionService