package ec.gob.gim.rental.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.cadaster.model.CompassPoint;
import ec.gob.gim.revenue.model.Contract;

@Audited
@Entity
@TableGenerator(
		name = "SpaceGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "Space", 
		initialValue = 1, allocationSize = 1
)
public class Space {
	@Id
	@GeneratedValue(generator = "SpaceGenerator", strategy = GenerationType.TABLE)
	private Long id;
	

	private double width;
	
	private double height;
	
	private double antennaHeight;
	
	private String address;
	
	private boolean hasPreEmit;
	
	@Transient
	private double totalArea;
	
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private CompassPoint compassPoint;
	
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private SpaceStatus spaceStatus;
	
	@ManyToOne
	private SpaceType spaceType;
	
	@OneToOne(cascade=CascadeType.ALL)
	private Contract currentContract;
	
	@OneToMany(mappedBy="", cascade=CascadeType.ALL)
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<Contract> contracts;
	
	public Space(){
		contracts = new ArrayList<Contract>();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public CompassPoint getCompassPoint() {
		return compassPoint;
	}

	public void setCompassPoint(CompassPoint compassPoint) {
		this.compassPoint = compassPoint;
	}

	public SpaceStatus getSpaceStatus() {
		return spaceStatus;
	}

	public void setSpaceStatus(SpaceStatus spaceStatus) {
		this.spaceStatus = spaceStatus;
	}

	public SpaceType getSpaceType() {
		return spaceType;
	}

	public void setSpaceType(SpaceType spaceType) {
		this.spaceType = spaceType;
	}

	public Contract getCurrentContract() {
		return currentContract;
	}

	public void setCurrentContract(Contract currentContract) {
		this.currentContract = currentContract;
	}

	public List<Contract> getContracts() {
		return contracts;
	}

	public void setContracts(List<Contract> contracts) {
		this.contracts = contracts;
	}

	public void setTotalArea(double totalArea) {
		this.totalArea = totalArea;
	}

	public double getTotalArea() {
		return totalArea;
	}

	public void setAntennaHeight(double antennaHeight) {
		this.antennaHeight = antennaHeight;
	}

	public double getAntennaHeight() {
		return antennaHeight;
	}
	
	public void add(Contract c){
		if(!this.contracts.contains(c) && c != null){
			this.contracts.add(c);
		}
	}
	
	public void remove(Contract c){
		if(c != null && this.contracts.contains(c)  ){
			this.contracts.remove(c);
		}
	}

	public void setHasPreEmit(boolean hasPreEmit) {
		this.hasPreEmit = hasPreEmit;
	}

	public boolean isHasPreEmit() {
		return hasPreEmit;
	}

}
