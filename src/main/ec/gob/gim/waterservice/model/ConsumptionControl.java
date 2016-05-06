package ec.gob.gim.waterservice.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/*@Entity
@TableGenerator(name = "ConsumptionControlGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "ConsumptionControl", initialValue = 1, allocationSize = 1)*/
@Deprecated
public class ConsumptionControl {
	
	/*@Id
	@GeneratedValue(generator = "ConsumptionControlGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	private Double latitude;
	private Double longitude;

	private boolean receivedLocal;
	private boolean transferredLocal;
	
	private boolean receivedRemote;
	private boolean transferredRemote;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateReceived;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTransferred;

	@OneToOne
	private Consumption consumption;

	public ConsumptionControl() {
		this.latitude = new Double(0);
		this.longitude = new Double(0);
		this.receivedLocal = false;
		this.transferredLocal = false;
		this.receivedRemote = false;
		this.transferredRemote = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public boolean getReceivedLocal() {
		return receivedLocal;
	}

	public void setReceivedLocal(boolean receivedLocal) {
		this.receivedLocal = receivedLocal;
	}

	public boolean getTransferredLocal() {
		return transferredLocal;
	}

	public void setTransferredLocal(boolean transferredLocal) {
		this.transferredLocal = transferredLocal;
	}

	public boolean getReceivedRemote() {
		return receivedRemote;
	}

	public void setReceivedRemote(boolean receivedRemote) {
		this.receivedRemote = receivedRemote;
	}

	public boolean getTransferredRemote() {
		return transferredRemote;
	}

	public void setTransferredRemote(boolean transferredRemote) {
		this.transferredRemote = transferredRemote;
	}

	public Consumption getConsumption() {
		return consumption;
	}

	public void setConsumption(Consumption consumption) {
		this.consumption = consumption;
	}

	public Date getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}

	public Date getDateTransferred() {
		return dateTransferred;
	}

	public void setDateTransferred(Date dateTransferred) {
		this.dateTransferred = dateTransferred;
	}
*/
}
