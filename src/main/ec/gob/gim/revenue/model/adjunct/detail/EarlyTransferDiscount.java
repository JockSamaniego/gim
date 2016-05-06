package ec.gob.gim.revenue.model.adjunct.detail;

public enum EarlyTransferDiscount {
	NOT_APPLICABLE(0.00),
	FIRST_YEAR(0.40),
	SECOND_YEAR(0.30),
	THIRD_YEAR(0.20);
	
	Double percentage;
	
	private EarlyTransferDiscount(Double percentage) {
		this.percentage = percentage;
	}

	public Double getPercentage() {
		return percentage;
	}
}
