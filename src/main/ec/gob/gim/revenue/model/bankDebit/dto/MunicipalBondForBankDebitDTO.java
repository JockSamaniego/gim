package ec.gob.gim.revenue.model.bankDebit.dto;

import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class MunicipalBondForBankDebitDTO {
	
	@NativeQueryResultColumn(index = 0)
	private Long municipalBondId;
	
	@NativeQueryResultColumn(index = 1)
	private BigDecimal paidTotal;

	public Long getMunicipalBondId() {
		return municipalBondId;
	}

	public void setMunicipalBondId(Long municipalBondId) {
		this.municipalBondId = municipalBondId;
	}

	public BigDecimal getPaidTotal() {
		return paidTotal;
	}

	public void setPaidTotal(BigDecimal paidTotal) {
		this.paidTotal = paidTotal;
	}
	
}
