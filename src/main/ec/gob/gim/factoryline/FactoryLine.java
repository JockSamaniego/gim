package ec.gob.gim.factoryline;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.cadaster.model.LotPosition;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.security.model.User;


/**
 * Línea de Fábrica: información de líneas de fábrica
 * 
 * @author Ronald Paladines C
 * @version 1.0
 * @created 26-Nov-2021
 */
@Audited
@Entity
@TableGenerator(
     name="FactoryLineGenerator",
     table="IdentityGenerator",
     pkColumnName="name",
     valueColumnName="value",
     pkColumnValue="FactoryLine",
     initialValue=1, allocationSize=1
)

public class FactoryLine implements Serializable{

    private static final long serialVersionUID = -1150082753160606161L;

    @Id
    @GeneratedValue(generator="FactoryLineGenerator",strategy=GenerationType.TABLE)
    private Long id;
    @Column(length=50)
    private String factoryLineNumber;
    private Boolean withoutCadastralInfo;
    private String cadastralCode;
    private String owner;
    private String identification;
    private String parish;
    private String neighborhood;
    private String block;
    private String street;
    private String streetBetween;
    private String streetBetweenTwo;
    private Double area;
    private Double front;
    private String propertyNumber;
    private Double lotBackground;
    private Boolean hasWater;
    private Boolean hasSewer;
    
    @Column(length=100)
    private String notMitigable;
    @Column(length=100)
    private String notMitigableTreatment;
    private String notMitigableDescription;
    @Column(length=100)
    private String mitigable;
    @Column(length=100)
    private String mitigableTreatment;
    private String mitigableDescription;
    @Column(length=200)
    private String specificRemark;
    
    @Enumerated(EnumType.STRING)
    @Column(length=20)
    private DepartmentName department;
    @Enumerated(EnumType.STRING)
    @Column(length=10)
    private SectorType sectorType;
    @Temporal(TemporalType.DATE)
    private Date creationDate; //Fecha de Creación
    @Temporal(TemporalType.DATE)
    private Date expiratedDate; //Fecha de terminación o expiración
    
    @Column(length=80)
    private String centralArea; // Área Central
    @Column(length=80)
    private String caFirstOrder; // Primer Orden
    @Column(length=80)
    private String caSecondOrder; // Segundo Orden
    @Column(length=80)
    private String elValle; // Subconjunto Histórico Plaza El Valle
    @Column(length=80)
    private String elValleFirstOrder; // Primer Orden
    @Column(length=80)
    private String elValleSecondOrder; // Segundo Orden
    @Column(length=80)
    private String culturalRoad; // Eje Vial Cultural
    @Column(length=80)
    private String influenceRoad; // Eje Vial de Influencia
    private boolean inventoried; // Inventariado

    @OneToOne
    @JoinColumn(name = "resident_id")
    private Resident resident; //Contribuyente

    @OneToOne
    @JoinColumn(name = "lotPosition_id")
    private LotPosition lotPosition; //Esquinero o intermedio

    @OneToOne
    @JoinColumn(name = "territorialPolygon_id")
    private TerritorialPolygon territorialPolygon;

    @OneToOne
    @JoinColumn(name = "territorialPolygon1_id")
    private TerritorialPolygon territorialPolygon1;

    @OneToOne
    @JoinColumn(name = "territorialPolygon2_id")
    private TerritorialPolygon territorialPolygon2;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(mappedBy = "factoryLines")
    private Set<FactoryLineRoad> roads = new HashSet<FactoryLineRoad>();

