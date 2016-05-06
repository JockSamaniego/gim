package ec.gob.gim.common.model;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import ec.gob.gim.revenue.model.MunicipalBond;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:34
 */

@Audited
@Entity
@TableGenerator(name = "FiscalPeriodGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "FiscalPeriod", initialValue = 1, allocationSize = 1)
@NamedQueries({
		@NamedQuery(name = "FiscalPeriod.findAll", query = "select o from FiscalPeriod o order by o.startDate"),
		@NamedQuery(name = "FiscalPeriod.findAllNames", query = "select o.name from FiscalPeriod o order by o.startDate"),
		@NamedQuery(name = "FiscalPeriod.findCurrent", query = "select o from FiscalPeriod o where :currentDate >= o.startDate and :currentDate <= o.endDate order by o.startDate"),
		@NamedQuery(name = "FiscalPeriod.findMaxDate", query = "select max(o.endDate) from FiscalPeriod o"),
		@NamedQuery(name = "FiscalPeriod.findMinDate", query = "select min(o.startDate) from FiscalPeriod o"),
		@NamedQuery(name = "FiscalPeriod.findById", query = "select o from FiscalPeriod o where o.id = :id ") })
		
public class FiscalPeriod {

	@Id
	@GeneratedValue(generator = "FiscalPeriodGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date endDate;

	@Column(length = 60, nullable=false)
	private String name;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date startDate;
	
	private BigDecimal basicSalary;
	
	private BigDecimal basicSalaryUnifiedForRevenue;
	
	private BigDecimal salariesNumber;

	private BigDecimal mortgageRate; // Porcentaje por Crédito Hipotecario

	@OneToMany(mappedBy = "fiscalPeriod", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<MunicipalBond> municipalBonds;

	public FiscalPeriod() {
		this.municipalBonds = new ArrayList<MunicipalBond>();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the creditTitles
	 */
	public List<MunicipalBond> getMunicipalBonds() {
		return municipalBonds;
	}

	/**
	 * @param municipalBonds
	 *            the creditTitles to set
	 */
	public void setMunicipalBonds(List<MunicipalBond> municipalBonds) {
		this.municipalBonds = municipalBonds;
	}

	public void add(MunicipalBond municipalBond) {
		if (!this.municipalBonds.contains(municipalBond)) {
			this.municipalBonds.add(municipalBond);
			municipalBond.setFiscalPeriod(this);
		}
	}

	public void remove(MunicipalBond municipalBond) {
		boolean removed = this.municipalBonds.remove(municipalBond);
		if (removed)
			municipalBond.setFiscalPeriod((FiscalPeriod) null);
	}

	@Override
	public int hashCode() {
		int hash = 13;
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

		FiscalPeriod that = (FiscalPeriod) o;
		if (getId() != null ? !(getId().equals(that.getId())) : that.getId() != null) {
			return false;
		}

		if (that.getId() == null && getId() == null) {
			return false;
		}

		return true;
	}

	public BigDecimal getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(BigDecimal basicSalary) {
		this.basicSalary = basicSalary;
	}

	public BigDecimal getSalariesNumber() {
		return salariesNumber;
	}

	public void setSalariesNumber(BigDecimal salariesNumber) {
		this.salariesNumber = salariesNumber;
	}

	public BigDecimal getMortgageRate() {
		return mortgageRate;
	}

	public void setMortgageRate(BigDecimal mortgageRate) {
		this.mortgageRate = mortgageRate;
	}

	public BigDecimal getBasicSalaryUnifiedForRevenue() {
		return basicSalaryUnifiedForRevenue;
	}

	public void setBasicSalaryUnifiedForRevenue(
			BigDecimal basicSalaryUnifiedForRevenue) {
		this.basicSalaryUnifiedForRevenue = basicSalaryUnifiedForRevenue;
	}

	/**
	 * 2015-05-04
	 * macartuche
	 */
	public String getFiscalYear() {
		return name.substring(14, 19);		
	}
}// end FiscalPeriod