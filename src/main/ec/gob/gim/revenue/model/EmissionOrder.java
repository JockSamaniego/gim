package ec.gob.gim.revenue.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.envers.Audited;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.common.model.Person;

/**
 * EmissionOrder
 * 
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:33
 */
@Audited
@Entity
@TableGenerator(
		name = "EmissionOrderGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "EmissionOrder", 
		initialValue = 1, allocationSize = 1
)


@NamedQueries(value = {
		@NamedQuery(name = "EmissionOrder.findMunicipalBondsByEmissionOrderId", 
				query =   "SELECT distinct mb FROM EmissionOrder eo "
						+ "join eo.municipalBonds mb "
						+ "LEFT JOIN FETCH mb.receipt "						
						+ "WHERE eo.id = :id"),
						
		@NamedQuery(name = "EmissionOrder.findMunicipalBondViewByEmissionOrderId", 
				query =   "SELECT NEW ec.gob.gim.revenue.model.MunicipalBondView(mb.id,resident.name,mb.description,mb.base,mb.paidTotal) FROM EmissionOrder eo "
						+ "join eo.municipalBonds mb "						
						+ "JOIN mb.resident resident "
						+ "WHERE eo.id = :id"),
								
		@NamedQuery(name = "EmissionOrder.findById", 
				query =   "SELECT eo FROM EmissionOrder eo "						
						+ "WHERE eo.id = :id"),
		@NamedQuery(name = "EmissionOrder.MunicipalBondsIdsByEmissionOrderId", 
				query =   "SELECT distinct mb.id FROM EmissionOrder eo "		
						+ "join eo.municipalBonds mb "
						+ "WHERE eo.id = :id")})

public class EmissionOrder {
	@Id
	@GeneratedValue(generator = "EmissionOrderGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	private String description;
	
	private Date serviceDate;
	
	private Boolean isDispatched;
		
	@Transient
	private boolean isSelected;
	
	@Version
	private Long version = 0L;
	
	public boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Transient
	private BigDecimal amount;
	
	@ManyToOne (fetch=FetchType.LAZY)
	@JoinColumn(name="emisor_id")
	private Person emisor;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="emissionOrder_id")
	private List<MunicipalBond> municipalBonds;
	
	@OneToOne(mappedBy="emissionOrder", fetch = FetchType.LAZY)
	private RevisionEmissionOrderFM revisionFM;
	
	public EmissionOrder(){
		municipalBonds = new ArrayList<MunicipalBond>();
		isDispatched = Boolean.FALSE;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<MunicipalBond> getMunicipalBonds() {
		return municipalBonds;
	}

	public void setMunicipalBonds(List<MunicipalBond> municipalBonds) {
		this.municipalBonds = municipalBonds;
	}

	public Date getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}
	
	public void add(MunicipalBond m){
		if (!municipalBonds.contains(m)){
			municipalBonds.add(m);			
		}
	}
	
	public void remove(MunicipalBond m){
		if (municipalBonds.contains(m)){
			municipalBonds.remove(m);			
		}
	}

	public void setEmisor(Person emisor) {
		this.emisor = emisor;
	}

	public Person getEmisor() {
		return emisor;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	
	public Boolean getIsDispatched() {
		return isDispatched;
	}

	public void setIsDispatched(Boolean isDispatched) {
		this.isDispatched = isDispatched;
	}

	public RevisionEmissionOrderFM getRevisionFM() {
		return revisionFM;
	}

	public void setRevisionFM(RevisionEmissionOrderFM revisionFM) {
		this.revisionFM = revisionFM;
	}


}
