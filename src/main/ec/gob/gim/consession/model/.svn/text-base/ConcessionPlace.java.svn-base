package ec.gob.gim.consession.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Contract;

/**
 * @version 1.0
 * @created 02-ago-2013 16:54:34
 */

@Audited
@Entity
@TableGenerator(name = "ConcessionPlaceGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "ConcessionPlace", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "ConcessionPlace.findIdsByGroup", query = "SELECT cp.id FROM ConcessionPlace cp "
				+ "WHERE cp.concessionGroup.id = :cgId and cp.isActive = :isActive "
				+ "order by CP.internalOrder"),
		@NamedQuery(name = "ConcessionPlace.findPlaceTypeByGroup", query = "select cp.consessionPlaceType from ConcessionPlace cp "
				+ "left join cp.concessionGroup cg where cg.id = :cgId"),

		@NamedQuery(name = "ConcessionPlace.findIdsByGroupAndPlaceType", query = "SELECT cp.id FROM ConcessionPlace cp "
				+ "WHERE cp.concessionGroup.id = :cgId and cp.isActive = :isActive and cp.consessionPlaceType.id=:idCpt "
				+ "order by CP.internalOrder"),

		@NamedQuery(name = "ConcessionPlace.findByGroupAndPlaceType", query = "SELECT cp FROM ConcessionPlace cp "
				+ "WHERE cp.concessionGroup.id = :cgId and cp.isActive = :isActive and cp.consessionPlaceType.id=:idCpt "
				+ "order by cp.internalOrder")

})
public class ConcessionPlace {

	@Id
	@GeneratedValue(generator = "ConcessionPlaceGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private ConcessionStatus concessionStatus;

	@Column(length = 30, nullable = false)
	private String name;

	private Boolean isActive;

	private Integer internalOrder;

	@ManyToOne
	@JoinColumn(name = "resident_id")
	private Resident resident;

	@ManyToOne
	@JoinColumn(name = "consessionPlaceType_id")
	private ConcessionPlaceType consessionPlaceType;

	@OneToOne(cascade = CascadeType.ALL)
	private Contract currentContract;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "concessionplace_id")
	private List<Contract> contracts;

	@OneToMany(mappedBy = "place", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<ConcessionItem> concessionItems;

	@ManyToOne
	private ConcessionGroup concessionGroup;

	public ConcessionPlace() {
		isActive = new Boolean(true);
		this.concessionStatus = ConcessionStatus.RENTED;
		this.contracts = new ArrayList<Contract>();
		this.concessionItems = new ArrayList<ConcessionItem>();
	}

	public void finalize() throws Throwable {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.toUpperCase();
	}

	public ConcessionStatus getConcessionStatus() {
		return concessionStatus;
	}

	public void setConcessionStatus(ConcessionStatus concessionStatus) {
		this.concessionStatus = concessionStatus;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public Contract getCurrentContract() {
		return currentContract;
	}

	public void setCurrentContract(Contract currentContract) {
		this.currentContract = currentContract;
	}

	public List<Contract> getContracts() {
		return contracts;
	}

	public void setContracts(List<Contract> contracts) {
		this.contracts = contracts;
	}

	public ConcessionGroup getConcessionGroup() {
		return concessionGroup;
	}

	public void setConcessionGroup(ConcessionGroup concessionGroup) {
		this.concessionGroup = concessionGroup;
	}

	public Integer getInternalOrder() {
		return internalOrder;
	}

	public void setInternalOrder(Integer internalOrder) {
		this.internalOrder = internalOrder;
	}

	public ConcessionPlaceType getConsessionPlaceType() {
		return consessionPlaceType;
	}

	public void setConsessionPlaceType(ConcessionPlaceType consessionPlaceType) {
		this.consessionPlaceType = consessionPlaceType;
	}

	public void add(Contract c) {
		if (!this.contracts.contains(c) && c != null) {
			this.contracts.add(c);
		}
	}

	public void remove(Contract c) {
		if (c != null && this.contracts.contains(c)) {
			this.contracts.remove(c);
		}
	}

	public List<ConcessionItem> getConcessionItems() {
		return concessionItems;
	}

	public void setConcessionItems(List<ConcessionItem> concessionItems) {
		this.concessionItems = concessionItems;
	}

	public void add(ConcessionItem ci) {
		if (!this.concessionItems.contains(ci)) {
			this.concessionItems.add(ci);
			ci.setPlace(this);
		}
	}

	public void remove(ConcessionItem ci) {
		boolean removed = this.concessionItems.remove(ci);
		if (removed)
			ci.setPlace(null);
	}

}// end ConcessionPlace