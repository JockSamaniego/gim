package ec.gob.gim.waterservice.model;

import java.math.BigDecimal;
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

@Entity
@TableGenerator(
		name = "MaintenanceEntryGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "MaintenanceEntry", 
		initialValue = 1, allocationSize = 1
)



@NamedQueries(value = {		
		@NamedQuery(name = "MaintenanceEntry.findByWaterSupply", query = "SELECT m FROM MaintenanceEntry m "
				+ "WHERE m.waterSupply.id = :waterSupplyId order by m.emitted, m.date desc"),
		
		@NamedQuery(name = "MaintenanceEntry.SumValuesByWaterSupply", query = "SELECT new ec.gob.gim.waterservice.model.MaintenanceEntryDTO(ws.id,sum(m.value)) FROM MaintenanceEntry m "
				+ "JOIN m.waterSupply ws "
				+ "WHERE ws.id in (:waterSuppliesIds) and m.emitted = false and isValid = true " 
				+ "GROUP BY ws.id"),
		
		@NamedQuery(name = "MaintenanceEntry.IdSumValuesByWaterSupply", query = "SELECT m.id FROM MaintenanceEntry m "
				+ "WHERE m.waterSupply.id = :waterSupplyId and m.emitted = false and isValid = true"),
				
		@NamedQuery(name = "MaintenanceEntry.setToEmittedStatus", query = "UPDATE MaintenanceEntry me "
				+ "SET me.emitted = true "				
				+ "WHERE me.waterSupply.id IN (:watersupplyIds)")})

public class MaintenanceEntry {
	
	@Id
	@GeneratedValue(generator = "MaintenanceEntryGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Temporal(TemporalType.TIME)
	private Date time;
	
	private BigDecimal value;
	
	private Boolean emitted;
	
	private Boolean isValid;

	@ManyToOne
	@JoinColumn(name = "waterSupply_id")
	private WaterSupply waterSupply;
	
	public MaintenanceEntry(){
		emitted = Boolean.FALSE;
		isValid = Boolean.TRUE;
	}
	
	public WaterSupply getWaterSupply() {
		return waterSupply;
	}

	public void setWaterSupply(WaterSupply waterSupply) {
		this.waterSupply = waterSupply;
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

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Boolean getEmitted() {
		return emitted;
	}

	public void setEmitted(Boolean emitted) {
		this.emitted = emitted;
	}
	
	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

}
