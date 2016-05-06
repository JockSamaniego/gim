package ec.gob.gim.cadaster.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PropertyHistoryDTO {

	private Date date;
	private String username;
	private String cadastralCode;
	private BigDecimal area;
	private BigDecimal front;
	private BigDecimal frontsLength;
	private String observations;
	private BigDecimal side;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCadastralCode() {
		return cadastralCode;
	}
	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}
	public BigDecimal getArea() {
		return area;
	}
	public void setArea(BigDecimal area) {
		this.area = area;
	}
	public BigDecimal getFront() {
		return front;
	}
	public void setFront(BigDecimal front) {
		this.front = front;
	}
	public BigDecimal getFrontsLength() {
		return frontsLength;
	}
	public void setFrontsLength(BigDecimal frontsLength) {
		this.frontsLength = frontsLength;
	}
	public String getObservations() {
		return observations;
	}
	public void setObservations(String observations) {
		this.observations = observations;
	}
	public BigDecimal getSide() {
		return side;
	}
	public void setSide(BigDecimal side) {
		this.side = side;
	}
	@Override
	public String toString() {
		return "PropertyHistoryDTO [date=" + date + ", username=" + username
				+ ", cadastralCode=" + cadastralCode + ", area=" + area
				+ ", front=" + front + ", frontsLength=" + frontsLength
				+ ", observations=" + observations + ", side=" + side + "]";
	}
	
}