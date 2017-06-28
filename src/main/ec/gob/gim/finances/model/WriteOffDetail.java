package ec.gob.gim.finances.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

import ec.gob.gim.revenue.model.MunicipalBond;

@Audited
@Entity
@TableGenerator(name = "WriteOffRequestGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "WriteOffRequest", initialValue = 1, allocationSize = 1)
public class WriteOffDetail {
	
	@Id
	@GeneratedValue(generator = "WriteOffRequestGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "oldmb_id")
	private MunicipalBond oldMunicipalBond;
	
	@ManyToOne
	@JoinColumn(name = "newmb_id")
	private MunicipalBond newMunicipalBond;
	
	@ManyToOne
	@JoinColumn(name = "writeoffrequest_id")
	private WriteOffRequest writeOffRequest;

}
