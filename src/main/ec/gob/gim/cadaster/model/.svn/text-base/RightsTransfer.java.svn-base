package ec.gob.gim.cadaster.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Resident;

@Audited
@Entity
	@TableGenerator(
		name = "RightsTransferGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 	
		valueColumnName = "value", 
		pkColumnValue = "RightsTransfer", 
		initialValue = 1, allocationSize = 1)
@Deprecated
public class RightsTransfer {

	@Id
	@GeneratedValue(generator = "RightsTransferGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seller_id")
	private Resident seller;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "buyer_id")
	private Resident buyer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "property_id")
	private Property property;

	@Temporal(TemporalType.DATE)
	private Date date;
	
	private Integer actionsNumber;
	private Integer heirsNumber;
	private BigDecimal commercialValue;
	private String observation;
	private String tramitNumber;
	
	public RightsTransfer() {
		date = new Date();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getActionsNumber() {
		return actionsNumber;
	}

	public void setActionsNumber(Integer actionsNumber) {
		this.actionsNumber = actionsNumber;
	}

	public Integer getHeirsNumber() {
		return heirsNumber;
	}

	public void setHeirsNumber(Integer heirsNumber) {
		this.heirsNumber = heirsNumber;
	}

	public BigDecimal getCommercialValue() {
		return commercialValue;
	}

	public void setCommercialValue(BigDecimal commercialValue) {
		this.commercialValue = commercialValue;
	}

	public Resident getSeller() {
		return seller;
	}

	public void setSeller(Resident seller) {
		this.seller = seller;
	}

	public Resident getBuyer() {
		return buyer;
	}

	public void setBuyer(Resident buyer) {
		this.buyer = buyer;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public String getTramitNumber() {
		return tramitNumber;
	}

	public void setTramitNumber(String tramitNumber) {
		this.tramitNumber = tramitNumber;
	}
	
}
