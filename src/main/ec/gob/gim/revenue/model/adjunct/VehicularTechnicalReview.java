package ec.gob.gim.revenue.model.adjunct;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import ec.gob.gim.revenue.model.Adjunct;
import ec.gob.gim.revenue.model.adjunct.detail.VehicleMaker;
import ec.gob.gim.revenue.model.adjunct.detail.VehicleType;

@Audited
@Entity
@DiscriminatorValue("REV") 
public class VehicularTechnicalReview extends Adjunct{
	private String vin;
	private String engineNumber;
	private Integer year;
	private Double cubicCentimeters;
	private Double weightCapacity;
	@ManyToOne(fetch=FetchType.LAZY)
	private VehicleMaker vehicleMaker;
	@ManyToOne(fetch=FetchType.LAZY)
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
		pair = new ValuePair("Número chasis", vin);
		details.add(pair);
		pair = new ValuePair("Número motor: ", engineNumber);
		details.add(pair);
		pair = new ValuePair("Año", year!=null ? year.toString(): "");
		details.add(pair);
		pair = new ValuePair("Cilindraje", cubicCentimeters!=null ? cubicCentimeters.toString(): "");
		details.add(pair);
		
		return details;
	}	
}
