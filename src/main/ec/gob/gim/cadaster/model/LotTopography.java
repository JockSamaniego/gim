package ec.gob.gim.cadaster.model;

import java.math.BigDecimal;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:38
 */
public enum LotTopography {
	IN_LEVEL(new BigDecimal(1.0), new BigDecimal(1.0)),
	UNDER_LEVEL(new BigDecimal(0.95), new BigDecimal(1.0)),
	ABOVE_LEVEL(new BigDecimal(0.98), new BigDecimal(1.0)),
	PRECIPITOUS(new BigDecimal(0.75), new BigDecimal(1.0));
	
	BigDecimal factor;
	BigDecimal factorOrd43_2021; // Ordenanza 43-2021
	
	private LotTopography(BigDecimal factor, BigDecimal factorOrd43_2021) {
		this.factor = factor;
		this.factorOrd43_2021 = factorOrd43_2021;
	}

	public BigDecimal getFactor() {
		return factor;
	}

	public void setFactor(BigDecimal factor) {
		this.factor = factor;
	}

	public BigDecimal getFactorOrd43_2021() {
		return factorOrd43_2021;
	}

	public void setFactorOrd43_2021(BigDecimal factorOrd43_2021) {
		this.factorOrd43_2021 = factorOrd43_2021;
	}

}