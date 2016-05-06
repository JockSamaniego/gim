package ec.gob.gim.complementvoucher.model;

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.income.model.Receipt;

/**
 * @author macartuche
 * @version 1.0
 * @created 17-abrl-2015 12:35:33
 */
@Audited
@Entity
@TableGenerator(name = "ComplementVoucherGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value",
				pkColumnValue = "ComplementVoucher", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { @NamedQuery(name = "ComplementVoucher.findAll", query = "SELECT cv FROM ComplementVoucher cv order by cv.name"),
		 				@NamedQuery(name = "ComplementVoucher.findById", query = "SELECT cv FROM ComplementVoucher cv where cv.id =:id"),
		 				@NamedQuery(name = "ComplementVoucher.findByInstituion", 
		 				query = "SELECT cv FROM ComplementVoucher cv where cv.institutionService=:institution")})
public class ComplementVoucher {

	@Id
	@GeneratedValue(generator = "ComplementVoucherGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length = 50, nullable = false)
	private String name;
	
	@Column(length = 50, nullable = false)
	private String institutionNumber;
	
	@Column(length = 50, nullable = false)
	private String emisionPointNumber;
	
	@Column(nullable = false)
	private Long currentValue;
	 
	@ManyToOne(fetch=FetchType.EAGER)
	private InstitutionService institutionService;

	@OneToMany(mappedBy = "complementVoucher",  cascade = { CascadeType.MERGE,CascadeType.PERSIST }) 
	private List<TypeEmissionPoint> typesemiEmissionPointsList;	
	
	
	@ManyToOne
	@JoinColumn(name="person_id")
	private Person person;
	
	public ComplementVoucher() {
		typesemiEmissionPointsList = new ArrayList<TypeEmissionPoint>();
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

	public String getInstitutionNumber() {
		return institutionNumber;
	}

	public void setInstitutionNumber(String institutionNumber) {
		this.institutionNumber = institutionNumber;
	}

	public String getEmisionPointNumber() {
		return emisionPointNumber;
	}

	public void setEmisionPointNumber(String emisionPointNumber) {
		this.emisionPointNumber = emisionPointNumber;
	}
	
	public Long getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(Long currentValue) {
		this.currentValue = currentValue;
	}
 
 
	public InstitutionService getInstitutionService() {
		return institutionService;
	}

	public void setInstitutionService(InstitutionService institutionService) {
		this.institutionService = institutionService;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	public List<TypeEmissionPoint> getTypesemiEmissionPointsList() {
		return typesemiEmissionPointsList;
	}

	public void setTypesemiEmissionPointsList(
			List<TypeEmissionPoint> typesemiEmissionPointsList) {
		this.typesemiEmissionPointsList = typesemiEmissionPointsList;
	}

}// end ComplementVoucher

