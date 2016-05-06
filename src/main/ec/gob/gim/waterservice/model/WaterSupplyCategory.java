package ec.gob.gim.waterservice.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:49
 */
@Audited
@Entity
@TableGenerator(name = "WaterSupplyCategoryGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "WaterSupplyCategory", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "WaterSupplyCategory.findByName", query = "SELECT wsc FROM WaterSupplyCategory wsc "
				+ "WHERE lower(wsc.name) LIKE lower(concat(:waterSupplyCategoryName,'%'))"),
		@NamedQuery(name = "WaterSupplyCategory.findAll", query = "SELECT wsc FROM WaterSupplyCategory wsc "),
		@NamedQuery(name = "WaterSupplyCategory.findBydIds", query = "SELECT wsc FROM WaterSupplyCategory wsc where wsc.id in(:wscIds) ") })
public class WaterSupplyCategory {

	@Id
	@GeneratedValue(generator = "WaterSupplyCategoryGenerator", strategy = GenerationType.TABLE)
	private Long id;
	@Column(length = 20, nullable = false)
	private String name;

	//todo quitar esto no se usa, esta en las reglas de drools
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "waterSupplyCategory_id")
	private List<Tariff> tariffs;

	@Transient
	private List<WaterConsumptionIndicator> consumptionIndicators;
	
	@Transient
	private Long subscriber_good_total = new Long(0);
	@Transient
	private Long subscriber_damaged_total = new Long(0);
	@Transient
	private Long subscriber_unmetered_total = new Long(0);

	// //// consumo en metros cubicos
	@Transient
	private BigDecimal consumption_good_total = new BigDecimal(0);
	@Transient
	private BigDecimal consumption_damaged_total = new BigDecimal(0);
	@Transient
	private BigDecimal consumption_unmetered_total = new BigDecimal(0);

	// //// costos del consumo
	@Transient
	private BigDecimal agua_total;
	@Transient
	private BigDecimal alcantarillado_total;
	@Transient
	private BigDecimal p_maestro_total;
	@Transient
	private BigDecimal s_ciudadana_total;
	@Transient
	private BigDecimal m_cuencas_total;
	@Transient
	private BigDecimal c_basico_total;
	@Transient
	private BigDecimal basura_total;
	@Transient
	private BigDecimal paidTotal_total = new BigDecimal(0);

	public WaterSupplyCategory() {
		this.tariffs = new ArrayList<Tariff>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name.toUpperCase();
	}

	public List<Tariff> getTariffs() {
		return tariffs;
	}

	public void setTariffs(List<Tariff> tariffs) {
		this.tariffs = tariffs;
	}

	public void add(Tariff tariff) {
		if (!this.tariffs.contains(tariff)) {
			this.tariffs.add(tariff);
		}
	}

	public void remove(Tariff tariff) {
		this.tariffs.remove(tariff);
	}

	public List<WaterConsumptionIndicator> getConsumptionIndicators() {
		return consumptionIndicators;
	}

	public void setConsumptionIndicators(
			List<WaterConsumptionIndicator> consumptionIndicators) {
		this.consumptionIndicators = consumptionIndicators;
	}

	public Long getSubscriber_good_total() {
		return subscriber_good_total;
	}

	public void setSubscriber_good_total(Long subscriber_good_total) {
		this.subscriber_good_total = subscriber_good_total;
	}

	public Long getSubscriber_damaged_total() {
		return subscriber_damaged_total;
	}

	public void setSubscriber_damaged_total(Long subscriber_damaged_total) {
		this.subscriber_damaged_total = subscriber_damaged_total;
	}

	public Long getSubscriber_unmetered_total() {
		return subscriber_unmetered_total;
	}

	public void setSubscriber_unmetered_total(Long subscriber_unmetered_total) {
		this.subscriber_unmetered_total = subscriber_unmetered_total;
	}

	public BigDecimal getConsumption_good_total() {
		return consumption_good_total;
	}

	public void setConsumption_good_total(BigDecimal consumption_good_total) {
		this.consumption_good_total = consumption_good_total;
	}

	public BigDecimal getConsumption_damaged_total() {
		return consumption_damaged_total;
	}

	public void setConsumption_damaged_total(BigDecimal consumption_damaged_total) {
		this.consumption_damaged_total = consumption_damaged_total;
	}

	public BigDecimal getConsumption_unmetered_total() {
		return consumption_unmetered_total;
	}

	public void setConsumption_unmetered_total(
			BigDecimal consumption_unmetered_total) {
		this.consumption_unmetered_total = consumption_unmetered_total;
	}

	public BigDecimal getAgua_total() {
		return agua_total;
	}

	public void setAgua_total(BigDecimal agua_total) {
		this.agua_total = agua_total;
	}

	public BigDecimal getAlcantarillado_total() {
		return alcantarillado_total;
	}

	public void setAlcantarillado_total(BigDecimal alcantarillado_total) {
		this.alcantarillado_total = alcantarillado_total;
	}

	public BigDecimal getP_maestro_total() {
		return p_maestro_total;
	}

	public void setP_maestro_total(BigDecimal p_maestro_total) {
		this.p_maestro_total = p_maestro_total;
	}

	public BigDecimal getS_ciudadana_total() {
		return s_ciudadana_total;
	}

	public void setS_ciudadana_total(BigDecimal s_ciudadana_total) {
		this.s_ciudadana_total = s_ciudadana_total;
	}

	public BigDecimal getM_cuencas_total() {
		return m_cuencas_total;
	}

	public void setM_cuencas_total(BigDecimal m_cuencas_total) {
		this.m_cuencas_total = m_cuencas_total;
	}

	public BigDecimal getC_basico_total() {
		return c_basico_total;
	}

	public void setC_basico_total(BigDecimal c_basico_total) {
		this.c_basico_total = c_basico_total;
	}

	public BigDecimal getBasura_total() {
		return basura_total;
	}

	public void setBasura_total(BigDecimal basura_total) {
		this.basura_total = basura_total;
	}

	public BigDecimal getPaidTotal_total() {
		return paidTotal_total;
	}

	public void setPaidTotal_total(BigDecimal paidTotal_total) {
		this.paidTotal_total = paidTotal_total;
	}
	
	public void waxingValues(){
		subscriber_good_total = new Long(0);
		subscriber_damaged_total = new Long(0);
		subscriber_unmetered_total = new Long(0);

		// //// consumo en metros cubicos
		consumption_good_total = new BigDecimal(0);
		consumption_damaged_total = new BigDecimal(0);
		consumption_unmetered_total = new BigDecimal(0);

		// //// costos del consumo
		agua_total  = new BigDecimal(0);
		alcantarillado_total  = new BigDecimal(0);
		p_maestro_total = new BigDecimal(0);
		s_ciudadana_total = new BigDecimal(0);
		m_cuencas_total = new BigDecimal(0);
		c_basico_total = new BigDecimal(0);
		basura_total = new BigDecimal(0);
		paidTotal_total = new BigDecimal(0);
	}

}// end WaterSupplyCategory