package org.gob.gim.concession.action;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.revenue.action.EntryHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.consession.model.ConcessionPlaceType;
import ec.gob.gim.revenue.model.Entry;

@Name("concessionPlaceTypeHome")
public class ConcessionPlaceTypeHome extends EntityHome<ConcessionPlaceType> {

	@In(create = true)
	EntryHome entryHome;
	
	@In
	private FacesMessages facesMessages;

	private String entryCode;
	private String criteriaEntry;
	private List<Entry> entries;
	
	private Boolean isFirstTime = Boolean.TRUE;

	public void setConcessionPlaceTypeId(Long id) {
		setId(id);
	}

	public Long getConcessionPlaceTypeId() {
		return (Long) getId();
	}

	@Override
	protected ConcessionPlaceType createInstance() {
		ConcessionPlaceType concessionPlaceType = new ConcessionPlaceType();
		return concessionPlaceType;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Entry entry = entryHome.getDefinedInstance();
		if (entry != null) {
			getInstance().setEntry(entry);
		}
	}

	public boolean isWired() {
		return true;
	}

	public ConcessionPlaceType getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public String getEntryCode() {
		return entryCode;
	}

	public void setEntryCode(String entryCode) {
		this.entryCode = entryCode;
	}

	public String getCriteriaEntry() {
		return criteriaEntry;
	}

	public void setCriteriaEntry(String criteriaEntry) {
		this.criteriaEntry = criteriaEntry;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	public Boolean getIsFirstTime() {
		return isFirstTime;
	}

	public void setIsFirstTime(Boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}
	
	private String completeEntryCode(String code) {
		StringBuffer codeBuffer = new StringBuffer(code.trim());
		while (codeBuffer.length() < 5) {
			codeBuffer.insert(0, '0');
		}
		return codeBuffer.toString();
	}

	public void searchEntry() {
		if (entryCode != null) {
			Entry entry = findEntryByCode(entryCode);
			if (entry != null) {
				this.getInstance().setEntry(entry);
				if (entry.getAccount() == null) {
					setEntryCode(entry.getCode());
				} else {
					setEntryCode(entry.getAccount().getAccountCode());
				}
			} else {
				this.getInstance().setEntry(null);
				setEntryCode(null);
			}
		}
	}

	public void searchEntryByCriteria() {
		if (this.criteriaEntry != null && !this.criteriaEntry.isEmpty()) {
			entries = findByCriteria(criteriaEntry);
		}
	}

	public void entrySelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Entry entry = (Entry) component.getAttributes().get("entry");
		this.getInstance().setEntry(entry);
		if (entry.getAccount() == null) {
			setEntryCode(entry.getCode());
		} else {
			setEntryCode(entry.getAccount().getAccountCode());
		}
	}

	public List<Entry> findByCriteria(String criteria) {
		Query query = getEntityManager().createNamedQuery(
				"Entry.findByCriteria");
		query.setParameter("criteria", criteria);
		return query.getResultList();
	}

	public void clearSearchEntryPanel() {
		this.setCriteriaEntry(null);
		entries = null;
	}

	public Entry findEntryByCode(String code) {
		code = this.completeEntryCode(code);
		Query query = getEntityManager().createNamedQuery("Entry.findByCode");
		query.setParameter("code", code);
		List<Entry> results = (List<Entry>) query.getResultList();
		if (!results.isEmpty())
			return results.get(0);
		return null;
	}

}
