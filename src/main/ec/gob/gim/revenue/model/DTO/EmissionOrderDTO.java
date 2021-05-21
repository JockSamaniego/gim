/**
 * 
 */
package ec.gob.gim.revenue.model.DTO;

import java.util.Date;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

/**
 * @author René
 *
 */
@NativeQueryResultEntity
public class EmissionOrderDTO {
	@NativeQueryResultColumn(index = 0)
	private Long number;

	@NativeQueryResultColumn(index = 1)
	private String emitterName;

	@NativeQueryResultColumn(index = 2)
	private Date serviceDate;

	@NativeQueryResultColumn(index = 3)
	private String residentName;

	@NativeQueryResultColumn(index = 4)
	private String description;

	@NativeQueryResultColumn(index = 5)
	private int numberBonds;

	@NativeQueryResultColumn(index = 6)
	private String bondsIds;

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public String getEmitterName() {
		return emitterName;
	}

	public void setEmitterName(String emitterName) {
		this.emitterName = emitterName;
	}

	public Date getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}

	public String getResidentName() {
		return residentName;
	}

	public void setResidentName(String residentName) {
		this.residentName = residentName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNumberBonds() {
		return numberBonds;
	}

	public void setNumberBonds(int numberBonds) {
		this.numberBonds = numberBonds;
	}

	public String getBondsIds() {
		return bondsIds;
	}

	public void setBondsIds(String bondsIds) {
		this.bondsIds = bondsIds;
	}

	@Override
	public String toString() {
		return "EmissionOrderDTO [number=" + number + ", emitterName="
				+ emitterName + ", serviceDate=" + serviceDate
				+ ", residentName=" + residentName + ", description="
				+ description + ", numberBonds=" + numberBonds + ", bondsIds="
				+ bondsIds + "]";
	}

}
