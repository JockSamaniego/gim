package ec.gob.gim.revenue.model.adjunct;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ec.gob.gim.revenue.model.Adjunct;
import ec.gob.gim.waterservice.model.Consumption;
import ec.gob.gim.waterservice.model.MaintenanceEntryDTO;
import ec.gob.gim.waterservice.model.Route;

@Entity
@DiscriminatorValue("WSR")
public class WaterServiceReference extends Adjunct {

	private String waterMeterNumber;

	private String waterMeterStatus;

	private String waterSupplyCategory;

	private String routeName;

	private Long consumptionPreviousReading;

	private Long consumptionCurrentReading;
	
	private BigDecimal accruedInterest;
	
	@Transient
	private MaintenanceEntryDTO maintenanceEntryDTO;
	
//	private String serviceNumber; 
//	private String serviceAddress;
	
	@ManyToOne
	private Route route;
	
	@ManyToOne
	private Consumption consumption;

	private BigDecimal consumptionAmount;

	private BigDecimal exemptionValue;
	
	private String exemptionType;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(waterMeterNumber != null ? waterMeterNumber : "ND");
		sb.append(" - ");
		sb.append(waterSupplyCategory != null ? waterSupplyCategory : "ND");
		sb.append(" - ");
		sb.append(waterMeterStatus != null ? waterMeterStatus : "ND");
		return sb.toString();
	}

	public List<ValuePair> getDetails() {
		List<ValuePair> details = new LinkedList<ValuePair>();
				
		ValuePair pair;
//		pair = new ValuePair("Servicio", serviceNumber);//numeroSer / mes-anio
//		details.add(pair);
//		
		pair = new ValuePair("Número medidor", waterMeterNumber);
		details.add(pair);

		pair = new ValuePair("Estado medidor", waterMeterStatus);
		details.add(pair);
		
		pair = new ValuePair("Categoría", waterSupplyCategory);
		details.add(pair);
		
		pair = new ValuePair("Ruta", routeName);
		details.add(pair);
		
		pair = new ValuePair("Lectura anterior", consumptionPreviousReading != null ? consumptionPreviousReading.toString() : "0");
		details.add(pair);
		
		pair = new ValuePair("Lectura actual",consumptionCurrentReading != null ? consumptionCurrentReading.toString() : "0");
		details.add(pair);
		
		pair = new ValuePair("Consumo (m3)", consumptionAmount != null ? consumptionAmount.toString() : "0");
		details.add(pair);
		
		pair = new ValuePair("Tipo exención", exemptionType);
		details.add(pair);
		
		pair = new ValuePair("Valor exención", exemptionValue != null ? exemptionValue.toString() : "0");
		details.add(pair);
		
//		pair = new ValuePair("Ubicación", serviceAddress != null ? serviceAddress : "-");
//		details.add(pair);
		
		return details;
	}

	public String getWaterMeterNumber() {
		return waterMeterNumber;
	}

	public void setWaterMeterNumber(String waterMeterNumber) {
		this.waterMeterNumber = waterMeterNumber;
	}

	public String getWaterMeterStatus() {
		return waterMeterStatus;
	}

	public void setWaterMeterStatus(String waterMeterStatus) {
		this.waterMeterStatus = waterMeterStatus;
	}

	public String getWaterSupplyCategory() {
		return waterSupplyCategory;
	}

	public void setWaterSupplyCategory(String waterSupplyCategory) {
		this.waterSupplyCategory = waterSupplyCategory;
	}

	public Long getConsumptionPreviousReading() {
		return consumptionPreviousReading;
	}

	public void setConsumptionPreviousReading(Long consumptionPreviousReading) {
		this.consumptionPreviousReading = consumptionPreviousReading;
	}

	public Long getConsumptionCurrentReading() {
		return consumptionCurrentReading;
	}

	public void setConsumptionCurrentReading(Long consumptionCurrentReading) {
		this.consumptionCurrentReading = consumptionCurrentReading;
	}

	public BigDecimal getConsumptionAmount() {
		return consumptionAmount;
	}

	public void setConsumptionAmount(BigDecimal consumptionAmount) {
		this.consumptionAmount = consumptionAmount;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public Consumption getConsumption() {
		return consumption;
	}

	public void setConsumption(Consumption consumption) {
		this.consumption = consumption;
	}

	public MaintenanceEntryDTO getMaintenanceEntryDTO() {
		return maintenanceEntryDTO;
	}

	public void setMaintenanceEntryDTO(MaintenanceEntryDTO maintenanceEntryDTO) {
		this.maintenanceEntryDTO = maintenanceEntryDTO;
	}

	public BigDecimal getExemptionValue() {
		return exemptionValue;
	}

	public void setExemptionValue(BigDecimal exemptionValue) {
		this.exemptionValue = exemptionValue;
	}

	public String getExemptionType() {
		return exemptionType;
	}

	public void setExemptionType(String exemptionType) {
		this.exemptionType = exemptionType;
	}

	public BigDecimal getAccruedInterest() {
		return accruedInterest;
	}

	public void setAccruedInterest(BigDecimal accruedInterest) {
		this.accruedInterest = accruedInterest;
	}
	
	

//	public String getServiceNumber() {
//		return serviceNumber;
//	}
//
//	public void setServiceNumber(String serviceNumber) {
//		this.serviceNumber = serviceNumber;
//	}
//
//	public String getServiceAddress() {
//		return serviceAddress;
//	}
//
//	public void setServiceAddress(String serviceAddress) {
//		this.serviceAddress = serviceAddress;
//	}
//
}
