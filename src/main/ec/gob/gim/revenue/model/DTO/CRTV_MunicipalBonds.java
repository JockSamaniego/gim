package ec.gob.gim.revenue.model.DTO;

import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

/**
 * 
 * @author rfam
 * @since 2019-05-12
 *
 */
@NativeQueryResultEntity
public class CRTV_MunicipalBonds {

	@NativeQueryResultColumn(index = 0)
	private Long bondId;

	@NativeQueryResultColumn(index = 1)
	private String residentIdentification;

	@NativeQueryResultColumn(index = 2)
	private String residentName;

	@NativeQueryResultColumn(index = 3)
	private String bondState;

	@NativeQueryResultColumn(index = 4)
	private String bondEntryName;

	@NativeQueryResultColumn(index = 5)
	private Long bondNumber;

	@NativeQueryResultColumn(index = 6)
	private BigDecimal bondBase;

	@NativeQueryResultColumn(index = 7)
	private BigDecimal bondValue;

	@NativeQueryResultColumn(index = 8)
	private BigDecimal bondPaidtotal;

	public CRTV_MunicipalBonds() {

	}

	public CRTV_MunicipalBonds(Long bondId, String residentIdentification, String residentName, String bondState,
			String bondEntryName, Long bondNumber, BigDecimal bondBase, BigDecimal bondValue,
			BigDecimal bondPaidtotal) {
		super();
		this.bondId = bondId;
		this.residentIdentification = residentIdentification;
		this.residentName = residentName;
		this.bondState = bondState;
		this.bondEntryName = bondEntryName;
		this.bondNumber = bondNumber;
		this.bondBase = bondBase;
		this.bondValue = bondValue;
		this.bondPaidtotal = bondPaidtotal;
	}

	public Long getBondId() {
		return bondId;
	}

	public void setBondId(Long bondId) {
		this.bondId = bondId;
	}

	public String getResidentIdentification() {
		return residentIdentification;
	}

	public void setResidentIdentification(String residentIdentification) {
		this.residentIdentification = residentIdentification;
	}

	public String getResidentName() {
		return residentName;
	}

	public void setResidentName(String residentName) {
		this.residentName = residentName;
	}

	public String getBondState() {
		return bondState;
	}

	public void setBondState(String bondState) {
		this.bondState = bondState;
	}

	public String getBondEntryName() {
		return bondEntryName;
	}

	public void setBondEntryName(String bondEntryName) {
		this.bondEntryName = bondEntryName;
	}

	public Long getBondNumber() {
		return bondNumber;
	}

	public void setBondNumber(Long bondNumber) {
		this.bondNumber = bondNumber;
	}

	public BigDecimal getBondBase() {
		return bondBase;
	}

	public void setBondBase(BigDecimal bondBase) {
		this.bondBase = bondBase;
	}

	public BigDecimal getBondValue() {
		return bondValue;
	}

	public void setBondValue(BigDecimal bondValue) {
		this.bondValue = bondValue;
	}

	public BigDecimal getBondPaidtotal() {
		return bondPaidtotal;
	}

	public void setBondPaidtotal(BigDecimal bondPaidtotal) {
		this.bondPaidtotal = bondPaidtotal;
	}

}
