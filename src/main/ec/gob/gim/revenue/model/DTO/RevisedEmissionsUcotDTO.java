package ec.gob.gim.revenue.model.DTO;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class RevisedEmissionsUcotDTO {

	@NativeQueryResultColumn(index = 0)
	private String identificationNumber;
	
	@NativeQueryResultColumn(index = 1)
	private String name;
	
	@NativeQueryResultColumn(index = 2)
	private String article;
	
	@NativeQueryResultColumn(index = 3)
	private String infractionNumber;
	
	@NativeQueryResultColumn(index = 4)
	private Long gimNumber;
	
	@NativeQueryResultColumn(index = 5)
	private String vehiclePlate;
	
	@NativeQueryResultColumn(index = 6)
	@Temporal(TemporalType.TIMESTAMP) 
	private Date citationDate;

	@NativeQueryResultColumn(index = 7)
	private BigDecimal value;
	

	public RevisedEmissionsUcotDTO() {
		super();
	}

	public RevisedEmissionsUcotDTO(String identificationNumber, String name,
			String article, String infractionNumber, Long gimNumber,
			String vehiclePlate, Date citationDate, BigDecimal value) {
		super();
		this.identificationNumber = identificationNumber;
		this.name = name;
		this.article = article;
		this.infractionNumber = infractionNumber;
		this.gimNumber = gimNumber;
		this.vehiclePlate = vehiclePlate;
		this.citationDate = citationDate;
		this.value = value;
	}

	@Override
	public String toString() {
		return "RevisedEmissionsUcotDTO [identificationNumber="
				+ identificationNumber + ", name=" + name + ", article="
				+ article + ", infractionNumber=" + infractionNumber
				+ ", gimNumber=" + gimNumber + ", vehiclePlate=" + vehiclePlate
				+ ", citationDate=" + citationDate + ", value=" + value + "]";
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getInfractionNumber() {
		return infractionNumber;
	}

	public void setInfractionNumber(String infractionNumber) {
		this.infractionNumber = infractionNumber;
	}

	public Long getGimNumber() {
		return gimNumber;
	}

	public void setGimNumber(Long gimNumber) {
		this.gimNumber = gimNumber;
	}

	public String getVehiclePlate() {
		return vehiclePlate;
	}

	public void setVehiclePlate(String vehiclePlate) {
		this.vehiclePlate = vehiclePlate;
	}

	public Date getCitationDate() {
		return citationDate;
	}

	public void setCitationDate(Date citationDate) {
		this.citationDate = citationDate;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	
	
}
