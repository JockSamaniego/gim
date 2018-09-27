package ec.gob.gim.ant.ucot.model;
import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity	
public class CoipDTO {
	
	@NativeQueryResultColumn(index = 0)
	private BigDecimal points;
	
	@NativeQueryResultColumn(index = 1)
	private BigDecimal percetaje;
	
	@NativeQueryResultColumn(index = 2)
	private String detail;

	public BigDecimal getPoints() {
		return points;
	}

	public void setPoints(BigDecimal points) {
		this.points = points;
	}

	public BigDecimal getPercetaje() {
		return percetaje;
	}

	public void setPercetaje(BigDecimal percetaje) {
		this.percetaje = percetaje;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	

}
