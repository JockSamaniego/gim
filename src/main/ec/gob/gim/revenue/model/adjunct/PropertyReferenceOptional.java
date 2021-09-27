package ec.gob.gim.revenue.model.adjunct;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.revenue.model.Adjunct;

@Audited
@Entity
@DiscriminatorValue("PRO")
public class PropertyReferenceOptional extends Adjunct {
	@ManyToOne
	private Property property;

	@Column(length = 150)
	private String owner;

	@Column(length = 100)
	private String location;

	private Boolean emitWithoutProperty;

	// rfam ML-JRM-2020-571-M
	@ManyToOne
	private ItemCatalog category;

	private Boolean emitWithoutCategory;

	public Boolean getEmitWithoutProperty() {
		return emitWithoutProperty;
	}

	public void setEmitWithoutProperty(Boolean emitWithoutProperty) {
		this.emitWithoutProperty = emitWithoutProperty;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public Property getProperty() {
		return property;
	}

	public PropertyReferenceOptional() {
		this.emitWithoutProperty = Boolean.FALSE;
		this.emitWithoutCategory = Boolean.FALSE;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (property == null) {
			sb.append("");
		} else {
			if (property.getLocation() == null || this.emitWithoutProperty)
				return "";
			// return ;
			sb.append("Dir: "
					+ property.getLocation().getMainBlockLimit().getStreet()
							.getPlace() + " - " + location.trim());
		}

		if (category != null) {
			sb.append(" - ");
			sb.append(category.getName());
		}
		return sb.toString();
	}

	public List<ValuePair> getDetails() {
		List<ValuePair> details = new LinkedList<ValuePair>();
		ValuePair pair = new ValuePair("Propietario", owner);
		details.add(pair);
		if ((location != null) && (location.trim().length() > 0)) {
			pair = new ValuePair("Ubicación", location.trim());
			details.add(pair);
		}

		if (category != null) {
			pair = new ValuePair("Categoría", category.toString());
			details.add(pair);
		}
		return details;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public ItemCatalog getCategory() {
		return category;
	}

	public void setCategory(ItemCatalog category) {
		this.category = category;
	}

	@Override
	public String getCode() {
		if (this.emitWithoutProperty) {
			return "";
		} else {
			String pcc = property.getPreviousCadastralCode() != null ? property
					.getPreviousCadastralCode() : "";
			String cc = property.getCadastralCode() != null ? property
					.getCadastralCode() : "";
			return pcc + " - " + cc;
		}
	}

	public Boolean getEmitWithoutCategory() {
		return emitWithoutCategory;
	}

	public void setEmitWithoutCategory(Boolean emitWithoutCategory) {
		this.emitWithoutCategory = emitWithoutCategory;
	}

}
