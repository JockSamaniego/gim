package ec.gob.gim.binnaclecrv.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.revenue.model.MunicipalBond;
// import ec.gob.gim.revenue.model.adjunct.BinnacleCRVReference;
import ec.gob.gim.security.model.User;

/**
 * Bitácora Digital: Registro de ingresos y salidas del vehículo en el Centro de Retención Vehicular
 * 
 * @author Ronald Paladines C
 * @version 1.0
 * @created 28-Jun-2021
 */
@Audited
@Entity
@TableGenerator(name = "ArrivalHistoryBinnacleCRVGenerator", 
    table = "IdentityGenerator", 
    pkColumnName = "name", 
    valueColumnName = "value", 
    pkColumnValue = "ArrivalHistoryBinnacleCRV", 
    initialValue = 1, allocationSize = 1)

@NamedQueries(value = {
	@NamedQuery(name = "ArrivalHistoryBinnacleCRV.findById", 
		query = "select arrival from ArrivalHistoryBinnacleCRV arrival " +
				"where arrival.id=:id")
	})
public class ArrivalHistoryBinnacleCRV implements Serializable {

	private static final long serialVersionUID = 879334315199518385L;

	@Id
	@GeneratedValue(generator = "ArrivalHistoryBinnacleCRVGenerator", strategy = GenerationType.TABLE)
	private Long id;
    
	@Temporal(TemporalType.DATE)
	private Date arrivalDate; //Fecha de Ingreso
	@Temporal(TemporalType.DATE)
	private Date exitDate; //Fecha de salida
	@Temporal(TemporalType.DATE)
	private Date exitDateCrane; //Fecha de salida por grúa municipal
	private boolean hasCraneService; //incluye servicio de grúa municipal al ingreso del vehículo
	private BigDecimal km; //kms recorridos por la grúa municipal
	@Column(length=100)
	private String arrivalDetail; //detalle u orden judicial o memo de proceso de chatarrización, etc. Al ingreso
	@Column(length=100)
	private String exitDetail; //detalle u orden judicial o memo de proceso de chatarrización, etc. A la salida
	@Column(columnDefinition = "TEXT")
	private String arrivalObs; //Observaciones del ingreso
	@Column(columnDefinition = "TEXT")
	private String exitObs; //Observaciones de la salida
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private ExitTypeBinnacleCRV exitTypeBinnacleCRV;// Tipo de Salida del CRV
    
	@ManyToOne(cascade = CascadeType.MERGE)
	private BinnacleCRV binnacleCRV;
	
	/*@OneToMany(mappedBy = "arrivalHistoryBinnacleCRV", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<BinnacleCRVReference> binnacleCRVReferences; //Adjunto de refrerencia en la emisión del titulo*/
	
	@OneToOne
	@JoinColumn(name = "admissionType_id")
	private AdmissionType admissionType; //Tipo de Ingreso
	@ManyToOne(cascade = CascadeType.MERGE)
	private User userArrival;
	@ManyToOne(cascade = CascadeType.MERGE)
	private User userExit;
	@ManyToOne(cascade = CascadeType.MERGE)
	private MunicipalBond bondGaraje;//MunicipalBond por servicio de Garaje 
	@ManyToOne(cascade = CascadeType.MERGE)
	private MunicipalBond bondCrane;//MunicipalBond por servicio de Grúa Municipal

	public ArrivalHistoryBinnacleCRV() {
		km = BigDecimal.ZERO;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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
	 * @return the exitDateCrane
	 */
	public Date getExitDateCrane() {
		return exitDateCrane;
	}

	/**
	 * @param exitDateCrane the exitDateCrane to set
	 */
	public void setExitDateCrane(Date exitDateCrane) {
		this.exitDateCrane = exitDateCrane;
	}

	/**
	 * @return the hasCraneService
	 */
	public boolean isHasCraneService() {
		return hasCraneService;
	}

	/**
	 * @param hasCraneService the hasCraneService to set
	 */
	public void setHasCraneService(boolean hasCraneService) {
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
	 * @return the arrivalDetail
	 */
	public String getArrivalDetail() {
		return arrivalDetail;
	}

	/**
	 * @param arrivalDetail the arrivalDetail to set
	 */
	public void setArrivalDetail(String arrivalDetail) {
		this.arrivalDetail = arrivalDetail;
	}

	/**
	 * @return the exitDetail
	 */
	public String getExitDetail() {
		return exitDetail;
	}

	/**
	 * @param exitDetail the exitDetail to set
	 */
	public void setExitDetail(String exitDetail) {
		this.exitDetail = exitDetail;
	}

	/**
	 * @return the arrivalObs
	 */
	public String getArrivalObs() {
		return arrivalObs;
	}

	/**
	 * @param arrivalObs the arrivalObs to set
	 */
	public void setArrivalObs(String arrivalObs) {
		this.arrivalObs = arrivalObs;
	}

	/**
	 * @return the exitObs
	 */
	public String getExitObs() {
		return exitObs;
	}

	/**
	 * @param exitObs the exitObs to set
	 */
	public void setExitObs(String exitObs) {
		this.exitObs = exitObs;
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
	 * @return the binnacleCRVReferences
	 */
	/*public List<BinnacleCRVReference> getBinnacleCRVReferences() {
		return binnacleCRVReferences;
	}*/

	/**
	 * @param binnacleCRVReferences the binnacleCRVReferences to set
	 */
	/*public void setBinnacleCRVReferences(
			List<BinnacleCRVReference> binnacleCRVReferences) {
		this.binnacleCRVReferences = binnacleCRVReferences;
	}*/

	/**
	 * @return the admissionType
	 */
	public AdmissionType getAdmissionType() {
		return admissionType;
	}

	/**
	 * @param admissionType the admissionType to set
	 */
	public void setAdmissionType(AdmissionType admissionType) {
		this.admissionType = admissionType;
	}

	/**
	 * @return the userArrival
	 */
	public User getUserArrival() {
		return userArrival;
	}

	/**
	 * @param userArrival the userArrival to set
	 */
	public void setUserArrival(User userArrival) {
		this.userArrival = userArrival;
	}

	/**
	 * @return the userExit
	 */
	public User getUserExit() {
		return userExit;
	}

	/**
	 * @param userExit the userExit to set
	 */
	public void setUserExit(User userExit) {
		this.userExit = userExit;
	}

	/**
	 * @return the bondGaraje
	 */
	public MunicipalBond getBondGaraje() {
		return bondGaraje;
	}

	/**
	 * @param bondGaraje the bondGaraje to set
	 */
	public void setBondGaraje(MunicipalBond bondGaraje) {
		this.bondGaraje = bondGaraje;
	}

	/**
	 * @return the bondCrane
	 */
	public MunicipalBond getBondCrane() {
		return bondCrane;
	}

	/**
	 * @param bondCrane the bondCrane to set
	 */
	public void setBondCrane(MunicipalBond bondCrane) {
		this.bondCrane = bondCrane;
	}

}
