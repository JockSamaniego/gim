package org.gob.gim.finances.action;	

import org.gob.gim.common.action.ResidentHome;
import org.gob.gim.waterservice.action.WaterSupplyHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.finances.model.SequenceManager;
import ec.gob.gim.finances.model.WriteOffRequest;
import ec.gob.gim.finances.model.WriteOffType;
import ec.gob.gim.waterservice.model.WaterSupply;

@Name("writeOffRequestHome")
public class WriteOffRequestHome extends EntityHome<WriteOffRequest> {

	@In(create = true)
	ResidentHome residentHome;
	@In(create = true)
	SequenceManagerHome sequenceManagerHome;
	@In(create = true)
	WaterSupplyHome waterSupplyHome;
	@In(create = true)
	WriteOffTypeHome writeOffTypeHome;

	public void setWriteOffRequestId(Long id) {
		setId(id);
	}

	public Long getWriteOffRequestId() {
		return (Long) getId();
	}

	@Override
	protected WriteOffRequest createInstance() {
		WriteOffRequest writeOffRequest = new WriteOffRequest();
		return writeOffRequest;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Resident approvedBy = residentHome.getDefinedInstance();
		if (approvedBy != null) {
			getInstance().setApprovedBy(approvedBy);
		}
		Resident issueTo = residentHome.getDefinedInstance();
		if (issueTo != null) {
			getInstance().setIssueTo(issueTo);
		}
		Resident madeBy = residentHome.getDefinedInstance();
		if (madeBy != null) {
			getInstance().setMadeBy(madeBy);
		}
		Resident resident = residentHome.getDefinedInstance();
		if (resident != null) {
			getInstance().setResident(resident);
		}
		SequenceManager sequenceManager = sequenceManagerHome
				.getDefinedInstance();
		if (sequenceManager != null) {
			getInstance().setSequenceManager(sequenceManager);
		}
		WaterSupply waterSupply = waterSupplyHome.getDefinedInstance();
		if (waterSupply != null) {
			getInstance().setWaterSupply(waterSupply);
		}
		WriteOffType writeOffType = writeOffTypeHome.getDefinedInstance();
		if (writeOffType != null) {
			getInstance().setWriteOffType(writeOffType);
		}
	}

	public boolean isWired() {
		return true;
	}

	public WriteOffRequest getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
