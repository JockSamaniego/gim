package ec.gob.gim.revenue.model.adjunct;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import ec.gob.gim.revenue.model.Adjunct;

@Entity
@DiscriminatorValue("BUDGR")
public class BudgetReference extends Adjunct {

	private String budgetCode;
	private String cadastralCode;

	// private String observation;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(budgetCode != null ? budgetCode : "ND");
		sb.append(" - ");
		sb.append(cadastralCode != null ? cadastralCode : "ND");
		return sb.toString();
	}

	public List<ValuePair> getDetails() {
		List<ValuePair> details = new LinkedList<ValuePair>();
		ValuePair pair = new ValuePair("Presupuesto N°", budgetCode);
		details.add(pair);
		pair = new ValuePair("Clave Catastral", cadastralCode);
		details.add(pair);
		return details;
	}

	public String getBudgetCode() {
		return budgetCode;
	}

	public void setBudgetCode(String budgetCode) {
		this.budgetCode = budgetCode;
	}

	public String getCadastralCode() {
		return cadastralCode;
	}

	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}

}
