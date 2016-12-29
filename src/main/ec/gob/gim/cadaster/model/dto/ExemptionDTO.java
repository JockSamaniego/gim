package ec.gob.gim.cadaster.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;
@NativeQueryResultEntity	
public class ExemptionDTO {
	
	@NativeQueryResultColumn(index = 0)
	private BigDecimal commercialAppraisalResident;
	
	@NativeQueryResultColumn(index = 1)
	private BigDecimal commercialAppraisalPattern;
	
	@NativeQueryResultColumn(index = 2)
	private BigDecimal commercialAppraisalSpecial;
	
	@NativeQueryResultColumn(index = 3)
	private BigDecimal commercialAppraisalTable;

	public BigDecimal getCommercialAppraisalResident() {
		return commercialAppraisalResident;
	}

	public void setCommercialAppraisalResident(BigDecimal commercialAppraisalResident) {
		this.commercialAppraisalResident = commercialAppraisalResident;
	}

	public BigDecimal getCommercialAppraisalPattern() {
		return commercialAppraisalPattern;
	}

	public void setCommercialAppraisalPattern(BigDecimal commercialAppraisalPattern) {
		this.commercialAppraisalPattern = commercialAppraisalPattern;
	}

	public BigDecimal getCommercialAppraisalSpecial() {
		return commercialAppraisalSpecial;
	}

	public void setCommercialAppraisalSpecial(BigDecimal commercialAppraisalSpecial) {
		this.commercialAppraisalSpecial = commercialAppraisalSpecial;
	}

	public BigDecimal getCommercialAppraisalTable() {
		return commercialAppraisalTable;
	}

	public void setCommercialAppraisalTable(BigDecimal commercialAppraisalTable) {
		this.commercialAppraisalTable = commercialAppraisalTable;
	}
}
