package org.gob.gim.market.action;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.market.model.ProductType;

@Name("productTypeHome")
public class ProductTypeHome extends EntityHome<ProductType> {

	public void setProductTypeId(Long id) {
		setId(id);
	}

	public Long getProductTypeId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public ProductType getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
