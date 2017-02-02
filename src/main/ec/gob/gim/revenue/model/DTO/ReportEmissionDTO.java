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
	private Long cantidad;
	
	@NativeQueryResultColumn(index = 4)
	private BigDecimal amount_emission;
	
	@NativeQueryResultColumn(index = 5)
	private BigDecimal amount_bajas_posteriores;
	
	@NativeQueryResultColumn(index = 6)
	private BigDecimal amount_bajas_current;

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

	public Long getCantidad() {
		return cantidad;
	}

	public void setCantidad(Long cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getAmount_emission() {
		return amount_emission;
	}

	public void setAmount_emission(BigDecimal amount_emission) {
		this.amount_emission = amount_emission;
	}

	public BigDecimal getAmount_bajas_posteriores() {
		return amount_bajas_posteriores;
	}

	public void setAmount_bajas_posteriores(BigDecimal amount_bajas_posteriores) {
		this.amount_bajas_posteriores = amount_bajas_posteriores;
	}

	public BigDecimal getAmount_bajas_current() {
		return amount_bajas_current;
	}

	public void setAmount_bajas_current(BigDecimal amount_bajas_current) {
		this.amount_bajas_current = amount_bajas_current;
	}

	@Override
	public String toString() {
		return "ReportEmissionDTO [acount_id=" + acount_id + ", account_code="
				+ account_code + ", account_name=" + account_name
				+ ", cantidad=" + cantidad + ", amount_emission="
				+ amount_emission + ", amount_bajas_posteriores="
				+ amount_bajas_posteriores + ", amount_bajas_current="
				+ amount_bajas_current + "]";
	}
	
}