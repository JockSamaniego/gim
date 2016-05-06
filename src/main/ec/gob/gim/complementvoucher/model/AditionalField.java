package ec.gob.gim.complementvoucher.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

/**
 * @author manuel
 * @version 1.0
 * @created 17-abrl-2015 12:35:33
 */
@Audited
@Entity
@TableGenerator(name = "AditionalFieldGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "AditionalField", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "AditionalField.findAll", query = "SELECT af FROM AditionalField af order by af.label")})
public class AditionalField {

	@Id
	@GeneratedValue(generator = "AditionalFieldGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length = 50, nullable = false)
	private String label;
	
	
	@Column(length = 255, nullable = false)
	private String value;
	 
	@ManyToOne(fetch=FetchType.LAZY)
	private ElectronicVoucher electronicVoucher;
	
	public AditionalField() {
	}

	public void finalize() throws Throwable {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ElectronicVoucher getElectronicVoucher() {
		return electronicVoucher;
	}

	public void setElectronicVoucher(ElectronicVoucher electronicVoucher) {
		this.electronicVoucher = electronicVoucher;
	} 
}// end RetentionCode