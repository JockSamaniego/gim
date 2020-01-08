package ec.gob.gim.income.model;

import java.util.Collections;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:25
 */
//@Audited
@Entity
 @TableGenerator(
	 name="BranchGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Branch",
	 initialValue=1, allocationSize=1
 )
 @NamedQueries(value = {@NamedQuery(name="Branch.findAll", query="select branch from Branch branch order by branch.number"),
		 				@NamedQuery(name="Branch.findByNumber", query="select branch from Branch branch where branch.number = :number"),
		 				@NamedQuery(name="Branch.findCompensationCashierCollectors", query="select t.person from Branch branch join branch.tills t where branch.isPaidByCompensation = true")})
 public class Branch{

	@Id
	@GeneratedValue(generator="BranchGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(columnDefinition="varchar(50)")	
	private String name;
	
	@Column(unique = true)
	private Integer number;
	
	private String address;
	
	private Boolean isPaidByCompensation;
	
	private Boolean isMain = Boolean.FALSE;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	@OneToMany(mappedBy="branch", cascade=CascadeType.ALL)
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private java.util.List<Till> tills;

	public Branch(){
		tills = new java.util.ArrayList<Till>();
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

	public java.util.List<Till> getTills() {
		Collections.sort(tills);
		return tills;
	}

	public void setTills(java.util.List<Till> tills) {
		this.tills = tills;
	}
	
	public void add(Till till){
		if(!this.tills.contains(till)){
			this.tills.add(till);
			till.setBranch(this);
		}
	}
	
	public void remove(Till till){
		boolean removed = this.tills.remove(till);
		if (removed) till.setBranch((Branch)null);
	}

	public void setIsPaidByCompensation(Boolean isPaidByCompensation) {
		this.isPaidByCompensation = isPaidByCompensation;
	}

	public Boolean getIsPaidByCompensation() {
		return isPaidByCompensation;
	}
	
	@Override
	public boolean equals(Object object) {		
		if (!(object instanceof Branch)) {
			return false;
		}
		Branch other = (Branch) object;
					
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	public Boolean getIsMain() {
		return isMain;
	}

	public void setIsMain(Boolean isMain) {
		this.isMain = isMain;
	}
	
}//end Branch