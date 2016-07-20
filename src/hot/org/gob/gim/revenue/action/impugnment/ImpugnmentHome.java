package org.gob.gim.revenue.action.impugnment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.gob.gim.common.CatalogConstants;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.revenue.service.ImpugnmentService;
import org.gob.gim.revenue.service.ItemCatalogService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityController;

import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.impugnment.Impugnment;
import ec.gob.gim.revenue.model.impugnment.criteria.ImpugnmentSearchCriteria;

@Name("impugnmentHome")
@Scope(ScopeType.CONVERSATION)
public class ImpugnmentHome extends EntityController {

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	@In
	FacesMessages facesMessages;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ImpugnmentService impugnmentService;

	private ItemCatalogService itemCatalogService;

	/**
	 * criterios
	 * 
	 * @return
	 */

	// @In(create = true,required= false)
	// ImpugnmentDataModel dataModel;

	private ImpugnmentSearchCriteria criteria;

	private List<Impugnment> impugnments = new ArrayList<>();

	private Impugnment impugnmentSelected;

	private ItemCatalog typeImpugnmentFotoMulta;

	private ItemCatalog stateRegisterImpugnmentFotoMulta;

	// private ImpugnmentDataModel getDataModel() {
	//
	// ImpugnmentDataModel dataModel = (ImpugnmentDataModel) Contexts
	// .getConversationContext().get(ImpugnmentDataModel.class);
	//
	// System.out.println("Context:" + Contexts.getConversationContext());
	//
	// System.out.println("Context:"
	// + Contexts.getConversationContext().get("impugnments"));
	//
	// System.out.println("Data model en el getDataModel" + dataModel);
	//
	// return new ImpugnmentDataModel();
	//
	// }

	private Integer numberProsecution;

	private Integer numberInfringement;

	public ImpugnmentHome() {
		loadImpugnments();
	}

	public void findImpugnments() {

		// getDataModel().setCriteria(numberProsecution, numberInfringement);
		// getDataModel().setRowCount(getDataModel().getObjectsNumber());

		// return null;
		this.impugnments = this.impugnmentService
				.findImpugnmentsForCriteria(criteria);

		System.out
				.println("************------------Entra a Find Impugnments------------***********");
		System.out.println("tamaño:" + this.impugnments.size() + "----->"
				+ this.impugnments);

	}

	public void loadImpugnments() {
		initializeService();
		this.criteria = new ImpugnmentSearchCriteria();
		findImpugnments();
		typeImpugnmentFotoMulta = itemCatalogService
				.findItemByCodeAndCodeCatalog(
						CatalogConstants.CATALOG_TYPES_IMPUGNMENTS,
						CatalogConstants.ITEM_CATALOG_TYPE_IMPUGNMENT_FOTO_MULTA);
		stateRegisterImpugnmentFotoMulta = itemCatalogService
				.findItemByCodeAndCodeCatalog(
						CatalogConstants.CATALOG_STATES_IMPUGNMENT,
						CatalogConstants.ITEM_CATALOG_STATE_IMPUGNMENT_REGISTER);

		// this.impugnmentSelected = new Impugnment();

	}

	public ImpugnmentSearchCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(ImpugnmentSearchCriteria criteria) {
		this.criteria = criteria;
	}

	public List<Impugnment> getImpugnments() {
		return impugnments;
	}

	public void setImpugnments(List<Impugnment> impugnments) {
		this.impugnments = impugnments;
	}

	public Impugnment getImpugnmentSelected() {
		return impugnmentSelected;
	}

	public void setImpugnmentSelected(Impugnment impugnmentSelected) {
		this.impugnmentSelected = impugnmentSelected;
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

	public void initializeService() {
		if (impugnmentService == null) {
			impugnmentService = ServiceLocator.getInstance().findResource(
					ImpugnmentService.LOCAL_NAME);
		}
		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(
					ItemCatalogService.LOCAL_NAME);
		}
	}

	public void preparaRegisterImpugnment() {
		this.impugnmentSelected = new Impugnment();
		this.impugnmentSelected.setCreationDate(new Date());
		this.impugnmentSelected.setImpugnmentDate(new Date());
	}

	public void searchMunicipalBond() {
		if (this.impugnmentSelected.getNumberInfringement() != null) {
			MunicipalBond mb = impugnmentService
					.findMunicipalBondForImpugnment(this.impugnmentSelected
							.getNumberInfringement());

			this.impugnmentSelected.setMunicipalBond(mb);

			if (mb == null) {
				addFacesMessageFromResourceBundle(
						"revenue.impugnments.notFoundInfrigment",
						this.impugnmentSelected.getNumberInfringement());
			}
		}
	}

	public void registerImpugnment() {

		if (impugnmentSelected.getImpugnmentDate() == null
				|| impugnmentSelected.getMunicipalBond() == null
				|| impugnmentSelected.getNumberInfringement() == null
				|| impugnmentSelected.getNumberProsecution() == null) {
			facesMessages.addToControl("",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					"Campos obligatorios vacios");
		} else {
			this.impugnmentSelected.setStatus(stateRegisterImpugnmentFotoMulta);
			this.impugnmentSelected.setType(typeImpugnmentFotoMulta);
			this.impugnmentSelected.setUserRegister(this.userSession.getUser());
			this.impugnmentService.save(impugnmentSelected);
			this.impugnments = this.impugnmentService
					.findImpugnmentsForCriteria(criteria);
		}

	}

	public void searchImpugments() {
		System.out.println("***********-------------**************");
		this.criteria.setNumberInfringement(numberInfringement);
		this.criteria.setNumberProsecution(numberProsecution);
		System.out.println("Criteria:" + criteria);
		this.impugnments = this.impugnmentService
				.findImpugnmentsForCriteria(criteria);
		System.out.println("tamaño:" + this.impugnments.size() + "----->"
				+ this.impugnments);
	}
}