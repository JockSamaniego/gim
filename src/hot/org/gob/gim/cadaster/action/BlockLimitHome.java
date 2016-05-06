package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("blockLimitHome")
public class BlockLimitHome extends EntityHome<BlockLimit> {

	@In(create = true)
	SidewalkMaterialHome sidewalkMaterialHome;
	@In(create = true)
	StreetMaterialHome streetMaterialHome;
	@In(create = true)
	StreetTypeHome streetTypeHome;

	public void setStreetId(Long id) {
		setId(id);
	}

	public Long getStreetId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		SidewalkMaterial sidewalkMaterial = sidewalkMaterialHome.getDefinedInstance();
		if (sidewalkMaterial != null) {
			getInstance().setSidewalkMaterial(sidewalkMaterial);
		}
		StreetMaterial streetMaterial = streetMaterialHome.getDefinedInstance();
		if (streetMaterial != null) {
			getInstance().setStreetMaterial(streetMaterial);
		}
		StreetType streetType = streetTypeHome.getDefinedInstance();
		if (streetType != null) {
			getInstance().setStreetType(streetType);
		}
	}

	public boolean isWired() {
		return true;
	}

	public BlockLimit getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
