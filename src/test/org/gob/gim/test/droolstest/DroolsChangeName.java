package org.gob.gim.test.droolstest;

import java.util.GregorianCalendar;

import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderErrors;
import org.gob.gim.rule.KnowledgeBaseUtil;
import org.junit.Test;

import ec.gob.gim.common.model.Gender;
import ec.gob.gim.common.model.Person;

import junit.framework.TestCase;


public class DroolsChangeName extends TestCase{
	
	//	@Test
	//	public void testDrools(){
//		KnowledgeBaseUtil kBaseUtil = KnowledgeBaseUtil.createInstance();
		//		KnowledgeBuilder kBuilder = kBaseUtil.addResourceDrools("D:\\projects_seam_helios\\gim\\droolsexample\\personname.drl");
		//		KnowledgeBuilderErrors kBuilderErrors = kBaseUtil.compileDroolsFile(kBuilder);
		//		if (kBuilderErrors != null){
		//			System.out.println("Errores: "+ kBuilderErrors.toString());
		//			return;
		//		}
		//		kBaseUtil.addKnowledgePackages(kBuilder);
		//		Person p = new Person();
		//		p.setIdentificationNumber("1103367353");
		//		p.setGender(Gender.MALE);
		//		Person p1 = new Person();
		//		p1.setIdentificationNumber("1103367353");
		//		p1.setGender(Gender.FEMALE);
		//		GregorianCalendar gcal = new GregorianCalendar(1930,01,01);
		//		p1.setBirthday(gcal.getTime());
		////		kBaseUtil.addFact(p);
		//		kBaseUtil.addFact(p1);
		//		kBaseUtil.invokeRules();
		//		System.out.println("p: "+p.getFirstName());
		//		System.out.println("p: "+p.getLastName());
		//		System.out.println("p1: "+p1.getFirstName());
		//		System.out.println("p1: "+p1.getLastName());
		//		p.setGender(Gender.FEMALE);
		////		kBaseUtil.addFact(p);
		//		kBaseUtil.addFact(p1);		
		//		kBaseUtil.invokeRules();
		//		System.out.println("======Segunda Pasada=========");
		//		System.out.println("p: "+p.getFirstName());
		//		System.out.println("p: "+p.getLastName());
		//		System.out.println("p1: "+p1.getFirstName());
		//		System.out.println("p1: "+p1.getLastName());
		//		
		//	}
}
