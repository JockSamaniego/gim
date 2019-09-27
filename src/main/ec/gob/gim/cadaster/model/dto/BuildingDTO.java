package ec.gob.gim.cadaster.model.dto;

import java.math.BigDecimal;

public class BuildingDTO {
	
	private Long id;
	private Integer anioconst;
	private BigDecimal area;
	private Integer buildingyear;
	private String externalfinishing;
	private Integer floorsnumber;
	private Boolean hasequipment;
	private Boolean isfinished;
	private Integer number;
	private String preservationstate;
	private String roofmaterial;
	private String structurematerial;
	private String wallmaterial;
	private Long property_id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getAnioconst() {
		return anioconst;
	}
	public void setAnioconst(Integer anioconst) {
		this.anioconst = anioconst;
	}
	public BigDecimal getArea() {
		return area;
	}
	public void setArea(BigDecimal area) {
		this.area = area;
	}
	public Integer getBuildingyear() {
		return buildingyear;
	}
	public void setBuildingyear(Integer buildingyear) {
		this.buildingyear = buildingyear;
	}
	public String getExternalfinishing() {
		return externalfinishing;
	}
	public void setExternalfinishing(String externalfinishing) {
		this.externalfinishing = externalfinishing;
	}
	public Integer getFloorsnumber() {
		return floorsnumber;
	}
	public void setFloorsnumber(Integer floorsnumber) {
		this.floorsnumber = floorsnumber;
	}
	public Boolean getHasequipment() {
		return hasequipment;
	}
	public void setHasequipment(Boolean hasequipment) {
		this.hasequipment = hasequipment;
	}
	public Boolean getIsfinished() {
		return isfinished;
	}
	public void setIsfinished(Boolean isfinished) {
		this.isfinished = isfinished;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getPreservationstate() {
		return preservationstate;
	}
	public void setPreservationstate(String preservationstate) {
		this.preservationstate = preservationstate;
	}
	public String getRoofmaterial() {
		return roofmaterial;
	}
	public void setRoofmaterial(String roofmaterial) {
		this.roofmaterial = roofmaterial;
	}
	public String getStructurematerial() {
		return structurematerial;
	}
	public void setStructurematerial(String structurematerial) {
		this.structurematerial = structurematerial;
	}
	public String getWallmaterial() {
		return wallmaterial;
	}
	public void setWallmaterial(String wallmaterial) {
		this.wallmaterial = wallmaterial;
	}
	public Long getProperty_id() {
		return property_id;
	}
	public void setProperty_id(Long property_id) {
		this.property_id = property_id;
	}
}
