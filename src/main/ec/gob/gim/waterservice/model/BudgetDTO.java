package ec.gob.gim.waterservice.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:25
 */

public class BudgetDTO {

	private Long id;
	private String residentName;
	private Long residentId;
	private Integer code;
	private Integer year;	
	private Date date;
	private Date inspectionDate;
	private Boolean isServiceOrderGenerate;
	private String cadastralCode;
	private String propertyCadastralCode;	
	private BigDecimal total;
	
	public BudgetDTO(Long id, Long residentId, String residentName, Integer code, Integer year, Date date, Date inspectionDate, Boolean isServiceOrderGenerate,String cadastralCode, String propertyCadastralCode, BigDecimal total){
		this.id = id;
		this.residentId = residentId;
		this.residentName = residentName;
		this.code = code;
		this.year = year;	
		this.date = date;
		this.inspectionDate = inspectionDate;
		this.isServiceOrderGenerate = isServiceOrderGenerate;
		this.cadastralCode = cadastralCode;
		this.propertyCadastralCode = propertyCadastralCode;		
		this.total = total;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getInspectionDate() {
		return inspectionDate;
	}
	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}
	public Boolean getIsServiceOrderGenerate() {
		return isServiceOrderGenerate;
	}
	public void setIsServiceOrderGenerate(Boolean isServiceOrderGenerate) {
		this.isServiceOrderGenerate = isServiceOrderGenerate;
	}
	public String getCadastralCode() {
		return cadastralCode;
	}
	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}
	public String getPropertyCadastralCode() {
		return propertyCadastralCode;
	}
	public void setPropertyCadastralCode(String propertyCadastralCode) {
		this.propertyCadastralCode = propertyCadastralCode;
	}

	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getResidentName() {
		return residentName;
	}

	public void setResidentName(String residentName) {
		this.residentName = residentName;
	}

	public Long getResidentId() {
		return residentId;
	}

	public void setResidentId(Long residentId) {
		this.residentId = residentId;
	}
	
}