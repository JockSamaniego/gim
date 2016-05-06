package ec.gob.gim.income.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AccountComparator implements Comparator<Account>{

	@Override
	public int compare(Account o1, Account o2) {
		List<Account> accounts = new ArrayList<Account>();
		List<String> accountCodes = new ArrayList<String>();
		accountCodes.add(o1.getAccountCode());
		accountCodes.add(o2.getAccountCode());
		Collections.sort(accountCodes);
		for(String s: accountCodes){
			if(o1.getAccountCode().equals(s)&& !accounts.contains(o1)){
				accounts.add(o1);
			}
			if(o2.getAccountCode().equals(s)&& !accounts.contains(o2)){
				accounts.add(o2);
			}
			
		}
		if(accounts.indexOf(o1) < accounts.indexOf(o2)){
			return -1;
		}
		
		if(accounts.indexOf(o1) > accounts.indexOf(o2)){
			return 1;
		}
		return 0;
	}

}
