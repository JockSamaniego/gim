package ec.gob.gim.cadaster.model.dto;

import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class AffectationFactorDTO {
	
	@NativeQueryResultColumn(index = 0)
	private Long affectationfactor_id;
	
	@NativeQueryResultColumn(index = 1)
	private String category;
	
	@NativeQueryResultColumn(index = 2)
	private BigDecimal coefficient;
	
	@NativeQueryResultColumn(index = 3)
	private String catalogCode;
	
	@NativeQueryResultColumn(index = 4)
	private String type;

	public Long getAffectationfactor_id() {
		return affectationfactor_id;
	}

	public void setAffectationfactor_id(Long affectationfactor_id) {
		this.affectationfactor_id = affectationfactor_id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public BigDecimal getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(BigDecimal coefficient) {
		this.coefficient = coefficient;
	}

	public String getCatalogCode() {
		return catalogCode;
	}

	public void setCatalogCode(String catalogCode) {
		this.catalogCode = catalogCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "AffectationFactorDTO [affectationfactor_id="
				+ affectationfactor_id + ", category=" + category
				+ ", coefficient=" + coefficient + ", catalogCode="
				+ catalogCode + ", type=" + type + "]";
	}
	
}
