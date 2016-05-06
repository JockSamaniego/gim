package org.gob.gim.income.action;

import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.income.model.Money;

@Name("moneyHome")
public class MoneyHome extends EntityHome<Money> {

	public void setMoneyId(Long id) {
		setId(id);
	}

	public Long getMoneyId() {
		return (Long) getId();
	}
	
	@In
	FacesMessages facesMessages;

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}
	
	@SuppressWarnings("unchecked")
	private List<Money> findMoneyByTypeAndValue(Money m) {		
		Query query = getEntityManager().createNamedQuery("Money.findByMoneyTypeAndValue");
		query.setParameter("value", m.getValue());
		query.setParameter("moneyType", m.getMoneyType());
		return query.getResultList();		
	}
	
	
	private boolean existsValue(Money m) {		
		List<Money> list = findMoneyByTypeAndValue(m);
		if(list.size() > 1){
			return true;
		}
		if(list.size() == 0){
			return false;
		}
		
		if(list.get(0).getId().equals(m.getId())) return false;
		return true;		
	}
	
	public void addMessages(){
		String message = Interpolator.instance().interpolate("#{messages['money.existValue']}", new Object[0]);
		facesMessages.addToControl("",
				org.jboss.seam.international.StatusMessage.Severity.ERROR,
				message);
	}
	
	public String save(){
		if(existsValue(getInstance())) {
			addMessages();
			return "failed";
		}
		if(isManaged()){
			return super.update();
		}else{
			return super.persist();
		}
	}

	public boolean isWired() {
		return true;
	}

	public Money getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
