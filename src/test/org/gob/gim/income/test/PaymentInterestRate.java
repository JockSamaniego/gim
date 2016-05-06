/**
 * 
 */
package org.gob.gim.income.test;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderErrors;
import org.gob.gim.rule.KnowledgeBaseUtil;
import org.junit.Before;
import org.junit.Test;

import ec.gob.gim.income.model.InterestRate;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.Item;
import ec.gob.gim.revenue.model.MunicipalBond;

/**
 * @author wilman
 *
 */
public class PaymentInterestRate {

	KnowledgeBaseUtil kbu;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		kbu = KnowledgeBaseUtil.createInstance();
	}
	
	/*private void compileFile(String filePath) throws Exception{
		KnowledgeBuilder kbuilder = kbu.addResourceDrools(filePath);
		KnowledgeBuilderErrors kbuilderErrors = kbu.compileDroolsFile(kbuilder);
		if (kbuilderErrors != null){
			System.err.println(kbuilderErrors.toString());
		}
		kbu.addKnowledgePackages(kbuilder.getKnowledgePackages());
	}*/
	
	private List<InterestRate> buildInterestRateList(){
		List<InterestRate> interestRateList = new ArrayList<InterestRate>();
		
		InterestRate ir1 = new InterestRate();
		GregorianCalendar gci1 = new GregorianCalendar(2011,0,1);
		GregorianCalendar gcf1 = new GregorianCalendar(2011,2,31);
		ir1.setBeginDate(gci1.getTime());
		ir1.setEndDate(gcf1.getTime());
		ir1.setValue(new BigDecimal(8.0));
		ir1.setFactor(100);
		
		InterestRate ir2 = new InterestRate();
		GregorianCalendar gci2 = new GregorianCalendar(2011,3,1);
		GregorianCalendar gcf2 = new GregorianCalendar(2011,5,30);
		ir2.setBeginDate(gci2.getTime());
		ir2.setEndDate(gcf2.getTime());
		ir2.setValue(new BigDecimal(10.0));
		ir2.setFactor(100);
		
		InterestRate ir3 = new InterestRate();
		GregorianCalendar gci3 = new GregorianCalendar(2011,6,1);
		GregorianCalendar gcf3 = new GregorianCalendar(2011,8,31);
		ir3.setBeginDate(gci3.getTime());
		ir3.setEndDate(gcf3.getTime());
		ir3.setValue(new BigDecimal(12.0));
		ir3.setFactor(100);
		
		InterestRate ir4 = new InterestRate();
		GregorianCalendar gci4 = new GregorianCalendar(2011,9,1);
		GregorianCalendar gcf4 = new GregorianCalendar(2011,11,31);
		ir4.setBeginDate(gci4.getTime());
		ir4.setEndDate(gcf4.getTime());
		ir4.setValue(new BigDecimal(14.0));
		ir4.setFactor(100);
		
		InterestRate ir5 = new InterestRate();
		GregorianCalendar gci5 = new GregorianCalendar(2012,0,1);
		GregorianCalendar gcf5 = new GregorianCalendar(2012,2,31);
		ir5.setBeginDate(gci5.getTime());
		ir5.setEndDate(gcf5.getTime());
		ir5.setValue(new BigDecimal(15.0));
		ir5.setFactor(100);
		
		//interestRateList.add(ir1);
		//interestRateList.add(ir2);
		interestRateList.add(ir3);
		interestRateList.add(ir4);
		//interestRateList.add(ir5);
		
		return interestRateList;
	}
	
	@Test
	public void interestRateCalculate() throws Exception {
		//compileFile("/home/wilman/sandbox/gim/src/rules/ec/gob/gim/pf2011/penalty/Interest.drl");
		
		MunicipalBond bond = new MunicipalBond();
		GregorianCalendar gcExpirationDate = new GregorianCalendar(2011,7,10);
		bond.setExpirationDate(gcExpirationDate.getTime());
		bond.setValue(new BigDecimal(100.00));
		
		
		Entry entryInterest =  new Entry();
		entryInterest.setCode("00006");
		
		Item itemInterest = new Item();
		itemInterest.setEntry(entryInterest);
		
		//bond.addInterestItem(itemInterest);
		
		
		List<InterestRate> rates = this.buildInterestRateList();
		
		System.out.println("-------rates son:  " + rates.size());
		
		kbu.addFact(itemInterest);
		
		/*for (InterestRate rate : rates){
			kbu.addFact(rate);
		}*/
		kbu.addFact(rates);
		kbu.addFact(bond);
				
		kbu.invokeRules();
		
		
		
		/*for (Item i: bond.getInterestItems()){
			System.out.println(" ---Item " +  i.getAmount() + ", " + i.getValue() + ", " + i.getTotal());
		}*/
		
		
		//bond.calculateInterest();
		System.out.println("-------total Interest:  " + bond.getInterest());
		
	}

}
