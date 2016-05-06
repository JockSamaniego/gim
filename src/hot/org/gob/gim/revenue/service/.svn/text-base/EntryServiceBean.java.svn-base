/**
 * 
 */
package org.gob.gim.revenue.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.gob.gim.common.service.CrudService;
import org.gob.gim.revenue.exception.EntryDefinitionNotFoundException;

import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.EntryDefinition;
import ec.gob.gim.revenue.model.EntryStructure;
import ec.gob.gim.revenue.model.EntryStructureType;

/**
 * @author wilman
 *
 */
@Stateless(name="EntryService")
public class EntryServiceBean implements EntryService {
	
	@EJB
	CrudService crudService;
	
	private String completeEntryCode(String code){
		StringBuffer codeBuffer = new StringBuffer(code.trim());
		while (codeBuffer.length() < 5){
			codeBuffer.insert(0,'0');
		}
		return codeBuffer.toString();
	}

	/** 
	 * @see org.gob.gim.revenue.service.EntryService#find(java.lang.String)
	 */
	@Override
	public Entry find(String code) {
		code = this.completeEntryCode(code);
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("code", code);
		@SuppressWarnings("unchecked")
		List<Entry> results = (List<Entry>)crudService.findWithNamedQuery("Entry.findByCode", parameters, 1);
		if (! results.isEmpty())
			return results.get(0); 
		return null;
	}

	/**
	 * @see org.gob.gim.revenue.service.EntryService#find(java.lang.Long)
	 */
	@Override
	public Entry find(Long id) {
		Map<String, Long> parameters = new HashMap<String, Long>();
		parameters.put("entryId", id);
		@SuppressWarnings("unchecked")
		List<Entry> results = (List<Entry>)crudService.findWithNamedQuery("Entry.findById", parameters, 1);
		if (! results.isEmpty())
			return results.get(0); 
		return null;
	}

	/** 
	 * @see org.gob.gim.revenue.service.EntryService#findByCriteria(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Entry> findByCriteria(String criteria) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("criteria", criteria);
		return crudService.findWithNamedQuery("Entry.findByCriteria", parameters);
	}

	/**
	 * Get Entry child from entry parent
	 * @param id entryParent
	 * @return List
	 */
	public List<Entry> findByEntryParent(Long entryParentId) {
		List<Entry> list = new ArrayList<Entry>();
		List<EntryStructure> entryStructureList =  findEntryStructureChildren(entryParentId);
		for (EntryStructure es : entryStructureList){
			list.add(es.getChild());
		}
		return list;
	}

	/**
	 * Get Entry parents from entry children
	 * @param id entryChild 
	 * @return List
	 */
	@Override
	public List<Entry> findByEntryChild(Long entryChildId) {
		List<Entry> list = new ArrayList<Entry>();
		List<EntryStructure> entryStructureList =  findEntryStructureParent(entryChildId);
		for (EntryStructure es : entryStructureList){
			list.add(es.getChild());
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EntryStructure> findEntryStructureChildren(Long entryParentId) {
		Map<String, Long> parameters = new HashMap<String, Long>();
		parameters.put("entryParentId", entryParentId);
		return crudService.findWithNamedQuery("EntryStructure.findEntryStructureChildren", parameters);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EntryStructure> findEntryStructureParent(Long entryChildId) {
		Map<String, Long> parameters = new HashMap<String, Long>();
		parameters.put("entryChildId", entryChildId);
		return crudService.findWithNamedQuery("EntryStructure.findEntryStructureParent", parameters);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EntryStructure> findEntryStructureChildrenByType(Long entryParentId, EntryStructureType entryStructureType) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("entryParentId", entryParentId);
		parameters.put("entryStructureType", entryStructureType);
		return crudService.findWithNamedQuery("EntryStructure.findEntryStructureChildrenByType", parameters);
	}
	
	
	@Override
	public List<EntryDefinition> findEntryDefinitionCurrentsToEntries(List<Long> entryIds, 
			Date serviceCalculationDate) throws EntryDefinitionNotFoundException{
		List<Long> entryDefinitionIds = this.findMaxIdEntryDefinitionCurrentsToEntries(entryIds, serviceCalculationDate);
		if (entryDefinitionIds != null && entryDefinitionIds.isEmpty()){
			throw new EntryDefinitionNotFoundException();
		}
		return findEntryDefinitionsByIds(entryDefinitionIds);
	}
	
	@Override
	public EntryDefinition findEntryDefinitionCurrentToEntry(Long entryId, 
			Date serviceCalculationDate) throws EntryDefinitionNotFoundException{
		EntryDefinition ed = null;
		Entry parent = find(entryId);
		if (parent != null){
			List<Long> entryIds = new ArrayList<Long>();
			entryIds.add(parent.getId());
			List<EntryDefinition> entryDefinitions = this.findEntryDefinitionCurrentsToEntries(entryIds, serviceCalculationDate);
			if (!entryDefinitions.isEmpty()){
				ed = entryDefinitions.get(0);
			}
		}
		return ed;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> findMaxIdEntryDefinitionCurrentsToEntries(List<Long> entryIds, 
			Date serviceCalculationDate){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("entryIds", entryIds);
		parameters.put("serviceDate", serviceCalculationDate);
		return crudService.findWithNamedQuery("EntryDefinition.findMaxIdByEntryAndServiceDate", parameters);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EntryDefinition> findEntryDefinitionsByIds(List<Long> entryDefinitionIds){
		Map<String, List<Long>> parameters = new HashMap<String, List<Long>>();
		parameters.put("ids", entryDefinitionIds);
		return crudService.findWithNamedQuery("EntryDefinition.findByIds", parameters);	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public EntryDefinition findEntryDefinitionsById(Long entryDefinitionId){
		Map<String, Long> parameters = new HashMap<String, Long>();
		parameters.put("entryDefinitionId", entryDefinitionId);
		List<EntryDefinition> results = crudService.findWithNamedQuery("EntryDefinition.findById", parameters, 1);
		if (!results.isEmpty())
			return results.get(0);
		return null;
	}
	
	@Override
	public Entry findByAccountCode(String accountCode){
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("accountCode", accountCode);
		@SuppressWarnings("unchecked")
		List<Entry> results = (List<Entry>)crudService.findWithNamedQuery("Entry.findByAccountCode", parameters, 1);
		if (! results.isEmpty())
			return results.get(0); 
		return null;
	}
	
}
