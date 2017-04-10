package ec.gob.gim.income.model.dto;

import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

/**
 * Reporte de bajas por cuenta contable 2017-02-23
 * 
 * @author richard
 *
 */
@NativeQueryResultEntity
public class ReplacementAccountDTO {

	@NativeQueryResultColumn(index = 0)
	private String accountcode;

	@NativeQueryResultColumn(index = 1)
	private String accountname;

	@NativeQueryResultColumn(index = 2)
	private BigDecimal total;

	public String getAccountcode() {
		return accountcode;
	}

	public void setAccountcode(String accountcode) {
		this.accountcode = accountcode;
	}

	public String getAccountname() {
		return accountname;
	}

	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	/*public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}*/

}
