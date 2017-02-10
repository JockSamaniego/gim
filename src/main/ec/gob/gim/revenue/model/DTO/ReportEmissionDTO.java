/**
 * 
 */
package ec.gob.gim.revenue.model.DTO;

import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

/**
 * @author Rene
 *
 */
@NativeQueryResultEntity
public class ReportEmissionDTO {
	
	@NativeQueryResultColumn(index = 0)
	private Long acount_id;
	
	@NativeQueryResultColumn(index = 1)
	private String account_code;
	
	@NativeQueryResultColumn(index = 2)
	private String account_name;
	
	@NativeQueryResultColumn(index = 3)
	private Long cantidad_emisiones;
	
	@NativeQueryResultColumn(index = 4)
	private BigDecimal valor_emision;
	
	@NativeQueryResultColumn(index = 5)
	private Long cantidad_bajas;
	
	@NativeQueryResultColumn(index = 6)
	private BigDecimal valor_bajas;
	
	@NativeQueryResultColumn(index = 7)
	private BigDecimal total_emision;
	
	@NativeQueryResultColumn(index = 8)
	private String tipo;

	public Long getAcount_id() {
		return acount_id;
	}

	public void setAcount_id(Long acount_id) {
		this.acount_id = acount_id;
	}

	public String getAccount_code() {
		return account_code;
	}

	public void setAccount_code(String account_code) {
		this.account_code = account_code;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public Long getCantidad_emisiones() {
		return cantidad_emisiones;
	}

	public void setCantidad_emisiones(Long cantidad_emisiones) {
		this.cantidad_emisiones = cantidad_emisiones;
	}

	public BigDecimal getValor_emision() {
		return valor_emision;
	}

	public void setValor_emision(BigDecimal valor_emision) {
		this.valor_emision = valor_emision;
	}

	public Long getCantidad_bajas() {
		return cantidad_bajas;
	}

	public void setCantidad_bajas(Long cantidad_bajas) {
		this.cantidad_bajas = cantidad_bajas;
	}

	public BigDecimal getValor_bajas() {
		return valor_bajas;
	}

	public void setValor_bajas(BigDecimal valor_bajas) {
		this.valor_bajas = valor_bajas;
	}

	public BigDecimal getTotal_emision() {
		return total_emision;
	}

	public void setTotal_emision(BigDecimal total_emision) {
		this.total_emision = total_emision;
	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "ReportEmissionDTO [acount_id=" + acount_id + ", account_code="
				+ account_code + ", account_name=" + account_name
				+ ", cantidad_emisiones=" + cantidad_emisiones
				+ ", valor_emision=" + valor_emision + ", cantidad_bajas="
				+ cantidad_bajas + ", valor_bajas=" + valor_bajas
				+ ", total_emision=" + total_emision + ", tipo=" + tipo + "]";
	}

}