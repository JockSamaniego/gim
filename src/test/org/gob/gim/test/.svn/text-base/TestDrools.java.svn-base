/**
 * 
 */
package org.gob.gim.test;

import java.math.BigDecimal;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderErrors;
//import org.drools.process.command.GetAgendaEventListenersCommand;
import org.gob.gim.rule.KnowledgeBaseUtil;
import org.junit.Test;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.Item;
import ec.gob.gim.revenue.model.MunicipalBond;

/**
 * @author wilman
 *
 */
public class TestDrools extends TestCase{
	
	/*@Test
	public void testTestsRulesEngineWaterSupply() throws Exception {
		
		KnowledgeBaseUtil kbu = KnowledgeBaseUtil.createInstance();
		KnowledgeBuilder kbuilder = kbu.addResourceDrools("/home/wilman/sandbox/gim/resources/rules/waterservice2011.drl");
		KnowledgeBuilderErrors kbuilderErrors = kbu.compileDroolsFile(kbuilder);
		if (kbuilderErrors != null){
			System.err.println(kbuilderErrors.toString());
		}
		
	}*/
	
	@Test
	public void testTestsRulesEngineMayorEdad() throws Exception {
		/*
		KnowledgeBaseUtil kbu = KnowledgeBaseUtil.createInstance();
		KnowledgeBuilder kbuilder = kbu.addResourceDrools("/home/wilman/sandbox/gim/resources/rules/iva.drl");
		KnowledgeBuilderErrors kbuilderErrors = kbu.compileDroolsFile(kbuilder);
		if (kbuilderErrors != null){
			System.err.println(kbuilderErrors.toString());
		}
		kbu.addKnowledgePackages(kbuilder.getKnowledgePackages());
		
		kbuilder = kbu.addResourceDrools("/home/wilman/sandbox/gim/resources/rules/mayorEdad.drl");
		kbuilderErrors = kbu.compileDroolsFile(kbuilder);
		if (kbuilderErrors != null){
			System.err.println(kbuilderErrors.toString());
		}
		kbu.addKnowledgePackages(kbuilder.getKnowledgePackages());
		
		GregorianCalendar gc = new GregorianCalendar(1940, 11, 03);
		
		Person person = new Person();
		person.setBirthday(gc.getTime());
		person.setIsHandicaped(Boolean.TRUE);
		
		MunicipalBond bond = new MunicipalBond();
		bond.setResident(person);
		
		Item itemRubro = new Item();
		itemRubro.setAmount(new BigDecimal(1));
		itemRubro.setValue(new BigDecimal(100.00));
		itemRubro.setTotal(itemRubro.getAmount().multiply(itemRubro.getValue()));
		
		bond.setTaxableTotal(itemRubro.getTotal());
		bond.add(itemRubro);
		
		Item itemIva12 = new Item();
		Entry entryIva =  new Entry();
		entryIva.setName("IVA");
		itemIva12.setEntry(entryIva);
		bond.add(itemIva12);
		
		Item itemDiscount = new Item();
		Entry entryDiscount =  new Entry();
		entryDiscount.setName("DESCUENTO");
		itemDiscount.setEntry(entryDiscount);
		bond.add(itemDiscount);
				
		
		
		kbu.addFact(itemIva12);
		kbu.addFact(itemDiscount);
		kbu.addFact(bond);
		
		
		
		kbu.invokeRules();
		
		
		
		for (Item i: bond.getItems()){
			System.out.println(" ---Item " +  i.getAmount() + ", " + i.getValue() + ", " + i.getTotal());
		}
		
		System.out.println("-------Items ahora son:  " + bond.getItems().size());
		bond.calculateValue();
		System.out.println("-------total:  " + bond.getValue());
		*/
			
		
	}
	
	/*@Test
	public void testTestsRulesEngineIva() throws Exception {
		
		KnowledgeBaseUtil kbu = KnowledgeBaseUtil.createInstance();
		KnowledgeBuilder kbuilder = kbu.addResourceDrools("/home/wilman/sandbox/gim/resources/rules/iva.drl");
		KnowledgeBuilderErrors kbuilderErrors = kbu.compileDroolsFile(kbuilder);
		if (kbuilderErrors != null){
			System.err.println(kbuilderErrors.toString());
		}
		kbu.addKnowledgePackages(kbuilder.getKnowledgePackages());
		
		MunicipalBond bond = new MunicipalBond(); 
		Item itemRubro = new Item();
		itemRubro.setAmount(new BigDecimal(1));
		itemRubro.setValue(new BigDecimal(100.00));
		itemRubro.calculateTotal();
		
		Item itemIva12 = new Item();
		
		bond.add(itemRubro);
		bond.add(itemIva12);
		
		//kbu.fireRule(itemRubro, itemIva12);
		//kbu.fireRule(bond, itemIva12);
		//kbu.fireRule(itemIva12, bond);
		kbu.addFact(itemIva12);
		kbu.addFact(bond);
		
		kbu.invokeRules();
		
		System.out.println("-------El iva es:  " + itemIva12.getTotal());
		System.out.println("-------total:  " + bond.getTotal());
		
		itemRubro.setAmount(new BigDecimal(2));
		itemRubro.calculateTotal();
		
		Item itemIva13 = new Item();
		itemIva13.setAmount(new BigDecimal(1));
		itemIva13.setValue(new BigDecimal(25.00));
		bond.add(itemIva13);
		
		kbu.fireRules(itemIva13, bond);
		
		//org.drools.runtime.rule.FactHandle fh = kbu.addFact(bond);
		//kbu.updateFact(itemIva12);
		//kbu.updateFact(bond);
		kbu.addFact(bond);
		
		//System.out.println("-------existen hechos" + kbu.hasFacts());
		
		kbu.invokeRules();
		
		System.out.println("-------El iva es:  " + itemIva12.getTotal());
		System.out.println("-------total:  " + bond.getTotal());
		
	}*/
	
	/*@Test
	public void testAgePerson() throws Exception {
		
		GregorianCalendar gc = new GregorianCalendar(1977, 12, 03);
		
		Person person = new Person();
		person.setBirthday(gc.getTime());
		
		System.out.println("-------Edad:  " + person.getAge());
		System.out.println("-------Edad Human:  " + person.getAgeToString());
		
	}*/

}
