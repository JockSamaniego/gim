package ec.gob.gim.waterservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:49
 */
@Audited
@Entity
@TableGenerator(name = "WaterMeterStatusGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "WaterMeterStatus", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "WaterMeterStatus.findWaterMeterStatus", query = "SELECT waterMeterStatus FROM WaterMeterStatus waterMeterStatus"),
		@NamedQuery(name = "WaterMeterStatus.findAll", query = "SELECT wms FROM WaterMeterStatus wms"),
		@NamedQuery(name = "WaterMeterStatus.findByIds", query = "SELECT wms FROM WaterMeterStatus wms where wms.id in(:wmsIds)"), 
		@NamedQuery(name = "WaterMeterStatus.findByName", query = "SELECT wms FROM WaterMeterStatus wms where lower(wms.name) = lower(:wmsName)") 

})
public class WaterMeterStatus {

	@Id
	@GeneratedValue(generator = "WaterMeterStatusGenerator", strategy = GenerationType.TABLE)
	private Long id;
	@Column(length = 20, nullable = false)
	private String name;

	public WaterMeterStatus() {

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
}// end WaterMeterStatus