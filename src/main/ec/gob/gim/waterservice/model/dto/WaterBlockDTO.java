package ec.gob.gim.waterservice.model.dto;

import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class WaterBlockDTO {

	@NativeQueryResultColumn(index = 0)
	private Integer year;
	
	@NativeQueryResultColumn(index = 1)
	private Integer month;
	
	@NativeQueryResultColumn(index = 2)
	private String estado_consumption;
	
	@NativeQueryResultColumn(index = 3)
	private Long ant;
	
	@NativeQueryResultColumn(index = 4)
	private Long act;
	
	@NativeQueryResultColumn(index = 5)
	private BigDecimal consumo;
	
	@NativeQueryResultColumn(index = 6)
	private Long numero;
	
	@NativeQueryResultColumn(index = 7)
	private String estado_mb;
	
	@NativeQueryResultColumn(index = 8)
	private BigDecimal total;

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public String getEstado_consumption() {
		return estado_consumption;
	}

	public void setEstado_consumption(String estado_consumption) {
		this.estado_consumption = estado_consumption;
	}

	public Long getAnt() {
		return ant;
	}

	public void setAnt(Long ant) {
		this.ant = ant;
	}

	public Long getAct() {
		return act;
	}

	public void setAct(Long act) {
		this.act = act;
	}

	public BigDecimal getConsumo() {
		return consumo;
	}

	public void setConsumo(BigDecimal consumo) {
		this.consumo = consumo;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public String getEstado_mb() {
		return estado_mb;
	}

	public void setEstado_mb(String estado_mb) {
		this.estado_mb = estado_mb;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
}
