package ec.gob.gim.revenue.model.adjunct;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.gob.gim.common.DateUtils;

import ec.gob.gim.revenue.model.Adjunct;

@NamedQueries(value = {
		@NamedQuery(name = "ANTReference.findFoto-Multa", query = "select count(antRef.contraventionNumber) from ANTReference antRef "				
				+ "where lower(antRef.contraventionNumber) = lower(:contraventionNumber)")
})


@Entity
@DiscriminatorValue("ANTR")
public class ANTReference extends Adjunct {
	
	private String numberPlate;
	
	/**
	 * numero generado por empresa
	 */
	//private Long antNumber;
	
	/**
	 * nuevo numero generad por la empresa
	 * es alfanumerico rfarmijosm 2017-07-19
	 */
	private String contraventionNumber;
	/**
	 * exceso de velocidad
	 */
	private BigDecimal speeding;
	
	/**
	 * tipo liviano o pesado
	 */
	private String vehicleType;
	
	/**
	 * publico o privado
	 */
	private String serviceType;
	
	/**
	 * fecha citacion
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date citationDate;
	
	/**
	 * rfam 2017-07-21
	 * ML-JRM-2017-404-MY
	 * 332 TV-UMTTTSV-2017
	 */
	private String supportDocumentURL;
	
	/**
	 * rfam 2018-02-19
	 * ML-PSM-2018-374
	 * UMTTTSV-FV-2018-0086-O
	 */
	private String physicalInfractionNumber;

	// private String observation;

	@Override
	public String toString() {
		//StringBuilder sb = new StringBuilder();
		//sb.append(numberPlate != null ? numberPlate : "ND");
		//rfarmijosm 2016-03-23
		StringBuilder sb = new StringBuilder();
		sb.append(vehicleType != null ? vehicleType : "ND");
		
		/*sb.append(" - ");
		sb.append(cadastralCode != null ? cadastralCode : "ND");*/
		return sb.toString().toUpperCase();
	}

	public List<ValuePair> getDetails() {
		List<ValuePair> details = new LinkedList<ValuePair>();
		ValuePair pair = new ValuePair("Placa N°", numberPlate);
		details.add(pair);
		
		pair = new ValuePair("Velocidad", speeding != null ? speeding.toString() : "-");
		details.add(pair);
		
		pair = new ValuePair("Tipo", vehicleType != null ? vehicleType.toUpperCase() : "-");
		details.add(pair);
		
		pair = new ValuePair("Servicio", serviceType != null ? serviceType.toUpperCase() : "-");
		details.add(pair);
		
		pair = new ValuePair("Fecha citación", citationDate != null ? DateUtils.formatFullDate(citationDate) : "-");
		details.add(pair);
		
		pair = new ValuePair("Nro. Infracción", contraventionNumber != null ? contraventionNumber.toUpperCase() : "-");
		details.add(pair);
		
		if (physicalInfractionNumber != null && physicalInfractionNumber.equals("")) {
			pair = new ValuePair("Boleta Infracción", physicalInfractionNumber != null ? physicalInfractionNumber.toUpperCase() : "-");
			details.add(pair);
		}
		
		return details;
	}

	public String getNumberPlate() {
		return numberPlate;
	}

	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
	}

	/*public Long getAntNumber() {
		return antNumber;
	}
	public void setAntNumber(Long antNumber) {
		this.antNumber = antNumber;
	}*/

	public BigDecimal getSpeeding() {
		return speeding;
	}

	public void setSpeeding(BigDecimal speeding) {
		this.speeding = speeding;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Date getCitationDate() {
		return citationDate;
	}

	public void setCitationDate(Date citationDate) {
		this.citationDate = citationDate;
	}

	public String getContraventionNumber() {
		return contraventionNumber;
	}

	public void setContraventionNumber(String contraventionNumber) {
		this.contraventionNumber = contraventionNumber;
	}
	
	public String getSupportDocumentURL() {
		return supportDocumentURL;
	}

	public void setSupportDocumentURL(String supportDocumentURL) {
		this.supportDocumentURL = supportDocumentURL;
	}

	public String getPhysicalInfractionNumber() {
		return physicalInfractionNumber;
	}

	public void setPhysicalInfractionNumber(String physicalInfractionNumber) {
		this.physicalInfractionNumber = physicalInfractionNumber;
	}

	
	
}