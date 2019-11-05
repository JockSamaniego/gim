package org.gob.gim.electronicvoucher.creditnote.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.exception.InvoiceNumberOutOfRangeException;
import org.gob.gim.income.facade.IncomeService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
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
import ec.gob.gim.revenue.model.Item;
import ec.gob.gim.revenue.model.MunicipalBond;
//import org.joda.time.Days;

@Name("creditNoteElectHome")
@Scope(ScopeType.CONVERSATION)
public class CreditNoteElectHome extends EntityHome<ElectronicVoucher>
		implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long municipalBondNumber;
	private Resident resident;
	private static final String PAID_BOND_STATUS_ID = "MUNICIPAL_BOND_STATUS_ID_PAID";
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
			SystemParameterService systemParameterService = ServiceLocator
					.getInstance().findResource(
							SystemParameterService.LOCAL_NAME);
			Long paidMunicipalBondStatusId = systemParameterService
					.findParameter(PAID_BOND_STATUS_ID);

			List<Long> municipalBondStatusIds = new ArrayList<Long>();
			municipalBondStatusIds.add(paidMunicipalBondStatusId);

			String queryString = "SELECT e from MunicipalBond e "
					+ "WHERE e.number =:municipalBondNumber and e.municipalBondStatus.id IN (:municipalBondStatusIds) ";
			Query query = getEntityManager().createQuery(queryString);
			query.setParameter("municipalBondNumber", municipalBondNumber);
			query.setParameter("municipalBondStatusIds", municipalBondStatusIds);
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
		getInstance().setItems(new ArrayList<ElectronicItem>());
		for (Item item : items) 
		{
		
			// costo de procesamiento de datos
			if (item.getEntry().getId() == 444
					|| item.getEntry().getId() == 448
					|| item.getEntry().getId() == 543) {
				continue;
			}
			
			ElectronicItem eItem = new ElectronicItem();
			eItem.setAmount(item.getAmount());
			eItem.setDiscountedBond(item.getDiscountedBond());
			eItem.setElectronicVoucher(this.instance);
			eItem.setEntry(item.getEntry());
			eItem.setIsTaxable(item.getIsTaxable());
			eItem.setItem(item);
			eItem.setObservations(item.getObservations());
			eItem.setOrderNumber(item.getOrderNumber());
			eItem.setSurchargedBond(item.getSurchargedBond());
			eItem.setTargetEntry(item.getTargetEntry());
			eItem.setTotal(BigDecimal.ZERO);
			eItem.setValue(BigDecimal.ZERO);
			
			//por cada item se agrega un detalle adicional
			eItem.setDetails(new ArrayList<AditionalDetail>());
			AditionalDetail detail = new AditionalDetail();
			detail.setName("EntryCode");
			detail.setValue(item.getEntry().getCode());
			detail.setItem(eItem);
			eItem.getDetails().add(detail);
			getInstance().getItems().add(eItem);	
		}
	}

	public void calculateRow(ElectronicItem item) {
		if (item.getValue() != null) {
			BigDecimal value = item.getValue();
			BigDecimal total = value.multiply(item.getAmount());
			item.setTotal(total);
			calculateTotal();
		}
	}

	private void calculateTotal() {
		// guardar los valorse que son imponibles y no
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal nonTaxable = BigDecimal.ZERO;
		BigDecimal taxable = BigDecimal.ZERO;
		BigDecimal taxesTotal = BigDecimal.ZERO;
		List<ElectronicItem> items = this.instance.getItems();
		for (ElectronicItem item : items) {
			total = total.add(item.getTotal());
			if (item.getIsTaxable()) {
				taxable = taxable.add(item.getTotal());
				BigDecimal tax = item.getTotal().multiply(
						new BigDecimal("0.12"));
				taxesTotal = taxesTotal.add(tax);
			} else {
				nonTaxable = nonTaxable.add(item.getTotal());
			}
		}

		BigDecimal totalPaid = taxable.add(nonTaxable).add(taxesTotal); 
		this.instance.setTaxableTotal(taxable);
		this.instance.setNonTaxableTotal(nonTaxable);
		this.instance.setTotalTaxes(taxesTotal);
		this.instance.setTotal(total);
		this.instance.setTotalPaid(totalPaid);
		//System.out.println("==========>"+total);

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
			Integer type = receipt.getReceiptType().getCode();
			switch (type) {
			case 1: // factura
				this.instance.setDocumentType("01");
				this.instance.setDocumentModify("FACTURA");
				break;
			case 2: // nota de credito
				this.instance.setDocumentType("04");
				this.instance.setDocumentModify("NOTAS DE CREDITO");
				break;
			case 3: // nota de debito
				this.instance.setDocumentType("05");
				this.instance.setDocumentModify("NOTAS DE DEBITO");
				break;
			case 4: // guia de remision
				this.instance.setDocumentType("06");
				this.instance.setDocumentModify("GUÍA DE REMISION");
				break;
			case 5: // comprobante de retencion
				this.instance.setDocumentType("07");
				this.instance.setDocumentModify("COMPROBANTE DE RETENCION");
				break;
			}

			this.instance.setDocumentNumber(receipt.toString());
			this.instance.setDocumentDate(receipt.getDate());
			this.instance.setResident(municipalBond.getResident());
			addItems(municipalBond.getItems());
		}
	}

	public String persist() {
		// validar si el numero de documento es valido
		if (this.instance.getDocumentNumber().length() > 17) {
			addFacesMessageFromResourceBundle("electronicVoucher.docNumberFormat");
			return null;
		}

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

			this.instance.setSequentialNumber(this.sequentialNumber);
			// actualizar el valor actual del contador del voucher
			// complementario

			this.instance.setTypeEmissionPoint(this.typeEmissionPoint);
			this.instance.setVoucherNumber(number);
			this.typeEmissionPoint.setCurrentValue(number); 
			getEntityManager().merge(this.typeEmissionPoint); 
			super.persist(); 
			return "persisted";
		} catch (InvoiceNumberOutOfRangeException e) {
			e.printStackTrace();
			return null;
		}
	}
	
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

	public void wire() {
		getInstance();
		if (getInstance().getId() == null) {
			init();
		}
	}
	
	public boolean isEnabled(ElectronicVoucher voucher){
		return (voucher.getElectronicStatus().equals(StatusElectronicReceipt.AUTHORIZED.name()) 
				|| voucher.getElectronicStatus().equals(StatusElectronicReceipt.CANCEL.name()))? false : true;
	}
	
	public boolean canCancel(ElectronicVoucher voucher) {
		return (!voucher.getElectronicStatus().equals("CANCEL")) ? true : false;
	}
}
