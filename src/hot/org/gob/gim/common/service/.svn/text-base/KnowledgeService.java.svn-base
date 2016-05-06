package org.gob.gim.common.service;

import java.util.Collection;
import java.util.List;

import javax.ejb.Local;

import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.definition.KnowledgePackage;
import org.drools.runtime.rule.FactHandle;

/**
*
* @author wilman
*/
@Local
public interface KnowledgeService {
	
	KnowledgeBuilder addResourceDrools(byte[] bytes);
	KnowledgeBuilder addResourceDrools(String pathRulesFile);
	KnowledgeBuilder addResourceDrools(List<byte[]> byteList);
	
	KnowledgeBuilderErrors compileDroolsFile(KnowledgeBuilder knowledgeBuilder);
	
	void addKnowledgePackages(Collection<KnowledgePackage> knowledgePackages);
	void addKnowledgePackages(KnowledgeBuilder knowledgeBuilder);
	
	void loadRule(byte[] fileBytes);
	void loadRule(List<byte[]> fileByteList);
	
	/**
	 * Add if not exist else update a fact
	 * @param object
	 */
	FactHandle addFact(Object object);
	List<FactHandle> addFacts(Object ... objects);
	Boolean existFacts();
	
	/**
	 * Remove all facts from Knowledge
	 */
	void removeFacts();
	
	void invokeRules();
	void invokeRules(Object... object);
	void invokeRules(List<Object> objects);
	void invokeRulesWithRuntimeLogger();
	
	
	
}
