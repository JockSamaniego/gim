package ec.gob.gim.revenue.model.adjunct;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import ec.gob.gim.revenue.model.Adjunct;
import ec.gob.gim.firestation.model.EmisionFireRate;
import ec.gob.gim.firestation.model.LocalFireRate;

import javax.persistence.CascadeType;

import org.hibernate.annotations.Cascade;

@Audited
@Entity
@DiscriminatorValue("FRR")
public class FireRateReference extends Adjunct {
	
	

	private String address;

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.ALL })
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private EmisionFireRate emisionFireRate;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(address != null ? address : "ND");
		return sb.toString();
	}

	@Override
	public List<ValuePair> getDetails() {
		DecimalFormat myFormatter = new DecimalFormat("$###,###.###");
		
		List<ValuePair> details = new LinkedList<ValuePair>();
		ValuePair pair;
		for (LocalFireRate lfr : emisionFireRate.getRates()) {
			pair = new ValuePair(lfr.getActivity(), " \t "+myFormatter.format(lfr
					.getTotal()));
			details.add(pair);
		}
		return details;
	}

	public EmisionFireRate getEmisionFireRate() {
		return emisionFireRate;
	}

	public void setEmisionFireRate(EmisionFireRate emisionFireRate) {
		this.emisionFireRate = emisionFireRate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
