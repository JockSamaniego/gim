package org.gob.gim.electronicvoucher.creditnote.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.common.NativeQueryResultsMapper;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.CrudService;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.exception.InvoiceNumberOutOfRangeException;
import org.gob.gim.income.dto.CreditNoteElectDTO;
import org.gob.gim.income.facade.IncomeService;
import org.gob.gim.revenue.action.SolvencyReportHome;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.common.model.SystemParameter;
import ec.gob.gim.complementvoucher.model.AditionalDetail;
import ec.gob.gim.complementvoucher.model.AditionalField;
import ec.gob.gim.complementvoucher.model.ComplementVoucher;
import ec.gob.gim.complementvoucher.model.ComplementVoucherType;
import ec.gob.gim.complementvoucher.model.ElectronicItem;
import ec.gob.gim.complementvoucher.model.ElectronicVoucher;
import ec.gob.gim.complementvoucher.model.InstitutionService;
import ec.gob.gim.complementvoucher.model.TypeEmissionPoint;
import ec.gob.gim.income.model.Receipt;
import ec.gob.gim.income.model.StatusElectronicReceipt;
import ec.gob.gim.income.model.TaxItem;
import ec.gob.gim.revenue.model.Item;
import ec.gob.gim.revenue.model.MunicipalBond;
//import org.joda.time.Days;
import ec.gob.gim.revenue.model.DTO.CRTV_MunicipalBonds;

@Name("creditNoteElectHome")
public class CreditNoteElectHome extends EntityHome<ElectronicVoucher>
		implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long municipalBondNumber;
	private Resident resident;
