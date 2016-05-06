package ec.gob.gim.complementvoucher.model;

import java.util.List;

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

import org.hibernate.envers.Audited;

import ec.gob.gim.revenue.model.MunicipalBond;

/**
 * @author macartuche
 * @version 1.0
 * @created 17-abrl-2015 12:35:33
 */
@Audited
@Entity
@TableGenerator(name = "TypeEmissionPointGenerator", 
	table = "IdentityGenerator", 
	pkColumnName = "name", 
	valueColumnName = "value", 
	pkColumnValue = "TypeEmissionPoint", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { 
		@NamedQuery(name = "TypeEmissionPoint.findAll", query = "SELECT tep FROM TypeEmissionPoint tep"),
		@NamedQuery(name = "TypeEmissionPoint.findByPersonAndType", 
					query = "SELECT tep FROM TypeEmissionPoint tep WHERE tep.complementVoucher.person=:person and tep.complementVoucherType.code =:code")
})
public class TypeEmissionPoint {

	@Id
	@GeneratedValue(generator = "TypeEmissionPointGenerator", strategy = GenerationType.TABLE)
	private Long id;
	 

	@ManyToOne(fetch=FetchType.EAGER) 
	@JoinColumn(name="complementvoucher_id")
	private ComplementVoucher complementVoucher;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "complementvouchertype_id")
	private ComplementVoucherType complementVoucherType;

	@Column(nullable = false)
	private Long currentValue;
	 	
	public TypeEmissionPoint() {
	}

	public void finalize() throws Throwable {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public ComplementVoucher getComplementVoucher() {
		return complementVoucher;
	}

	public void setComplementVoucher(ComplementVoucher complementVoucher) {
		this.complementVoucher = complementVoucher;
	}

	public ComplementVoucherType getComplementVoucherType() {
		return complementVoucherType;
	}

	public void setComplementVoucherType(ComplementVoucherType complementVoucherType) {
		this.complementVoucherType = complementVoucherType;
	}

	public Long getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(Long currentValue) {
		this.currentValue = currentValue;
	}
}// end TypeEmissionPoint