package org.gob.gim.concession.action;

import org.gob.gim.market.action.ProductTypeHome;
import org.gob.gim.revenue.action.MunicipalBondHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.consession.model.ConcessionItem;
import ec.gob.gim.consession.model.ConcessionPlace;
import ec.gob.gim.market.model.ProductType;
import ec.gob.gim.revenue.model.MunicipalBond;

@Name("concessionItemHome")
public class ConcessionItemHome extends EntityHome<ConcessionItem> {

	@In(create = true)
	MunicipalBondHome municipalBondHome;
	@In(create = true)
	ConcessionPlaceHome concessionPlaceHome;
	@In(create = true)
	ProductTypeHome productTypeHome;

	public void setConcessionItemId(Long id) {
		setId(id);
	}

	public Long getConcessionItemId() {
		return (Long) getId();
	}

	@Override
	protected ConcessionItem createInstance() {
		ConcessionItem concessionItem = new ConcessionItem();
		return concessionItem;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		MunicipalBond municipalBond = municipalBondHome.getDefinedInstance();
		if (municipalBond != null) {
			getInstance().setMunicipalBond(municipalBond);
		}
		ConcessionPlace place = concessionPlaceHome.getDefinedInstance();
		if (place != null) {
			getInstance().setPlace(place);
		}
		ProductType productType = productTypeHome.getDefinedInstance();
		if (productType != null) {
			getInstance().setProductType(productType);
		}
	}

	public boolean isWired() {
		return true;
	}

	public ConcessionItem getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
