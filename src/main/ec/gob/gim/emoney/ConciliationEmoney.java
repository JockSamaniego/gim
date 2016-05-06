package ec.gob.gim.emoney;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

/**
 * @author macartuche
 * @version 1.0
 * @created 17-abr-2016 15:57:36
 */
@Audited
@Entity
@TableGenerator(name = "ConciliationEmoneyGenerator", table = "IdentityGenerator", pkColumnName = "name", 
valueColumnName = "value", pkColumnValue = "ConciliationEmoney", initialValue = 1, allocationSize = 1)
@NamedQueries({
	@NamedQuery(name="ConciliationEmoney.findByDateAndAccount",
				query=  "SELECT cem FROM ConciliationEmoney cem WHERE cem.datetransaction between :startDate AND :endDate"
						+ " AND cem.account like :account"
						+ " ORDER BY cem.datetransaction")
})
public class ConciliationEmoney {
	@Id
	@GeneratedValue(generator = "ConciliationEmoneyGenerator", strategy = GenerationType.TABLE)
	private Long id;

	private String agentid;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date datetransaction;
	
	private Long transactionid;
	
	private Long movementid;
	
	private String service;
	
	private String movtype;
	
	private BigDecimal amount;
	
	private BigDecimal balanceStart;
	
	private BigDecimal balanceEnd;
	
	private String agentname;
	
	private String sourceUser;
	
	private String destinyUser;
	
	private String utfi;
	
	private String interfacews;
	
	private String identification;
	
	private String externaltransid;
	
	private String account;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public Date getDatetransaction() {
		return datetransaction;
	}

	public void setDatetransaction(Date datetransaction) {
		this.datetransaction = datetransaction;
	}

	public Long getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(Long transactionid) {
		this.transactionid = transactionid;
	}

	public Long getMovementid() {
		return movementid;
	}

	public void setMovementid(Long movementid) {
		this.movementid = movementid;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getMovtype() {
		return movtype;
	}

	public void setMovtype(String movtype) {
		this.movtype = movtype;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getBalanceStart() {
		return balanceStart;
	}

	public void setBalanceStart(BigDecimal balanceStart) {
		this.balanceStart = balanceStart;
	}

	public BigDecimal getBalanceEnd() {
		return balanceEnd;
	}

	public void setBalanceEnd(BigDecimal balanceEnd) {
		this.balanceEnd = balanceEnd;
	}

	public String getAgentname() {
		return agentname;
	}

	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}

	public String getSourceUser() {
		return sourceUser;
	}

	public void setSourceUser(String sourceUser) {
		this.sourceUser = sourceUser;
	}

	public String getDestinyUser() {
		return destinyUser;
	}

	public void setDestinyUser(String destinyUser) {
		this.destinyUser = destinyUser;
	}

	public String getUtfi() {
		return utfi;
	}

	public void setUtfi(String utfi) {
		this.utfi = utfi;
	}

	public String getInterfacews() {
		return interfacews;
	}

	public void setInterfacews(String interfacews) {
		this.interfacews = interfacews;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getExternaltransid() {
		return externaltransid;
	}

	public void setExternaltransid(String externaltransid) {
		this.externaltransid = externaltransid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
}
