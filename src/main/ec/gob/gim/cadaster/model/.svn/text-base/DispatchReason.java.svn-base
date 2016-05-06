package ec.gob.gim.cadaster.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

@Audited
@Entity
@TableGenerator(name = "DispatchReasonGenerator", 
				table = "IdentityGenerator", 
				pkColumnName = "name", 
				valueColumnName = "value", 
				pkColumnValue = "DispatchReason", 
				initialValue = 1, 
				allocationSize = 1)
public class DispatchReason {
	@Id
	@GeneratedValue(generator = "DispatchReasonGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	private String observation;
	
	private DispatchReasonType dispatchReasonType; 

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public String getObservation() {
		return observation;
	}

	public void setDispatchReasonType(DispatchReasonType dispatchReasonType) {
		this.dispatchReasonType = dispatchReasonType;
	}

	public DispatchReasonType getDispatchReasonType() {
		return dispatchReasonType;
	}

}
