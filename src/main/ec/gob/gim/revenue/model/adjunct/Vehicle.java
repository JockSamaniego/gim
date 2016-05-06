package ec.gob.gim.revenue.model.adjunct;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import ec.gob.gim.revenue.model.Adjunct;
import ec.gob.gim.revenue.model.adjunct.detail.VehicleMaker;
import ec.gob.gim.revenue.model.adjunct.detail.VehicleType;

@Entity
@DiscriminatorValue("VEH") 
public class Vehicle extends Adjunct{
	private String vin;
	private String engineNumber;
	private Integer year;
	private Double cubicCentimeters;
	private Double weightCapacity;
	@ManyToOne
	private VehicleMaker vehicleMaker;
	@ManyToOne
	private VehicleType vehicleType;
	
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getEngineNumber() {
		return engineNumber;
	}
	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}
	public VehicleMaker getVehicleMaker() {
		return vehicleMaker;
	}
	public void setVehicleMaker(VehicleMaker vehicleMaker) {
		this.vehicleMaker = vehicleMaker;
	}
	public VehicleType getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Double getCubicCentimeters() {
		return cubicCentimeters;
	}
	public void setCubicCentimeters(Double cubicCentimeters) {
		this.cubicCentimeters = cubicCentimeters;
	}
	public Double getWeightCapacity() {
		return weightCapacity;
	}
	public void setWeightCapacity(Double weightCapacity) {
		this.weightCapacity = weightCapacity;
	} 
	
	@Override
	public String toString() {	
		return vehicleMaker.getName() + " - Chasis: " + vin + " - Motor: " + engineNumber;
	}
	
	public List<ValuePair> getDetails(){
		List<ValuePair> details = new LinkedList<ValuePair>();
		ValuePair pair = new ValuePair("Marca", vehicleMaker.getName());
		details.add(pair);
		pair = new ValuePair("Numero chasis", vin);
		details.add(pair);
		pair = new ValuePair("Numero motor: ", engineNumber);
		details.add(pair);
		pair = new ValuePair("Año", year!=null ? year.toString(): "");
		details.add(pair);
		pair = new ValuePair("Cilindraje", cubicCentimeters!=null ? cubicCentimeters.toString(): "");
		details.add(pair);
		
		return details;
	}	
}
