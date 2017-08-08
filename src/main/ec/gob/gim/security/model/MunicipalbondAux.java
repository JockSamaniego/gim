package ec.gob.gim.security.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.coercive.model.Notification;
import ec.gob.gim.income.model.Deposit;
import ec.gob.gim.revenue.model.MunicipalBond;

@Entity
@Audited
@TableGenerator(name = "MunicipalbondAuxGenerator", 
				table = "IdentityGenerator", 
				pkColumnName = "name", 
				valueColumnName = "value", 
				pkColumnValue = "MunicipalbondAux", 
				initialValue = 1, 
				allocationSize = 1)

@NamedQueries(value = { 
		@NamedQuery(name = "MunicipalbondAux.findByDepositId", 
					query = "SELECT mba FROM MunicipalbondAux mba where mba.deposit in :depositList"),
		@NamedQuery(name="MunicipalbondAux.setAsVoid", 
		query="UPDATE MunicipalbondAux mba SET mba.status = :status "
				+ "WHERE mba.deposit.id in :depositList ")})

public class MunicipalbondAux {

	@Id
	@GeneratedValue(generator = "MunicipalbondAuxGenerator", strategy = GenerationType.TABLE)
	private Long id;
	

	@Temporal(TemporalType.DATE)
	private Date liquidationDate;

	@Temporal(TemporalType.TIME)
	private Date liquidationTime;
	
	private BigDecimal payValue;
	
	private BigDecimal interest;
	
	private Boolean itconverinterest;
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "municipalbond_id")
	private MunicipalBond municipalbond;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deposit_id")
	private Deposit deposit;
	
	
	private String status;
	

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Date getLiquidationDate() {
		return liquidationDate;
	}


	public void setLiquidationDate(Date liquidationDate) {
		this.liquidationDate = liquidationDate;
	}


	public Date getLiquidationTime() {
		return liquidationTime;
	}


	public void setLiquidationTime(Date liquidationTime) {
		this.liquidationTime = liquidationTime;
	}

	public Boolean getItconverinterest() {
		return itconverinterest;
	}


	public void setItconverinterest(Boolean itconverinterest) {
		this.itconverinterest = itconverinterest;
	}


	public MunicipalBond getMunicipalbond() {
		return municipalbond;
	}


	public void setMunicipalbond(MunicipalBond municipalbond) {
		this.municipalbond = municipalbond;
	}


	public BigDecimal getPayValue() {
		return payValue;
	}


	public void setPayValue(BigDecimal payValue) {
		this.payValue = payValue;
	}


	public BigDecimal getInterest() {
		return interest;
	}


	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}


	public Deposit getDeposit() {
		return deposit;
	}


	public void setDeposit(Deposit deposit) {
		this.deposit = deposit;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
}
