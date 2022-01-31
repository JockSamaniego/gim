package ec.gob.gim.factoryline;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;


/**
 * Polígono Territorial: información de los polífonos territoriales urbanos y rurales, aplicados en línea de fábrica
 * 
 * @author Ronald Paladines C
 * @version 1.0
 * @created 26-Nov-2021
 */
@Audited
@Entity
@TableGenerator(
	 name="TerritorialPolygonGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="TerritorialPolygon",
	 initialValue=1, allocationSize=1
)

@NamedQueries(value = {
	@NamedQuery(name = "TerritorialPolygon.findAllActive", query = "select t from TerritorialPolygon t " +
			"where t.active = true " +
			"order by t.polygonCode"),
	@NamedQuery(name = "TerritorialPolygon.findAllActiveBySectorType", query = "select t from TerritorialPolygon t " +
			"where t.active = true and t.sectorType = :sectorType " +
			"order by t.polygonCode"),
})
public class TerritorialPolygon implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8370412845184925964L;
 
	@Id
	@GeneratedValue(generator="TerritorialPolygonGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(length=10)
    private SectorType sectorType;
	@Column(length=20)
    private String polygonCode;
	@Column(length=20)
	private String classiffication;
	@Column(length=50)
	private String subclassiffication;
	@Column(length=50)
	private String category;
	@Column(length=100)
	private String polygonName;
	@Column(length=5)
	private String generalUseCode;
	@Column(length=40)
	private String generalUseName;
	private int minimumLot;
	private int averageLot;
	private int averageFront;
	private int maxLot;
	private int maxFront;
	private int minFront;
	@Column(length=60)
	private String frontalRetreat;
	private int sideRetreat;
	private int laterWithdrawal;
	private int middleBackground;
	private int cos;
	private int cus;
	@Column(length=100)
	private String implantation;
	private int lotArea;
	private int floorHeight;
	private int buildability;
	private boolean active;
	@Column(length=300)
	private String obs1;
	@Column(length=300)
	private String obs2;
	@Column(length=300)
	private String obs3;
	@Column(length=300)
	private String obs4;

    @OneToOne
	@JoinColumn(name = "principalUse_id")
	private LandUseFactoryLine principalUse; //Uso de suelo principal

	@ManyToMany(mappedBy = "tpComplementaries")
	private List<LandUseFactoryLine> complementaryUses;

	@ManyToMany(mappedBy = "tpProhibiteds")
	private List<LandUseFactoryLine> prohibitedUses;

	@ManyToMany(mappedBy = "tpRestricteds")
	private List<LandUseFactoryLine> restrictedUses;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the sectorType
	 */
	public SectorType getSectorType() {
		return sectorType;
	}

	/**
	 * @param sectorType the sectorType to set
	 */
	public void setSectorType(SectorType sectorType) {
		this.sectorType = sectorType;
	}

	/**
	 * @return the polygonCode
	 */
	public String getPolygonCode() {
		return polygonCode;
	}

	/**
	 * @param polygonCode the polygonCode to set
	 */
	public void setPolygonCode(String polygonCode) {
		this.polygonCode = polygonCode;
	}

	/**
	 * @return the classiffication
	 */
	public String getClassiffication() {
		return classiffication;
	}

	/**
	 * @param classiffication the classiffication to set
	 */
	public void setClassiffication(String classiffication) {
		this.classiffication = classiffication;
	}

	/**
	 * @return the subclassiffication
	 */
	public String getSubclassiffication() {
		return subclassiffication;
	}

	/**
	 * @param subclassiffication the subclassiffication to set
	 */
	public void setSubclassiffication(String subclassiffication) {
		this.subclassiffication = subclassiffication;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the polygonName
	 */
	public String getPolygonName() {
		return polygonName;
	}

	/**
	 * @param polygonName the polygonName to set
	 */
	public void setPolygonName(String polygonName) {
		this.polygonName = polygonName;
	}

	/**
	 * @return the generalUseCode
	 */
	public String getGeneralUseCode() {
		return generalUseCode;
	}

	/**
	 * @param generalUseCode the generalUseCode to set
	 */
	public void setGeneralUseCode(String generalUseCode) {
		this.generalUseCode = generalUseCode;
	}

	/**
	 * @return the generalUseName
	 */
	public String getGeneralUseName() {
		return generalUseName;
	}

	/**
	 * @param generalUseName the generalUseName to set
	 */
	public void setGeneralUseName(String generalUseName) {
		this.generalUseName = generalUseName;
	}

	/**
	 * @return the minimumLot
	 */
	public int getMinimumLot() {
		return minimumLot;
	}

	/**
	 * @param minimumLot the minimumLot to set
	 */
	public void setMinimumLot(int minimumLot) {
		this.minimumLot = minimumLot;
	}

	/**
	 * @return the maxLot
	 */
	public int getMaxLot() {
		return maxLot;
	}

	/**
	 * @param maxLot the maxLot to set
	 */
	public void setMaxLot(int maxLot) {
		this.maxLot = maxLot;
	}

	/**
	 * @return the averageLot
	 */
	public int getAverageLot() {
		return averageLot;
	}

	/**
	 * @param averageLot the averageLot to set
	 */
	public void setAverageLot(int averageLot) {
		this.averageLot = averageLot;
	}

	/**
	 * @return the averageFront
	 */
	public int getAverageFront() {
		return averageFront;
	}

	/**
	 * @param averageFront the averageFront to set
	 */
	public void setAverageFront(int averageFront) {
		this.averageFront = averageFront;
	}

	/**
	 * @return the maxFront
	 */
	public int getMaxFront() {
		return maxFront;
	}

	/**
	 * @param maxFront the maxFront to set
	 */
	public void setMaxFront(int maxFront) {
		this.maxFront = maxFront;
	}

	/**
	 * @return the minFront
	 */
	public int getMinFront() {
		return minFront;
	}

	/**
	 * @param minFront the minFront to set
	 */
	public void setMinFront(int minFront) {
		this.minFront = minFront;
	}

	/**
	 * @return the frontalRetreat
	 */
	public String getFrontalRetreat() {
		return frontalRetreat;
	}

	/**
	 * @param frontalRetreat the frontalRetreat to set
	 */
	public void setFrontalRetreat(String frontalRetreat) {
		this.frontalRetreat = frontalRetreat;
	}

	/**
	 * @return the sideRetreat
	 */
	public int getSideRetreat() {
		return sideRetreat;
	}

	/**
	 * @param sideRetreat the sideRetreat to set
	 */
	public void setSideRetreat(int sideRetreat) {
		this.sideRetreat = sideRetreat;
	}

	/**
	 * @return the laterWithdrawal
	 */
	public int getLaterWithdrawal() {
		return laterWithdrawal;
	}

	/**
	 * @param laterWithdrawal the laterWithdrawal to set
	 */
	public void setLaterWithdrawal(int laterWithdrawal) {
		this.laterWithdrawal = laterWithdrawal;
	}

	/**
	 * @return the middleBackground
	 */
	public int getMiddleBackground() {
		return middleBackground;
	}

	/**
	 * @param middleBackground the middleBackground to set
	 */
	public void setMiddleBackground(int middleBackground) {
		this.middleBackground = middleBackground;
	}

	/**
	 * @return the cos
	 */
	public int getCos() {
		return cos;
	}

	/**
	 * @param cos the cos to set
	 */
	public void setCos(int cos) {
		this.cos = cos;
	}

	/**
	 * @return the cus
	 */
	public int getCus() {
		return cus;
	}

	/**
	 * @param cus the cus to set
	 */
	public void setCus(int cus) {
		this.cus = cus;
	}

	/**
	 * @return the implantation
	 */
	public String getImplantation() {
		return implantation;
	}

	/**
	 * @param implantation the implantation to set
	 */
	public void setImplantation(String implantation) {
		this.implantation = implantation;
	}

	/**
	 * @return the lotArea
	 */
	public int getLotArea() {
		return lotArea;
	}

	/**
	 * @param lotArea the lotArea to set
	 */
	public void setLotArea(int lotArea) {
		this.lotArea = lotArea;
	}

	/**
	 * @return the floorHeight
	 */
	public int getFloorHeight() {
		return floorHeight;
	}

	/**
	 * @param floorHeight the floorHeight to set
	 */
	public void setFloorHeight(int floorHeight) {
		this.floorHeight = floorHeight;
	}

	/**
	 * @return the buildability
	 */
	public int getBuildability() {
		return buildability;
	}

	/**
	 * @param buildability the buildability to set
	 */
	public void setBuildability(int buildability) {
		this.buildability = buildability;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the obs1
	 */
	public String getObs1() {
		return obs1;
	}

	/**
	 * @param obs1 the obs1 to set
	 */
	public void setObs1(String obs1) {
		this.obs1 = obs1;
	}

	/**
	 * @return the obs2
	 */
	public String getObs2() {
		return obs2;
	}

	/**
	 * @param obs2 the obs2 to set
	 */
	public void setObs2(String obs2) {
		this.obs2 = obs2;
	}

	/**
	 * @return the obs3
	 */
	public String getObs3() {
		return obs3;
	}

	/**
	 * @param obs3 the obs3 to set
	 */
	public void setObs3(String obs3) {
		this.obs3 = obs3;
	}

	/**
	 * @return the obs4
	 */
	public String getObs4() {
		return obs4;
	}

	/**
	 * @param obs4 the obs4 to set
	 */
	public void setObs4(String obs4) {
		this.obs4 = obs4;
	}

	/**
	 * @return the principalUse
	 */
	public LandUseFactoryLine getPrincipalUse() {
		return principalUse;
	}

	/**
	 * @param principalUse the principalUse to set
	 */
	public void setPrincipalUse(LandUseFactoryLine principalUse) {
		this.principalUse = principalUse;
	}

	/**
	 * @return the complementaryUses
	 */
	public List<LandUseFactoryLine> getComplementaryUses() {
		return complementaryUses;
	}

	/**
	 * @param complementaryUses the complementaryUses to set
	 */
	public void setComplementaryUses(List<LandUseFactoryLine> complementaryUses) {
		this.complementaryUses = complementaryUses;
	}

	/**
	 * @return the prohibitedUses
	 */
	public List<LandUseFactoryLine> getProhibitedUses() {
		return prohibitedUses;
	}

	/**
	 * @param prohibitedUses the prohibitedUses to set
	 */
	public void setProhibitedUses(List<LandUseFactoryLine> prohibitedUses) {
		this.prohibitedUses = prohibitedUses;
	}

	/**
	 * @return the restrictedUses
	 */
	public List<LandUseFactoryLine> getRestrictedUses() {
		return restrictedUses;
	}

	/**
	 * @param restrictedUses the restrictedUses to set
	 */
	public void setRestrictedUses(List<LandUseFactoryLine> restrictedUses) {
		this.restrictedUses = restrictedUses;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + averageLot;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result
				+ ((classiffication == null) ? 0 : classiffication.hashCode());
		result = prime * result + cos;
		result = prime * result + cus;
		result = prime * result + floorHeight;
		result = prime * result
				+ ((frontalRetreat == null) ? 0 : frontalRetreat.hashCode());
		result = prime * result
				+ ((generalUseCode == null) ? 0 : generalUseCode.hashCode());
		result = prime * result
				+ ((generalUseName == null) ? 0 : generalUseName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((implantation == null) ? 0 : implantation.hashCode());
		result = prime * result + laterWithdrawal;
		result = prime * result + lotArea;
		result = prime * result + maxFront;
		result = prime * result + middleBackground;
		result = prime * result + minFront;
		result = prime * result + minimumLot;
		result = prime * result
				+ ((polygonCode == null) ? 0 : polygonCode.hashCode());
		result = prime * result
				+ ((polygonName == null) ? 0 : polygonName.hashCode());
		result = prime * result
				+ ((principalUse == null) ? 0 : principalUse.hashCode());
		result = prime * result
				+ ((prohibitedUses == null) ? 0 : prohibitedUses.hashCode());
		result = prime * result
				+ ((sectorType == null) ? 0 : sectorType.hashCode());
		result = prime * result + sideRetreat;
		result = prime
				* result
				+ ((subclassiffication == null) ? 0 : subclassiffication
						.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TerritorialPolygon other = (TerritorialPolygon) obj;
		if (averageLot != other.averageLot)
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (classiffication == null) {
			if (other.classiffication != null)
				return false;
		} else if (!classiffication.equals(other.classiffication))
			return false;
		if (cos != other.cos)
			return false;
		if (cus != other.cus)
			return false;
		if (floorHeight != other.floorHeight)
			return false;
		if (frontalRetreat != other.frontalRetreat)
			return false;
		if (generalUseCode == null) {
			if (other.generalUseCode != null)
				return false;
		} else if (!generalUseCode.equals(other.generalUseCode))
			return false;
		if (generalUseName == null) {
			if (other.generalUseName != null)
				return false;
		} else if (!generalUseName.equals(other.generalUseName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (implantation == null) {
			if (other.implantation != null)
				return false;
		} else if (!implantation.equals(other.implantation))
			return false;
		if (laterWithdrawal != other.laterWithdrawal)
			return false;
		if (lotArea != other.lotArea)
			return false;
		if (maxFront != other.maxFront)
			return false;
		if (middleBackground != other.middleBackground)
			return false;
		if (minFront != other.minFront)
			return false;
		if (minimumLot != other.minimumLot)
			return false;
		if (polygonCode == null) {
			if (other.polygonCode != null)
				return false;
		} else if (!polygonCode.equals(other.polygonCode))
			return false;
		if (polygonName == null) {
			if (other.polygonName != null)
				return false;
		} else if (!polygonName.equals(other.polygonName))
			return false;
		if (principalUse == null) {
			if (other.principalUse != null)
				return false;
		} else if (!principalUse.equals(other.principalUse))
			return false;
		if (prohibitedUses == null) {
			if (other.prohibitedUses != null)
				return false;
		} else if (!prohibitedUses.equals(other.prohibitedUses))
			return false;
		if (sectorType != other.sectorType)
			return false;
		if (sideRetreat != other.sideRetreat)
			return false;
		if (subclassiffication == null) {
			if (other.subclassiffication != null)
				return false;
		} else if (!subclassiffication.equals(other.subclassiffication))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TerritorialPolygon [id=" + id + ", sectorType=" + sectorType
				+ ", polygonCode=" + polygonCode + ", classiffication="
				+ classiffication + ", subclassiffication="
				+ subclassiffication + ", category=" + category
				+ ", polygonName=" + polygonName + ", generalUseCode="
				+ generalUseCode + ", generalUseName=" + generalUseName
				+ ", minimumLot=" + minimumLot + ", averageLot=" + averageLot
				+ ", maxLot=" + maxLot + ", maxFront=" + maxFront
				+ ", minFront=" + minFront + ", frontalRetreat="
				+ frontalRetreat + ", sideRetreat=" + sideRetreat
				+ ", laterWithdrawal=" + laterWithdrawal
				+ ", middleBackground=" + middleBackground + ", cos=" + cos
				+ ", cus=" + cus + ", implantation=" + implantation
				+ ", lotArea=" + lotArea + ", floorHeight=" + floorHeight
				+ ", buildability=" + buildability + ", active=" + active
				+ ", obs1=" + obs1 + ", obs2=" + obs2 + ", obs3=" + obs3
				+ ", obs4=" + obs4 + ", principalUse=" + principalUse
				+ ", complementaryUses=" + complementaryUses
				+ ", prohibitedUses=" + prohibitedUses + ", restrictedUses="
				+ restrictedUses + "]";
	}

}
