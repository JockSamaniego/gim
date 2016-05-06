package ec.gob.gim.complementvoucher.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@TableGenerator(name = "RetentionCodeGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "RetentionCode", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "RetentionCode.findAll", query = "SELECT rc FROM RetentionCode rc order by rc.code"),
		@NamedQuery(name = "RetentionCode.findByType", query = "SELECT rc FROM RetentionCode rc "
				+ "where (lower(rc.name) like :name  or lower(rc.formCode) like :name) and rc.typeRetention =:type  order by rc.code"), 
				@NamedQuery(name = "RetentionCode.findByPercentage", query = "SELECT rc FROM RetentionCode rc "
						+ "where  rc.percentage=:percentage and rc.typeRetention =:type order by rc.code"), })
public class RetentionCode {

	@Id
	@GeneratedValue(generator = "RetentionCodeGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length = 50, nullable = false)
	private String code;

	@Column(length = 255, nullable = false)
	private String name;
	
	@Column(length = 255, nullable = true)
	private String formCode;
	
	@OneToMany(mappedBy = "retentionCode")
	private  List<ElectronicVoucherDetail> electronicVoucherDetail;
	
	@Column(length = 10, nullable = false)
	private String typeRetention;
	
	@Column(nullable = true)
	private BigDecimal percentage;
	
	 
	public RetentionCode() {
	}

	public void finalize() throws Throwable {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotAudited
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.toUpperCase();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFormCode() {
		return formCode;
	}

	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}

	public List<ElectronicVoucherDetail> getElectronicVoucherDetail() {
		return electronicVoucherDetail;
	}

	public void setElectronicVoucherDetail(
			List<ElectronicVoucherDetail> electronicVoucherDetail) {
		this.electronicVoucherDetail = electronicVoucherDetail;
	}

	public String getTypeRetention() {
		return typeRetention;
	}

	public void setTypeRetention(String typeRetention) {
		this.typeRetention = typeRetention;
	}

	public BigDecimal getPercentage() {
		return percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}
}// end RetentionCode