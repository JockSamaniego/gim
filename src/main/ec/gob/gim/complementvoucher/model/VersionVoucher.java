package ec.gob.gim.complementvoucher.model;

import java.util.Date;
import java.util.List;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

/**
 * @author manuel
 * @version 1.0
 * @created 17-abril-2015 17:45
 */
@Audited
@Entity
@TableGenerator(name = "VersionVoucherGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "VersionVoucher", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { @NamedQuery(name = "VersionVoucher.findAll", query = "SELECT v FROM VersionVoucher v order by v.startDate") })
public class VersionVoucher {

	@Id
	@GeneratedValue(generator = "VersionVoucherGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date startDate;

	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	@Column(nullable = false)
	private Boolean active;
	
	@Column(length = 20, nullable = false)
	private String xsd;
	
	@OneToMany(mappedBy = "versionVoucher", cascade = {CascadeType.ALL}) 
	private List<ComplementVoucherType> complementVoucherTypeList;
	
	public VersionVoucher() {
	}

	public void finalize() throws Throwable {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getXsd() {
		return xsd;
	}

	public void setXsd(String xsd) {
		this.xsd = xsd;
	}
}// end InstitutionService