    public FactoryLine() {
        hasSewer = false;
        hasWater = false;
        inventoried = false;
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the factoryLineNumber
     */
    public String getFactoryLineNumber() {
        return factoryLineNumber;
    }

    /**
     * @param factoryLineNumber the factoryLineNumber to set
     */
    public void setFactoryLineNumber(String factoryLineNumber) {
        this.factoryLineNumber = factoryLineNumber;
    }

    /**
     * @return the withoutCadastralInfo
     */
    public Boolean getWithoutCadastralInfo() {
        return withoutCadastralInfo;
    }

    /**
     * @param withoutCadastralInfo the withoutCadastralInfo to set
     */
    public void setWithoutCadastralInfo(Boolean withoutCadastralInfo) {
        this.withoutCadastralInfo = withoutCadastralInfo;
    }

    /**
     * @return the cadastralCode
     */
    public String getCadastralCode() {
        return cadastralCode;
    }

    /**
     * @param cadastralCode the cadastralCode to set
     */
    public void setCadastralCode(String cadastralCode) {
        this.cadastralCode = cadastralCode;
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return the identification
     */
    public String getIdentification() {
        return identification;
    }

    /**
     * @param identification the identification to set
     */
    public void setIdentification(String identification) {
        this.identification = identification;
    }

    /**
     * @return the parish
     */
    public String getParish() {
        return parish;
    }

    /**
     * @param parish the parish to set
     */
    public void setParish(String parish) {
        this.parish = parish;
    }

    /**
     * @return the neighborhood
     */
    public String getNeighborhood() {
        return neighborhood;
    }

    /**
     * @param neighborhood the neighborhood to set
     */
    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    /**
     * @return the block
     */
    public String getBlock() {
        return block;
    }

    /**
     * @param block the block to set
     */
    public void setBlock(String block) {
        this.block = block;
    }

    /**
     * @return the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * @param street the street to set
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * @return the streetBetween
     */
    public String getStreetBetween() {
        return streetBetween;
    }

    /**
     * @param streetBetween the streetBetween to set
     */
    public void setStreetBetween(String streetBetween) {
        this.streetBetween = streetBetween;
    }

    /**
     * @return the streetBetweenTwo
     */
    public String getStreetBetweenTwo() {
        return streetBetweenTwo;
    }

    /**
     * @param streetBetweenTwo the streetBetweenTwo to set
     */
    public void setStreetBetweenTwo(String streetBetweenTwo) {
        this.streetBetweenTwo = streetBetweenTwo;
    }

    /**
     * @return the area
     */
    public Double getArea() {
        return area;
    }

    /**
     * @param area the area to set
     */
    public void setArea(Double area) {
        this.area = area;
    }

    /**
     * @return the front
     */
    public Double getFront() {
        return front;
    }

    /**
     * @param front the front to set
     */
    public void setFront(Double front) {
        this.front = front;
    }

    /**
     * @return the propertyNumber
     */
    public String getPropertyNumber() {
        return propertyNumber;
    }

    /**
     * @param propertyNumber the propertyNumber to set
     */
    public void setPropertyNumber(String propertyNumber) {
        this.propertyNumber = propertyNumber;
    }

    /**
     * @return the lotBackground
     */
    public Double getLotBackground() {
        return lotBackground;
    }

    /**
     * @param lotBackground the lotBackground to set
     */
    public void setLotBackground(Double lotBackground) {
        this.lotBackground = lotBackground;
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
     * @return the hasSewer
     */
    public Boolean getHasSewer() {
        return hasSewer;
    }

    /**
     * @param hasSewer the hasSewer to set
     */
    public void setHasSewer(Boolean hasSewer) {
        this.hasSewer = hasSewer;
    }

    /**
     * @return the centralArea
     */
    public String getCentralArea() {
        return centralArea;
    }

    /**
     * @param centralArea the centralArea to set
     */
    public void setCentralArea(String centralArea) {
        this.centralArea = centralArea;
    }

    /**
     * @return the caFirstOrder
     */
    public String getCaFirstOrder() {
        return caFirstOrder;
    }

    /**
     * @param caFirstOrder the caFirstOrder to set
     */
    public void setCaFirstOrder(String caFirstOrder) {
        this.caFirstOrder = caFirstOrder;
    }

    /**
     * @return the caSecondOrder
     */
    public String getCaSecondOrder() {
        return caSecondOrder;
    }

    /**
     * @param caSecondOrder the caSecondOrder to set
     */
    public void setCaSecondOrder(String caSecondOrder) {
        this.caSecondOrder = caSecondOrder;
    }

    /**
     * @return the elValle
     */
    public String getElValle() {
        return elValle;
    }

    /**
     * @param elValle the elValle to set
     */
    public void setElValle(String elValle) {
        this.elValle = elValle;
    }

    /**
     * @return the elValleFirstOrder
     */
    public String getElValleFirstOrder() {
        return elValleFirstOrder;
    }

    /**
     * @param elValleFirstOrder the elValleFirstOrder to set
     */
    public void setElValleFirstOrder(String elValleFirstOrder) {
        this.elValleFirstOrder = elValleFirstOrder;
    }

    /**
     * @return the elValleSecondOrder
     */
    public String getElValleSecondOrder() {
        return elValleSecondOrder;
    }

    /**
     * @param elValleSecondOrder the elValleSecondOrder to set
     */
    public void setElValleSecondOrder(String elValleSecondOrder) {
        this.elValleSecondOrder = elValleSecondOrder;
    }

    /**
     * @return the culturalRoad
     */
    public String getCulturalRoad() {
        return culturalRoad;
    }

    /**
     * @param culturalRoad the culturalRoad to set
     */
    public void setCulturalRoad(String culturalRoad) {
        this.culturalRoad = culturalRoad;
    }

    /**
     * @return the influenceRoad
     */
    public String getInfluenceRoad() {
        return influenceRoad;
    }

    /**
     * @param influenceRoad the influenceRoad to set
     */
    public void setInfluenceRoad(String influenceRoad) {
        this.influenceRoad = influenceRoad;
    }

    /**
     * @return the inventoried
     */
    public boolean isInventoried() {
        return inventoried;
    }

    /**
     * @param inventoried the inventoried to set
     */
    public void setInventoried(boolean inventoried) {
        this.inventoried = inventoried;
    }

    /**
     * @return the resident
     */
    public Resident getResident() {
        return resident;
    }

    /**
     * @param resident the resident to set
     */
    public void setResident(Resident resident) {
        this.resident = resident;
    }

    /**
     * @return the lotPosition
     */
    public LotPosition getLotPosition() {
        return lotPosition;
    }

    /**
     * @param lotPosition the lotPosition to set
     */
    public void setLotPosition(LotPosition lotPosition) {
        this.lotPosition = lotPosition;
    }

    /**
     * @return the territorialPolygon
     */
    public TerritorialPolygon getTerritorialPolygon() {
        return territorialPolygon;
    }

    /**
     * @param territorialPolygon the territorialPolygon to set
     */
    public void setTerritorialPolygon(TerritorialPolygon territorialPolygon) {
        this.territorialPolygon = territorialPolygon;
    }

    /**
     * @return the territorialPolygon1
     */
    public TerritorialPolygon getTerritorialPolygon1() {
        return territorialPolygon1;
    }

    /**
     * @param territorialPolygon1 the territorialPolygon1 to set
     */
    public void setTerritorialPolygon1(TerritorialPolygon territorialPolygon1) {
        this.territorialPolygon1 = territorialPolygon1;
    }

    /**
     * @return the territorialPolygon2
     */
    public TerritorialPolygon getTerritorialPolygon2() {
        return territorialPolygon2;
    }

    /**
     * @param territorialPolygon2 the territorialPolygon2 to set
     */
    public void setTerritorialPolygon2(TerritorialPolygon territorialPolygon2) {
        this.territorialPolygon2 = territorialPolygon2;
    }

    /**
     * @return the department
     */
    public DepartmentName getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(DepartmentName department) {
        this.department = department;
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
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the expiratedDate
     */
    public Date getExpiratedDate() {
        return expiratedDate;
    }

    /**
     * @param expiratedDate the expiratedDate to set
     */
    public void setExpiratedDate(Date expiratedDate) {
        this.expiratedDate = expiratedDate;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the roads
     */
    public Set<FactoryLineRoad> getRoads() {
        return roads;
    }

    /**
     * @param roads the roads to set
     */
    public void setRoads(Set<FactoryLineRoad> roads) {
        this.roads = roads;
    }

    public void add(FactoryLineRoad factoryLineRoad){
        if (!roads.contains(factoryLineRoad)){
            roads.add(factoryLineRoad);
            factoryLineRoad.add(this);
        }
    }
    
    public void remove(FactoryLineRoad factoryLineRoad){
        if (roads.contains(factoryLineRoad)){
            roads.remove(factoryLineRoad);
            factoryLineRoad.remove(this);
        }
    }
    
    /**
     * @return the notMitigable
     */
    public String getNotMitigable() {
        return notMitigable;
    }

    /**
     * @param notMitigable the notMitigable to set
     */
    public void setNotMitigable(String notMitigable) {
        this.notMitigable = notMitigable;
    }

    /**
     * @return the notMitigableTreatment
     */
    public String getNotMitigableTreatment() {
        return notMitigableTreatment;
    }

    /**
     * @param notMitigableTreatment the notMitigableTreatment to set
     */
    public void setNotMitigableTreatment(String notMitigableTreatment) {
        this.notMitigableTreatment = notMitigableTreatment;
    }

    /**
     * @return the notMitigableDescription
     */
    public String getNotMitigableDescription() {
        return notMitigableDescription;
    }

    /**
     * @param notMitigableDescription the notMitigableDescription to set
     */
    public void setNotMitigableDescription(String notMitigableDescription) {
        this.notMitigableDescription = notMitigableDescription;
    }

    /**
     * @return the mitigable
     */
    public String getMitigable() {
        return mitigable;
    }

    /**
     * @param mitigable the mitigable to set
     */
    public void setMitigable(String mitigable) {
        this.mitigable = mitigable;
    }

    /**
     * @return the mitigableTreatment
     */
    public String getMitigableTreatment() {
        return mitigableTreatment;
    }

    /**
     * @param mitigableTreatment the mitigableTreatment to set
     */
    public void setMitigableTreatment(String mitigableTreatment) {
        this.mitigableTreatment = mitigableTreatment;
    }

    /**
     * @return the mitigableDescription
     */
    public String getMitigableDescription() {
        return mitigableDescription;
    }

    /**
     * @param mitigableDescription the mitigableDescription to set
     */
    public void setMitigableDescription(String mitigableDescription) {
        this.mitigableDescription = mitigableDescription;
    }

    /**
     * @return the specificRemark
     */
    public String getSpecificRemark() {
        return specificRemark;
    }

    /**
     * @param specificRemark the specificRemark to set
     */
    public void setSpecificRemark(String specificRemark) {
        this.specificRemark = specificRemark;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((area == null) ? 0 : area.hashCode());
        result = prime * result + ((block == null) ? 0 : block.hashCode());
        result = prime * result
                + ((cadastralCode == null) ? 0 : cadastralCode.hashCode());
        result = prime * result
                + ((factoryLineNumber == null) ? 0 : factoryLineNumber.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((identification == null) ? 0 : identification.hashCode());
        result = prime * result
                + ((lotBackground == null) ? 0 : lotBackground.hashCode());
        result = prime * result
                + ((neighborhood == null) ? 0 : neighborhood.hashCode());
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        result = prime * result + ((parish == null) ? 0 : parish.hashCode());
        result = prime * result
                + ((resident == null) ? 0 : resident.hashCode());
        result = prime * result + ((street == null) ? 0 : street.hashCode());
        result = prime * result
                + ((streetBetween == null) ? 0 : streetBetween.hashCode());
        result = prime
                * result
                + ((streetBetweenTwo == null) ? 0 : streetBetweenTwo.hashCode());
        result = prime
                * result
                + ((withoutCadastralInfo == null) ? 0 : withoutCadastralInfo
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
        FactoryLine other = (FactoryLine) obj;
        if (area == null) {
            if (other.area != null)
                return false;
        } else if (!area.equals(other.area))
            return false;
        if (block == null) {
            if (other.block != null)
                return false;
        } else if (!block.equals(other.block))
            return false;
        if (cadastralCode == null) {
            if (other.cadastralCode != null)
                return false;
        } else if (!cadastralCode.equals(other.cadastralCode))
            return false;
        if (factoryLineNumber != other.factoryLineNumber)
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (identification == null) {
            if (other.identification != null)
                return false;
        } else if (!identification.equals(other.identification))
            return false;
        if (lotBackground == null) {
            if (other.lotBackground != null)
                return false;
        } else if (!lotBackground.equals(other.lotBackground))
            return false;
        if (neighborhood == null) {
            if (other.neighborhood != null)
                return false;
        } else if (!neighborhood.equals(other.neighborhood))
            return false;
        if (owner == null) {
            if (other.owner != null)
                return false;
        } else if (!owner.equals(other.owner))
            return false;
        if (parish == null) {
            if (other.parish != null)
                return false;
        } else if (!parish.equals(other.parish))
            return false;
        if (resident == null) {
            if (other.resident != null)
                return false;
        } else if (!resident.equals(other.resident))
            return false;
        if (street == null) {
            if (other.street != null)
                return false;
        } else if (!street.equals(other.street))
            return false;
        if (streetBetween == null) {
            if (other.streetBetween != null)
                return false;
        } else if (!streetBetween.equals(other.streetBetween))
            return false;
        if (streetBetweenTwo == null) {
            if (other.streetBetweenTwo != null)
                return false;
        } else if (!streetBetweenTwo.equals(other.streetBetweenTwo))
            return false;
        if (withoutCadastralInfo == null) {
            if (other.withoutCadastralInfo != null)
                return false;
        } else if (!withoutCadastralInfo.equals(other.withoutCadastralInfo))
            return false;
        return true;
    }

}
