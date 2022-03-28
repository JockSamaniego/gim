package ec.gob.gim.revenue.model.adjunct;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.binnaclecrv.model.ArrivalHistoryBinnacleCRV;
import ec.gob.gim.binnaclecrv.model.BinnacleCRV;
import ec.gob.gim.binnaclecrv.model.ExitTypeBinnacleCRV;
import ec.gob.gim.revenue.model.Adjunct;
import ec.gob.gim.revenue.model.MunicipalBond;

@Audited
@Entity
@NamedQueries(value = {
	@NamedQuery(name = "BinnacleCRVReference.findById", 
		query = "select binnacleCRVReference from BinnacleCRVReference binnacleCRVReference " +
				"where binnacleCRVReference.id=:idBinnacleCRVReference "),
	@NamedQuery(name = "BinnacleCRVReference.findByArrivalAndEntryId", 
		query = "select binnacleCRVReference from BinnacleCRVReference binnacleCRVReference " +
				"where binnacleCRVReference.arrivalHistoryBinnacleCRV=:arrivalHistoryBinnacleCRV " +
			"and binnacleCRVReference.entryId = :entryId " +
//    		"and (binnacleCRVReference.bond.municipalBondStatus.id = 6 " +
//    		"or binnacleCRVReference.bond.municipalBondStatus.id = 11) " +
    		"order by binnacleCRVReference.id desc")
	})
@DiscriminatorValue("BitaCRV")
public class BinnacleCRVReference extends Adjunct{
	private Boolean emitWithoutBinnacle;
	private Boolean hasCraneService; //incluye servicio de grúa municipal al ingreso del vehículo
	private BigDecimal km; //kms recorridos por la grúa municipal
	private Double tonnage; //Tonelaje
	@Column(length = 30)
	private String type;
	@Temporal(TemporalType.DATE)
	private Date arrivalDate; //Fecha de Ingreso
	@Temporal(TemporalType.DATE)
	private Date exitDate; //Fecha de salida
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private ExitTypeBinnacleCRV exitTypeBinnacleCRV;// Tipo de Salida del CRV
	private Long entryId;// id del rubro que se emite (Servicio de Garaje o Grúa)
	
	@ManyToOne(cascade = CascadeType.MERGE)
	private BinnacleCRV binnacleCRV;
	@OneToOne
	@JoinColumn(name = "arrivalHistoryBinnacleCRV_id")
	private ArrivalHistoryBinnacleCRV arrivalHistoryBinnacleCRV;
	@OneToOne
	@JoinColumn(name = "municipalbond_id")
	private MunicipalBond bond;
	
	public BinnacleCRVReference() {
		emitWithoutBinnacle = Boolean.TRUE;
	}

	@Override
	public String toString() {
		if(binnacleCRV != null){
			if (binnacleCRV.getModel() != null)
				return binnacleCRV.getModel();
		}
		return "Bitacora CRV";
	}
	
	public List<ValuePair> getDetails(){
		List<ValuePair> details = new LinkedList<ValuePair>();
		if (arrivalHistoryBinnacleCRV == null) return details;
		BinnacleCRV binnacleCRV = arrivalHistoryBinnacleCRV.getBinnacleCRV();
		ValuePair pair = new ValuePair("Vehiculo Placa: ", binnacleCRV.getLicensePlate());
		details.add(pair);
		pair = new ValuePair("Informe Nro: ", binnacleCRV.getSerialNumber());
		details.add(pair);
		pair = new ValuePair("Salida: ", this.getExitTypeBinnacleCRV().name());
		details.add(pair);
		pair = new ValuePair("Tipo: ", this.getType());
		details.add(pair);
		pair = new ValuePair("Tonelaje: ", this.getTonnage().toString());
		details.add(pair);
		pair = new ValuePair("Uso de Grúa Municipal: ", this.getHasCraneService() ? "SI" : "NO");
		details.add(pair);
		pair = new ValuePair("Km recorridos: ", this.getKm().toString());
		if (this.getHasCraneService()) details.add(pair);
		return details;
	}

