package org.gob.gim.finances.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SequenceManagerService;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.finances.pagination.WriteOffRequestDataModel;
import org.gob.gim.finances.service.WriteOffService;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.finances.model.SequenceManager;
import ec.gob.gim.finances.model.WriteOffDetail;
import ec.gob.gim.finances.model.WriteOffRequest;
import ec.gob.gim.finances.model.WriteOffType;
import ec.gob.gim.finances.model.DTO.ConsumptionPreviousDTO;
import ec.gob.gim.finances.model.DTO.DetailTableAuxDTO;
import ec.gob.gim.finances.model.DTO.WriteOffDetailDTO;
import ec.gob.gim.finances.model.DTO.WriteOffRequestDTO;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.waterservice.model.MonthType;
import ec.gob.gim.waterservice.model.WaterSupply;

@Name("writeOffRequestHome")
public class WriteOffRequestHome extends EntityHome<WriteOffRequest> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	private WriteOffService writeOffService;

	private SequenceManagerService sequenceManagerService;

	private String criteria;

	private List<Resident> residents;

	private String identificationNumber;

	private List<WaterSupply> waterSupplies = new ArrayList<WaterSupply>();

	private WaterSupply waterSupplySelected;

	private Long numberOldBondAux;

	private Long numberNewBondAux;

	private WriteOffDetailDTO detail_aux_old = new WriteOffDetailDTO();

	private WriteOffDetailDTO detail_aux_new = new WriteOffDetailDTO();

	private Boolean disabled_old_fields = Boolean.TRUE;

	private Boolean disabled_new_fields = Boolean.TRUE;

	private List<WriteOffDetail> details = new ArrayList<WriteOffDetail>();

	private List<DetailTableAuxDTO> detailsTableOld = new ArrayList<DetailTableAuxDTO>();

	private List<DetailTableAuxDTO> detailsTableNew = new ArrayList<DetailTableAuxDTO>();

	private List<WriteOffType> _types = new ArrayList<WriteOffType>();

	private boolean isFirstTime = true;

	private List<ConsumptionPreviousDTO> previous_consumptions = new ArrayList<ConsumptionPreviousDTO>();

	/*
	 * CRITERIA
	 */

	private String number_request_criteria = null;

	private String identification_number_criteria = null;

	private String name_resident_criteria = null;

	/*
	 * Auxiliar para la baja seleccionada(print)
	 */
	private WriteOffRequestDTO writeOffRequestSelected;

	private Charge revenueCharge;

	private Delegate revenueDelegate;

	private Charge commercializationCharge;

	private Delegate commercializationDelegate;

	private Charge commercializationRevisionCharge;

	private Delegate commercializationRevisionDelegate;

	private Charge finantialCharge;

	private Delegate finantialDelegate;

	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	private SystemParameterService systemParameterService;

	private Boolean not_found_adjunct_bond_old = Boolean.FALSE;

	private Boolean not_found_adjunct_bond_new = Boolean.FALSE;

	private WriteOffDetail aux_entity_detail;

	public List<WaterSupply> getWaterSupplies() {
		return waterSupplies;
	}

	public void setWaterSupplies(List<WaterSupply> waterSupplies) {
		this.waterSupplies = waterSupplies;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public void setWriteOffRequestId(Long id) {
		setId(id);
	}

	public Long getWriteOffRequestId() {
		return (Long) getId();
	}

	public WaterSupply getWaterSupplySelected() {
		return waterSupplySelected;
	}

	public void setWaterSupplySelected(WaterSupply waterSupplySelected) {
		this.waterSupplySelected = waterSupplySelected;
	}

	public Long getNumberOldBondAux() {
		return numberOldBondAux;
	}

	public void setNumberOldBondAux(Long numberOldBondAux) {
		this.numberOldBondAux = numberOldBondAux;
	}

	public Long getNumberNewBondAux() {
		return numberNewBondAux;
	}

	public void setNumberNewBondAux(Long numberNewBondAux) {
		this.numberNewBondAux = numberNewBondAux;
	}

	public WriteOffDetailDTO getDetail_aux_old() {
		return detail_aux_old;
	}

	public void setDetail_aux_old(WriteOffDetailDTO detail_aux_old) {
		this.detail_aux_old = detail_aux_old;
	}

	public WriteOffDetailDTO getDetail_aux_new() {
		return detail_aux_new;
	}

	public void setDetail_aux_new(WriteOffDetailDTO detail_aux_new) {
		this.detail_aux_new = detail_aux_new;
	}

	public Boolean getDisabled_old_fields() {
		return disabled_old_fields;
	}

	public void setDisabled_old_fields(Boolean disabled_old_fields) {
		this.disabled_old_fields = disabled_old_fields;
	}

	public Boolean getDisabled_new_fields() {
		return disabled_new_fields;
	}

	public void setDisabled_new_fields(Boolean disabled_new_fields) {
		this.disabled_new_fields = disabled_new_fields;
	}

	public List<WriteOffDetail> getDetails() {
		return details;
	}

	public void setDetails(List<WriteOffDetail> details) {
		this.details = details;
	}

	public List<DetailTableAuxDTO> getDetailsTableOld() {
		return detailsTableOld;
	}

	public void setDetailsTableOld(List<DetailTableAuxDTO> detailsTableOld) {
		this.detailsTableOld = detailsTableOld;
	}

	public List<DetailTableAuxDTO> getDetailsTableNew() {
		return detailsTableNew;
	}

	public void setDetailsTableNew(List<DetailTableAuxDTO> detailsTableNew) {
		this.detailsTableNew = detailsTableNew;
	}

	public List<WriteOffType> get_types() {
		return _types;
	}

	public void set_types(List<WriteOffType> _types) {
		this._types = _types;
	}

	public String getNumber_request_criteria() {
		return number_request_criteria;
	}

	public void setNumber_request_criteria(String number_request_criteria) {
		this.number_request_criteria = number_request_criteria;
	}

	public String getIdentification_number_criteria() {
		return identification_number_criteria;
	}

	public void setIdentification_number_criteria(
			String identification_number_criteria) {
		this.identification_number_criteria = identification_number_criteria;
	}

	public String getName_resident_criteria() {
		return name_resident_criteria;
	}

	public void setName_resident_criteria(String name_resident_criteria) {
		this.name_resident_criteria = name_resident_criteria;
	}

	public Delegate getRevenueDelegate() {
		return revenueDelegate;
	}

	public void setRevenueDelegate(Delegate revenueDelegate) {
		this.revenueDelegate = revenueDelegate;
	}

	public Charge getCommercializationCharge() {
		return commercializationCharge;
	}

	public void setCommercializationCharge(Charge commercializationCharge) {
		this.commercializationCharge = commercializationCharge;
	}

	public Delegate getCommercializationDelegate() {
		return commercializationDelegate;
	}

	public void setCommercializationDelegate(Delegate commercializationDelegate) {
		this.commercializationDelegate = commercializationDelegate;
	}

	public Charge getFinantialCharge() {
		return finantialCharge;
	}

	public void setFinantialCharge(Charge finantialCharge) {
		this.finantialCharge = finantialCharge;
	}

	public Delegate getFinantialDelegate() {
		return finantialDelegate;
	}

	public void setFinantialDelegate(Delegate finantialDelegate) {
		this.finantialDelegate = finantialDelegate;
	}

	public List<ConsumptionPreviousDTO> getPrevious_consumptions() {
		return previous_consumptions;
	}

	public void setPrevious_consumptions(
			List<ConsumptionPreviousDTO> previous_consumptions) {
		this.previous_consumptions = previous_consumptions;
	}

	public Charge getCommercializationRevisionCharge() {
		return commercializationRevisionCharge;
	}

	public void setCommercializationRevisionCharge(
			Charge commercializationRevisionCharge) {
		this.commercializationRevisionCharge = commercializationRevisionCharge;
	}

	public Delegate getCommercializationRevisionDelegate() {
		return commercializationRevisionDelegate;
	}

	public void setCommercializationRevisionDelegate(
			Delegate commercializationRevisionDelegate) {
		this.commercializationRevisionDelegate = commercializationRevisionDelegate;
	}

	public WriteOffRequestDTO getWriteOffRequestSelected() {
		return writeOffRequestSelected;
	}

	public void setWriteOffRequestSelected(
			WriteOffRequestDTO writeOffRequestSelected) {
		this.writeOffRequestSelected = writeOffRequestSelected;
	}

	public Charge getRevenueCharge() {
		return revenueCharge;
	}

	public void setRevenueCharge(Charge revenueCharge) {
		this.revenueCharge = revenueCharge;
	}

	public Boolean getNot_found_adjunct_bond_old() {
		return not_found_adjunct_bond_old;
	}

	public void setNot_found_adjunct_bond_old(Boolean not_found_adjunct_bond_old) {
		this.not_found_adjunct_bond_old = not_found_adjunct_bond_old;
	}

	public Boolean getNot_found_adjunct_bond_new() {
		return not_found_adjunct_bond_new;
	}

	public void setNot_found_adjunct_bond_new(Boolean not_found_adjunct_bond_new) {
		this.not_found_adjunct_bond_new = not_found_adjunct_bond_new;
	}

	public WriteOffDetail getAux_entity_detail() {
		return aux_entity_detail;
	}

	public void setAux_entity_detail(WriteOffDetail aux_entity_detail) {
		this.aux_entity_detail = aux_entity_detail;
	}

	@Override
	protected WriteOffRequest createInstance() {
		WriteOffRequest writeOffRequest = new WriteOffRequest();
		return writeOffRequest;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {

		getInstance();

		if (isFirstTime) {

			isFirstTime = Boolean.FALSE;
			this.details = new ArrayList<WriteOffDetail>();

			/*
			 * Inicializar tipos de inconsistencias
			 */
			this._types = this.findTypes();

			loadCharge();

		}

		if (writeOffService == null) {
			writeOffService = ServiceLocator.getInstance().findResource(
					WriteOffService.LOCAL_NAME);
		}

		if (sequenceManagerService == null) {
			sequenceManagerService = ServiceLocator.getInstance().findResource(
					SequenceManagerService.LOCAL_NAME);
		}

	}

	public boolean isWired() {
		return true;
	}

	public WriteOffRequest getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria() {
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery(
					"Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			setResidents(query.getResultList());
		}
	}

	public void searchResident() {
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();

			this.getInstance().setResident(resident);

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

			if (this.instance.getResident() != null) {
				Query q = this.getEntityManager().createNamedQuery(
						"WaterSupply.findByResident");
				q.setParameter("idResident", this.instance.getResident()
						.getId());
				this.waterSupplies = q.getResultList();
			}

		} catch (Exception e) {
			this.getInstance().setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void clearSearchResidentPanel() {
		this.setCriteria(null);
		setResidents(null);
	}

	public void residentSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes()
				.get("resident");
		this.getInstance().setResident(resident);
		this.setIdentificationNumber(resident.getIdentificationNumber());

		if (this.instance.getResident() != null) {
			Query q = this.getEntityManager().createNamedQuery(
					"WaterSupply.findByResident");
			q.setParameter("idResident", this.instance.getResident().getId());
			this.waterSupplies = q.getResultList();
		}

	}

	public void onSelectService() {
		int aux_index = -1;

		for (int i = 0; i < waterSupplySelected.getWaterMeters().size(); i++) {
			if (waterSupplySelected.getWaterMeters().get(i).getIsActive()) {
				aux_index = i;
				break;
			}
		}

		if (aux_index != -1) {
			this.instance.setWaterMeter(waterSupplySelected.getWaterMeters()
					.get(aux_index));
		}

	}

	public void onSelectType() {
		System.out.println("----Llega al select Tipo----");
		System.out.println(this.instance.getWriteOffType().getName());

	}

	public void prepareAddBondLower() {
		// Encerar los valores del dialogo
		this.detail_aux_new = new WriteOffDetailDTO();
		this.detail_aux_old = new WriteOffDetailDTO();
		this.numberNewBondAux = null;
		this.numberOldBondAux = null;
		this.disabled_new_fields = Boolean.TRUE;
		this.disabled_old_fields = Boolean.TRUE;
		this.aux_entity_detail = new WriteOffDetail();
	}

	public void searchBondDetailOld() {

		this.aux_entity_detail = new WriteOffDetail();

		if (this.instance.getResident() == null) {
			addFacesMessageFromResourceBundle("No ha ingresado el contribuyente");
		} else {

			List<WriteOffDetailDTO> retorno_bd = this.writeOffService
					.searchBondDetail(this.numberOldBondAux);

			if (retorno_bd.size() == 0) {
				addFacesMessageFromResourceBundle("No existe la obligacion con el numero ingresado");
				this.not_found_adjunct_bond_old = Boolean.TRUE;
			} else if (retorno_bd.size() > 0) {

				if (retorno_bd.get(0).getCount_old().longValue() > 0
						|| retorno_bd.get(0).getCount_new().longValue() > 0) {
					addFacesMessageFromResourceBundle("Obligacion ya ingresada en otra baja");
				} else if (retorno_bd.get(0).getResident_id().intValue() == this.instance
						.getResident().getId().intValue()) {
					this.detail_aux_old = retorno_bd.get(0);
					if (this.detail_aux_old.getAdjunct_id() == 0) {
						this.detail_aux_old.setAdjunct_id(null);
						this.detail_aux_old.setAmount_m3(null);
						this.detail_aux_old.setCurrentreading(null);
						this.detail_aux_old.setMonth(null);
						this.detail_aux_old.setMonthtype(null);
						this.detail_aux_old.setPreviousreading(null);
						this.detail_aux_old.setYear(null);
						this.disabled_old_fields = false;
					} else {
						this.disabled_old_fields = true;
					}

				} else {
					addFacesMessageFromResourceBundle("La obligacion no pertenece al contribuyente ingresado");
					this.detail_aux_old = null;
					this.disabled_old_fields = true;
					this.numberOldBondAux = null;
				}

			}

		}

	}

	public void searchBondDetailNew() {

		this.aux_entity_detail = new WriteOffDetail();

		if (this.instance.getResident() == null) {
			addFacesMessageFromResourceBundle("No ha ingresado el contribuyente");
		} else {

			List<WriteOffDetailDTO> retorno_bd = this.writeOffService
					.searchBondDetail(this.numberNewBondAux);

			if (retorno_bd.size() == 0) {
				addFacesMessageFromResourceBundle("No existe la obligacion con el numero ingresado");
				this.not_found_adjunct_bond_old = Boolean.TRUE;
			} else if (retorno_bd.size() > 0) {

				if (retorno_bd.get(0).getCount_new().longValue() > 0
						|| retorno_bd.get(0).getCount_old().longValue() > 0) {
					addFacesMessageFromResourceBundle("Obligacion ya ingresada en otra baja");
				} else if (retorno_bd.get(0).getResident_id().intValue() == this.instance
						.getResident().getId().intValue()) {
					this.detail_aux_new = retorno_bd.get(0);
					if (this.detail_aux_new.getAdjunct_id() == 0) {
						this.detail_aux_new.setAdjunct_id(null);
						this.detail_aux_new.setAmount_m3(null);
						this.detail_aux_new.setCurrentreading(null);
						this.detail_aux_new.setMonth(null);
						this.detail_aux_new.setMonthtype(null);
						this.detail_aux_new.setPreviousreading(null);
						this.detail_aux_new.setYear(null);
						this.disabled_new_fields = false;
					} else {
						this.disabled_new_fields = true;
					}

				} else {
					addFacesMessageFromResourceBundle("La obligacion no pertenece al contribuyente ingresado");
					this.detail_aux_new = null;
					this.disabled_new_fields = true;
					this.numberNewBondAux = null;
				}

			}

		}

	}

	public void addBonds() {

		System.out.println("Llega al add bonds");

		this.instance.setMadeBy(this.userSession.getPerson());

		WriteOffDetail detail = new WriteOffDetail();
		detail.setNewAmount(this.detail_aux_new.getAmount_m3());
		detail.setNewCurrentReading(this.detail_aux_new.getCurrentreading());
		detail.setNewMunicipalBond(getEntityManager().find(MunicipalBond.class,
				this.detail_aux_new.getBond_id()));

		detail.setNewPreviousReading(this.detail_aux_new.getPreviousreading());
		detail.setOldAmount(this.detail_aux_old.getAmount_m3());
		detail.setOldCurrentReading(this.detail_aux_old.getCurrentreading());
		detail.setOldMunicipalBond(getEntityManager().find(MunicipalBond.class,
				this.detail_aux_old.getBond_id()));
		detail.setOldPreviousReading(this.detail_aux_old.getPreviousreading());
		detail.setWriteOffRequest(this.instance);

		/*
		 * COMUNES
		 */

		if (!this.disabled_old_fields) {
			detail.setMonthType(this.aux_entity_detail.getMonthType());
			detail.setMonth(this.aux_entity_detail.getMonthType().getMonthInt());
		} else {
			detail.setMonthType(MonthType.getByValue(this.detail_aux_new
					.getMonth()));
			detail.setMonth(this.detail_aux_new.getMonth());
		}

		detail.setYear(this.detail_aux_new.getYear());
		detail.setWriteOffRequest(this.instance);

		this.instance.addDetail(detail);

		this.details.add(detail);

		System.out.println(this.details);

		this.detailsTableNew = new ArrayList<DetailTableAuxDTO>();

		this.detailsTableOld = new ArrayList<DetailTableAuxDTO>();

		for (int i = 0; i < details.size(); i++) {
			WriteOffDetail det = this.details.get(i);

			DetailTableAuxDTO old = new DetailTableAuxDTO();
			old.setIndex(i + 1);
			old.setBond_number(det.getOldMunicipalBond().getNumber());
			old.setCurrent_reading(det.getOldCurrentReading());
			old.setM3(det.getOldAmount());
			old.setMonth_name(det.getMonthType().name());
			old.setPrevious_reading(det.getOldPreviousReading());
			old.setValue(det.getOldMunicipalBond().getValue());
			old.setYear(det.getYear());

			this.detailsTableOld.add(old);

			DetailTableAuxDTO _new = new DetailTableAuxDTO();
			_new.setIndex(i + 1);
			_new.setBond_number(det.getNewMunicipalBond().getNumber());
			_new.setCurrent_reading(det.getNewCurrentReading());
			_new.setM3(det.getNewAmount());
			_new.setMonth_name(det.getMonthType().name());
			_new.setPrevious_reading(det.getNewPreviousReading());
			_new.setValue(det.getNewMunicipalBond().getValue());
			_new.setYear(det.getYear());

			this.detailsTableNew.add(_new);

		}

		System.out.println(this.detailsTableOld);
		System.out.println(this.detailsTableNew);

	}

	public List<WriteOffType> findTypes() {
		Query query = getEntityManager().createNamedQuery(
				"WriteOffType.findAll");
		List<WriteOffType> _return = query.getResultList();
		return _return;
	}

	@Transactional
	public String save() {

		System.out.println("Llega al save");

		SequenceManager sequence = new SequenceManager();
		sequence.setCode(this.sequenceManagerService.getNextValue());
		sequence.setExplanation("Baja de Agua potable");
		sequence.setSequenceManagerType(this.sequenceManagerService
				.getTypeByCode("AGUA_POTABLE"));
		sequence.setTakenBy(this.userSession.getPerson());

		this.instance.setSequenceManager(sequence);

		return this.persist();

	}

	private WriteOffRequestDataModel getDataModel() {

		/*
		 * WriteOffRequestDataModel dataModel = (WriteOffRequestDataModel)
		 * Contexts
		 * .getConversationContext().get(WriteOffRequestDataModel.class);
		 */
		WriteOffRequestDataModel dataModel = (WriteOffRequestDataModel) Component
				.getInstance(WriteOffRequestDataModel.class, true);
		return dataModel;
	}

	public void loadWrites() {
		getDataModel().setCriteria(this.number_request_criteria,
				this.identification_number_criteria,
				this.name_resident_criteria);
		getDataModel().setRowCount(getDataModel().getObjectsNumber());

		loadCharge();
	}

	public void search() {
		getDataModel().setCriteria(this.number_request_criteria,
				this.identification_number_criteria,
				this.name_resident_criteria);
		getDataModel().setRowCount(getDataModel().getObjectsNumber());
	}

	public String print(Long writeOffRequest_id) {

		if (writeOffService == null) {
			writeOffService = ServiceLocator.getInstance().findResource(
					WriteOffService.LOCAL_NAME);
		}

		this.writeOffRequestSelected = this.writeOffService
				.findById(writeOffRequest_id);

		WriteOffRequest writeOff = this.getEntityManager().find(
				WriteOffRequest.class, writeOffRequest_id);

		this.detailsTableOld = new ArrayList<DetailTableAuxDTO>();

		this.detailsTableNew = new ArrayList<DetailTableAuxDTO>();

		String _month = "";

		for (int i = 0; i < writeOff.getDetails().size(); i++) {

			WriteOffDetail det = writeOff.getDetails().get(i);

			DetailTableAuxDTO old = new DetailTableAuxDTO();
			old.setIndex(i + 1);
			old.setBond_number(det.getOldMunicipalBond().getNumber());
			old.setCurrent_reading(det.getOldCurrentReading());
			old.setM3(det.getOldAmount());
			old.setMonth_name(det.getMonthType().name());
			old.setPrevious_reading(det.getOldPreviousReading());
			old.setValue(det.getOldMunicipalBond().getValue());
			old.setYear(det.getYear());

			this.detailsTableOld.add(old);

			DetailTableAuxDTO _new = new DetailTableAuxDTO();
			_new.setIndex(i + 1);
			_new.setBond_number(det.getNewMunicipalBond().getNumber());
			_new.setCurrent_reading(det.getNewCurrentReading());
			_new.setM3(det.getNewAmount());
			_new.setMonth_name(det.getMonthType().name());
			_new.setPrevious_reading(det.getNewPreviousReading());
			_new.setValue(det.getNewMunicipalBond().getValue());
			_new.setYear(det.getYear());

			_month = String.valueOf(det.getMonthType().getMonthInt());

			this.detailsTableNew.add(_new);

		}

		this.previous_consumptions = this.writeOffService.findPreviousReading(
				writeOff.getWaterMeter().getId(), this.writeOffRequestSelected
						.get_year().toString(), _month);

		return "/finances/WriteOffRequestReportPDF.xhtml";
	}

	public BigDecimal totalOldBonds() {

		BigDecimal sum = BigDecimal.ZERO;

		for (DetailTableAuxDTO detailTableAuxDTO : detailsTableOld) {
			sum = sum.add(detailTableAuxDTO.getValue());
		}

		return sum;
	}

	public BigDecimal totalNewBonds() {

		BigDecimal sum = BigDecimal.ZERO;

		for (DetailTableAuxDTO detailTableAuxDTO : detailsTableNew) {
			sum = sum.add(detailTableAuxDTO.getValue());
		}

		return sum;
	}

	private void loadCharge() {

		revenueCharge = getCharge("DELEGATE_ID_REVENUE");
		if (revenueCharge != null) {
			for (Delegate d : revenueCharge.getDelegates()) {
				if (d.getIsActive())
					revenueDelegate = d;
			}
		}

		finantialCharge = getCharge("DELEGATE_ID_FINANTIAL");
		if (finantialCharge != null) {
			for (Delegate d : finantialCharge.getDelegates()) {
				if (d.getIsActive())
					finantialDelegate = d;
			}
		}

		commercializationCharge = getCharge("DELEGATE_ID_COMMERCIALIZATION");
		if (commercializationCharge != null) {
			for (Delegate d : commercializationCharge.getDelegates()) {
				if (d.getIsActive())
					commercializationDelegate = d;
			}
		}

		commercializationRevisionCharge = getCharge("RESPONSIBLE_ID_VERIFICATION");
		if (commercializationRevisionCharge != null) {
			for (Delegate d : commercializationRevisionCharge.getDelegates()) {
				if (d.getIsActive())
					commercializationRevisionDelegate = d;
			}
		}

	}

	private Charge getCharge(String systemParameter) {
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		Charge charge = systemParameterService.materialize(Charge.class,
				systemParameter);
		return charge;
	}

}