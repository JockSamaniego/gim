package ec.gob.gim.revenue.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Resident;

/**
 * @author ronaldpc
 * @version 1.0
 * @created 02-Oct-2014
 */
//@Audited
//@Entity
//@TableGenerator(name = "MunicipalBondCorrectionGenerator", table = "IdentityGenerator", 
//	pkColumnName = "name", valueColumnName = "value", 
//	pkColumnValue = "MunicipalBondCorrection", initialValue = 1, allocationSize = 1)
//
//@NamedQueries(value = {
//		@NamedQuery(name = "MunicipalBondCorrection.findAll", 
//			query =   "SELECT mbc FROM MunicipalBondCorrection mbc " 
//					+ "ORDER BY mbc.id")
//})

@Deprecated
public class MunicipalBondCorrection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3670188519556238327L;

	@Id
	@GeneratedValue(generator = "MunicipalBondCorrectionGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@Column(length=200)
	private String motivation;
	
	@Temporal(TemporalType.DATE)
	private Date creationDate;
	
	@Temporal(TemporalType.TIME)
	private Date creationTime;
	
//	@OneToOne(mappedBy="mbOriginal")
//	MunicipalBond mbOriginal;
//	
//	@OneToOne(mappedBy="mbNew")
//	MunicipalBond mbNew;
//	
//	@ManyToOne
//	@JoinColumn(name="user_id")
//	private Resident user;
//	
}