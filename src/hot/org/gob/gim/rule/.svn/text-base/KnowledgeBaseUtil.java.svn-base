package org.gob.gim.rule;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;

@Deprecated
public class KnowledgeBaseUtil {
	
	private static KnowledgeBaseUtil instance = null;
	
	private KnowledgeBase knowledgeBase;
	
	private StatefulKnowledgeSession session;
		
	public static synchronized KnowledgeBaseUtil createInstance(){
		if (instance == null){
			instance = new KnowledgeBaseUtil();
		}
		return instance;
	}
	
	private KnowledgeBaseUtil(){
		knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
	}
	
	/**
	 * @return the knowledgeBase
	 */
	public KnowledgeBase getKnowledgeBase() {
		return knowledgeBase;
	}

	public KnowledgeBuilder addResourceDrools(byte[] bytes, KnowledgeBuilder knowledgeBuilder){
		InputStream is = new ByteArrayInputStream(bytes);
		knowledgeBuilder.add(ResourceFactory.newInputStreamResource(is), ResourceType.DRL);
		//getKnowledgeBuilder().add(ResourceFactory.newInputStreamResource(is), ResourceType.DRL);
		System.out.println("---- Added resource file drools");
		//return getKnowledgeBuilder();
		return knowledgeBuilder;
	}
	
	public void  addResourceDrools(String pathRulesFile, KnowledgeBuilder knowledgeBuilder){
		
		knowledgeBuilder.add(ResourceFactory.newFileResource(pathRulesFile), ResourceType.DRL);
		System.out.println("---- Added resource file drools");
		//return getKnowledgeBuilder();
		//return knowledgeBuilder;
	}
	
	public KnowledgeBuilder addResourceDrools(List<byte[]> byteList){
		KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		for (byte[] bytes : byteList){
			InputStream is = new ByteArrayInputStream(bytes);
			knowledgeBuilder.add(ResourceFactory.newInputStreamResource(is), ResourceType.DRL);
			//addResourceDrools(bytes);
		}
		System.out.println("---- Added resource List files drools");
		//return getKnowledgeBuilder();
		return knowledgeBuilder;
	}
	
	public KnowledgeBuilderErrors compileDroolsFile(KnowledgeBuilder knowledgeBuilder){
		if (knowledgeBuilder.hasErrors()){
			return knowledgeBuilder.getErrors();
		}
		return null;	
	}
	
	/**
	 * Add to Knowledge Base packages from Collection of the knowledgePackages
	 * @param knowledgePackages
	 */
	public void addKnowledgePackages(Collection<KnowledgePackage> knowledgePackages){
        knowledgeBase.addKnowledgePackages(knowledgePackages);
        System.out.println("=== Resources add in knowledgeBase: " + knowledgeBase.getKnowledgePackages().size());
        if (session == null)
        	session = knowledgeBase.newStatefulKnowledgeSession();        
	}
	
	/**
	 * Add to Knowledge Base packages from the builder which are actually the rules from the drl file (All)
	 */
	public void addKnowledgePackages(KnowledgeBuilder knowledgeBuilder){
		//Add to Knowledge Base packages from the builder which are actually the rules from the drl file.
		addKnowledgePackages(knowledgeBuilder.getKnowledgePackages());	
	}

	/*public static KnowledgeBase createKnowledgeBase(byte[] bytes) {
		KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		InputStream is = new ByteArrayInputStream(bytes);
		builder.add(ResourceFactory.newInputStreamResource(is), ResourceType.DRL);
		
		if (builder.hasErrors()){
			throw new RuntimeException(builder.getErrors().toString());  
        }  
  
        KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();  
  
        //Add to Knowledge Base packages from the builder which are actually the rules from the drl file.  
        knowledgeBase.addKnowledgePackages(builder.getKnowledgePackages());  
  
        return knowledgeBase;  
	}*/
	
	/*public static void fireRule(KnowledgeBase knowledgeBase, Object... objects){
		StatefulKnowledgeSession session = knowledgeBase.newStatefulKnowledgeSession();
		
		for (Object o : objects){
			session.insert(o);
		}
		
		//Only now we will fire the rules which are already in the agenda
		System.out.println("Fire Rules............");
        session.fireAllRules();
	}*/
		
	
	public void readRule(byte[] fileBytes, KnowledgeBuilder knowledgeBuilder){
		this.addResourceDrools(fileBytes, knowledgeBuilder);
		addKnowledgePackages(knowledgeBuilder.getKnowledgePackages());
	}
	
	public void readRule(List<byte[]> fileByteList){
		KnowledgeBuilder knowledgeBuilder = this.addResourceDrools(fileByteList);
		addKnowledgePackages(knowledgeBuilder.getKnowledgePackages());
	}
	
	public FactHandle addFact(Object object){
		
		if (session == null)
			session = knowledgeBase.newStatefulKnowledgeSession();
		
		FactHandle factHandle = session.getFactHandle(object); 
		
		if (factHandle == null){
			System.out.println("===== Insertando new Fact");
			factHandle = session.insert(object); 
		}else{
			System.out.println("===== Updating new Fact");
			session.update(factHandle, object);
		}
		
		return factHandle;	
	}
	
	public List<FactHandle> addFact(Object... objects){
		List<FactHandle> list = new ArrayList<FactHandle>();
		for (Object o : objects){
			list.add(addFact(o));
		}	
		return list;		
	}
	
	public void updateFact(Object object){
		FactHandle fh = session.getFactHandle(object);
		session.update(fh, object);	
	}
	
	public List<FactHandle> addFact(List<?> objects){
		List<FactHandle> list = new ArrayList<FactHandle>();
		for (Object o : objects){
			list.add(addFact(o));
		}	
		return list;		
	}
	
	/**
	 * Remove all facts from Knowledge
	 */
	public void removeFacts(){
		Collection<FactHandle> factHandles= session.getFactHandles();
		for (FactHandle fh : factHandles)
			session.retract(fh);
	}
	
	/*public Boolean hasFacts(){
		if (session == null){
			return Boolean.FALSE;
		}
		System.out.println("Ingresando a verificar si existen hechos" + session.getFactHandles().isEmpty());
		System.out.println("Ingresando a verificar si existen objetos " + session.getObjects().isEmpty());
		return !session.getObjects().isEmpty();
		
	}*/
	
	public void invokeRules(){
		if (session == null)
			session = knowledgeBase.newStatefulKnowledgeSession();
		//KnowledgeRuntimeLoggerFactory.newConsoleLogger(session);
		//Only now we will fire the rules which are already in the agenda
		int total = session.fireAllRules();
		System.out.println("Fire Rules...........: " + total);
	}
	
	public void invokeRules(Object... object){
		this.addFact(object);
		this.invokeRules();
	}
	
	@SuppressWarnings("rawtypes")
	public void invokeRules(List objects){
		this.addFact(objects);
		this.invokeRules();
	}
	
}
