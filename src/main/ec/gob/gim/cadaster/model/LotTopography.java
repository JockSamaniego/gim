package ec.gob.gim.cadaster.model;

import java.math.BigDecimal;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:38
 */
public enum LotTopography {
	IN_LEVEL(new BigDecimal(1.0)),
	UNDER_LEVEL(new BigDecimal(0.95)),
	ABOVE_LEVEL(new BigDecimal(0.98)),
	PRECIPITOUS(new BigDecimal(0.75));
	
	BigDecimal factor;
	
	private LotTopography(BigDecimal factor) {
		this.factor = factor;
	}

	public BigDecimal getFactor() {
		return factor;
	}

	public void setFactor(BigDecimal factor) {
		this.factor = factor;
	}

}