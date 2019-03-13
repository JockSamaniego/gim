package org.gob.gim.common.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession; 
import org.drools.runtime.rule.FactHandle;


/**
 * @author wilman
 *
 */
@Deprecated
@Stateless(name="KnowledgeService")
public class KnowledgeServiceBean implements KnowledgeService{
	
	
	private KnowledgeBase knowledgeBase;
	private StatefulKnowledgeSession session;
	
	
	public KnowledgeServiceBean() {
		knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
	}
 
	@Override
	public KnowledgeBuilder addResourceDrools(byte[] bytes) {
		InputStream is = new ByteArrayInputStream(bytes);
		KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		knowledgeBuilder.add(ResourceFactory.newInputStreamResource(is), ResourceType.DRL);
		//System.out.println("---- Added resource bytes drools");
		return knowledgeBuilder;
	}

	@Override
	public KnowledgeBuilder addResourceDrools(String pathRulesFile) {
		KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		knowledgeBuilder.add(ResourceFactory.newFileResource(pathRulesFile), ResourceType.DRL);
		//System.out.println("---- Added resource file drools");
		return knowledgeBuilder;
	}

	@Override
	public KnowledgeBuilder addResourceDrools(List<byte[]> byteList) {
		KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		for (byte[] bytes : byteList){
			InputStream is = new ByteArrayInputStream(bytes);
			knowledgeBuilder.add(ResourceFactory.newInputStreamResource(is), ResourceType.DRL);
		}
		//System.out.println("---- Added resource byte List drools");
		return knowledgeBuilder;
	}

	
	@Override
	public KnowledgeBuilderErrors compileDroolsFile(KnowledgeBuilder knowledgeBuilder) {
		if (knowledgeBuilder.hasErrors()){
			return knowledgeBuilder.getErrors();
		}
		return null;
	}

	/**
	 * Add to Knowledge Base packages from Collection of the knowledgePackages
	 * @param knowledgePackages
	 */
	@Override
	public void addKnowledgePackages(Collection<KnowledgePackage> knowledgePackages){
        knowledgeBase.addKnowledgePackages(knowledgePackages);
        //System.out.println("=== Resources add in knowledgeBase: " + knowledgeBase.getKnowledgePackages().size());
        if (session == null)
        	session = knowledgeBase.newStatefulKnowledgeSession();        
	}

	/**
	 * Add to Knowledge Base packages from the builder which are actually the rules from the drl file (All)
	 * @param knowledgeBuilder
	 */
	@Override
	public void addKnowledgePackages(KnowledgeBuilder knowledgeBuilder){
		//Add to Knowledge Base packages from the builder which are actually the rules from the drl file.
		addKnowledgePackages(knowledgeBuilder.getKnowledgePackages());	
	}

	@Override
	public void loadRule(byte[] fileBytes) {
		KnowledgeBuilder knowledgeBuilder = this.addResourceDrools(fileBytes);
		addKnowledgePackages(knowledgeBuilder.getKnowledgePackages());
	}

	@Override
	public void loadRule(List<byte[]> fileByteList) {
		KnowledgeBuilder knowledgeBuilder = this.addResourceDrools(fileByteList);
		addKnowledgePackages(knowledgeBuilder.getKnowledgePackages());
	}

	/**
	 * Add if not exist else update a fact
	 * @param object
	 */
	@Override
	public FactHandle addFact(Object object) {
		if (session == null)
			session = knowledgeBase.newStatefulKnowledgeSession();
		
		FactHandle factHandle = session.getFactHandle(object); 
		
		if (factHandle == null){
			//System.out.println("===== Insertando new Fact");
			factHandle = session.insert(object);
		}else{
			//System.out.println("===== Updating new Fact");
			session.update(factHandle, object);
		}
		
		return factHandle;
	}
	
	@Override
	public List<FactHandle> addFacts(Object... objects) {
		List<FactHandle> list = new ArrayList<FactHandle>();
		for (Object o : objects){
			list.add(addFact(o));
		}	
		return list;
	}
	
	@Override
	public Boolean existFacts(){
		if (session == null)
			session = knowledgeBase.newStatefulKnowledgeSession();
		Collection<FactHandle> factHandles = session.getFactHandles();
		//System.out.println("=== Tamanio factHandles: " + factHandles.size() + ", " + factHandles.isEmpty() + ", " + !factHandles.isEmpty());
		return factHandles.size() > 0 ? Boolean.TRUE : Boolean.FALSE; 
	}

	/**
	 * Remove all facts from Knowledge
	 */
	@Override
	public void removeFacts() {
		Collection<FactHandle> factHandles = session.getFactHandles(); 
		for (FactHandle fh : factHandles)
			session.retract(fh);
	}
	
	/**
	 * Invoke all rules from Knowledge with facts is presents
	 */
	@Override
	public void invokeRules() {
		if (session == null)
			session = knowledgeBase.newStatefulKnowledgeSession();
		int total = session.fireAllRules();
		//System.out.println("Fire Rules...........: " + total);	
	}

	/**
	 * Invoke all rules from Knowledge previous add facts
	 * @param objects 
	 */
	@Override
	public void invokeRules(Object... object) {
		this.addFact(object);
		this.invokeRules();
	}
	
	/**
	 * Invoke all rules from Knowledge previous add facts list
	 * @param objects 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void invokeRules(List objects) {
		this.addFact(objects);
		this.invokeRules();
	}
	
	@Override
	public void invokeRulesWithRuntimeLogger(){
		if (session == null)
			session = knowledgeBase.newStatefulKnowledgeSession();
		KnowledgeRuntimeLoggerFactory.newConsoleLogger(session);
		//Only now we will fire the rules which are already in the agenda
		int total = session.fireAllRules();
		//System.out.println("Fire Rules...........: " + total);
	}

}
