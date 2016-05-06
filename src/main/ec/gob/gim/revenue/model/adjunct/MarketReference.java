package ec.gob.gim.revenue.model.adjunct;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import ec.gob.gim.market.model.Market;
import ec.gob.gim.market.model.Stand;
import ec.gob.gim.revenue.model.Adjunct;

@Entity
@DiscriminatorValue("MR")
public class MarketReference extends Adjunct {

	private BigDecimal area;
	private String standName;
	private Integer standNumber;
	private String standType;
	private String marketName;

//	private String serviceDetail;

	/**
	 * Giro de venta
	 */
	private String productType;
	/*private String explanation;
	private String reference;*/

	@ManyToOne
	private Market market;

	@ManyToOne
	private Stand stand;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(marketName != null ? marketName : "ND");
		sb.append(" - ");
		sb.append(standType != null ? standType : "ND");
		sb.append(" - ");
		sb.append(standName != null ? standName : "ND");
		return sb.toString();
	}

	public List<ValuePair> getDetails() {
		List<ValuePair> details = new LinkedList<ValuePair>();
		
		ValuePair pair;
//		pair = new ValuePair("Corresponde a", serviceDetail);
//		details.add(pair);
		pair = new ValuePair("Mercado", marketName);
		details.add(pair);
		pair = new ValuePair("Local #", standName);
		details.add(pair);
		pair = new ValuePair("Tipo Venta", productType);
		details.add(pair);
		return details;
	}

	public BigDecimal getArea() {
		return area;
	}

	public void setArea(BigDecimal area) {
		this.area = area;
	}

	public String getStandName() {
		return standName;
	}

	public void setStandName(String standName) {
		this.standName = standName;
	}

	public Integer getStandNumber() {
		return standNumber;
	}

	public void setStandNumber(Integer standNumber) {
		this.standNumber = standNumber;
	}

	public String getStandType() {
		return standType;
	}

	public void setStandType(String standType) {
		this.standType = standType;
	}

	public String getMarketName() {
		return marketName;
	}

	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

	public Market getMarket() {
		return market;
	}

	public void setMarket(Market market) {
		this.market = market;
	}

	public Stand getStand() {
		return stand;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public void setStand(Stand stand) {
		this.stand = stand;
	}

//	public String getServiceDetail() {
//		return serviceDetail;
//	}
//
//	public void setServiceDetail(String serviceDetail) {
//		this.serviceDetail = serviceDetail;
//	}
//
//
}
