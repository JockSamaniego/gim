package ec.gob.gim.security.model;

import java.util.Comparator;

public class PermissionComparator implements Comparator<Permission>{

	@Override
	public int compare(Permission o1, Permission o2) {	
		return (o1.getAction().getPriority()<o2.getAction().getPriority() ? -1 : (o1.getAction().getPriority()==o2.getAction().getPriority() ? 0 : 1));		
	}

}
