package org.gob.gim.revenue.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import ec.gob.gim.revenue.model.EntryDefinition;

@Local
public interface EntryDefinitionService {

	EntryDefinition findCurrent(Long entryId, Date startDate);
	List<EntryDefinition> findCurrent(List<Long> entryIds, Date startDate);
	List<EntryDefinition> findCurrentByEntryParent(Long entryParentId, Date startDate);
}
