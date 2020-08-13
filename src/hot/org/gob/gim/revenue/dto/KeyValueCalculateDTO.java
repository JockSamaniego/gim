package org.gob.gim.revenue.dto;

import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class KeyValueCalculateDTO {
	@NativeQueryResultColumn(index = 0)
	private Long entry_id;

	@NativeQueryResultColumn(index = 1)
	private BigDecimal value;

	public Long getEntry_id() {
		return entry_id;
	}

	public void setEntry_id(Long entry_id) {
		this.entry_id = entry_id;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "KeyValueCalculateDTO [entry_id=" + entry_id + ", value="
				+ value + "]";
	}
}