	@Override
	public String getCode() {
		if (arrivalHistoryBinnacleCRV == null) return "Bitácora CRV";
		BinnacleCRV binnacleCRV = arrivalHistoryBinnacleCRV.getBinnacleCRV();
		String code = binnacleCRV.getLicensePlate();
		return code;
	}

	/**
	 * @return the hasCraneService
	 */
	public Boolean getHasCraneService() {
		return hasCraneService;
	}

	/**
	 * @param hasCraneService the hasCraneService to set
	 */
	public void setHasCraneService(Boolean hasCraneService) {
		this.hasCraneService = hasCraneService;
	}

	/**
	 * @return the km
	 */
	public BigDecimal getKm() {
		return km;
	}

	/**
	 * @param km the km to set
	 */
	public void setKm(BigDecimal km) {
		this.km = km;
	}

	/**
	 * @return the tonnage
	 */
	public Double getTonnage() {
		return tonnage;
	}

	/**
	 * @param tonnage the tonnage to set
	 */
	public void setTonnage(Double tonnage) {
		this.tonnage = tonnage;
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
	 * @return the arrivalDate
	 */
	public Date getArrivalDate() {
		return arrivalDate;
	}

	/**
	 * @param arrivalDate the arrivalDate to set
	 */
	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	/**
	 * @return the exitDate
	 */
	public Date getExitDate() {
		return exitDate;
	}

	/**
	 * @param exitDate the exitDate to set
	 */
	public void setExitDate(Date exitDate) {
		this.exitDate = exitDate;
	}

	/**
	 * @return the exitTypeBinnacleCRV
	 */
	public ExitTypeBinnacleCRV getExitTypeBinnacleCRV() {
		return exitTypeBinnacleCRV;
	}

	/**
	 * @param exitTypeBinnacleCRV the exitTypeBinnacleCRV to set
	 */
	public void setExitTypeBinnacleCRV(ExitTypeBinnacleCRV exitTypeBinnacleCRV) {
		this.exitTypeBinnacleCRV = exitTypeBinnacleCRV;
	}

	/**
	 * @return the entryId
	 */
	public Long getEntryId() {
		return entryId;
	}

	/**
	 * @param entryId the entryId to set
	 */
	public void setEntryId(Long entryId) {
		this.entryId = entryId;
	}

	/**
	 * @return the emitWithoutBinnacle
	 */
	public Boolean getEmitWithoutBinnacle() {
		return emitWithoutBinnacle;
	}

	/**
	 * @param emitWithoutBinnacle the emitWithoutBinnacle to set
	 */
	public void setEmitWithoutBinnacle(Boolean emitWithoutBinnacle) {
		this.emitWithoutBinnacle = emitWithoutBinnacle;
	}

	/**
	 * @return the bondGaraje
	 */
	public MunicipalBond getBond() {
		return bond;
	}

	/**
	 * @param bondGaraje the bondGaraje to set
	 */
	public void setBond(MunicipalBond bond) {
		this.bond = bond;
	}

	/**
	 * @return the binnacleCRV
	 */
	public BinnacleCRV getBinnacleCRV() {
		return binnacleCRV;
	}

	/**
	 * @param binnacleCRV the binnacleCRV to set
	 */
	public void setBinnacleCRV(BinnacleCRV binnacleCRV) {
		this.binnacleCRV = binnacleCRV;
	}

	/**
	 * @return the arrivalHistoryBinnacleCRV
	 */
	public ArrivalHistoryBinnacleCRV getArrivalHistoryBinnacleCRV() {
		return arrivalHistoryBinnacleCRV;
	}

	/**
	 * @param arrivalHistoryBinnacleCRV the arrivalHistoryBinnacleCRV to set
	 */
	public void setArrivalHistoryBinnacleCRV(
			ArrivalHistoryBinnacleCRV arrivalHistoryBinnacleCRV) {
		this.arrivalHistoryBinnacleCRV = arrivalHistoryBinnacleCRV;
	}

}
