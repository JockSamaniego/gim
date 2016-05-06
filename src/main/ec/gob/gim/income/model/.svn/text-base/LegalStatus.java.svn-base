package ec.gob.gim.income.model;

import java.util.ArrayList;
import java.util.List;

public enum LegalStatus {
	ACCEPTED,
	COMPLAINT,
	LAWSUIT;
	
	public static List<LegalStatus> getRestrictedLegalStatuses(){
		List<LegalStatus> restricted = new ArrayList<LegalStatus>();
		restricted.add(LegalStatus.COMPLAINT);
		restricted.add(LegalStatus.LAWSUIT);
		return restricted;
	}
}
