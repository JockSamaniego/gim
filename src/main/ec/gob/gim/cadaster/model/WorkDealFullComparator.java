package ec.gob.gim.cadaster.model;

import java.util.Comparator;

import ec.gob.gim.cadaster.model.dto.WorkDealFull;

public class WorkDealFullComparator  implements Comparator<WorkDealFull> {
	@Override
	public int compare(WorkDealFull o1, WorkDealFull o2) {
		String str3 = o1.getCadastralCode();
		String str4 = o2.getCadastralCode();
		return str3.compareTo(str4);

	}
}
