package ec.gob.gim.revenue.model.impugnment.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class ImpugnmentDTO {
	@NativeQueryResultColumn(index = 0)
	private Long id;
	
	@NativeQueryResultColumn(index = 1)
    private Date creationdate;
	
	@NativeQueryResultColumn(index = 2)
    private String numberinfringement;
	
	@NativeQueryResultColumn(index = 3)
    private String numberprosecution;
	
	@NativeQueryResultColumn(index = 4)
    private String numbertramit;
	
	@NativeQueryResultColumn(index = 5)
    private String observation;
	
	@NativeQueryResultColumn(index = 6)
    private Date updatedate;
	
	@NativeQueryResultColumn(index = 7)
    private Long municipalbond_id;
	
	@NativeQueryResultColumn(index = 8)
    private Long status_id;
	
	@NativeQueryResultColumn(index = 9)
    private Long userregister_id;
	
	@NativeQueryResultColumn(index = 10)
    private Long userupdate_id;
	
	@NativeQueryResultColumn(index = 11)
    private String identificationnumber;
    
	@NativeQueryResultColumn(index = 12)
    private String resident_name;
	
    @NativeQueryResultColumn(index = 13)
    private String statusname;
    
    @NativeQueryResultColumn(index = 14)
    private Integer municipalbond_number;
    
    @NativeQueryResultColumn(index = 15)
    private BigDecimal municipalbond_value;
    
    @NativeQueryResultColumn(index = 16)
    private String statuscode;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreationdate() {
		return creationdate;
	}
	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}
	public String getNumberinfringement() {
		return numberinfringement;
	}
	public void setNumberinfringement(String numberinfringement) {
		this.numberinfringement = numberinfringement;
	}
	public String getNumberprosecution() {
		return numberprosecution;
	}
	public void setNumberprosecution(String numberprosecution) {
		this.numberprosecution = numberprosecution;
	}
	public String getNumbertramit() {
		return numbertramit;
	}
	public void setNumbertramit(String numbertramit) {
		this.numbertramit = numbertramit;
	}
	public String getObservation() {
		return observation;
	}
	public void setObservation(String observation) {
		this.observation = observation;
	}
	public Date getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}
	public Long getMunicipalbond_id() {
		return municipalbond_id;
	}
	public void setMunicipalbond_id(Long municipalbond_id) {
		this.municipalbond_id = municipalbond_id;
	}
	public Long getStatus_id() {
		return status_id;
	}
	public void setStatus_id(Long status_id) {
		this.status_id = status_id;
	}
	public Long getUserregister_id() {
		return userregister_id;
	}
	public void setUserregister_id(Long userregister_id) {
		this.userregister_id = userregister_id;
	}
	public Long getUserupdate_id() {
		return userupdate_id;
	}
	public void setUserupdate_id(Long userupdate_id) {
		this.userupdate_id = userupdate_id;
	}
	public String getIdentificationnumber() {
		return identificationnumber;
	}
	public void setIdentificationnumber(String identificationnumber) {
		this.identificationnumber = identificationnumber;
	}
	public String getResident_name() {
		return resident_name;
	}
	public void setResident_name(String resident_name) {
		this.resident_name = resident_name;
	}
	public String getStatusname() {
		return statusname;
	}
	public void setStatusname(String statusname) {
		this.statusname = statusname;
	}
	public Integer getMunicipalbond_number() {
		return municipalbond_number;
	}
	public void setMunicipalbond_number(Integer municipalbond_number) {
		this.municipalbond_number = municipalbond_number;
	}
	public BigDecimal getMunicipalbond_value() {
		return municipalbond_value;
	}
	public void setMunicipalbond_value(BigDecimal municipalbond_value) {
		this.municipalbond_value = municipalbond_value;
	}
	public String getStatuscode() {
		return statuscode;
	}
	public void setStatuscode(String statuscode) {
		this.statuscode = statuscode;
	}
}