package ec.gob.gim.security.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Resident;

@Audited
@Entity
@Table(name="_User")
@TableGenerator(name = "UserGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "User", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "User.findByUsernameAndPassword", query = "select u from User u "
				+ "left join fetch u.resident resident "
				+ "left join fetch resident.currentAddress "
				+ "where u.name = :name and u.password = :password"),
		@NamedQuery(name = "User.findByUsername", query = "select u from User u where u.name = :name"),
		@NamedQuery(name = "User.findResidentByUserId", query = "select u.resident from User u where u.id = :userId"),		
		@NamedQuery(name = "User.findNameByUserId", query = "select u.resident.name from User u where u.id = :userId"),
		@NamedQuery(name = "User.findByUserId", query = "select u from User u where u.id = :userId"),
		@NamedQuery(name = "User.findRolesByUserId", query = "select u.roles from User u where u.id = :userId"),
		@NamedQuery(name = "User.lockAll", query = "update User u set u.isBlocked = true where not lower(u.name) = lower(:rootRoleName)"),
		@NamedQuery(name = "User.unlockAll", query = "update User u set u.isBlocked = false  where not lower(u.name) = lower(:rootRoleName)"),
		@NamedQuery(name = "User.findByResidentId", query = "select u from User u inner join fetch u.resident r where r.id = :residentId")
		})
public class User implements Comparable<User> {

	@Id
	@GeneratedValue(generator = "UserGenerator", strategy = GenerationType.TABLE)
	private Long id;
	@Column(nullable = false, unique = true)
	private String name;
	@Column(nullable = false)
	private String password;
	private Boolean isActive;
	private Boolean isBlocked;
	private Date expirationDate;
	private Date passwordRenewalDate;
	
	@Transient
	private String plainPassword;
	@Transient
	private String ipAddress;
	

	@OneToOne(fetch = FetchType.EAGER, mappedBy="user")
	private Resident resident;

	@ManyToMany(mappedBy = "users")
	private List<Role> roles;

	@Column(name="cashier_id")
	private Long cashierid;
	
	//@author macartuche
	//@tag turnero
	//2016-08-02T11:13
	private Boolean callTicket;
	
	public User() {
		roles = new ArrayList<Role>();
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
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public Boolean getIsActive() {
		return isActive == null?  Boolean.FALSE : isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public void add(Role role) {
		if (role != null && !roles.contains(role)) {
			this.roles.add(role);
			role.add(this);
		}
	}

	public void remove(Role role) {
		if (role != null && roles.contains(role)) {
			this.roles.remove(role);
			role.remove(this);
		}
	}

	public Boolean hasRole(String rolename) {		
		if (rolename != null && rolename.length() > 0) {
			for (Role role : roles) {
//				System.out.println("ROLE FOUND "+role.getName());
				if (rolename.trim().equalsIgnoreCase(role.getName().trim())) {
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		User that = (User) o;
		if (getId() != null ? !(getId().equals(that.getId())) : that.getId() != null) {
			return false;
		}

		if (that.getId() == null && getId() == null) {
			return false;
		}

		return true;
	}

	public Boolean getIsBlocked() {
		return isBlocked == null?  Boolean.FALSE : isBlocked;
	}

	public void setIsBlocked(Boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public Date getPasswordRenewalDate() {
		return passwordRenewalDate;
	}

	public void setPasswordRenewalDate(Date passwordRenewalDate) {
		this.passwordRenewalDate = passwordRenewalDate;
	}

	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Override
	public int compareTo(User o) {
		if(o != null){ 
			return this.getResident().getName().compareToIgnoreCase(o.getResident().getName());
		}
		return 0;
	}

	public Long getCashierid() {
		return cashierid;
	}

	public void setCashierid(Long cashierid) {
		this.cashierid = cashierid;
	}

	//@author macartuche
	//@tag turnero
	//2016-08-02T11:14
	public Boolean getCallTicket() {
		return callTicket;
	}

	public void setCallTicket(Boolean callTicket) {
		this.callTicket = callTicket;
	} 
	
}
