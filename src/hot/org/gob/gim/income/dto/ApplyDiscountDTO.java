package org.gob.gim.income.dto;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class ApplyDiscountDTO {

	@NativeQueryResultColumn(index = 0)
	private Long pendientes;
	
	@NativeQueryResultColumn(index = 1)
	private Long pagadas;
	
	@NativeQueryResultColumn(index = 2)
	private Long abonos;
	
	@NativeQueryResultColumn(index = 3)
	private Long convenios;
	
	@NativeQueryResultColumn(index = 4)
	private Long compensacion;
	
	@NativeQueryResultColumn(index = 5)
	private Long vencidas;
	
	@NativeQueryResultColumn(index = 6)
	private Long futuras;
	
	@NativeQueryResultColumn(index = 7)
	private Long pagadasExternas;
	
	
	public ApplyDiscountDTO() {
		super();
	}
	
	public ApplyDiscountDTO(Long pendientes, Long pagadas, Long abonos, Long convenios, Long compensacion,
			Long vencidas) {
		super();
		this.pendientes = pendientes;
		this.pagadas = pagadas;
		this.abonos = abonos;
		this.convenios = convenios;
		this.compensacion = compensacion;
		this.vencidas = vencidas;
	}

	public Long getPendientes() {
		return pendientes;
	}

	public void setPendientes(Long pendientes) {
		this.pendientes = pendientes;
	}

	public Long getPagadas() {
		return pagadas;
	}

	public void setPagadas(Long pagadas) {
		this.pagadas = pagadas;
	}

	public Long getAbonos() {
		return abonos;
	}

	public void setAbonos(Long abonos) {
		this.abonos = abonos;
	}

	public Long getConvenios() {
		return convenios;
	}

	public void setConvenios(Long convenios) {
		this.convenios = convenios;
	}

	public Long getCompensacion() {
		return compensacion;
	}

	public void setCompensacion(Long compensacion) {
		this.compensacion = compensacion;
	}

	public Long getVencidas() {
		return vencidas;
	}

	public void setVencidas(Long vencidas) {
		this.vencidas = vencidas;
	}

	public Long getFuturas() {
		return futuras;
	}

	public void setFuturas(Long futuras) {
		this.futuras = futuras;
	}

	public Long getPagadasExternas() {
		return pagadasExternas;
	}

	public void setPagadasExternas(Long pagadasExternas) {
		this.pagadasExternas = pagadasExternas;
	}
	
	public int totalCuotas() {
		int total = pendientes.intValue() + pagadas.intValue() 
		+ abonos.intValue() + convenios.intValue() + futuras.intValue() + pagadasExternas.intValue();
		/*this.abonos = abonos;
		this.convenios = convenios;
		this.compensacion = compensacion;
		this.vencidas = vencidas;
		this.futuras = futuras;
		this.pagadasExternas = pagadasExternas;*/
		return total;
	}

}
