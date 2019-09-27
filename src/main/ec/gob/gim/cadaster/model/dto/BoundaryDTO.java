package ec.gob.gim.cadaster.model.dto;

public class BoundaryDTO {
	
	private Long id;
	
	private String description;
	
	private double length;
	
	private String compasspoint;
	
	private Long domain_id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public String getCompasspoint() {
		return compasspoint;
	}

	public void setCompasspoint(String compasspoint) {
		this.compasspoint = compasspoint;
	}

	public Long getDomain_id() {
		return domain_id;
	}

	public void setDomain_id(Long domain_id) {
		this.domain_id = domain_id;
	}
	
}
