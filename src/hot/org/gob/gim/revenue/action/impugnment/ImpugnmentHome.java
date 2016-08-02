package org.gob.gim.revenue.action.impugnment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.gob.gim.common.CatalogConstants;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.service.ImpugnmentService;
import org.gob.gim.revenue.service.ItemCatalogService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityController;

import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.income.model.Account;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.impugnment.Impugnment;
import ec.gob.gim.revenue.model.impugnment.criteria.ImpugnmentSearchCriteria;
import ec.gob.gim.security.model.Role;
import ec.gob.gim.security.model.User;

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

	private SystemParameterService systemParameterService;

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

	private List<ItemCatalog> statesImpugnmentFotoMulta;

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

	private String numberInfringement;

	private Boolean isEditAction;

	private ItemCatalog stateForImpugnmentFotoMultaEdit;

	private Boolean existObligationForInpugnmentNumber;

	private Role role_Coactivas;

	private Role role_Matriculacion;

	private Role role_Ver;

	public ImpugnmentHome() {
		loadImpugnments();
	}

	public void findImpugnments() {

		// getDataModel().setCriteria(numberProsecution, numberInfringement);
		// getDataModel().setRowCount(getDataModel().getObjectsNumber());

		// return null;
		this.impugnments = this.impugnmentService
				.findImpugnmentsForCriteria(criteria);
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

		List<Long> itemsExceptIds = new ArrayList<Long>();
		itemsExceptIds.add(stateRegisterImpugnmentFotoMulta.getId());

		statesImpugnmentFotoMulta = itemCatalogService
				.findItemsForCatalogCodeExceptIds(
						CatalogConstants.CATALOG_STATES_IMPUGNMENT,
						itemsExceptIds);

		// this.impugnmentSelected = new Impugnment();

		this.role_Coactivas = systemParameterService.materialize(Role.class,
				"ID_ROL_IMPUGNACIONES_COACTIVAS");
		this.role_Matriculacion = systemParameterService.materialize(
				Role.class, "ID_ROL_IMPUGNACIONES_MATRICULACION");
		this.role_Ver = systemParameterService.materialize(Role.class,
				"ID_ROL_IMPUGNACIONES_VER");

		// if(userSession.getUser().hasRole(formalizingRoleName)){

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

	public String getNumberInfringement() {
		return numberInfringement;
	}

	public void setNumberInfringement(String numberInfringement) {
		this.numberInfringement = numberInfringement;
	}

	public Boolean getIsEditAction() {
		return isEditAction;
	}

	public void setIsEditAction(Boolean isEditAction) {
		this.isEditAction = isEditAction;
	}

	public List<ItemCatalog> getStatesImpugnmentFotoMulta() {
		return statesImpugnmentFotoMulta;
	}

	public void setStatesImpugnmentFotoMulta(
			List<ItemCatalog> statesImpugnmentFotoMulta) {
		this.statesImpugnmentFotoMulta = statesImpugnmentFotoMulta;
	}

	public ItemCatalog getStateForImpugnmentFotoMultaEdit() {
		return stateForImpugnmentFotoMultaEdit;
	}

	public void setStateForImpugnmentFotoMultaEdit(
			ItemCatalog stateForImpugnmentFotoMultaEdit) {
		this.stateForImpugnmentFotoMultaEdit = stateForImpugnmentFotoMultaEdit;
	}

	public Boolean getExistObligationForInpugnmentNumber() {
		return existObligationForInpugnmentNumber;
	}

	public void setExistObligationForInpugnmentNumber(
			Boolean existObligationForInpugnmentNumber) {
		this.existObligationForInpugnmentNumber = existObligationForInpugnmentNumber;
	}

	public Role getRole_Coactivas() {
		return role_Coactivas;
	}

	public void setRole_Coactivas(Role role_Coactivas) {
		this.role_Coactivas = role_Coactivas;
	}

	public Role getRole_Matriculacion() {
		return role_Matriculacion;
	}

	public void setRole_Matriculacion(Role role_Matriculacion) {
		this.role_Matriculacion = role_Matriculacion;
	}

	public Role getRole_Ver() {
		return role_Ver;
	}

	public void setRole_Ver(Role role_Ver) {
		this.role_Ver = role_Ver;
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
		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SystemParameterService.LOCAL_NAME);
		}
	}

	public void preparaRegisterImpugnment() {
		this.impugnmentSelected = new Impugnment();
		this.impugnmentSelected.setCreationDate(new Date());
		this.impugnmentSelected.setImpugnmentDate(new Date());
		this.isEditAction = Boolean.FALSE;
		this.existObligationForInpugnmentNumber = Boolean.TRUE;
	}

	public void prepareUpdateImpugnment(Long impugnmentId) {
		this.impugnmentSelected = impugnmentService.findById(impugnmentId);
		this.isEditAction = Boolean.TRUE;
		this.stateForImpugnmentFotoMultaEdit = null;
	}

	public void searchMunicipalBond() {
		if (this.impugnmentSelected.getNumberInfringement() != null) {
			MunicipalBond mb = impugnmentService
					.findMunicipalBondForImpugnment(this.impugnmentSelected
							.getNumberInfringement());

			if (mb == null) {
				this.impugnmentSelected.setMunicipalBond(new MunicipalBond());
				this.existObligationForInpugnmentNumber = Boolean.FALSE;
				addFacesMessageFromResourceBundle(
						"revenue.impugnments.notFoundInfrigment",
						this.impugnmentSelected.getNumberInfringement());
			} else {
				this.existObligationForInpugnmentNumber = Boolean.TRUE;
				this.impugnmentSelected.setMunicipalBond(mb);
			}
		}
	}

	public void registerImpugnment() {

		if (impugnmentSelected.getImpugnmentDate() == null
				|| impugnmentSelected.getMunicipalBond() == null
				|| impugnmentSelected.getNumberInfringement() == null
				|| impugnmentSelected.getNumberProsecution() == null
				|| impugnmentSelected.getMunicipalBond().getNumber() == null
				|| impugnmentSelected.getMunicipalBond().getId() == null) {
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

	public void searchMunicipalBondByNumber() {
		if (this.impugnmentSelected.getMunicipalBond().getNumber() != null) {
			MunicipalBond mb = impugnmentService
					.findMunicipalBondByNumber(this.impugnmentSelected
							.getMunicipalBond().getNumber());

			if (mb == null) {
				this.existObligationForInpugnmentNumber = Boolean.FALSE;
				addFacesMessageFromResourceBundle(
						"revenue.impugnments.notFoundMunicipalBond",
						this.impugnmentSelected.getMunicipalBond().getNumber());
			} else {
				this.impugnmentSelected.setMunicipalBond(mb);
				this.existObligationForInpugnmentNumber = Boolean.TRUE;
			}
		}
	}

	public void updateImpugnment() {
		if (this.stateForImpugnmentFotoMultaEdit == null) {
			facesMessages.addToControl("",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					"Debe seleccionar un estado");
		} else {
			this.impugnmentSelected.setStatus(stateForImpugnmentFotoMultaEdit);
			this.impugnmentSelected.setUserUpdate(this.userSession.getUser());
			this.impugnmentSelected.setUpdateDate(new Date());
			this.impugnmentService.update(impugnmentSelected);
			this.impugnments = this.impugnmentService
					.findImpugnmentsForCriteria(criteria);
		}
	}

	public void searchImpugments() {
		this.criteria.setNumberInfringement(numberInfringement);
		this.criteria.setNumberProsecution(numberProsecution);
		this.impugnments = this.impugnmentService
				.findImpugnmentsForCriteria(criteria);
	}

	public Boolean isUserWithRolCoercive() {
		if (this.userSession.hasRoleByNameRol(role_Coactivas.getName())) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	public Boolean isUserWithRolMatriculacion() {
		
		if (this.userSession.hasRoleByNameRol(role_Matriculacion.getName())) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	public Boolean isUserWithRolVer() {
		if (this.userSession.hasRoleByNameRol(role_Ver.getName())) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

}