package ec.gob.gim.revenue.model.bankDebit.dto;

import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class BankDebitReportDTO {
	
	@NativeQueryResultColumn(index = 0)
	private String identificacion;
	
	@NativeQueryResultColumn(index = 1)
    private String contribuyente;
	
	@NativeQueryResultColumn(index = 2)
    private String tipo_cuenta;
	
	@NativeQueryResultColumn(index = 3)
    private String numero_cuenta;
	
	@NativeQueryResultColumn(index = 4)
    private String titular;
	
	@NativeQueryResultColumn(index = 5)
    private Integer servicio;
	
	@NativeQueryResultColumn(index = 6)
    private Integer cantidad;
	
	@NativeQueryResultColumn(index = 7)
    private BigDecimal valor;

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getContribuyente() {
		return contribuyente;
	}

	public void setContribuyente(String contribuyente) {
		this.contribuyente = contribuyente;
	}

	public String getTipo_cuenta() {
		return tipo_cuenta;
	}

	public void setTipo_cuenta(String tipo_cuenta) {
		this.tipo_cuenta = tipo_cuenta;
	}

	public String getNumero_cuenta() {
		return numero_cuenta;
	}

	public void setNumero_cuenta(String numero_cuenta) {
		this.numero_cuenta = numero_cuenta;
	}

	public String getTitular() {
		return titular;
	}

	public void setTitular(String titular) {
		this.titular = titular;
	}

	public Integer getServicio() {
		return servicio;
	}

	public void setServicio(Integer servicio) {
		this.servicio = servicio;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}