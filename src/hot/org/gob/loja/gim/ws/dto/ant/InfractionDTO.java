/**
 * 
 */
package org.gob.loja.gim.ws.dto.ant;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Rene
 *
 */
public class InfractionDTO {
	private Date infractionDate;
	private String citationNumber;
	private String type;
	private String plate;
	private String origin;
	private String identificationAgent;
	private String nameAgent;
	private String place;
	private String observation;
	private BigDecimal points;
	private String literalName;
	private String literalDescription;
	private BigDecimal percentSBU;
	private String articleNumber;
	private String articleDescription;
	/**
	 * @return the infractionDate
	 */
	public Date getInfractionDate() {
		return infractionDate;
	}
	/**
	 * @param infractionDate the infractionDate to set
	 */
	public void setInfractionDate(Date infractionDate) {
		this.infractionDate = infractionDate;
	}
	/**
	 * @return the citationNumber
	 */
	public String getCitationNumber() {
		return citationNumber;
	}
	/**
	 * @param citationNumber the citationNumber to set
	 */
	public void setCitationNumber(String citationNumber) {
		this.citationNumber = citationNumber;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the plate
	 */
	public String getPlate() {
		return plate;
	}
	/**
	 * @param plate the plate to set
	 */
	public void setPlate(String plate) {
		this.plate = plate;
	}
	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}
	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	/**
	 * @return the identificationAgent
	 */
	public String getIdentificationAgent() {
		return identificationAgent;
	}
	/**
	 * @param identificationAgent the identificationAgent to set
	 */
	public void setIdentificationAgent(String identificationAgent) {
		this.identificationAgent = identificationAgent;
	}
	/**
	 * @return the nameAgent
	 */
	public String getNameAgent() {
		return nameAgent;
	}
	/**
	 * @param nameAgent the nameAgent to set
	 */
	public void setNameAgent(String nameAgent) {
		this.nameAgent = nameAgent;
	}
	/**
	 * @return the place
	 */
	public String getPlace() {
		return place;
	}
	/**
	 * @param place the place to set
	 */
	public void setPlace(String place) {
		this.place = place;
	}
	/**
	 * @return the observation
	 */
	public String getObservation() {
		return observation;
	}
	/**
	 * @param observation the observation to set
	 */
	public void setObservation(String observation) {
		this.observation = observation;
	}
	/**
	 * @return the points
	 */
	public BigDecimal getPoints() {
		return points;
	}
	/**
	 * @param points the points to set
	 */
	public void setPoints(BigDecimal points) {
		this.points = points;
	}
	/**
	 * @return the literalName
	 */
	public String getLiteralName() {
		return literalName;
	}
	/**
	 * @param literalName the literalName to set
	 */
	public void setLiteralName(String literalName) {
		this.literalName = literalName;
	}
	/**
	 * @return the literalDescription
	 */
	public String getLiteralDescription() {
		return literalDescription;
	}
	/**
	 * @param literalDescription the literalDescription to set
	 */
	public void setLiteralDescription(String literalDescription) {
		this.literalDescription = literalDescription;
	}
	/**
	 * @return the percentSBU
	 */
	public BigDecimal getPercentSBU() {
		return percentSBU;
	}
	/**
	 * @param percentSBU the percentSBU to set
	 */
	public void setPercentSBU(BigDecimal percentSBU) {
		this.percentSBU = percentSBU;
	}
	/**
	 * @return the articleNumber
	 */
	public String getArticleNumber() {
		return articleNumber;
	}
	/**
	 * @param articleNumber the articleNumber to set
	 */
	public void setArticleNumber(String articleNumber) {
		this.articleNumber = articleNumber;
	}
	/**
	 * @return the articleDescription
	 */
	public String getArticleDescription() {
		return articleDescription;
	}
	/**
	 * @param articleDescription the articleDescription to set
	 */
	public void setArticleDescription(String articleDescription) {
		this.articleDescription = articleDescription;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InfractionDTO [infractionDate=" + infractionDate
				+ ", citationNumber=" + citationNumber + ", type=" + type
				+ ", plate=" + plate + ", origin=" + origin
				+ ", identificationAgent=" + identificationAgent
				+ ", nameAgent=" + nameAgent + ", place=" + place
				+ ", observation=" + observation + ", points=" + points
				+ ", literalName=" + literalName + ", literalDescription="
				+ literalDescription + ", percentSBU=" + percentSBU
				+ ", articleNumber=" + articleNumber + ", articleDescription="
				+ articleDescription + "]";
	}
		
}
