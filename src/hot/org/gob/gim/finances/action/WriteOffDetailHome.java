package org.gob.gim.finances.action;

import org.gob.gim.revenue.action.MunicipalBondHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.finances.model.WriteOffDetail;
import ec.gob.gim.finances.model.WriteOffRequest;
import ec.gob.gim.revenue.model.MunicipalBond;

@Name("writeOffDetailHome")
public class WriteOffDetailHome extends EntityHome<WriteOffDetail> {

	@In(create = true)
	MunicipalBondHome municipalBondHome;
	@In(create = true)
	WriteOffRequestHome writeOffRequestHome;

	public void setWriteOffDetailId(Long id) {
		setId(id);
	}

	public Long getWriteOffDetailId() {
		return (Long) getId();
	}

	@Override
	protected WriteOffDetail createInstance() {
		WriteOffDetail writeOffDetail = new WriteOffDetail();
		return writeOffDetail;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		MunicipalBond newMunicipalBond = municipalBondHome.getDefinedInstance();
		if (newMunicipalBond != null) {
			getInstance().setNewMunicipalBond(newMunicipalBond);
		}
		MunicipalBond oldMunicipalBond = municipalBondHome.getDefinedInstance();
		if (oldMunicipalBond != null) {
			getInstance().setOldMunicipalBond(oldMunicipalBond);
		}
		WriteOffRequest writeOffRequest = writeOffRequestHome
				.getDefinedInstance();
		if (writeOffRequest != null) {
			getInstance().setWriteOffRequest(writeOffRequest);
		}
	}

	public boolean isWired() {
		return true;
	}

	public WriteOffDetail getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
