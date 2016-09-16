package ec.gob.gim.cadaster.model.dto;

import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity	
public class AppraisalsPropertyDTO {
	
	@NativeQueryResultColumn(index = 0)
	private Integer year_appraisal;
	
	@NativeQueryResultColumn(index = 1)
	private BigDecimal commercial_appraisal;

	public Integer getYear_appraisal() {
		return year_appraisal;
	}

	public void setYear_appraisal(Integer year_appraisal) {
		this.year_appraisal = year_appraisal;
	}

	public BigDecimal getCommercial_appraisal() {
		return commercial_appraisal;
	}

	public void setCommercial_appraisal(BigDecimal commercial_appraisal) {
		this.commercial_appraisal = commercial_appraisal;
	}
	
}
