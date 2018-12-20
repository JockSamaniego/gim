package ec.gob.gim.cadaster.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:48
 */
@Audited
@Entity
@TableGenerator(name = "TerritorialDivisionGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "TerritorialDivision", initialValue = 1, allocationSize = 1)
@NamedQueries({
		@NamedQuery(name = "TerritorialDivision.findAll", query = "select o from TerritorialDivision o"),
		@NamedQuery(name = "TerritorialDivision.findByTypeAndCode", query = "select o from TerritorialDivision o where o.code=:code and o.territorialDivisionType.id = :territorialDivisionTypeId"),
		@NamedQuery(name = "TerritorialDivision.findByTypeAndCodeAndParent", query = "select o from TerritorialDivision o where "
				+ "o.code=:code and o.territorialDivisionType.id = :territorialDivisionTypeId "
				+ "and o.parent = :parent"),
		@NamedQuery(name = "TerritorialDivision.findByCodeAndParent", query = "select o from TerritorialDivision o where "
				+ "o.code=:code and o.parent = :parent"),
		@NamedQuery(name = "TerritorialDivision.findByParent", query = "select o from TerritorialDivision o where o.parent.id = :parentId order by o.code"),
		//@tag cambioClave
		@NamedQuery(name = "TerritorialDivision.findByParentNew", query = "select o from TerritorialDivision o where o.parent.id = :parentId and o.classifierGeo = :classifierGeo order by o.code"),
		//@tag predioColoma
		@NamedQuery(name = "TerritorialDivision.findByParentAndSpecial", query = "select o from TerritorialDivision o where o.parent.id = :parentId and type=:type order by o.code"),
		
		@NamedQuery(name = "TerritorialDivision.findProvinces", query = "select o from TerritorialDivision o where o.parent IS NULL order by o.name"),
		@NamedQuery(name = "TerritorialDivision.findProvinceByCode", query = "select o from TerritorialDivision o where o.parent IS NULL and o.code=:code order by o.name")})
public class TerritorialDivision {

	@Id
	@GeneratedValue(generator = "TerritorialDivisionGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length = 25, nullable = false)
	private String name;

	@Column(length = 10, nullable = false)
	private String code;

	/*
	 * Relationships
	 */
	@ManyToOne
	@JoinColumn(name = "territorialDivisionType_id", nullable = false)
	@OrderBy(value = "priority")
	private TerritorialDivisionType territorialDivisionType;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OrderBy(value = "code ASC")
	private List<TerritorialDivision> territorialDivisions;

	@ManyToOne
	private TerritorialDivision parent;

	@OneToMany(mappedBy = "sector", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<Block> blocks;
	
	//@tag predioEspecial
	//@author macartuche
	//@date 2016-10-27T16:07
	private String type;
	
	//macartuche
	//2018-11-08
	//para los nuevo codigos
	private Boolean classifierGeo=Boolean.FALSE;

	public TerritorialDivision() {
		this.territorialDivisions = new ArrayList<TerritorialDivision>();
		this.blocks = new ArrayList<Block>();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

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

	public TerritorialDivisionType getTerritorialDivisionType() {
		return territorialDivisionType;
	}

	public void setTerritorialDivisionType(
			TerritorialDivisionType territorialDivisionType) {
		this.territorialDivisionType = territorialDivisionType;
	}

	public List<TerritorialDivision> getTerritorialDivisions() {
		return territorialDivisions;
	}

	public void setTerritorialDivisions(
			List<TerritorialDivision> territorialDivisions) {
		this.territorialDivisions = territorialDivisions;
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}

	public TerritorialDivision getParent() {
		return parent;
	}

	public void setParent(TerritorialDivision parent) {
		this.parent = parent;
	}

	public void add(TerritorialDivision territorialDivision) {
		if (!this.territorialDivisions.contains(territorialDivision)) {
			this.territorialDivisions.add(territorialDivision);
			territorialDivision.setParent(this);
		}
	}

	public void remove(TerritorialDivision territorialDivision) {
		boolean removed = this.territorialDivisions.remove(territorialDivision);
		if (removed)
			territorialDivision.setParent((TerritorialDivision) null);
	}

	public void add(Block block) {
		if (!this.blocks.contains(block)) {
			this.blocks.add(block);
			block.setSector(this);
		}
	}

	public void remove(Block block) {
		boolean removed = this.blocks.remove(block);
		if (removed)
			block.setSector((TerritorialDivision) null);
	}

	@Override
	public int hashCode() {
		int hash = 13;
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

		TerritorialDivision that = (TerritorialDivision) o;
		if (getId() != null ? !(getId().equals(that.getId()))
				: that.getId() != null) {
			return false;
		}

		if (that.getId() == null && getId() == null) {
			return false;
		}

		return true;
	}

	public String getRoute() {
		StringBuilder sb = new StringBuilder();
		TerritorialDivision parent = this.getParent();
		while (parent != null) {
			sb.append(parent.getName());
			if (parent.getParent() != null) {
				sb.append(" - ");
			}
			parent = parent.getParent();
		}
		return sb.toString();
	}

	
	//@tag predioEspecial
	//@author macartuche
	//@date 2016-10-27T16:07
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	//@tag cambioClaves
	//@author macartuche
	//@date 2018-1108T11:31
	public Boolean getClassifierGeo() {
		return classifierGeo;
	}

	public void setClassifierGeo(Boolean classifierGeo) {
		this.classifierGeo = classifierGeo;
	}
}// end TerritorialDivision