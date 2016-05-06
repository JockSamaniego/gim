package ec.gob.gim.commercial.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * @author richard
 * @version 1.0
 * @created 05-nov-2013 08:36:22
 */

@Audited
@Entity
@TableGenerator(name = "BusinessCatalogGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "BusinessCatalog", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "BusinessCatalog.findByName", query = "select bc.name from BusinessCatalog bc where "
				+ "bc.name = :name"),
		@NamedQuery(name = "BusinessCatalog.findAllActive", query = "select bc from BusinessCatalog bc where "
				+ "bc.isActive = TRUE order by bc.name"),
		@NamedQuery(name = "BusinessCatalog.findAllTourist", query = "select bc from BusinessCatalog bc where "
				+ "bc.isActive = TRUE and isTourist = TRUE order by bc.name"),
		@NamedQuery(name = "BusinessCatalog.findAllTouristOnlyNames", query = "select bc.name from BusinessCatalog bc where "
				+ "bc.isActive = TRUE and isTourist = TRUE order by bc.name"),
		@NamedQuery(name = "BusinessCatalog.findAllNames", query = "select bc.name from BusinessCatalog bc where "
				+ "bc.isActive = TRUE and isInFeature = TRUE order by bc.name"),
				
		@NamedQuery(name = "BusinessCatalog.findAllNamesTourism", query = "select bc from BusinessCatalog bc where "
						+ "bc.isActive = TRUE and isTourist = TRUE order by bc.name"),
						
						
		@NamedQuery(name = "BusinessCatalog.findAllFeatureActive", query = "select bc from BusinessCatalog bc where "
				+ "bc.isActive = TRUE and isInFeature = TRUE order by bc.name"),
		@NamedQuery(name = "BusinessCatalog.findAllActiveByCriteria", query = "select bc from BusinessCatalog bc where "
				+ "lower(bc.name) like lower(concat(:criteria, '%')) and "
				+ "bc.isActive = TRUE order by bc.name") })
public class BusinessCatalog {

	@Id
	@GeneratedValue(generator = "BusinessCatalogGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length = 80)
	private String name;

	private Boolean isActive;

	private Boolean isInFeature;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsInFeature() {
		return isInFeature;
	}

	public void setIsInFeature(Boolean isInFeature) {
		this.isInFeature = isInFeature;
	}
	
//autor: Jock Samaniego M.
//Para crear nueva columna de clasificacion de catalogo.
	//@JoinColumn(name="isTourist")
	private Boolean isTourist;

	public Boolean getIsTourist() {
		return isTourist;
	}

	public void setIsTourist(Boolean isTourist) {
		this.isTourist = isTourist;
	}

	/*public boolean isTourist() {
		return isTourist;
	}

	public void setTourist(boolean isTourist) {
		this.isTourist = isTourist;
	}*/
	
}