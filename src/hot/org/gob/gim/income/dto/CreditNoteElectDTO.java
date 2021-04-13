package org.gob.gim.income.dto;


import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.income.model.CreditNote;

@NativeQueryResultEntity	
public class CreditNoteElectDTO {
	
	@NativeQueryResultColumn(index = 0)
	private Resident resident;
	
	@NativeQueryResultColumn(index = 1)
	private CreditNote creditNote;

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public CreditNote getCreditNote() {
		return creditNote;
	}

	public void setCreditNote(CreditNote creditNote) {
		this.creditNote = creditNote;
	}

}
