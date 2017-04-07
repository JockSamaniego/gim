package org.gob.gim.finances.action;

import javax.persistence.Query;

import org.gob.gim.common.action.UserSession;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.finances.model.SequenceManager;
import ec.gob.gim.finances.model.SequenceManagerType;

@Name("sequenceManagerHome")
public class SequenceManagerHome extends EntityHome<SequenceManager> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In
	private FacesMessages facesMessages;

	private Boolean isFirstTime = Boolean.TRUE;

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	public void setSequenceManagerId(Long id) {
		setId(id);
	}

	public Long getSequenceManagerId() {
		return (Long) getId();
	}

	@Override
	protected SequenceManager createInstance() {
		SequenceManager sequenceManager = new SequenceManager();
		return sequenceManager;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		if (this.getInstance().getId() == null) {
			this.instance.setTakenBy(this.userSession.getPerson());
		}
	}

	public boolean isWired() {
		return true;
	}

	public SequenceManager getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public Boolean getIsFirstTime() {
		return isFirstTime;
	}

	public void setIsFirstTime(Boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}

	@Override
	public String persist() {
		String sql = "select ( max(code) + 1 ) code_to " + "from gimprod.SequenceManager sm "
				+ "where extract(year from sm.date) = date_part('year', current_date) " 
				+ "and sm.isactive is true";
		Integer code = 0;
		try {
			Query q = this.getEntityManager().createNativeQuery(sql);
			code = (Integer) q.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(".................... ");
		}

		String sqlSMT = "select smt from SequenceManagerType smt where smt.id = :id";
		Query qsmt = this.getEntityManager().createQuery(sqlSMT);
		qsmt.setParameter("id", new Long("1"));
		SequenceManagerType type = (SequenceManagerType) qsmt.getSingleResult();

		this.getInstance().setCode(code);
		this.getInstance().setSequenceManagerType(type);
		
		super.persist();
		return null;
	}

}
