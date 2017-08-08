package org.gob.gim.electronicvoucher.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
//import javax.persistence.TypedQuery;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.exception.InvoiceNumberOutOfRangeException;
import org.gob.gim.income.facade.IncomeService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.complementvoucher.model.AditionalField;
import ec.gob.gim.complementvoucher.model.ComplementVoucher;
import ec.gob.gim.complementvoucher.model.ComplementVoucherType;
import ec.gob.gim.complementvoucher.model.DetailReport;
import ec.gob.gim.complementvoucher.model.ElectronicVoucher;
import ec.gob.gim.complementvoucher.model.ElectronicVoucherDetail;
import ec.gob.gim.complementvoucher.model.InstitutionService;
import ec.gob.gim.complementvoucher.model.Provider;
import ec.gob.gim.complementvoucher.model.RetentionCode;
import ec.gob.gim.complementvoucher.model.RetentionTax;
import ec.gob.gim.complementvoucher.model.TypeEmissionPoint;
import ec.gob.gim.income.model.StatusElectronicReceipt;

@Name("electronicVoucherHome")
@Scope(ScopeType.CONVERSATION)
public class ElectronicVoucherHome extends EntityHome<ElectronicVoucher>implements Serializable {

	private static final long serialVersionUID = 1L;

	Log logger;

	private List<Resident> residents;

	private String identificationNumber;

	private String criteria;

	private InstitutionService institution;

	private List<ComplementVoucher> complements = new ArrayList<ComplementVoucher>();

	private String sequentialNumber;

	private String sequenceName;

	private ComplementVoucher complementVoucher;

	private TypeEmissionPoint typeEmissionPoint;

	private ComplementVoucherType complementVoucherType;

	private ElectronicVoucherDetail detail;

	private List<RetentionCode> codeList;

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	private ElectronicVoucher voucher;

	private AditionalField field;

	private ElectronicVoucher cancelVoucher;

	private Boolean enableProcessNumber = false;

	/**** Para reportes de comprobantes de retencion ****/
	private Date startDate;
	private Date endDate;
	private List<DetailReport> detailsReport;
	private BigDecimal totalIVA;
	private BigDecimal totalISD;
	private BigDecimal totalRenta;
	private String start;
	private String end;
	private String emissionPoint = "";
	private String numberPoint;
	private String nameEmitter;

