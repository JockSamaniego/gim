package org.gob.gim.market.action;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.revenue.action.EntryHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.market.model.StandType;
import ec.gob.gim.revenue.model.Entry;

@Name("standTypeHome")
public class StandTypeHome extends EntityHome<StandType> {

	@In(create = true)
	EntryHome entryHome;

	@In
	private FacesMessages facesMessages;

	private String entryCode;
	private String criteriaEntry;
	private List<Entry> entries;

	public void setStandTypeId(Long id) {
		setId(id);
	}

	public Long getStandTypeId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}
	
	private Boolean isFirstTime = Boolean.TRUE;
	
	public void wire() {
		getInstance();
		if(isFirstTime && getInstance().getEntry() != null) setEntryCode(getInstance().getEntry().getAccount().getAccountCode());
		isFirstTime = Boolean.FALSE;
	}

	public boolean isWired() {
		return true;
	}

	public StandType getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
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

	@Override
	public String persist() {

		if (this.getInstance().getEntry() != null) {
			return super.persist();
		} else {
			String message = Interpolator.instance().interpolate(
					"Debe seleccionar un Rubro", new Object[0]);
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			return null;
		}
	}
	
}
