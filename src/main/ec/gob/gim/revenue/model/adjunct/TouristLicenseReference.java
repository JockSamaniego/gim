package ec.gob.gim.revenue.model.adjunct;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import ec.gob.gim.commercial.model.TouristLicenseItem;
import ec.gob.gim.revenue.model.Adjunct;

@Audited
@Entity
@DiscriminatorValue("TLR")
public class TouristLicenseReference extends Adjunct{
	
	private String category;
	private Integer year;
	private String bussinessName;
	//private String owner;
		

	@ManyToOne
	private TouristLicenseItem licenseItem;

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(year != null ? year : "ND");
		sb.append(" - ");
		sb.append(category != null ? category : "ND");
		
		return sb.toString();
	}

	public List<ValuePair> getDetails() {
		List<ValuePair> details = new LinkedList<ValuePair>();
		ValuePair pair = new ValuePair("Año", String.valueOf(year));
		details.add(pair);
		pair = new ValuePair("Categoría", category);
		details.add(pair);
		pair = new ValuePair("Local", bussinessName);
		details.add(pair);
		return details;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public TouristLicenseItem getLicenseItem() {
		return licenseItem;
	}

	public void setLicenseItem(TouristLicenseItem licenseItem) {
		this.licenseItem = licenseItem;
	}

	public String getBussinessName() {
		return bussinessName;
	}

	public void setBussinessName(String bussinessName) {
		this.bussinessName = bussinessName;
	}

	
}
