package ec.gob.gim.commercial.model;

import java.math.BigInteger;
import java.util.Date;

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
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Person;

@Audited
@Entity
@TableGenerator(name = "OperatingLicenseGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "OperatingLicense", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "OperatingLicense.findByPaperCode", query = "SELECT operatingLicense from OperatingLicense operatingLicense WHERE "
				+ "operatingLicense.paper_code = :code and operatingLicense.nullified = false "), 
		@NamedQuery(name = "OperatingLicense.findByLocalCode", query = "SELECT operatingLicense from OperatingLicense operatingLicense WHERE "
				+ "operatingLicense.local_code = :localCode and operatingLicense.nullified = false ORDER BY operatingLicense.date_emission DESC")
})
public class OperatingLicense {

	@Id
	@GeneratedValue(generator = "OperatingLicenseGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@JoinColumn(name = "date_emission")
	@Temporal(TemporalType.DATE)
	private Date date_emission;

	@JoinColumn(name = "date_validity")
	@Temporal(TemporalType.DATE)
	private Date date_validity;

	@JoinColumn(name = "local_code")
	@Column(length = 20)
	private String local_code;

	@JoinColumn(name = "local_ruc")
	@Column(length = 20)
	private String local_ruc;
	
	@JoinColumn(name = "local_id")
	@Column(length = 20)
	private Long local_id;

	@JoinColumn(name = "responsible_user")
	@Column(length = 40)
	private String responsible_user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "responsible_id")
	private Person responsible;

	@JoinColumn(name = "paper_code")
	@Column(length = 20)
	private String paper_code;
	
	@Column(length = 120)
	private String economic_activity;

	@Column(length = 6)
	private Boolean nullified;

	@Column(length = 120)
	private String reasonNullified;

	@Column(length = 40)
	private String responsible_delegate;

	public Person getResponsible() {
		return responsible;
	}

	public void setResponsible(Person responsible) {
		this.responsible = responsible;
	}

	public OperatingLicense() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate_emission() {
		return date_emission;
	}

	public void setDate_emission(Date date_emission) {
		this.date_emission = date_emission;
	}

	public Date getDate_validity() {
		return date_validity;
	}

	public void setDate_validity(Date date_validity) {
		this.date_validity = date_validity;
	}

	public String getLocal_code() {
		return local_code;
	}

	public void setLocal_code(String local_code) {
		this.local_code = local_code;
	}

	public String getLocal_ruc() {
		return local_ruc;
	}

	public void setLocal_ruc(String local_ruc) {
		this.local_ruc = local_ruc;
	}

	public Long getLocal_id() {
		return local_id;
	}

	public void setLocal_id(Long local_id) {
		this.local_id = local_id;
	}

	public String getResponsible_user() {
		return responsible_user;
	}

	public void setResponsible_user(String responsible_user) {
		this.responsible_user = responsible_user;
	}

	public String getPaper_code() {
		return paper_code;
	}

	public void setPaper_code(String paper_code) {
		this.paper_code = paper_code;
	}

	public String getEconomic_activity() {
		return economic_activity;
	}

	public void setEconomic_activity(String economic_activity) {
		this.economic_activity = economic_activity;
	}

	public Boolean getNullified() {
		return nullified;
	}

	public void setNullified(Boolean nullified) {
		this.nullified = nullified;
	}

	public String getReasonNullified() {
		return reasonNullified;
	}

	public void setReasonNullified(String reasonNullified) {
		this.reasonNullified = reasonNullified;
	}

	public String getResponsible_delegate() {
		return responsible_delegate;
	}

	public void setResponsible_delegate(String responsible_delegate) {
		this.responsible_delegate = responsible_delegate;
	}
}