//	private static final String PAID_BOND_STATUS_ID = "MUNICIPAL_BOND_STATUS_ID_PAID";
//	private static final String PAID_BOND_STATUS_ID_EXTERNAL_CHANNEL = "MUNICIPAL_BOND_STATUS_ID_PAID_FROM_EXTERNAL_CHANNEL";
	private InstitutionService institution;
	private List<ComplementVoucher> complements = new ArrayList<ComplementVoucher>();
	private ComplementVoucher complementVoucher;
	private ComplementVoucherType complementVoucherType;
	private List<Resident> residents;
	private String identificationNumber;
	private String criteria;
	private String sequenceName;
	private TypeEmissionPoint typeEmissionPoint;
	private String sequentialNumber;
	private ElectronicVoucher cancelVoucher;

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	private AditionalField field;
	
	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public Long getMunicipalBondNumber() {
		return municipalBondNumber;
	}

	public void setMunicipalBondNumber(Long municipalBondNumber) {
		this.municipalBondNumber = municipalBondNumber;
	}

	public InstitutionService getInstitution() {
		return institution;
	}

	public void setInstitution(InstitutionService institution) {
		this.institution = institution;
	}

	public List<ComplementVoucher> getComplements() {
		return complements;
	}

	public void setComplements(List<ComplementVoucher> complements) {
		this.complements = complements;
	}

	public ComplementVoucher getComplementVoucher() {
		return complementVoucher;
	}

	public void setComplementVoucher(ComplementVoucher complementVoucher) {
		this.complementVoucher = complementVoucher;
	}

	public ComplementVoucherType getComplementVoucherType() {
		return complementVoucherType;
	}

	public void setComplementVoucherType(
			ComplementVoucherType complementVoucherType) {
		this.complementVoucherType = complementVoucherType;
	}

	public ElectronicVoucher getCancelVoucher() {
		return cancelVoucher;
	}

	public void setCancelVoucher(ElectronicVoucher cancelVoucher) {
		cancelVoucher.setElectronicStatus(StatusElectronicReceipt.CANCEL.name());
		//System.out.println("=>"+cancelVoucher.getSequentialNumber());
		cancelVoucher.setActive(Boolean.FALSE);
		this.cancelVoucher = cancelVoucher;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}
	
	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getSequenceName() {
		return sequenceName;
	}

	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}

	public TypeEmissionPoint getTypeEmissionPoint() {
		return typeEmissionPoint;
	}

	public void setTypeEmissionPoint(TypeEmissionPoint typeEmissionPoint) {
		this.typeEmissionPoint = typeEmissionPoint;
	}

	public String getSequentialNumber() {
		return sequentialNumber;
	}

	public void setSequentialNumber(String sequentialNumber) {
		this.sequentialNumber = sequentialNumber;
	}
	
	public AditionalField getField() {
		return field;
	}

	public void setField(AditionalField field) {
		this.field = field;
	}

	/**** METHODS *****/
	public String  cancelVoucher() {
		//System.out.println("============>"+this.cancelVoucher.getAccessCode());
		getEntityManager().merge(this.cancelVoucher);
		getEntityManager().flush();
		return "/electronicvoucher/creditNote/CreditNoteList.xhtml";
	}

	public String addMunicipalBond() {

		if (municipalBondNumber != null) {
//			SystemParameterService systemParameterService = ServiceLocator
//					.getInstance().findResource(
//							SystemParameterService.LOCAL_NAME);
//			Long paidMunicipalBondStatusId = systemParameterService
//					.findParameter(PAID_BOND_STATUS_ID);
//			
//			Long paidExternalChannelMunicipalBondStatusId = systemParameterService
//					.findParameter(PAID_BOND_STATUS_ID_EXTERNAL_CHANNEL);
//
//			List<Long> municipalBondStatusIds = new ArrayList<Long>();
//			municipalBondStatusIds.add(paidMunicipalBondStatusId);
//			municipalBondStatusIds.add(paidExternalChannelMunicipalBondStatusId);

			String queryString = "SELECT e from MunicipalBond e "
					+ "WHERE e.number =:municipalBondNumber and e.municipalBondStatus.id IN (:municipalBondStatusIds) ";
			Query query = getEntityManager().createQuery(queryString);
			query.setParameter("municipalBondNumber", municipalBondNumber);
			query.setParameter("municipalBondStatusIds", this.mbStatus);
			try {
				MunicipalBond municipalBond = (MunicipalBond) query
						.getSingleResult();

				if (municipalBond != null) {

					if (municipalBond.getReceipt() != null) {
						addVoucher(municipalBond);
					} else {
						addFacesMessageFromResourceBundle(
								"creditNote.municipalBondIsInOtherCreditNote",
								municipalBondNumber, municipalBond
										.getCreditNote().getId());
					}
				}
				creditNoteControls(this.voucherSelected);
				this.municipalBondNumber = null;
			} catch (Exception e) {
				addFacesMessageFromResourceBundle(
						"creditNote.municipalBondDoesNotExistAsPaidForResident",
						municipalBondNumber);
			}
		} else {
			addFacesMessageFromResourceBundle("creditNote.enterMunicipalBondNumberFirst");
		}
		return null;
	}

	/**
	 *  Put Field in aditionalFields array
	 */
	public void putField(){  
		
		this.instance.getFields().add(this.field);
		//System.out.println("**=>"+this.field.getValue());
		//System.out.println("**=>"+this.field.getLabel());
	}
	
	/**
	 * Create new Adittional Field
	 */
	public void addField(){
		this.field = new AditionalField();
		this.field.setElectronicVoucher(this.instance);
	}
	
	
	private void addItems(List<Item> items) {
		joinTransaction();
		this.voucherSelected.setItems(new ArrayList<ElectronicItem>());
		BigDecimal total = BigDecimal.ZERO;
		for (Item item : items) 
		{
			
			// costo de procesamiento de datos, control para devolucion de rubros
//			if (item.getEntry().getId() == 444
//					|| item.getEntry().getId() == 448
//					|| item.getEntry().getId() == 543) {
//				continue;
//			}
			
			if(this.entries.contains(item.getEntry().getId())){
				continue;
			}
			
			
			ElectronicItem eItem = new ElectronicItem();
			eItem.setAmount(item.getAmount());
			eItem.setDiscountedBond(item.getDiscountedBond());
			eItem.setElectronicVoucher(this.voucherSelected);
			eItem.setEntry(item.getEntry());
			eItem.setIsTaxable(item.getIsTaxable());
			eItem.setItem(item);
			eItem.setObservations(item.getObservations());
			eItem.setOrderNumber(item.getOrderNumber());
			eItem.setSurchargedBond(item.getSurchargedBond());
			eItem.setTargetEntry(item.getTargetEntry());
			eItem.setTotal(item.getTotal());
			eItem.setValue(item.getValue());
			
			//por cada item se agrega un detalle adicional
			eItem.setDetails(new ArrayList<AditionalDetail>());
			AditionalDetail detail = new AditionalDetail();
			detail.setName("EntryCode");
			detail.setValue(item.getEntry().getCode());
			detail.setItem(eItem);
			eItem.getDetails().add(detail);
			total = total.add(item.getTotal());
			this.voucherSelected.getItems().add(eItem);
		}
		this.voucherSelected.setTotal(total);
	}

