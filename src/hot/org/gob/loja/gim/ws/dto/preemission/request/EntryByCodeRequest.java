package org.gob.loja.gim.ws.dto.preemission.request;

public class EntryByCodeRequest {
	private String entryCode;

	public String getEntryCode() {
		return entryCode;
	}

	public void setEntryCode(String entryCode) {
		this.entryCode = entryCode;
	}

	@Override
	public String toString() {
		return "EntryByCodeRequest [entryCode=" + entryCode + "]";
	}
	
}
