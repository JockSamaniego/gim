package ec.gob.gim.income.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Person;

/**
 * Caja
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:48
 */
//@Audited
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"branch_id", "number"})) 
@TableGenerator(
	 name="TillGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Till",
	 initialValue=1, allocationSize=1
)
 @NamedQueries(value = {
			@NamedQuery(name="Till.findActive", query="SELECT o FROM Till o LEFT JOIN FETCH o.branch b WHERE o.isActive = true"),
                        @NamedQuery(name="Till.findIsTillBank", query="SELECT  o FROM Till o WHERE o.isTillBank = true AND o.isActive = true"),
			@NamedQuery(name="Till.findByPersonId", query="SELECT o FROM Till o WHERE o.person.id = :personId"),
			@NamedQuery(name="Till.findById", query="SELECT o FROM Till o WHERE o.id = :tillId"),
			@NamedQuery(name="Till.findBranchByTillId", query="SELECT o.branch FROM Till o WHERE o.id = :tillId")
})

public class Till implements Comparable<Till>{

	@Id
	@GeneratedValue(generator="TillGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(nullable=false)
	private Integer number;
	
	private Integer windowNumber;
	
	@Column(length=30)
	private String address;
	
	@Column(nullable=false)
	private boolean isActive = Boolean.FALSE;

	private boolean isTillBank = Boolean.FALSE;

	private boolean emissionElectronicOnLine = Boolean.FALSE;

	private boolean electronicInvoiceEnable = Boolean.FALSE;

	@ManyToOne(cascade=CascadeType.ALL)
	private Branch branch;
	

	@OneToOne
	@JoinColumn(name = "person_id")
	private Person person;	
	
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Till(){

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isActive() {
		return isActive;
	}
	
	public boolean isTillBank() {
		return isTillBank;
	}

	public void setTillBank(boolean isTillBank) {
		this.isTillBank = isTillBank;
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

		Till that = (Till) o;
		if (getId() != null ? !(getId().equals(that.getId())) : that.getId() != null) {
			return false;
		}

		if (that.getId() == null && getId() == null) {
			return false;
		}

		return true;		
		/*
		
		if (!(object instanceof Till)) {
			return false;
		}
		Till other = (Till) object;
					
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
		*/
	}

	public Integer getWindowNumber() {
		return windowNumber;
	}

	public void setWindowNumber(Integer windowNumber) {
		this.windowNumber = windowNumber;
	}

	public boolean isEmissionElectronicOnLine() {
		return emissionElectronicOnLine;
	}

	public void setEmissionElectronicOnLine(boolean emissionElectronicOnLine) {
		this.emissionElectronicOnLine = emissionElectronicOnLine;
	}

	public boolean isElectronicInvoiceEnable() {
		return electronicInvoiceEnable;
	}

	public void setElectronicInvoiceEnable(boolean electronicInvoiceEnable) {
		this.electronicInvoiceEnable = electronicInvoiceEnable;
	}

	@Override
	public int compareTo(Till o) {
		if(o != null && this.getNumber() != null && o.getNumber() != null){
			return this.getNumber().compareTo(o.getNumber());
		}
		return 0;
	}
	
}