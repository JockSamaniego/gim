package org.gob.gim.revenue.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import org.gob.gim.revenue.exception.EntryDefinitionNotFoundException;

import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.EntryDefinition;
import ec.gob.gim.revenue.model.EntryStructure;
import ec.gob.gim.revenue.model.EntryStructureType;

@Local
public interface EntryService {
	
	public String LOCAL_NAME = "/gim/EntryService/local";

	Entry find(String code);
	Entry find(Long id);
	List<Entry> findByCriteria(String criteria);
	/**
	 * Get Entry child from entry parent
	 * @param entryParentId
	 * @return List
	 */
	List<Entry> findByEntryParent(Long entryParentId);
	/**
	 * Get Entry parents from entry children
	 * @param entryChildId
	 * @return List
	 */
	List<Entry> findByEntryChild(Long entryChildId);
	
	List<EntryStructure> findEntryStructureChildren(Long entryParentId);
	List<EntryStructure> findEntryStructureParent(Long entryChildId);
	
	List<EntryDefinition> findEntryDefinitionCurrentsToEntries(List<Long> entryIds, 
			Date serviceCalculationDate) throws EntryDefinitionNotFoundException;
	List<Long> findMaxIdEntryDefinitionCurrentsToEntries(List<Long> entryIds, 
			Date serviceCalculationDate);
	List<EntryDefinition> findEntryDefinitionsByIds(List<Long> entryDefinitionIds);
	EntryDefinition findEntryDefinitionsById(Long entryDefinitionId);
	EntryDefinition findEntryDefinitionCurrentToEntry(Long entryId,
			Date serviceCalculationDate) throws EntryDefinitionNotFoundException;
	List<EntryStructure> findEntryStructureChildrenByType(Long entryParentId,
			EntryStructureType entryStructureType);
	Entry findByAccountCode(String accountCode);
	
	
}
