package ec.gob.gim.waterservice.model;

import java.math.BigDecimal;

public class WaterConsumptionIndicator {

	private WaterMeterStatus wms;
	private ConsumptionRange consumptionRange;
	
	// //// numero de abonados
	private Long subscriber_good;
	private Long subscriber_damaged;
	private Long subscriber_unmetered;
	private Long subscriber_sewerage;

	// //// consumo en metros cubicos
	private BigDecimal consumption_good;
	private BigDecimal consumption_damaged;
	private BigDecimal consumption_unmetered;

	// //// costos del consumo
	private BigDecimal agua;
	private BigDecimal alcantarillado;
	private BigDecimal p_maestro;
	private BigDecimal s_ciudadana;
	private BigDecimal m_cuencas;
	private BigDecimal c_basico;
	private BigDecimal basura;
	private BigDecimal paidTotal;

	public ConsumptionRange getConsumptionRange() {
		return consumptionRange;
	}

	public void setConsumptionRange(ConsumptionRange consumptionRange) {
		this.consumptionRange = consumptionRange;
	}

	public Long getSubscriber_good() {
		return subscriber_good;
	}

	public void setSubscriber_good(Long subscriber_good) {
		this.subscriber_good = subscriber_good;
	}

	public Long getSubscriber_damaged() {
		return subscriber_damaged;
	}

	public void setSubscriber_damaged(Long subscriber_damaged) {
		this.subscriber_damaged = subscriber_damaged;
	}

	public Long getSubscriber_unmetered() {
		return subscriber_unmetered;
	}

	public void setSubscriber_unmetered(Long subscriber_unmetered) {
		this.subscriber_unmetered = subscriber_unmetered;
	}

	/**
	 * @return the subscriber_sewerage
	 */
	public Long getSubscriber_sewerage() {
		return subscriber_sewerage;
	}

	/**
	 * @param subscriber_sewerage the subscriber_sewerage to set
	 */
	public void setSubscriber_sewerage(Long subscriber_sewerage) {
		this.subscriber_sewerage = subscriber_sewerage;
	}

	public BigDecimal getAgua() {
		return agua;
	}

	public void setAgua(BigDecimal agua) {
		this.agua = agua;
	}

	public BigDecimal getAlcantarillado() {
		return alcantarillado;
	}

	public void setAlcantarillado(BigDecimal alcantarillado) {
		this.alcantarillado = alcantarillado;
	}

	public BigDecimal getP_maestro() {
		return p_maestro;
	}

	public void setP_maestro(BigDecimal p_maestro) {
		this.p_maestro = p_maestro;
	}

	public BigDecimal getS_ciudadana() {
		return s_ciudadana;
	}

	public void setS_ciudadana(BigDecimal s_ciudadana) {
		this.s_ciudadana = s_ciudadana;
	}

	public BigDecimal getM_cuencas() {
		return m_cuencas;
	}

	public void setM_cuencas(BigDecimal m_cuencas) {
		this.m_cuencas = m_cuencas;
	}

	public BigDecimal getC_basico() {
		return c_basico;
	}

	public void setC_basico(BigDecimal c_basico) {
		this.c_basico = c_basico;
	}

	public BigDecimal getBasura() {
		return basura;
	}

	public void setBasura(BigDecimal basura) {
		this.basura = basura;
	}

	public BigDecimal getPaidTotal() {
		return paidTotal;
	}

	public void setPaidTotal(BigDecimal paidTotal) {
		this.paidTotal = paidTotal;
	}

	public BigDecimal getConsumption_good() {
		return consumption_good;
	}

	public void setConsumption_good(BigDecimal consumption_good) {
		this.consumption_good = consumption_good;
	}

	public BigDecimal getConsumption_damaged() {
		return consumption_damaged;
	}

	public void setConsumption_damaged(BigDecimal consumption_damaged) {
		this.consumption_damaged = consumption_damaged;
	}

	public BigDecimal getConsumption_unmetered() {
		return consumption_unmetered;
	}

	public void setConsumption_unmetered(BigDecimal consumption_unmetered) {
		this.consumption_unmetered = consumption_unmetered;
	}

	public WaterMeterStatus getWms() {
		return wms;
	}

	public void setWms(WaterMeterStatus wms) {
		this.wms = wms;
	}

}
