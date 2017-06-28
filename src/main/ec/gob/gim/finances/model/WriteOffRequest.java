package ec.gob.gim.finances.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.waterservice.model.WaterSupply;

@Audited
@Entity
@TableGenerator(name = "WriteOffRequestGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "WriteOffRequest", initialValue = 1, allocationSize = 1)
public class WriteOffRequest {
	
	@Id
	@GeneratedValue(generator = "WriteOffRequestGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date date;

	private Boolean isActive;
	
	@Column(length = 50)
	private String internalProcessNumber;

	
	/**
	 * numero de secuencia a dar de baja
	 */
	@ManyToOne
	@JoinColumn(name = "sequencemanager_id")
	private SequenceManager sequenceManager;
	

	/**
	 * servicio de agua potable a dar de baja
	 */
	@ManyToOne
	@JoinColumn(name = "watersupply_id")
	private WaterSupply waterSupply; 	
	
	
	/**
	 * contribuyente
	 */
	@ManyToOne
	@JoinColumn(name = "resident_id")
	private Resident resident;
	
	/**
	 * usuario que alabora la baja
	 */
	@ManyToOne
	@JoinColumn(name = "madeby_id")
	private Resident madeBy;
	
	/**
	 * usuario que aprueba la baja
	 */
	@ManyToOne
	@JoinColumn(name = "approvedby_id")
	private Resident approvedBy;
	
	

}
