package ec.gob.gim.income.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class ReplacementAgreementDTO {

	@NativeQueryResultColumn(index = 0)
	private Long replacement_id;

	@NativeQueryResultColumn(index = 1)
	private Date replacement_date;

	@NativeQueryResultColumn(index = 2)
	private String replacement_detail;

	@NativeQueryResultColumn(index = 3)
	private BigDecimal replacement_total;

	@NativeQueryResultColumn(index = 4)
	private String identificationnumber;

	@NativeQueryResultColumn(index = 5)
	private String name_resident;

	@NativeQueryResultColumn(index = 6)
	private Long mb_number;

	@NativeQueryResultColumn(index = 7)
	private BigDecimal mb_value;

	@NativeQueryResultColumn(index = 8)
	private Date mb_emisiondate;

	@NativeQueryResultColumn(index = 9)
	private String ent_code;

	@NativeQueryResultColumn(index = 10)
	private String ent_name;

	public Long getReplacement_id() {
		return replacement_id;
	}

	public void setReplacement_id(Long replacement_id) {
		this.replacement_id = replacement_id;
	}

	public Date getReplacement_date() {
		return replacement_date;
	}

	public void setReplacement_date(Date replacement_date) {
		this.replacement_date = replacement_date;
	}

	public String getReplacement_detail() {
		return replacement_detail;
	}

	public void setReplacement_detail(String replacement_detail) {
		this.replacement_detail = replacement_detail;
	}

	public BigDecimal getReplacement_total() {
		return replacement_total;
	}

	public void setReplacement_total(BigDecimal replacement_total) {
		this.replacement_total = replacement_total;
	}

	public String getIdentificationnumber() {
		return identificationnumber;
	}

	public void setIdentificationnumber(String identificationnumber) {
		this.identificationnumber = identificationnumber;
	}

	public String getName_resident() {
		return name_resident;
	}

	public void setName_resident(String name_resident) {
		this.name_resident = name_resident;
	}
	

	public Long getMb_number() {
		return mb_number;
	}

	public void setMb_number(Long mb_number) {
		this.mb_number = mb_number;
	}

	public BigDecimal getMb_value() {
		return mb_value;
	}

	public void setMb_value(BigDecimal mb_value) {
		this.mb_value = mb_value;
	}

	public Date getMb_emisiondate() {
		return mb_emisiondate;
	}

	public void setMb_emisiondate(Date mb_emisiondate) {
		this.mb_emisiondate = mb_emisiondate;
	}

	public String getEnt_code() {
		return ent_code;
	}

	public void setEnt_code(String ent_code) {
		this.ent_code = ent_code;
	}

	public String getEnt_name() {
		return ent_name;
	}

	public void setEnt_name(String ent_name) {
		this.ent_name = ent_name;
	}

}
