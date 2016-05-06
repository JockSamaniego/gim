package ec.gob.gim.common.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.income.model.TaxpayerRecord;

@Audited
@Entity
@TableGenerator(name="ChargeGenerator",
				pkColumnName="name",
				pkColumnValue="Charge",
				table="IdentityGenerator",
				valueColumnName="value",
				allocationSize = 1, initialValue = 1)	

@NamedQueries(value = {
		@NamedQuery(name="Charge.findDepartments", 
				query="select charge.department from Charge charge ")
})
public class Charge {
	
	@Id
	@GeneratedValue(generator="ChargeGenerator", strategy=GenerationType.TABLE)
	private Long id;
	
	private String name;
	
	private String department;
	
	@ManyToOne
	private TaxpayerRecord institution;
	
	@OneToMany(mappedBy="charge", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)	
	private List<Delegate> delegates;
	
	public Charge(){
		delegates = new ArrayList<Delegate>();
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
		if(name != null) this.name = name.toUpperCase();
	}

	public void setInstitution(TaxpayerRecord institution) {
		this.institution = institution;
	}

	public TaxpayerRecord getInstitution() {
		return institution;
	}

	public void setDelegates(List<Delegate> delegates) {
		this.delegates = delegates;
	}

	public List<Delegate> getDelegates() {
		return delegates;
	}
	
	public void add(Delegate d){
		if(!delegates.contains(d) && d != null){
			delegates.add(d);	
			d.setCharge(this);
		}		
	}
	
	public void remove(Delegate d){
		if(delegates.contains(d)){
			delegates.remove(d);			
		}		
	}

	public void setDepartment(String department) {
		if(department != null) this.department = department.toUpperCase();
	}

	public String getDepartment() {
		return department;
	}
}