//	public void calculateRow(ElectronicItem item) {
//		if (item.getValue() != null) {
//			BigDecimal value = item.getValue();
//			BigDecimal total = value.multiply(item.getAmount());
//			item.setTotal(total);
//			calculateTotal();
//		}
//	}

//	private void calculateTotal() {
//		// guardar los valorse que son imponibles y no
//		BigDecimal total = BigDecimal.ZERO;
//		BigDecimal nonTaxable = BigDecimal.ZERO;
//		BigDecimal taxable = BigDecimal.ZERO;
//		BigDecimal taxesTotal = BigDecimal.ZERO;
//		List<ElectronicItem> items = this.voucherSelected.getItems();
//		for (ElectronicItem item : items) {
//			total = total.add(item.getTotal());
//			if (item.getIsTaxable()) {
//				taxable = taxable.add(item.getTotal());
//				BigDecimal tax = item.getTotal().multiply(
//						new BigDecimal("0.12"));
//				taxesTotal = taxesTotal.add(tax);
//			} else {
//				nonTaxable = nonTaxable.add(item.getTotal());
//			}
//		}
//
//		BigDecimal totalPaid = taxable.add(nonTaxable).add(taxesTotal); 
//		this.voucherSelected.setTaxableTotal(taxable);
//		this.voucherSelected.setNonTaxableTotal(nonTaxable);
//		this.voucherSelected.setTotalTaxes(taxesTotal);
//		this.voucherSelected.setTotal(total);
//		this.voucherSelected.setTotalPaid(totalPaid);
//		//System.out.println("==========>"+total);
//
//	}

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
		} catch (Exception e) {
			this.getInstance().setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void setType() {
		if (this.complementVoucherType != null) {
			this.instance.setDocumentType(this.complementVoucherType.getCode());
		}
	}

	/**
	 * 
	 */
	private void init() {
		this.instance.setAccessCode("0000000000000000000000000000000000000000000000000");
		this.instance.setAuthorizationCode("0000000000000000000000000000000000000000000000000");
		Date now = Calendar.getInstance().getTime();
		this.instance.setCreationDate(now);
		this.instance.setCreationTime(now);
		this.instance.setEmissionDate(now);
		this.instance.setEmisionTime(now);
		this.instance.setDocumentNumber("000-000-123456789");
		Person emitter = userSession.getPerson();
		this.instance.setEmitter(emitter);
		this.instance.setElectronicStatus(StatusElectronicReceipt.PENDING.name());
		checkUserCanEmitt();

	}

	/**
	 * Verifica que el usuario pueda emitir notas de credito
	 **/
	@SuppressWarnings("unchecked")
	public void checkUserCanEmitt() { 
		EntityManager em = getEntityManager();
		Person person = userSession.getPerson();
		Query q = em.createNamedQuery("TypeEmissionPoint.findByPersonAndType");
		q.setParameter("person", person);
		q.setParameter("code", "04");
		List<TypeEmissionPoint> typesByEmissionPoint = q.getResultList();
		if (typesByEmissionPoint.size() > 0) {
			this.typeEmissionPoint = typesByEmissionPoint.get(0);
			verifySequenceExist();
		} else {
			System.out
					.println("La empresa no tienen el tipo de comprobante relacionado");
			return;
		}
	}

	public void verifySequenceExist() {
		if (this.typeEmissionPoint != null) {
			// sequence_name = seq_elec_institutionId_typeemissionPointID;
			InstitutionService institution = this.typeEmissionPoint
					.getComplementVoucher().getInstitutionService();
			this.sequenceName = "seq_elec_" + institution.getId() + "_"
					+ this.typeEmissionPoint.getId();
			//System.out.println("SEQ CRE: " + this.sequenceName);
			IncomeService incomeService = ServiceLocator.getInstance()
					.findResource(IncomeService.LOCAL_NAME);
			incomeService.createSequenceComplementVoucher(this.sequenceName);
		}
	}

	@SuppressWarnings("unchecked")
	public List<MunicipalBond> searchVoucher(Object suggest) {
		String suggestion = ((String) suggest);
		String queryString = "SELECT e from MunicipalBond e "
				+ "WHERE e.number =:number";
		Query query = getEntityManager().createQuery(queryString);
		Long number = Long.valueOf(suggestion.toString());
		query.setParameter("number", number);
		return (List<MunicipalBond>) query.getResultList();
	}

	public void addVoucher(MunicipalBond municipalBond) {
		// buscar los datos si es una factura u otro tipo de comprobante

		//System.out.println("=>" + municipalBond.getReceipt());
		if (municipalBond.getReceipt() == null) {
			// error
			System.out.println("ERROR");
		} else {
			 Receipt receipt = municipalBond.getReceipt();
			// tabla 4 del sri de tipos de comprobantes
			//Integer type = receipt.getReceiptType().getCode();

//			switch (type) {
//			case 1: // factura
//				this.voucherSelected.setDocumentType("01");
//				this.voucherSelected.setDocumentModify("FACTURA");
//				break;
//			case 2: // nota de credito
				this.voucherSelected.setDocumentType("04");
				this.voucherSelected.setDocumentModify("NOTAS DE CREDITO");
//				break;
//			case 3: // nota de debito
//				this.voucherSelected.setDocumentType("05");
//				this.voucherSelected.setDocumentModify("NOTAS DE DEBITO");
//				break;
//			case 4: // guia de remision
//				this.voucherSelected.setDocumentType("06");
//				this.voucherSelected.setDocumentModify("GUÍA DE REMISION");
//				break;
//			case 5: // comprobante de retencion
//				this.voucherSelected.setDocumentType("07");
//				this.voucherSelected.setDocumentModify("COMPROBANTE DE RETENCION");
//				break;
//			}

			this.voucherSelected.setDocumentNumber(receipt.toString());
			this.voucherSelected.setDocumentDate(receipt.getDate());
			this.voucherSelected.setResident(municipalBond.getResident());
			this.voucherSelected.setMunicipalBond(municipalBond);
			addItems(municipalBond.getItems());
			this.voucherSelected.setTotal(this.voucherSelected.getTotal().add(municipalBond.getInterest().add(municipalBond.getSurcharge())));
			
			Query query = getEntityManager().createNamedQuery("TaxItem.findTaxItemByMunicipalBondId");
			query.setParameter("municipalBondId", municipalBond.getId());
			try {
				TaxItem taxItem = (TaxItem) query
						.getSingleResult();

				if (taxItem != null) {
					this.voucherSelected.setTotalTaxes(taxItem.getValue());
					this.voucherSelected.setTaxableTotal(municipalBond.getTaxableTotal());
					this.voucherSelected.setNonTaxableTotal(municipalBond.getNonTaxableTotal());
					this.voucherSelected.setTotalPaid(this.voucherSelected.getTotal().add(this.voucherSelected.getTotalTaxes()));
				}
			} catch (Exception e) {
				addFacesMessageFromResourceBundle(
						"No existe registro de impuestos",
						municipalBondNumber);
			}
		}
	}

//	public String persist() {
//		// validar si el numero de documento es valido
//		if (this.instance.getDocumentNumber().length() > 17) {
//			addFacesMessageFromResourceBundle("electronicVoucher.docNumberFormat");
//			return null;
//		}
//
//		// generar la secuencia
//		IncomeService incomeService = ServiceLocator.getInstance()
//				.findResource(IncomeService.LOCAL_NAME);
//		Long number;
//		try {
//			number = incomeService.generateNextValue(this.sequenceName);
//			String format = formatNumber(number);
//			// generar el numero secuencial
//			ComplementVoucher complement = this.typeEmissionPoint
//					.getComplementVoucher();
//			this.sequentialNumber = complement.getInstitutionNumber() + "-"
//					+ complement.getEmisionPointNumber() + "-" + format;
//
//			//System.out.println("SECUENCIAL====>" + this.sequentialNumber);
//
//			this.instance.setSequentialNumber(this.sequentialNumber);
//			// actualizar el valor actual del contador del voucher
//			// complementario
//
//			this.instance.setTypeEmissionPoint(this.typeEmissionPoint);
//			this.instance.setVoucherNumber(number);
//			this.typeEmissionPoint.setCurrentValue(number); 
//			getEntityManager().merge(this.typeEmissionPoint); 
//			super.persist(); 
//			return "persisted";
//		} catch (InvoiceNumberOutOfRangeException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
	
	public String prePrint(Long id) {
		setCreditNoteId(id);
		getInstance();
		return "/electronicvoucher/report/CreditNoteReport.xhtml";
	}


	/**
	 * Formar el numero de 9 digitos
	 * 
	 * @param number
	 * @return
	 */
	private String formatNumber(Long number) {
		String quantity = "" + number;
		String sequence = "";
		for (int i = quantity.length(); i < 9; i++) {
			sequence += "0";
		}
		sequence += number;
		return sequence;
	}

	public boolean isWired() {
		return true;
	}

	public ElectronicVoucher getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void setCreditNoteId(Long id) {
		setId(id);
	}

	public Long getCreditNoteId() {
		return (Long) getId();
	}
	
	private Boolean isFirstTime = Boolean.TRUE;

	public void wire() {
//		getInstance();
//		if (getInstance().getId() == null) {
//			init();
//		}
		if(isFirstTime){
			chargeValues();
			loadCharge();
			this.vouchersToPrint = new ArrayList();
			isFirstTime = Boolean.FALSE;
		}
	}
	
	public boolean isEnabled(ElectronicVoucher voucher){
		return (voucher.getElectronicStatus().equals(StatusElectronicReceipt.AUTHORIZED.name()) 
				|| voucher.getElectronicStatus().equals(StatusElectronicReceipt.CANCEL.name()))? false : true;
	}
	
	public boolean canCancel(ElectronicVoucher voucher) {
		return (!voucher.getElectronicStatus().equals("CANCEL")) ? true : false;
	}
	
	// para crear notas de credito electrónicas en bloque
	// Jock Samaniego M.
	
	private List<ElectronicVoucher> creditNotes = new ArrayList();
	private ElectronicVoucher voucherSelected;
	private Date emissionDate = new Date();
	private String emissionReason;
	private FiscalPeriod fiscalPeriod;
	private String monthFiscal;

	public List<ElectronicVoucher> getCreditNotes() {
		return creditNotes;
	}

	public void setCreditNotes(List<ElectronicVoucher> creditNotes) {
		this.creditNotes = creditNotes;
	}
	
	public ElectronicVoucher getVoucherSelected() {
		return voucherSelected;
	}

	public void setVoucherSelected(ElectronicVoucher voucherSelected) {
		this.voucherSelected = voucherSelected;
	}

	public Date getEmissionDate() {
		return emissionDate;
	}

	public void setEmissionDate(Date emissionDate) {
		this.emissionDate = emissionDate;
	}

	public String getEmissionReason() {
		return emissionReason;
	}

	public void setEmissionReason(String emissionReason) {
		this.emissionReason = emissionReason;
	}

	public FiscalPeriod getFiscalPeriod() {
		return fiscalPeriod;
	}

	public void setFiscalPeriod(FiscalPeriod fiscalPeriod) {
		this.fiscalPeriod = fiscalPeriod;
	}

	public String getMonthFiscal() {
		return monthFiscal;
	}

	public void setMonthFiscal(String monthFiscal) {
		this.monthFiscal = monthFiscal;
	}
	
	

	public void addCreditNote(){
		if(!creditNotes.contains(this.voucherSelected)){
			creditNotes.add(this.voucherSelected);
		}
	}
	
	public void removeCreditNote(ElectronicVoucher voucher){
		if(creditNotes.contains(voucher)){
			creditNotes.remove(voucher);
		}
	}
	
	public void createCreditNote(){
		this.voucherSelected = new ElectronicVoucher();
		this.voucherSelected.setAccessCode("0000000000000000000000000000000000000000000000000");
		this.voucherSelected.setAuthorizationCode("0000000000000000000000000000000000000000000000000");
		Date now = Calendar.getInstance().getTime();
		this.voucherSelected.setCreationDate(now);
		this.voucherSelected.setCreationTime(now);
		this.voucherSelected.setEmissionDate(now);
		this.voucherSelected.setEmisionTime(now);
		this.voucherSelected.setTotal(BigDecimal.ZERO);
		this.voucherSelected.setActive(Boolean.TRUE);
		// this.voucherSelected.setDocumentNumber("000-000-123456789");
		Person emitter = userSession.getPerson();
		this.voucherSelected.setEmitter(emitter);
		this.voucherSelected.setElectronicStatus(StatusElectronicReceipt.PENDING.name());
		checkUserCanEmitt();
		addMunicipalBond();
	}
	
	public void creditNoteControls(ElectronicVoucher voucher){
		if (voucher.getDocumentNumber().length() > 17) {
			addFacesMessageFromResourceBundle("electronicVoucher.docNumberFormat");
		}else{
			List<ElectronicVoucher>	eVauchers = new ArrayList();
			Query query = getEntityManager().createNamedQuery("ElectronicVoucher.findCreditNote");
			query.setParameter("mbNumber", voucher.getMunicipalBond().getNumber());
			eVauchers = query.getResultList();
			if(eVauchers.size() > 0){
				addFacesMessageFromResourceBundle("ya existe una nota de crédito para la obligación");
			}else{
				Boolean isContains = Boolean.FALSE;
				for(ElectronicVoucher ev : creditNotes){
					if(ev.getMunicipalBond().getNumber() == voucher.getMunicipalBond().getNumber()){
						isContains = Boolean.TRUE;
						break;
					}
				}
				if(isContains){
					addFacesMessageFromResourceBundle("ya agregó la obligación seleccionada");
				}else{
					addCreditNote();
				}
			}
		}
	}
	
	public void resetValues(){
		creditNotes = new ArrayList();
		voucherSelected = null;
		emissionDate = new Date();
		emissionReason = null;
		fiscalPeriod = null;
		monthFiscal = null;
	}
	
	public String saveElectronicCreditNotes(){
		joinTransaction();
		for(ElectronicVoucher ev : creditNotes){
			ElectronicVoucher forSave = generateSequence(ev);
			if(forSave != null){
				forSave.setReason(this.emissionReason);
				forSave.setEmissionDate(this.emissionDate);
				forSave.setFiscalPeriod(fiscalPeriod);
				forSave.setMonthFiscal(monthFiscal);
				this.setInstance(forSave);
				super.persist();
			}
		}
		resetValues();
		return "persisted";
	}
	
	public ElectronicVoucher generateSequence(ElectronicVoucher voucher){
		// generar la secuencia
				IncomeService incomeService = ServiceLocator.getInstance()
						.findResource(IncomeService.LOCAL_NAME);
				Long number;
				try {
					number = incomeService.generateNextValue(this.sequenceName);
					String format = formatNumber(number);
					// generar el numero secuencial
					ComplementVoucher complement = this.typeEmissionPoint
							.getComplementVoucher();
					this.sequentialNumber = complement.getInstitutionNumber() + "-"
							+ complement.getEmisionPointNumber() + "-" + format;

					//System.out.println("SECUENCIAL====>" + this.sequentialNumber);

					voucher.setSequentialNumber(this.sequentialNumber);
					// actualizar el valor actual del contador del voucher
					// complementario

					voucher.setTypeEmissionPoint(this.typeEmissionPoint);
					voucher.setVoucherNumber(number);
					this.typeEmissionPoint.setCurrentValue(number); 
					getEntityManager().merge(this.typeEmissionPoint); 
					return voucher;
				} catch (InvoiceNumberOutOfRangeException e) {
					e.printStackTrace();
					return null;
				}
				
	}
	
	private List<Long> entries;
	private List<Long> mbStatus;
	
	public void chargeValues(){
		entries = new ArrayList();
		mbStatus = new ArrayList();
		Query query = getEntityManager().createNamedQuery("SystemParameter.findByName");
		query.setParameter("name", "MB_ENTRIES_ELECTRONIC_CREDIT_NOTE");
		SystemParameter controlEntries = (SystemParameter) query.getSingleResult();
		String[] entriesStr = controlEntries.getValue().trim().split(",");
		for(String st : entriesStr){
			entries.add(Long.parseLong(st));
		}
		
		Query query2 = getEntityManager().createNamedQuery("SystemParameter.findByName");
		query2.setParameter("name", "MB_STATUS_ELECTRONIC_CREDIT_NOTE");
		SystemParameter controlStatus = (SystemParameter) query2.getSingleResult();
		String[] statusStr = controlStatus.getValue().trim().split(",");
		for(String stt : statusStr){
			mbStatus.add(Long.parseLong(stt));
		}
	}
	
	//para impresion
	
	private List<ElectronicVoucher> vouchersToPrint = new ArrayList();

	public List<ElectronicVoucher> getVouchersToPrint() {
		return vouchersToPrint;
	}

	public void setVouchersToPrint(List<ElectronicVoucher> vouchersToPrint) {
		this.vouchersToPrint = vouchersToPrint;
	}
	
	@In(create = true)
	CreditNoteElectList creditNoteElectList;
	
	
	public void checkVoucherToPrint(ElectronicVoucher voucher){
		dateReportFrom = creditNoteElectList.getEmissionDateFrom();
		dateReportUntil = creditNoteElectList.getEmissionDateUntil();
		totalValueForReport = BigDecimal.ZERO;
		totalBaseForReport = BigDecimal.ZERO;
		totalIvaForReport = BigDecimal.ZERO;
		if(!vouchersToPrint.contains(voucher)){
			voucher.setSelectToPrint(Boolean.TRUE);
			vouchersToPrint.add(voucher);
		}else{
			voucher.setSelectToPrint(Boolean.FALSE);
			vouchersToPrint.remove(voucher);
		}
		for(ElectronicVoucher ev : vouchersToPrint){
			if(ev.getElectronicStatus().equals("AUTHORIZED")){
				totalValueForReport = totalValueForReport.add(ev.getTotalPaid());
				totalBaseForReport = totalBaseForReport.add(ev.getMunicipalBond().getTaxableTotal());
				totalIvaForReport = totalIvaForReport.add(ev.getMunicipalBond().getTaxItems().get(0).getValue());
			}
		}
	}
	
	// charge and delegate
	
	private Charge incomeCharge;
	private Delegate incomeDelegate;

	public Charge getIncomeCharge() {
		return incomeCharge;
	}

	public void setIncomeCharge(Charge incomeCharge) {
		this.incomeCharge = incomeCharge;
	}

	public Delegate getIncomeDelegate() {
		return incomeDelegate;
	}

	public void setIncomeDelegate(Delegate incomeDelegate) {
		this.incomeDelegate = incomeDelegate;
	}
	
	
	public void loadCharge() {
		incomeCharge = getCharge("DELEGATE_ID_INCOME");
		if (incomeCharge != null) {
			for (Delegate d : incomeCharge.getDelegates()) {
				if (d.getIsActive())
					incomeDelegate = d;
			}
		}
	}

	private Charge getCharge(String systemParameter) {
		SystemParameterService systemParameterService = ServiceLocator.getInstance().findResource(
					SystemParameterService.LOCAL_NAME);
		Charge charge = systemParameterService.materialize(Charge.class,
				systemParameter);
		return charge;
	}
	
	public String prePrintAll() {
		if(this.vouchersToPrint.size() > 0){
			return "/electronicvoucher/report/CreditNoteTotalReport.xhtml";
		}
		return null;	
	}
	
	public String prePrintReport() {
		if(this.vouchersToPrint.size() > 0){
			return "/electronicvoucher/report/CreditNoteReportToAccounting.xhtml";
		}
		return null;	
	}
	
	private List<Long> mbIds = new ArrayList();
	private List<Resident> resByCreditNote = new ArrayList();
	private List<CreditNoteElectDTO> cneByCreditNote = new ArrayList();

	public List<Resident> getResByCreditNote() {
		return resByCreditNote;
	}

	public void setResByCreditNote(List<Resident> resByCreditNote) {
		this.resByCreditNote = resByCreditNote;
	}

	public List<CreditNoteElectDTO> getCneByCreditNote() {
		return cneByCreditNote;
	}

	public void setCneByCreditNote(List<CreditNoteElectDTO> cneByCreditNote) {
		this.cneByCreditNote = cneByCreditNote;
	}

	public String prePrintTotalByResident() {
		this.resByCreditNote = new ArrayList();
		// List<BigInteger> resIdAux = new ArrayList();
		if(this.vouchersToPrint.size() > 0){
			mbIds = new ArrayList();
			for(ElectronicVoucher ev : this.vouchersToPrint){
				mbIds.add(ev.getMunicipalBond().getId());
			}
			
			Query q = getEntityManager().createNamedQuery("Resident.findByCreditNoteElect");
			q.setParameter("mbIds", mbIds);
			this.resByCreditNote = q.getResultList();

			return "/electronicvoucher/report/CreditNoteTotalReportByResident.xhtml";
		}
		return null;	
	}
	
	
	public String prePrintTotalByCreditNote() {
		this.cneByCreditNote = new ArrayList();
		// List<BigInteger> resIdAux = new ArrayList();
		if(this.vouchersToPrint.size() > 0){
			mbIds = new ArrayList();
			for(ElectronicVoucher ev : this.vouchersToPrint){
				if(ev.getMunicipalBond().getCreditNote() != null){
					mbIds.add(ev.getMunicipalBond().getId());
				}
			}
			
			Query q = getEntityManager().createNamedQuery("Resident.findByCreditNoteElectAndCreditNote");
			q.setParameter("mbIds", mbIds);
			this.cneByCreditNote = NativeQueryResultsMapper.map(q.getResultList(), CreditNoteElectDTO.class);

			return "/electronicvoucher/report/CreditNoteTotalReportByCreditNote.xhtml";
		}
		return null;	
	}
	
	private List<ElectronicVoucher> evsByResident = new ArrayList();
	
	public List<ElectronicVoucher> prePrintByResident(Resident res){
		evsByResident = new ArrayList<ElectronicVoucher>();
		Query q = getEntityManager().createNamedQuery("ElectronicVoucher.findCreditNoteElectByResident");
		q.setParameter("resId", res.getId());
		q.setParameter("mbIds", mbIds);
		evsByResident = q.getResultList();
		return evsByResident;
	}
	
	public List<ElectronicVoucher> prePrintByCreditNote(CreditNoteElectDTO dto){
		evsByResident = new ArrayList<ElectronicVoucher>();
		Query q = getEntityManager().createNamedQuery("ElectronicVoucher.findCreditNoteElectByCreditNote");
		q.setParameter("resId", dto.getResident().getId());
		q.setParameter("cnId", dto.getCreditNote().getId());
		q.setParameter("mbIds", mbIds);
		evsByResident = q.getResultList();
		return evsByResident;
	}
	
	public BigDecimal sumTotalCreditNote(){
		BigDecimal total = BigDecimal.ZERO;
		for(ElectronicVoucher ev : evsByResident){
			total = total.add(ev.getTotalPaid());
		}
		return total;
	}
	
	public String findEmitterCreditNote(){
		return evsByResident.get(0).getEmitter().getName();
	}
	
	public String findCreditNoteNumber(){
		return evsByResident.get(0).getMunicipalBond().getCreditNote().getId().toString();
	}
	
	// Para crear desde el CreditNoteHome
	// Jock Samaniego
	
	private List<MunicipalBond> mbsForCreditNotes;

	public List<MunicipalBond> getMbsForCreditNotes() {
		return mbsForCreditNotes;
	}

	public void setMbsForCreditNotes(List<MunicipalBond> mbsForCreditNotes) {
		this.mbsForCreditNotes = mbsForCreditNotes;
	}
	
	
	public void createFromCreditNoteHome(){
		for(MunicipalBond mb : mbsForCreditNotes){
			this.setMunicipalBondNumber(mb.getNumber());
			createCreditNote();
		}
		saveElectronicCreditNotes();
	}
	
	
	public void creditNoteAllSelect(List<BigInteger> ids){
		totalValueForReport = BigDecimal.ZERO;
		totalBaseForReport = BigDecimal.ZERO;
		totalIvaForReport = BigDecimal.ZERO;
		this.vouchersToPrint = new ArrayList<ElectronicVoucher>();
		for(BigInteger id : ids){
			ElectronicVoucher ev = getEntityManager().find(ElectronicVoucher.class, id.longValue());
			if(ev.getElectronicStatus().equals("AUTHORIZED")){
				ev.setSelectToPrint(Boolean.TRUE);
				this.vouchersToPrint.add(ev);
				totalValueForReport = totalValueForReport.add(ev.getTotalPaid());
				totalBaseForReport = totalBaseForReport.add(ev.getMunicipalBond().getTaxableTotal());
				totalIvaForReport = totalIvaForReport.add(ev.getMunicipalBond().getTaxItems().get(0).getValue());
			}
		}
	}
	
	public void deselectAllCreditNotes(){
		for(ElectronicVoucher ev : this.vouchersToPrint){
			ev.setSelectToPrint(Boolean.FALSE);
		}
		this.vouchersToPrint = new ArrayList<ElectronicVoucher>();
	}
	
	private Date dateReportFrom;
	private Date dateReportUntil;
	private BigDecimal totalValueForReport;
	private BigDecimal totalBaseForReport;
	private BigDecimal totalIvaForReport;

	public Date getDateReportFrom() {
		return dateReportFrom;
	}

	public void setDateReportFrom(Date dateReportFrom) {
		this.dateReportFrom = dateReportFrom;
	}

	public Date getDateReportUntil() {
		return dateReportUntil;
	}

	public void setDateReportUntil(Date dateReportUntil) {
		this.dateReportUntil = dateReportUntil;
	}

	public BigDecimal getTotalValueForReport() {
		return totalValueForReport;
	}

	public void setTotalValueForReport(BigDecimal totalValueForReport) {
		this.totalValueForReport = totalValueForReport;
	}

	public BigDecimal getTotalBaseForReport() {
		return totalBaseForReport;
	}

	public void setTotalBaseForReport(BigDecimal totalBaseForReport) {
		this.totalBaseForReport = totalBaseForReport;
	}

	public BigDecimal getTotalIvaForReport() {
		return totalIvaForReport;
	}

	public void setTotalIvaForReport(BigDecimal totalIvaForReport) {
		this.totalIvaForReport = totalIvaForReport;
	}
	
}