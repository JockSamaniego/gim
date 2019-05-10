package ec.gob.gim.revenue.model.DTO;

public class CrtvVehicleDataDTO {
	
	private String ownerIdentification;
	
	private String ownerName;
	
	private String document;
	
	private String licensePlate;
	
	private String totalValue;
	
	private String paidOrderCode;

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public String getOwnerIdentification() {
		return ownerIdentification;
	}

	public void setOwnerIdentification(String ownerIdentification) {
		this.ownerIdentification = ownerIdentification;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(String totalValue) {
		this.totalValue = totalValue;
	}

	public String getPaidOrderCode() {
		return paidOrderCode;
	}

	public void setPaidOrderCode(String paidOrderCode) {
		this.paidOrderCode = paidOrderCode;
	}
	
	
	
}
