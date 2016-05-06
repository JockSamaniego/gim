package ec.gob.gim.cadaster.model;

import java.math.BigDecimal;

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
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:46
 */
@Audited
@Entity
@TableGenerator(
	 name="BlockLimitGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="BlockLimit",
	 initialValue=1, allocationSize=1
)

@NamedQueries({
	@NamedQuery(name="BlockLimit.findByBlockId",
			query="select limits from Block b " +
					"left join b.limits limits " +
					"left join fetch limits.street " +
					"where b.id = :blockId"
	)
})
public class BlockLimit {

	@Id
	@GeneratedValue(generator="BlockLimitGenerator",strategy=GenerationType.TABLE)
	private Long id;
		
	private BigDecimal streetWidth;
	private BigDecimal blockFront;
	private BigDecimal sidewalkWidth;
	private Boolean hasElectricity;
	private Boolean hasSewerage;
	private Boolean hasTelephony;
	private Boolean hasWater;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private SewerageType sewerageType;
	
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private WaterType waterType;
	
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private LineType electricLineType;
	
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private LineType telephonyLineType;
	
	/**
	 * Relationships
	 */
	@ManyToOne
	@JoinColumn(name="streetMaterial_id")
	private StreetMaterial streetMaterial;
	
	@ManyToOne
	@JoinColumn(name="street_id")
	private Street street;
	
	@ManyToOne
	@JoinColumn(name="streetType_id")
	private StreetType streetType;
	
	@ManyToOne
	@JoinColumn(name="sidewalkMaterial_id")
	private SidewalkMaterial sidewalkMaterial;

	public BlockLimit(){

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the streetWidth
	 */
	public BigDecimal getStreetWidth() {
		return streetWidth;
	}

	/**
	 * @param streetWidth the streetWidth to set
	 */
	public void setStreetWidth(BigDecimal streetWidth) {
		this.streetWidth = streetWidth;
	}

	/**
	 * @return the blockFront
	 */
	public BigDecimal getBlockFront() {
		return blockFront;
	}

	/**
	 * @param blockFront the blockFront to set
	 */
	public void setBlockFront(BigDecimal blockFront) {
		this.blockFront = blockFront;
	}

	/**
	 * @return the sidewalkWidth
	 */
	public BigDecimal getSidewalkWidth() {
		return sidewalkWidth;
	}

	/**
	 * @param sidewalkWidth the sidewalkWidth to set
	 */
	public void setSidewalkWidth(BigDecimal sidewalkWidth) {
		this.sidewalkWidth = sidewalkWidth;
	}

	/**
	 * @return the hasElectricity
	 */
	public Boolean getHasElectricity() {
		return hasElectricity;
	}

	/**
	 * @param hasElectricity the hasElectricity to set
	 */
	public void setHasElectricity(Boolean hasElectricity) {
		this.hasElectricity = hasElectricity;
	}

	/**
	 * @return the hasSewerage
	 */
	public Boolean getHasSewerage() {
		return hasSewerage;
	}

	/**
	 * @param hasSewerage the hasSewerage to set
	 */
	public void setHasSewerage(Boolean hasSewerage) {
		this.hasSewerage = hasSewerage;
	}

	/**
	 * @return the hasTelephony
	 */
	public Boolean getHasTelephony() {
		return hasTelephony;
	}

	/**
	 * @param hasTelephony the hasTelephony to set
	 */
	public void setHasTelephony(Boolean hasTelephony) {
		this.hasTelephony = hasTelephony;
	}

	/**
	 * @return the hasWater
	 */
	public Boolean getHasWater() {
		return hasWater;
	}

	/**
	 * @param hasWater the hasWater to set
	 */
	public void setHasWater(Boolean hasWater) {
		this.hasWater = hasWater;
	}

	/**
	 * @return the sewerageType
	 */
	public SewerageType getSewerageType() {
		return sewerageType;
	}

	/**
	 * @param sewerageType the sewerageType to set
	 */
	public void setSewerageType(SewerageType sewerageType) {
		this.sewerageType = sewerageType;
	}

	/**
	 * @return the waterType
	 */
	public WaterType getWaterType() {
		return waterType;
	}

	/**
	 * @param waterType the waterType to set
	 */
	public void setWaterType(WaterType waterType) {
		this.waterType = waterType;
	}

	/**
	 * @return the electricLineType
	 */
	public LineType getElectricLineType() {
		return electricLineType;
	}

	/**
	 * @param electricLineType the electricLineType to set
	 */
	public void setElectricLineType(LineType electricLineType) {
		this.electricLineType = electricLineType;
	}

	/**
	 * @return the telephonyLineType
	 */
	public LineType getTelephonyLineType() {
		return telephonyLineType;
	}

	/**
	 * @param telephonyLineType the telephonyLineType to set
	 */
	public void setTelephonyLineType(LineType telephonyLineType) {
		this.telephonyLineType = telephonyLineType;
	}

	/**
	 * @return the streetMaterial
	 */
	public StreetMaterial getStreetMaterial() {
		return streetMaterial;
	}

	/**
	 * @param streetMaterial the streetMaterial to set
	 */
	public void setStreetMaterial(StreetMaterial streetMaterial) {
		this.streetMaterial = streetMaterial;
	}

	/**
	 * @return the streetType
	 */
	public StreetType getStreetType() {
		return streetType;
	}

	/**
	 * @param streetType the streetType to set
	 */
	public void setStreetType(StreetType streetType) {
		this.streetType = streetType;
	}

	/**
	 * @return the sidewalkMaterial
	 */
	public SidewalkMaterial getSidewalkMaterial() {
		return sidewalkMaterial;
	}

	/**
	 * @param sidewalkMaterial the sidewalkMaterial to set
	 */
	public void setSidewalkMaterial(SidewalkMaterial sidewalkMaterial) {
		this.sidewalkMaterial = sidewalkMaterial;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		BlockLimit that = (BlockLimit) o;
		if (getId() != null ? !(getId().equals(that.getId())) : that.getId() != null) {
			return false;
		}

		if (that.getId() == null && getId() == null) {
			return false;
		}

		return true;
	}

	public Street getStreet() {
		return street;
	}

	public void setStreet(Street street) {
		this.street = street;
	}

}//end BlockLimit