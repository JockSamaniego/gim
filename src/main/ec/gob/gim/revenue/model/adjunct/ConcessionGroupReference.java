package ec.gob.gim.revenue.model.adjunct;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import ec.gob.gim.consession.model.ConcessionItem;
import ec.gob.gim.consession.model.ConcessionPlace;
import ec.gob.gim.revenue.model.Adjunct;

@Audited
@Entity
@DiscriminatorValue("CGR")
public class ConcessionGroupReference extends Adjunct {

	private String concessionPlaceNumber;
	private Integer concessionPlaceOrder;
	private String concessionPlaceType;
	private String concessionGroupName;

	/**
	 * Giro de venta
	 */
	private String productType;
	/*
	 * private String explanation; private String reference;
	 */

	@ManyToOne(fetch=FetchType.LAZY)
	private ConcessionItem concessionItem;

	@ManyToOne(fetch=FetchType.LAZY)
	private ConcessionPlace concessionPlace;

	/*
	 * @ManyToOne private ConcessionGroup concessionGroup;
	 */

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(concessionPlaceNumber != null ? concessionPlaceNumber : "ND");
		sb.append(" - ");
		/*
		 * sb.append(concessionPlaceOrder != null ? concessionPlaceOrder :
		 * "ND"); sb.append(" - ");
		 */
		sb.append(concessionPlaceType != null ? concessionPlaceType : "ND");
		/*
		 * sb.append(" - "); sb.append(concessionGroupName != null ?
		 * concessionGroupName : "ND");
		 */
		// sb.append(" - ");
		// sb.append(area != null ? area : "ND");
		return sb.toString();
	}

	public List<ValuePair> getDetails() {
		List<ValuePair> details = new LinkedList<ValuePair>();
		ValuePair pair = new ValuePair("Grupo", concessionGroupName);
		details.add(pair);
		pair = new ValuePair("Ubicación #", concessionPlaceNumber);
		details.add(pair);
		pair = new ValuePair("Tipo", productType);
		details.add(pair);
		return details;
	}

	public String getConcessionPlaceNumber() {
		return concessionPlaceNumber;
	}

	public void setConcessionPlaceNumber(String concessionPlaceNumber) {
		this.concessionPlaceNumber = concessionPlaceNumber;
	}

	public Integer getConcessionPlaceOrder() {
		return concessionPlaceOrder;
	}

	public void setConcessionPlaceOrder(Integer concessionPlaceOrder) {
		this.concessionPlaceOrder = concessionPlaceOrder;
	}

	public String getConcessionPlaceType() {
		return concessionPlaceType;
	}

	public void setConcessionPlaceType(String concessionPlaceType) {
		this.concessionPlaceType = concessionPlaceType;
	}

	public String getConcessionGroupName() {
		return concessionGroupName;
	}

	public void setConcessionGroupName(String concessionGroupName) {
		this.concessionGroupName = concessionGroupName;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public ConcessionItem getConcessionItem() {
		return concessionItem;
	}

	public void setConcessionItem(ConcessionItem concessionItem) {
		this.concessionItem = concessionItem;
	}

	public ConcessionPlace getConcessionPlace() {
		return concessionPlace;
	}

	public void setConcessionPlace(ConcessionPlace concessionPlace) {
		this.concessionPlace = concessionPlace;
	}

}
