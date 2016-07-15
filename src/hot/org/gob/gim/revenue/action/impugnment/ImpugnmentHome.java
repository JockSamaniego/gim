package org.gob.gim.revenue.action.impugnment;



import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.framework.EntityController;

@Name("impugnmentHome")
@Scope(ScopeType.CONVERSATION)
public class ImpugnmentHome extends EntityController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * criterios
	 * 
	 * @return
	 */
//	@In(create = true)
//	ImpugnmentDataModel dataModel;

	private Integer numberProsecution;

	private Integer numberInfringement;

	private ImpugnmentDataModel getDataModel() {

		ImpugnmentDataModel dataModel = (ImpugnmentDataModel) Contexts
				.getConversationContext().get(ImpugnmentDataModel.class);
		
		
		System.out.println("Context:" + Contexts.getConversationContext());
		
		System.out.println("Context:" + Contexts.getConversationContext().get("impugnments"));
		
		System.out.println("Data model en el getDataModel" + dataModel);
		return dataModel;
	}

	public String findImpugnments() {

		getDataModel().setCriteria(numberProsecution, numberInfringement);
		getDataModel().setRowCount(getDataModel().getObjectsNumber());

		return null;
	}
	
	public void loadImpugnments() {
		findImpugnments();
	}

	public Integer getNumberProsecution() {
		return numberProsecution;
	}

	public void setNumberProsecution(Integer numberProsecution) {
		this.numberProsecution = numberProsecution;
	}

	public Integer getNumberInfringement() {
		return numberInfringement;
	}

	public void setNumberInfringement(Integer numberInfringement) {
		this.numberInfringement = numberInfringement;
	}
	
}
