package ec.gob.gim.cadaster.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

@Audited
@Entity
@TableGenerator(name = "BoundaryGenerator", 
				table = "IdentityGenerator", 
				pkColumnName = "name", 
				valueColumnName = "value", 
				pkColumnValue = "Boundary", 
				initialValue = 1, 
				allocationSize = 1)
public class Boundary {
	@Id
	@GeneratedValue(generator = "BoundaryGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	private String description;
	
	private double length;
	
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private CompassPoint compassPoint;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setCompassPoint(CompassPoint compassPoint) {
		this.compassPoint = compassPoint;
	}

	public CompassPoint getCompassPoint() {
		return compassPoint;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getLength() {
		return length;
	}
	

}
