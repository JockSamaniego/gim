package ec.gob.gim.revenue.model.adjunct;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.gob.gim.common.DateUtils;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Adjunct;

@Audited
@NamedQueries(value = {
		@NamedQuery(name = "InfringementANTReference.countInfringementByCitationNumber", query = "select count(infrig.citationNumber) from InfringementANTReference infrig "
				+ "where lower(infrig.citationNumber) = lower(:citationNumber)") })

@Entity
@DiscriminatorValue("IANTR")
public class InfringementANTReference extends Adjunct {

	private String citationNumber;
	private String infringementType;
	private Date infringementDate;
	// private String infringementaddress; va al bondAddres
	private String numberPlate;
	private BigDecimal lostPoints;
	private String origin;

	@ManyToOne
	private Resident transitAgent;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(citationNumber != null ? citationNumber : "ND");

		sb.append(" - ");
		sb.append(numberPlate != null ? numberPlate : "ND");

		return sb.toString().toUpperCase();
	}

	public List<ValuePair> getDetails() {
		List<ValuePair> details = new LinkedList<ValuePair>();
		ValuePair pair = new ValuePair("Placa N°", numberPlate);
		details.add(pair);

		pair = new ValuePair("Nro. Citación", citationNumber != null ? citationNumber : "-");
		details.add(pair);

		pair = new ValuePair("Placa", numberPlate != null ? numberPlate : "-");
		details.add(pair);

		pair = new ValuePair("infringementType", infringementType != null ? infringementType.toUpperCase() : "-");
		details.add(pair);

		pair = new ValuePair("Fecha infracción",
				infringementDate != null ? DateUtils.formatFullDate(infringementDate) : "-");
		details.add(pair);

		return details;
	}

	public String getCitationNumber() {
		return citationNumber;
	}

	public void setCitationNumber(String citationNumber) {
		this.citationNumber = citationNumber;
	}

	public String getInfringementType() {
		return infringementType;
	}

	public void setInfringementType(String infringementType) {
		this.infringementType = infringementType;
	}

	public Date getInfringementDate() {
		return infringementDate;
	}

	public void setInfringementDate(Date infringementDate) {
		this.infringementDate = infringementDate;
	}

	public String getNumberPlate() {
		return numberPlate;
	}

	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
	}

	public BigDecimal getLostPoints() {
		return lostPoints;
	}

	public void setLostPoints(BigDecimal lostPoints) {
		this.lostPoints = lostPoints;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Resident getTransitAgent() {
		return transitAgent;
	}

	public void setTransitAgent(Resident transitAgent) {
		this.transitAgent = transitAgent;
	}

}