	/***
	 * Obtener el punto de emision de comprobantes de retencion de acuerdo al
	 * usuario
	 */
	public void getUserData() {
		System.out.println(userSession.getPerson());
		System.out.println(userSession.getPerson().getName());
		if (userSession != null) {
			Person person = userSession.getPerson();
			if (person != null) {
				EntityManager em = getEntityManager();
				Query q = em.createNamedQuery("TypeEmissionPoint.findByPersonAndType");
				q.setParameter("person", person);
				q.setParameter("code", "07");
				@SuppressWarnings("unchecked")
				List<TypeEmissionPoint> typesByEmissionPoint = q.getResultList();
				if (!typesByEmissionPoint.isEmpty()) {
					TypeEmissionPoint point = typesByEmissionPoint.get(0);
					emissionPoint = point.getComplementVoucher().getEmisionPointNumber();
					numberPoint = point.getComplementVoucher().getInstitutionNumber() + "-"
							+ point.getComplementVoucher().getEmisionPointNumber();
					nameEmitter = point.getComplementVoucher().getPerson().getName();
				}
			}
		}
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	public void setElectronicRetentionId(Long id) {
		setId(id);
	}

	public Long getElectronicRetentionId() {
		return (Long) getId();
	}

	public void loadVoucher(ElectronicVoucher voucher) {
		this.voucher = voucher;
	}

	public String prePrint(Long id) {
		setElectronicRetentionId(id);
		getInstance();
		return "/electronicvoucher/report/ElectronicRetentionReport.xhtml";
	}

	public ElectronicVoucher getVoucher() {
		return voucher;
	}

	public void setVoucher(ElectronicVoucher voucher) {
		this.voucher = voucher;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<DetailReport> getDetailsReport() {
		return detailsReport;
	}

	public void setDetailsReport(List<DetailReport> detailsReport) {
		this.detailsReport = detailsReport;
	}

	public BigDecimal getTotalIVA() {
		return totalIVA;
	}

	public void setTotalIVA(BigDecimal totalIVA) {
		this.totalIVA = totalIVA;
	}

	public BigDecimal getTotalISD() {
		return totalISD;
	}

	public void setTotalISD(BigDecimal totalISD) {
		this.totalISD = totalISD;
	}

	public BigDecimal getTotalRenta() {
		return totalRenta;
	}

	public void setTotalRenta(BigDecimal totalRenta) {
		this.totalRenta = totalRenta;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public ElectronicVoucher getCancelVoucher() {
		return cancelVoucher;
	}

	public void setCancelVoucher(ElectronicVoucher cancelVoucher) {
		cancelVoucher.setElectronicStatus(StatusElectronicReceipt.CANCEL.name());
		this.cancelVoucher = cancelVoucher;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}
 
	@SuppressWarnings("unchecked")
	public void wire() {
		getInstance();
		this.enableProcessNumber = false;
		if (this.instance.getId() == null) {
			init();
		}else{
			if(this.instance.getDocumentType()!=null && !this.instance.getDocumentType().isEmpty()){
				//buscar el complementvouchertype por el id
				String code = this.instance.getDocumentType();
				
				EntityManager em = getEntityManager();
				Query q = em.createNamedQuery("ComplementVoucherType.findByCode");
				q.setParameter("code", code);
				List<ComplementVoucherType> types = q.getResultList();
				System.out.println("==========================>"+code+" "+types.size());
				if(!types.isEmpty()){
					this.complementVoucherType = types.get(0);
				} 
			}
		}

	}

	public InstitutionService getInstitution() {
		return institution;
	}

	public void setInstitution(InstitutionService institution) {
		this.institution = institution;
	}

	public ComplementVoucher getComplementVoucher() {
		return complementVoucher;
	}

	public void setComplementVoucher(ComplementVoucher complementVoucher) {
		this.complementVoucher = complementVoucher;
	}

	public TypeEmissionPoint getTypeEmissionPoint() {
		return typeEmissionPoint;
	}

	public void setTypeEmissionPoint(TypeEmissionPoint typeEmissionPoint) {
		this.typeEmissionPoint = typeEmissionPoint;
	}

	public AditionalField getField() {
		return field;
	}

	public void setField(AditionalField field) {
		this.field = field;
	}

	public String getSequenceName() {
		return sequenceName;
	}

	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}

	@Override
	public String persist() {
		// validar si el numero de documento es valido
		if (this.instance.getDocumentNumber().length() > 17) {
			addFacesMessageFromResourceBundle("electronicVoucher.docNumberFormat");
			return null;
		}

		if (this.instance.getProvider() == null) {
			addFacesMessageFromResourceBundle("provider.error");
			return null;
		}

		IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
		Long number;
		try {
			number = incomeService.generateNextValue(this.sequenceName);
			String format = formatNumber(number);
			// generar el numero secuencial
			ComplementVoucher complement = this.typeEmissionPoint.getComplementVoucher();
			this.sequentialNumber = complement.getInstitutionNumber() + "-" + complement.getEmisionPointNumber() + "-"
					+ format;
			this.instance.setSequentialNumber(this.sequentialNumber);
			// actualizar el valor actual del contador del voucher
			// complementario

			this.instance.setTypeEmissionPoint(this.typeEmissionPoint);
			this.instance.setVoucherNumber(number);
			this.typeEmissionPoint.setCurrentValue(number);
			getEntityManager().merge(this.typeEmissionPoint);
			super.persist();
		} catch (InvoiceNumberOutOfRangeException e) {
			e.printStackTrace();
		}
		return "persisted";
	}

	public String cancelVoucher() {
		getEntityManager().merge(this.cancelVoucher);
		getEntityManager().flush();
		return "/electronicvoucher/ElectronicRetentionList.xhtml";
	}

	/**
	 * Verifica que el usuario pueda emitir comprobantes de retencion
	 */
	@SuppressWarnings("unchecked")
	public void checkUserCanEmitt() {

		EntityManager em = getEntityManager();
		Person person = userSession.getPerson();
		Query q = em.createNamedQuery("TypeEmissionPoint.findByPersonAndType");
		q.setParameter("person", person);
		q.setParameter("code", "07");

		List<TypeEmissionPoint> typesByEmissionPoint = q.getResultList();

		if (typesByEmissionPoint.size() > 0) {
			this.typeEmissionPoint = typesByEmissionPoint.get(0);
			verifySequenceExist();
		} else {
			System.out.println("La empresa no tienen el tipo de comprobante relacionado");
			return;
		}
	}

	public void verifySequenceExist() {
		if (this.typeEmissionPoint != null) {
			// sequence_name = seq_elec_institutionId_typeemissionPointID;
			InstitutionService institution = this.typeEmissionPoint.getComplementVoucher().getInstitutionService();
			this.sequenceName = "seq_elec_" + institution.getId() + "_" + this.typeEmissionPoint.getId();
			System.out.println("SEQ CRE: " + this.sequenceName);
			IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
			incomeService.createSequenceComplementVoucher(this.sequenceName);
		}
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

	/**
	 * Validar fechas de documento
	 */
	public void validateDates() {
		Date voucherDate = this.instance.getEmissionDate();
		Date documentDate = this.instance.getDocumentDate();
		Date now = new Date();

		Boolean error = false;
		if (voucherDate.after(now)) {
			error = true;
		}

		if (documentDate.after(now)) {
			error = true;
		}

		if (documentDate.after(voucherDate)) {
			error = true;
		}

		long diff = voucherDate.getTime() - documentDate.getTime();
		double days = diff / (1000 * 60 * 60 * 24);
		System.out.println("DIAS: " + days);
	}

	private void init() {
		this.instance.setAccessCode("0000000000000000000000000000000000000000000000000");
		this.instance.setAuthorizationCode("0000000000000000000000000000000000000000000000000");
		Date now = Calendar.getInstance().getTime();
		this.instance.setCreationDate(now);
		this.instance.setCreationTime(now);
		this.instance.setDocumentDate(now);
		this.instance.setDocumentNumber("000-000-123456789");
		this.instance.setEmissionDate(now);
		this.instance.setEmisionTime(now);
		Person emitter = userSession.getPerson();
		this.instance.setEmitter(emitter);
		this.instance.setSequentialNumber(this.sequentialNumber);
		this.instance.setElectronicStatus(StatusElectronicReceipt.PENDING.name());

		this.codeList = new ArrayList<RetentionCode>();
		this.instance.setDocumentType("01");
		setDetail();
		checkUserCanEmitt();
	}

	private void setDetail() {
		if (this.getInstance().getDetails().size() == 0) {
			ElectronicVoucherDetail det1 = new ElectronicVoucherDetail();
			det1.setElectronicVoucher(this.getInstance());
			det1.setRetentionTax(RetentionTax.IVA);
			det1.setTaxesTotal(BigDecimal.ZERO);

			ElectronicVoucherDetail det2 = new ElectronicVoucherDetail();
			det2.setElectronicVoucher(this.getInstance());
			det2.setRetentionTax(RetentionTax.RENTA);
			det2.setTaxesTotal(BigDecimal.ZERO);

			this.getInstance().getDetails().add(det1);
			this.getInstance().getDetails().add(det2);
		}
	}

	/**
	 * 
	 */
	 
	public void removeDetail(ElectronicVoucherDetail detail) {
		detail.setElectronicVoucher(null);
		this.getInstance().getDetails().remove(detail);
		calculateTotal();
	}

	public boolean isWired() {
		return true;
	}

	public ElectronicVoucher getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
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

	public List<ComplementVoucher> getComplements() {
		return complements;
	}

	public void setComplements(List<ComplementVoucher> complements) {
		this.complements = complements;
	}

	public String getSequentialNumber() {
		return sequentialNumber;
	}

	public void setSequentialNumber(String sequentialNumber) {
		this.sequentialNumber = sequentialNumber;
	}

	@Factory("documentTypes")
	@SuppressWarnings("unchecked")
	public List<ComplementVoucherType> findComplementTypes() {
		Query query = getPersistenceContext().createNamedQuery("ComplementVoucherType.findInCode");
		List<String> codes=new ArrayList<String>();
		codes.add("01");
		codes.add("03");
		query.setParameter("list", codes);
		return query.getResultList();
	}

	@Factory("retentionCodes")
	@SuppressWarnings("unchecked")
	public List<RetentionCode> findRetentionCodes() {
		Query query = getPersistenceContext().createNamedQuery("RetentionCode.findAll");
		return query.getResultList();
	}

	@Factory("fiscalperiods")
	@SuppressWarnings("unchecked")
	public List<FiscalPeriod> findFiscalPeriods() {
		Query query = getPersistenceContext().createNamedQuery("FiscalPeriod.findAll");
		return query.getResultList();
	}

	@Factory("institutions")
	@SuppressWarnings("unchecked")
	public List<InstitutionService> findInstitutions() {
		Query query = getPersistenceContext().createNamedQuery("InstitutionService.findAll");
		return query.getResultList();
	}

	@Factory("retentionTaxes")
	public List<RetentionTax> loadRetentionTaxes() {
		return Arrays.asList(RetentionTax.values());
	}

	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria() {
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			setResidents(query.getResultList());
		}
	}

	public List<ElectronicVoucherDetail> getDetails() {
		return getInstance() == null ? null : new ArrayList<ElectronicVoucherDetail>(getInstance().getDetails());
	}

	/**
	 * 
	 */
	public void searchResident() {
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
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

	/**
	 * 
	 */
	public void addDetailElectronicVoucher() {
		// crear dos instancias de detalle
		ElectronicVoucherDetail det1 = new ElectronicVoucherDetail();
		det1.setElectronicVoucher(this.getInstance());
		det1.setRetentionTax(RetentionTax.IVA);
		det1.setTaxesTotal(BigDecimal.ZERO);

		this.getInstance().getDetails().add(det1);

	}

	public void calculateTotal() {
		BigDecimal total = BigDecimal.ZERO;
		for (ElectronicVoucherDetail detail : this.instance.getDetails()) {
			detail.setTaxesTotal(detail.getTaxesTotal().setScale(2, RoundingMode.HALF_UP));
			total = total.add(detail.getTaxesTotal());
		}
		total = total.setScale(2, RoundingMode.HALF_UP);
		this.getInstance().setTotal(total);
		System.out.println("TOTAL=>: " + total);
	}

	public void calculateDetail() {

		if (detail.getTaxableBase() != null && detail.getRetentionPercent() != null) {
			BigDecimal base = detail.getTaxableBase();
			BigDecimal percent = detail.getRetentionPercent();

			if (base != null && percent != null) {
				BigDecimal subPercent = percent.divide(new BigDecimal(100));
				BigDecimal total = base.multiply(subPercent);
				detail.setTaxesTotal(total);
				calculateTotal();
			}
		}
	}

	/**
	 * 
	 */
	public void calculateDetail(ElectronicVoucherDetail det) {

		System.out.println("========>");
		if (det.getTaxableBase() != null && det.getRetentionPercent() != null) {
			BigDecimal base = det.getTaxableBase();
			BigDecimal percent = det.getRetentionPercent();

			if (base != null && percent != null) {
				BigDecimal subPercent = percent.divide(new BigDecimal(100));
				BigDecimal total = base.multiply(subPercent);
				det.setTaxesTotal(total);
				System.out.println("========>" + total);
				calculateTotal();
			}
		}
	}

	public void removeDetailElectronicVoucher(ElectronicVoucherDetail detail) {
		this.getInstance().getDetails().remove(detail);
		calculateTotal();
	}

	public ComplementVoucherType getComplementVoucherType() {
		return complementVoucherType;
	}

	public void setComplementVoucherType(ComplementVoucherType complementVoucherType) {
		this.complementVoucherType = complementVoucherType;
	}

	public void setType() {
		if (this.complementVoucherType != null) {
			this.instance.setDocumentType(this.complementVoucherType.getCode());
		}
	}

	public ElectronicVoucherDetail getDetail() {
		return detail;
	}

	public void setDetail(ElectronicVoucherDetail detail) {

		System.out.println("Setter=>" + detail.getRetentionTax().name());
		this.detail = detail;
	}

	public List<RetentionCode> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<RetentionCode> codeList) {
		this.codeList = codeList;
	}

	public void setPercentage() {
		if (this.detail.getRetentionCode() != null) {
			if (this.detail.getRetentionCode().getPercentage() != null) {
				this.detail.setRetentionPercent(this.detail.getRetentionCode().getPercentage());
				calculateDetail();
			}
		}
	}

	public void setPercentage(RetentionCode retentionCode, ElectronicVoucherDetail det) {
		if (det.getRetentionCode() != null) {
			if (det.getRetentionCode().getPercentage() != null) {
				det.setRetentionPercent(det.getRetentionCode().getPercentage());
				calculateDetail(det);
			}
		}
	}

	/**
	 * Put detail in voucher
	 */
	public void putDetail() {
		if (this.detail.getTaxesTotal() != null && this.detail.getRetentionCode() != null) {
			this.instance.getDetails().add(this.detail);
			calculateTotal();
		}
	}

	/**
	 * Create new Adittional Field
	 */
	public void addField() {
		this.field = new AditionalField();
		this.field.setElectronicVoucher(this.instance);
	}

	/**
	 * Put Field in aditionalFields array
	 */
	public void putField() {
		this.instance.getFields().add(this.field);
	}

	/**
	 * Remover el campo adicional
	 * 
	 * @param field
	 */
	public void removeField(AditionalField field) {
		this.instance.getFields().remove(field);
	}

	/**
	 * Agregar codigos de retencion
	 * 
	 * @param retentionCode
	 */
	public void addCode(RetentionCode retentionCode) {
		this.detail.setRetentionCode(retentionCode);
		setPercentage();
	}

	/*
	 * 
	 */
	public void addCode(RetentionCode retentionCode, ElectronicVoucherDetail detail) {
		detail.setRetentionCode(retentionCode);
		// this.detail.setRetentionCode(retentionCode);
		setPercentage(retentionCode, detail);
	}

	@SuppressWarnings("unchecked")
	public List<RetentionCode> searchRetentionCode(Object suggest) { 
		// tratar de convertir la sugerencia a numero
		// caso contrario se busca por el nombre del codigo de retencion
		String suggestion = ((String) suggest);
		Query query = null;

		try {
			Long value = Long.parseLong(suggestion);
			query = getEntityManager().createNamedQuery("RetentionCode.findByPercentage");
			query.setParameter("percentage", value);
			query.setParameter("type", this.detail.getRetentionTax().name());
		} catch (Exception e) {
			query = getEntityManager().createNamedQuery("RetentionCode.findByType");
			query.setParameter("name", "%" + ((String) suggest).toLowerCase() + "%");
			query.setParameter("type", this.detail.getRetentionTax().name());
		}
		return (List<RetentionCode>) query.getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<Provider> searchProvider(Object suggest) {
		System.out.println(suggest);
		Query query = getEntityManager().createNamedQuery("Provider.findByCriteria");
		query.setParameter("criteria", ((String) suggest.toString().toLowerCase()) + "%");
		return query.getResultList();
	}

	public void addProvider(Provider provider) {
		this.instance.setProvider(provider);
	}

	/**
	 * Clean the report
	 */
	public void clean() {
		startDate = null;
		endDate = null;
		this.detailsReport = new ArrayList<DetailReport>();
	}

	/**
	 * Generate retention report from dates
	 */

	@SuppressWarnings("unchecked")
	public void getReport() {
		DateFormat df = new SimpleDateFormat("yyyy-MMMM-dd");
		start = df.format(startDate);
		end = df.format(endDate);

		String reportTotals = "SELECT d.retentionTax, d.retentionPercent,  sum(d.taxesTotal)"
				+ " from ElectronicVoucherDetail d " + " where d.electronicVoucher.electronicStatus !=:status "
				+ " and d.electronicVoucher.emissionDate between :start and :end "
				+ " and d.electronicVoucher.sequentialNumber like :numberPoint "
				+ " group by d.retentionPercent, d.retentionTax " + " order by d.retentionPercent, d.retentionTax";

		Query query = getEntityManager().createQuery(reportTotals);
		query.setParameter("status", StatusElectronicReceipt.CANCEL.name());
		query.setParameter("start", startDate);
		query.setParameter("end", endDate);
		query.setParameter("numberPoint", numberPoint + "%");
		List<Object[]> list = query.getResultList();
		this.detailsReport = new ArrayList<DetailReport>();

		String queryDetail = "";
		for (Object[] object : list) {

			RetentionTax retentionTax = ((RetentionTax) object[0]);
			BigDecimal percentage = (BigDecimal) object[1];
			BigDecimal total = (BigDecimal) object[2];

			DetailReport detail = new DetailReport();
			detail.setRetentionPercent(percentage);
			detail.setTotal(total);
			detail.setRetentionTax(retentionTax.name());
			queryDetail = "Select d from ElectronicVoucherDetail d"
					+ " where d.electronicVoucher.electronicStatus !=:status and "
					+ " d.retentionTax=:retentionTax and " + " d.retentionPercent=:percentage and"
					+ " d.electronicVoucher.emissionDate between :start and :end and "
					+ " d.electronicVoucher.sequentialNumber like :numberPoint ";

			query = getEntityManager().createQuery(queryDetail);
			query.setParameter("status", StatusElectronicReceipt.CANCEL.name());
			query.setParameter("retentionTax", retentionTax);
			query.setParameter("percentage", percentage);
			query.setParameter("start", startDate);
			query.setParameter("end", endDate);
			query.setParameter("numberPoint", numberPoint + "%");

			List<ElectronicVoucherDetail> details = query.getResultList();
			detail.setDetails(details);
			detailsReport.add(detail);
		}
	}

	public void test() {
		System.out.println();
	}

	public boolean isEnabled(ElectronicVoucher voucher) {
		return (voucher.getElectronicStatus().equals(StatusElectronicReceipt.AUTHORIZED.name())
				|| voucher.getElectronicStatus().equals(StatusElectronicReceipt.CANCEL.name())) ? false : true;
	}

	public boolean canCancel(ElectronicVoucher voucher) {
		Person emitter = userSession.getPerson();
		return (!voucher.getElectronicStatus().equals("CANCEL")) ? true : false;
	}

	public void enableProcessNumber() {
		this.enableProcessNumber = true;
	}

	public String getEmissionPoint() {
		return emissionPoint;
	}

	public void setEmissionPoint(String emissionPoint) {
		this.emissionPoint = emissionPoint;
	}

	public String getNumberPoint() {
		return numberPoint;
	}

	public void setNumberPoint(String numberPoint) {
		this.numberPoint = numberPoint;
	}

	public String getNameEmitter() {
		return nameEmitter;
	}

	public void setNameEmitter(String nameEmitter) {
		this.nameEmitter = nameEmitter;
	}

	public boolean setParam(ElectronicVoucherDetail detail) {
		setDetail(detail);
		return false;
	}

	public Boolean getEnableProcessNumber() {
		return enableProcessNumber;
	}

	public void setEnableProcessNumber(Boolean enableProcessNumber) {
		this.enableProcessNumber = enableProcessNumber;
	}

}
