package ec.gob.gim.revenue.model;

import java.util.Date;

import javax.persistence.Entity;
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

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.common.model.FiscalPeriod;


@Audited
@Entity
@TableGenerator(name="LandExemptionGenerator",
				pkColumnName="name",
				pkColumnValue="LandExemption",
				table="IdentityGenerator",
				valueColumnName="value",
				allocationSize = 1, initialValue = 1)
@NamedQueries({
	@NamedQuery(name = "LandExemption.findByFiscalPeriodAndResident", 
			query = "select le from LandExemption le " +
					"where le.fiscalPeriod.id =:fiscalPeriodId and le.property.id = :propertyId"),
	@NamedQuery(name = "LandExemption.findByFiscalPeriod", 
			query = "select le from LandExemption le " +
					"where le.fiscalPeriod.id =:fiscalPeriodId")
	})
public class LandExemption {
	
	@Id
	@GeneratedValue(generator="LandExemptionGenerator", strategy=GenerationType.TABLE)
	private Long id;
	
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Temporal(TemporalType.TIME)
	private Date time;
		
	@ManyToOne
	@JoinColumn(name="property_id")
	private Property property;
	
	@ManyToOne
	@JoinColumn(name="fiscalPeriod_id")
	private FiscalPeriod fiscalPeriod;
	
	public LandExemption(){
		date = new Date();
		time = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public FiscalPeriod getFiscalPeriod() {
		return fiscalPeriod;
	}

	public void setFiscalPeriod(FiscalPeriod fiscalPeriod) {
		this.fiscalPeriod = fiscalPeriod;
	}
	
	
}
