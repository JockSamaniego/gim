package ec.gob.gim.waterservice.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:48
 */
@Audited
@Entity
@TableGenerator(name = "WaterMeterGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "WaterMeter", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "WaterMeter.findByWaterSupply", 
					query = "SELECT ws.waterMeters from WaterSupply ws JOIN ws.waterMeters wM " +
							"where ws.id = :wsId and wM.isActive = :active"),
		@NamedQuery(name = "WaterMeter.findByWaterSupplyActualMeter", 
					query = "SELECT ws.waterMeters from WaterSupply ws left outer join ws.waterMeters wM " +
							"where ws.id = :wsId and wM.isActive = :active order by wM.isActive DESC"),
		@NamedQuery(name = "WaterMeter.findDTOByWaterSupplyActualMeter", 
					query = "SELECT NEW ec.gob.gim.waterservice.model.WaterMeterDTO(wm.digitsNumber,ws.id) from WaterSupply ws " +
							"join ws.waterMeters wm " +
							"where wm.isActive = :active GROUP BY ws.id,wm.id")})
public class WaterMeter {

	@Id
	@GeneratedValue(generator = "WaterMeterGenerator", strategy = GenerationType.TABLE)
	private Long id;

	private Integer digitsNumber;
	private BigDecimal dimension;

	private Boolean isActive;
	@Column(length = 20)
	private String serial;

	@ManyToOne
	@JoinColumn(name = "waterMeterStatus_id")
	private WaterMeterStatus waterMeterStatus;

	public WaterMeter() {
		isActive=Boolean.TRUE;
	}

	public Integer getDigitsNumber() {
		return digitsNumber;
	}

	public void setDigitsNumber(Integer digitsNumber) {
		this.digitsNumber = digitsNumber;
	}

	public BigDecimal getDimension() {
		return dimension;
	}

	public void setDimension(BigDecimal dimension) {
		this.dimension = dimension;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public WaterMeterStatus getWaterMeterStatus() {
		return waterMeterStatus;
	}

	public void setWaterMeterStatus(WaterMeterStatus waterMeterStatus) {
		this.waterMeterStatus = waterMeterStatus;
	}
}// end WaterMeter