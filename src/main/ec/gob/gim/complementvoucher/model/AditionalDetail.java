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
@TableGenerator(name = "AditionalDetailGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value",
pkColumnValue = "AditionalDetail", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "AditionalDetail.findAll", query = "SELECT ad FROM AditionalDetail ad order by ad.name")})
public class AditionalDetail {

	@Id
	@GeneratedValue(generator = "AditionalDetailGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length = 50, nullable = false)
	private String name;
	
	
	@Column(length = 255, nullable = false)
	private String value;
	 
	@ManyToOne(fetch=FetchType.LAZY)
	private ElectronicItem item;
	
	public AditionalDetail() {
	}

	public void finalize() throws Throwable {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	 
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ElectronicItem getItem() {
		return item;
	}

	public void setItem(ElectronicItem item) {
		this.item = item;
	} 
}// end RetentionCode