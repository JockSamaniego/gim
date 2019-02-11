package ec.gob.gim.income.model;

import java.math.BigDecimal;
import java.util.Date;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class ReceiptDTO {


	@NativeQueryResultColumn(index = 0)	
	private Long  id;
	
	@NativeQueryResultColumn(index = 1)
	private String name;

	@NativeQueryResultColumn(index = 2)
	private Date emission;
	
	
	@NativeQueryResultColumn(index = 3)
	private Date authorizationDate;
	
	@NativeQueryResultColumn(index = 4)
	private String sequential;
	
	@NativeQueryResultColumn(index = 5)
	private String identification;
	
	@NativeQueryResultColumn(index = 6)
	private Long bondid;
	
	@NativeQueryResultColumn(index = 7)
	private BigDecimal total;

	public ReceiptDTO(Long id, String name, Date emission, Date authorizationDate, String sequential,
			String identification, Long bondid, BigDecimal total) {		
		this.id = id;
		this.name = name;
		this.emission = emission;
		this.authorizationDate = authorizationDate;
		this.sequential = sequential;
		this.identification = identification;
		this.bondid = bondid;
		this.total = total;
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

	public Date getEmission() {
		return emission;
	}

	public void setEmission(Date emission) {
		this.emission = emission;
	}

	public Date getAuthorizationDate() {
		return authorizationDate;
	}

	public void setAuthorizationDate(Date authorizationDate) {
		this.authorizationDate = authorizationDate;
	}

	public String getSequential() {
		return sequential;
	}

	public void setSequential(String sequential) {
		this.sequential = sequential;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public Long getBondid() {
		return bondid;
	}

	public void setBondid(Long bondid) {
		this.bondid = bondid;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
}
