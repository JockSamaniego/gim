package ec.gob.gim.coercive.model;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class NotificationPayedDTO {

	@NativeQueryResultColumn(index = 0)
	private Integer count;
	
	@NativeQueryResultColumn(index = 1)
	private String name;
	
	@NativeQueryResultColumn(index = 2)
	private BigDecimal total;
	
	@NativeQueryResultColumn(index = 3)
	private BigInteger entryId;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigInteger getEntryId() {
		return entryId;
	}

	public void setEntryId(BigInteger entryId) {
		this.entryId = entryId;
	}
	
	
}
