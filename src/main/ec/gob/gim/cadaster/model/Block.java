package ec.gob.gim.cadaster.model;

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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Attachment;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:25
 */
@Audited
@Entity
@TableGenerator(
	 name="BlockGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Block",
	 initialValue=1, allocationSize=1
)
@NamedQueries({
	@NamedQuery(name="Block.findBySector",
			query="select b from Block b where b.sector.id = :sectorId order by b.code"
	)
})
public class Block {

	@Id
	@GeneratedValue(generator="BlockGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(length=3, nullable=false)
	private String code;
	
	@Column(length=20, nullable=false)
	private String cadastralCode;
	
	@Column(length=20)
	private String previousCode;
	
	// TODO Hacer un control en la edicion para no permitir agregar mas
	// propiedades a la manzana sobre este limite (Verificar hasta codigo de predio, sin propiedad horizontal 
	private Integer propertyLimit;
	
	private BigDecimal xA;
	private BigDecimal yA;
	
	private BigDecimal xB;
	private BigDecimal yB;
	
	private BigDecimal xC;
	private BigDecimal yC;
	
	private BigDecimal xD;
	private BigDecimal yD;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.MERGE, CascadeType.PERSIST})
	private Attachment sketch;
	
	@ManyToOne
	private TerritorialDivision sector;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Neighborhood neighborhood;
	
	@OneToMany(mappedBy="block", cascade=CascadeType.ALL)
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<Property> properties;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="block_id")
	private List<BlockLimit> limits;
	
	private BigDecimal valueBySquareMeter;
	
	public Block(){
		this.properties = new ArrayList<Property>();
		this.limits = new ArrayList<BlockLimit>();
		this.sketch = new Attachment();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the previousCode
	 */
	public String getPreviousCode() {
		return previousCode;
	}

	/**
	 * @param previousCode the previousCode to set
	 */
	public void setPreviousCode(String previousCode) {
		this.previousCode = previousCode;
	}

	/**
	 * @return the sketch
	 */
	public Attachment getSketch() {
		return sketch;
	}

	/**
	 * @param sketch the sketch to set
	 */
	public void setSketch(Attachment sketch) {
		this.sketch = sketch;
	}

	/**
	 * @return the xA
	 */
	public BigDecimal getxA() {
		return xA;
	}

	/**
	 * @param xA the xA to set
	 */
	public void setxA(BigDecimal xA) {
		this.xA = xA;
	}

	/**
	 * @return the yA
	 */
	public BigDecimal getyA() {
		return yA;
	}

	/**
	 * @param yA the yA to set
	 */
	public void setyA(BigDecimal yA) {
		this.yA = yA;
	}

	/**
	 * @return the xB
	 */
	public BigDecimal getxB() {
		return xB;
	}

	/**
	 * @param xB the xB to set
	 */
	public void setxB(BigDecimal xB) {
		this.xB = xB;
	}

	/**
	 * @return the yB
	 */
	public BigDecimal getyB() {
		return yB;
	}

	/**
	 * @param yB the yB to set
	 */
	public void setyB(BigDecimal yB) {
		this.yB = yB;
	}

	/**
	 * @return the xC
	 */
	public BigDecimal getxC() {
		return xC;
	}

	/**
	 * @param xC the xC to set
	 */
	public void setxC(BigDecimal xC) {
		this.xC = xC;
	}

	/**
	 * @return the yC
	 */
	public BigDecimal getyC() {
		return yC;
	}

	/**
	 * @param yC the yC to set
	 */
	public void setyC(BigDecimal yC) {
		this.yC = yC;
	}

	/**
	 * @return the xD
	 */
	public BigDecimal getxD() {
		return xD;
	}

	/**
	 * @param xD the xD to set
	 */
	public void setxD(BigDecimal xD) {
		this.xD = xD;
	}

	/**
	 * @return the yD
	 */
	public BigDecimal getyD() {
		return yD;
	}

	/**
	 * @param yD the yD to set
	 */
	public void setyD(BigDecimal yD) {
		this.yD = yD;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public List<BlockLimit> getLimits() {
		return limits;
	}

	public void setLimits(List<BlockLimit> limits) {
		this.limits = limits;
	}

	/**
	 * @return the sector
	 */
	public TerritorialDivision getSector() {		
		return sector;
	}

	/**
	 * @param sector the sector to set
	 */
	public void setSector(TerritorialDivision sector) {
		this.sector = sector;
	}

	public void add(Property property){
		if (!this.properties.contains(property)){
			this.properties.add(property);
			property.setBlock(this);
		}
	}
	
	public void remove(Property property){
		boolean removed = this.properties.remove(property);
		if (removed) property.setBlock((Block)null);
	}
	
	public void add(BlockLimit limit){
		if (!this.limits.contains(limit)){
			this.limits.add(limit);
		}
	}
	
	public void remove(BlockLimit limit){
		this.limits.remove(limit);
	}
	
	public String getBlockStreetLimits(){
		StringBuilder builder = new StringBuilder();
		for(BlockLimit limit : limits){
			builder.append(limit.getStreet().getName());
			builder.append("\n");
		}
		return builder.toString();
	}

	public void setNeighborhood(Neighborhood neighborhood) {
		this.neighborhood = neighborhood;
	}

	public Neighborhood getNeighborhood() {
		return neighborhood;
	}

	public String getCadastralCode() {
		return cadastralCode;
	}

	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}

	public Integer getPropertyLimit() {
		return propertyLimit;
	}

	public void setPropertyLimit(Integer propertyLimit) {
		this.propertyLimit = propertyLimit;
	}

	public void setValueBySquareMeter(BigDecimal valueBySquareMeter) {
		this.valueBySquareMeter = valueBySquareMeter;
	}

	public BigDecimal getValueBySquareMeter() {
		return valueBySquareMeter;
	}
}//end Block