/**
 * 
 */
package org.gob.gim.income.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

import org.apache.commons.codec.binary.StringUtils;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.facade.RevenueService;
import org.gob.gim.revenue.service.EmissionService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityController;

import com.google.common.base.Joiner;

import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.DTO.ReportEmissionDTO;
import ec.gob.gim.revenue.model.criteria.ReportEmissionCriteria;


/**
 * @author Rene
 *
 */
@Name("emissionReportHome")
@Scope(ScopeType.CONVERSATION)
public class EmissionReportHome extends EntityController {
	
	private static final long serialVersionUID = 1L;
	/*
	 * INYENCCIONES
	 */
	@In
	FacesMessages facesMessages;
	
	/*
	 * ATRIBUTOS
	 */
	
	private boolean isFirstTime = true;
	
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	
	private SystemParameterService systemParameterService;
	
	private EmissionService emissionService;
	
	public static String REVENUE_SERVICE_NAME = "/gim/RevenueService/local";
	private final static String ENTRIES_EMAALEP_LIST = "ENTRIES_EMAALEP_LIST";
	
	private MunicipalBondStatus inPaymentAgreementStatus;
	private MunicipalBondStatus externalChannelStatus;
	private MunicipalBondStatus paidMunicipalBondStatus;
	private MunicipalBondStatus compensationMunicipalBondStatus;
	private MunicipalBondStatus blockedMunicipalBondStatus;
	private MunicipalBondStatus reversedMunicipalBondStatus;
	private MunicipalBondStatus pendingStatus;
	private MunicipalBondStatus cancelledBondStatus;
	private MunicipalBondStatus futureBondStatus;
	
	private MunicipalBondStatus municipalBondStatus;
	
	private Date startDate;
	private Date endDate;
	
	private Charge incomeCharge;
	private Charge revenueCharge;
	private Delegate incomeDelegate;
	private Delegate revenueDelegate;
	
	private String explanation;
	private String explanationFormalize;
	
	private String criteriaEntry;
	private Entry entry;
	private String entryCode;
	private List<Entry> entries;
	
	private List<ReportEmissionDTO> allResults = new ArrayList<ReportEmissionDTO>();
	
	private List<ReportEmissionDTO> detailsResults = new ArrayList<ReportEmissionDTO>();
	
	private List<ReportEmissionDTO> detailsFuturas = new ArrayList<ReportEmissionDTO>();
	
	private List<ReportEmissionDTO> detailsFormalizacionesPagoAnticipado = new ArrayList<ReportEmissionDTO>();
	
	private List<ReportEmissionDTO> detailsFormalizacionesNormales = new ArrayList<ReportEmissionDTO>();
	
	private List<ReportEmissionDTO> detailsAnuladas = new ArrayList<ReportEmissionDTO>();
	
	private Long total_cant_emisiones;
	
	private BigDecimal total_valor_emision;
	
	private Long total_cant_bajas;
	
	private BigDecimal total_valor_bajas;
	
	private BigDecimal total_emision;
	
	/*
	 * detalles
	 */
	
	private Long total_futuras;
	
	private BigDecimal total_valor_futuras;
	
	private Long total_formalizaciones_pago_anticipado;
	
	private BigDecimal total_valor_formalizaciones_pago_anticipado;
	
	private Long total_formalizaciones_normales;
	
	private BigDecimal total_valor_formalizaciones_normales;

	private Long total_anuladas;
	
	private BigDecimal total_valor_anuladas;
	
	private Boolean renderPrint = Boolean.FALSE;
	

	public EmissionReportHome() {
		
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

	public String getCriteriaEntry() {
		return criteriaEntry;
	}

	public void setCriteriaEntry(String criteriaEntry) {
		this.criteriaEntry = criteriaEntry;
	}

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	public String getEntryCode() {
		return entryCode;
	}

	public void setEntryCode(String entryCode) {
		this.entryCode = entryCode;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
	
	public MunicipalBondStatus getMunicipalBondStatus() {
		return municipalBondStatus;
	}

	public void setMunicipalBondStatus(MunicipalBondStatus municipalBondStatus) {
		this.municipalBondStatus = municipalBondStatus;
	}

	public List<ReportEmissionDTO> getAllResults() {
		return allResults;
	}

	public void setAllResults(List<ReportEmissionDTO> allResults) {
		this.allResults = allResults;
	}

	public Long getTotal_cant_emisiones() {
		return total_cant_emisiones;
	}

	public void setTotal_cant_emisiones(Long total_cant_emisiones) {
		this.total_cant_emisiones = total_cant_emisiones;
	}

	public BigDecimal getTotal_valor_emision() {
		return total_valor_emision;
	}

	public void setTotal_valor_emision(BigDecimal total_valor_emision) {
		this.total_valor_emision = total_valor_emision;
	}

	public Long getTotal_cant_bajas() {
		return total_cant_bajas;
	}

	public void setTotal_cant_bajas(Long total_cant_bajas) {
		this.total_cant_bajas = total_cant_bajas;
	}

	public BigDecimal getTotal_valor_bajas() {
		return total_valor_bajas;
	}

	public void setTotal_valor_bajas(BigDecimal total_valor_bajas) {
		this.total_valor_bajas = total_valor_bajas;
	}

	public BigDecimal getTotal_emision() {
		return total_emision;
	}

	public void setTotal_emision(BigDecimal total_emision) {
		this.total_emision = total_emision;
	}
	
	public Long getTotal_futuras() {
		return total_futuras;
	}

	public void setTotal_futuras(Long total_futuras) {
		this.total_futuras = total_futuras;
	}

	public BigDecimal getTotal_valor_futuras() {
		return total_valor_futuras;
	}

	public void setTotal_valor_futuras(BigDecimal total_valor_futuras) {
		this.total_valor_futuras = total_valor_futuras;
	}

	public Long getTotal_formalizaciones_pago_anticipado() {
		return total_formalizaciones_pago_anticipado;
	}

	public void setTotal_formalizaciones_pago_anticipado(
			Long total_formalizaciones_pago_anticipado) {
		this.total_formalizaciones_pago_anticipado = total_formalizaciones_pago_anticipado;
	}

	public BigDecimal getTotal_valor_formalizaciones_pago_anticipado() {
		return total_valor_formalizaciones_pago_anticipado;
	}

	public void setTotal_valor_formalizaciones_pago_anticipado(
			BigDecimal total_valor_formalizaciones_pago_anticipado) {
		this.total_valor_formalizaciones_pago_anticipado = total_valor_formalizaciones_pago_anticipado;
	}

	public Long getTotal_formalizaciones_normales() {
		return total_formalizaciones_normales;
	}

	public void setTotal_formalizaciones_normales(
			Long total_formalizaciones_normales) {
		this.total_formalizaciones_normales = total_formalizaciones_normales;
	}

	public BigDecimal getTotal_valor_formalizaciones_normales() {
		return total_valor_formalizaciones_normales;
	}

	public void setTotal_valor_formalizaciones_normales(
			BigDecimal total_valor_formalizaciones_normales) {
		this.total_valor_formalizaciones_normales = total_valor_formalizaciones_normales;
	}

	public Long getTotal_anuladas() {
		return total_anuladas;
	}

	public void setTotal_anuladas(Long total_anuladas) {
		this.total_anuladas = total_anuladas;
	}

	public BigDecimal getTotal_valor_anuladas() {
		return total_valor_anuladas;
	}

	public void setTotal_valor_anuladas(BigDecimal total_valor_anuladas) {
		this.total_valor_anuladas = total_valor_anuladas;
	}

	public List<ReportEmissionDTO> getDetailsFuturas() {
		return detailsFuturas;
	}

	public void setDetailsFuturas(List<ReportEmissionDTO> detailsFuturas) {
		this.detailsFuturas = detailsFuturas;
	}

	public List<ReportEmissionDTO> getDetailsFormalizacionesPagoAnticipado() {
		return detailsFormalizacionesPagoAnticipado;
	}

	public void setDetailsFormalizacionesPagoAnticipado(
			List<ReportEmissionDTO> detailsFormalizacionesPagoAnticipado) {
	this.detailsFormalizacionesPagoAnticipado = detailsFormalizacionesPagoAnticipado;
	}

	public List<ReportEmissionDTO> getDetailsFormalizacionesNormales() {
		return detailsFormalizacionesNormales;
	}

	public void setDetailsFormalizacionesNormales(
			List<ReportEmissionDTO> detailsFormalizacionesNormales) {
		this.detailsFormalizacionesNormales = detailsFormalizacionesNormales;
	}

	public List<ReportEmissionDTO> getDetailsAnuladas() {
		return detailsAnuladas;
	}

	public void setDetailsAnuladas(List<ReportEmissionDTO> detailsAnuladas) {
		this.detailsAnuladas = detailsAnuladas;
	}
	
	public Charge getIncomeCharge() {
		return incomeCharge;
	}

	public void setIncomeCharge(Charge incomeCharge) {
		this.incomeCharge = incomeCharge;
	}

	public Charge getRevenueCharge() {
		return revenueCharge;
	}

	public void setRevenueCharge(Charge revenueCharge) {
		this.revenueCharge = revenueCharge;
	}

	public Delegate getIncomeDelegate() {
		return incomeDelegate;
	}

	public void setIncomeDelegate(Delegate incomeDelegate) {
		this.incomeDelegate = incomeDelegate;
	}

	public Delegate getRevenueDelegate() {
		return revenueDelegate;
	}

	public void setRevenueDelegate(Delegate revenueDelegate) {
		this.revenueDelegate = revenueDelegate;
	}
	
	public Boolean getRenderPrint() {
		return renderPrint;
	}

	public void setRenderPrint(Boolean renderPrint) {
		this.renderPrint = renderPrint;
	}

	public void loadDefaultDates() {
		if (isFirstTime) {
			initializeServices();
			loadMunicipalBondStatus();
			loadDates();
			loadCharge();
			isFirstTime = false;
			explanation = systemParameterService
					.findParameter("STATUS_CHANGE_FUTURE_EMISSION_EXPLANATION");

			explanationFormalize = systemParameterService
					.findParameter("STATUS_CHANGE_FOMALIZE_EMISSION_EXPLANATION");
		}
	}
	
	private void initializeServices() {
		systemParameterService = ServiceLocator.getInstance().findResource(
				SystemParameterService.LOCAL_NAME);
		
		if (emissionService == null) {
			emissionService = ServiceLocator.getInstance().findResource(
					emissionService.LOCAL_NAME);
		}
	}
	
	private void loadMunicipalBondStatus() {
		if (pendingStatus != null)
			return;
		pendingStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PENDING");
		paidMunicipalBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PAID");
		compensationMunicipalBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class,
				"MUNICIPAL_BOND_STATUS_ID_COMPENSATION");
		inPaymentAgreementStatus = systemParameterService.materialize(
				MunicipalBondStatus.class,
				"MUNICIPAL_BOND_STATUS_ID_IN_PAYMENT_AGREEMENT");
		externalChannelStatus = systemParameterService.materialize(
				MunicipalBondStatus.class,
				"MUNICIPAL_BOND_STATUS_ID_PAID_FROM_EXTERNAL_CHANNEL");
		blockedMunicipalBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_BLOCKED");
		reversedMunicipalBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_REVERSED");
		futureBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class,
				"MUNICIPAL_BOND_STATUS_ID_FUTURE_ISSUANCE");
	}
	
	public void loadDates() {
		if (isFirstTime) {
			Calendar c = Calendar.getInstance();
			startDate = c.getTime();
			endDate = c.getTime();
			isFirstTime = false;
			loadReversedStatus();
		}

	}
	
	private void loadCharge() {
		incomeCharge = getCharge("DELEGATE_ID_INCOME");
		if (incomeCharge != null) {
			for (Delegate d : incomeCharge.getDelegates()) {
				if (d.getIsActive())
					incomeDelegate = d;
			}
		}
		revenueCharge = getCharge("DELEGATE_ID_REVENUE");
		if (revenueCharge != null) {
			for (Delegate d : revenueCharge.getDelegates()) {
				if (d.getIsActive())
					revenueDelegate = d;
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
	
	public void loadReversedStatus() {
		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		}
		cancelledBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_VOID");
	}
	
	public void searchEntry() {
		if (entryCode != null) {
			RevenueService revenueService = ServiceLocator.getInstance()
					.findResource(REVENUE_SERVICE_NAME);
			Entry entry = revenueService.findEntryByCode(entryCode);
			if (entry != null) {
				this.entry = entry;
				this.setEntry(entry);
				if (entry.getAccount() != null) {
					setEntryCode(entry.getAccount().getAccountCode());
				} else {
					setEntryCode(entry.getCode());
				}
			} else {
				setEntry(null);
			}
		}
	}
	
	public void searchEntryByCriteria() {
		// logger.info("SEARCH Entry BY CRITERIA "+this.criteriaEntry);
		if (this.criteriaEntry != null && !this.criteriaEntry.isEmpty()) {
			RevenueService revenueService = (RevenueService) ServiceLocator
					.getInstance().findResource(REVENUE_SERVICE_NAME);
			entries = revenueService.findEntryByCriteria(criteriaEntry);
		}
	}
	
	public void clearSearchEntryPanel() {
		this.setCriteriaEntry(null);
		entries = null;
	}
	
	public void entrySelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Entry entry = (Entry) component.getAttributes().get("entry");
		this.setEntry(entry);
		if (entry.getAccount() != null) {
			setEntryCode(entry.getAccount().getAccountCode());
		} else {
			setEntryCode(entry.getCode());
		}
	}
	
	public void generateReport(){
		
		this.renderPrint = Boolean.FALSE;
		
		List <Long> statusIds = new ArrayList<Long>();

		if (municipalBondStatus != null) {
			// Cuando seleccionan un estado (RevenueReport)
			statusIds.add(municipalBondStatus.getId());
		} else {
			// Cuando seleccionan todos (RevenueReport)
			statusIds.add(paidMunicipalBondStatus.getId());
			statusIds.add(pendingStatus.getId());
			statusIds.add(compensationMunicipalBondStatus.getId());
			statusIds.add(inPaymentAgreementStatus.getId());
			statusIds.add(externalChannelStatus.getId());
			statusIds.add(blockedMunicipalBondStatus.getId());
			statusIds.add(reversedMunicipalBondStatus.getId());
		}
		
		Joiner joiner = Joiner.on(",");
		
		String join = joiner.join(statusIds);
		
		
		ReportEmissionCriteria criteria = new ReportEmissionCriteria();
		criteria.setStartDate(this.startDate);
		criteria.setEndDate(this.endDate);
		
		criteria.setAccount_id(this.entry == null ? "null" : this.entry.getAccount().getId().toString());
		criteria.setStatus_ids(join);
		
		this.allResults = this.emissionService.findEmissionReport(criteria);
		
		this.total_cant_bajas = new Long(0);
		this.total_cant_emisiones = new Long(0);
		this.total_emision = BigDecimal.ZERO;
		this.total_valor_bajas = BigDecimal.ZERO;
		this.total_valor_emision = BigDecimal.ZERO;
		
		for (ReportEmissionDTO reportEmissionDTO : allResults) {
			this.total_cant_bajas = this.total_cant_bajas + reportEmissionDTO.getCantidad_bajas();
			this.total_cant_emisiones = this.total_cant_emisiones + reportEmissionDTO.getCantidad_emisiones();
			this.total_emision = this.total_emision.add(reportEmissionDTO.getTotal_emision());
			this.total_valor_bajas = this.total_valor_bajas.add(reportEmissionDTO.getValor_bajas());
			this.total_valor_emision = this.total_valor_emision.add(reportEmissionDTO.getValor_emision());
		}
		
		this.detailsResults = this.emissionService.findEmissionReportOtherDetails(criteria);
		
		System.out.println("-----Detalles------");
		System.out.println(this.detailsResults);
		
		this.detailsFormalizacionesNormales = new ArrayList<ReportEmissionDTO>();
		this.detailsFormalizacionesPagoAnticipado = new ArrayList<ReportEmissionDTO>();
		this.detailsFuturas = new ArrayList<ReportEmissionDTO>();
		this.detailsAnuladas = new ArrayList<ReportEmissionDTO>();
		this.total_formalizaciones_normales = new Long(0);
		this.total_formalizaciones_pago_anticipado = new Long(0);
		this.total_futuras = new Long(0);
		this.total_anuladas = new Long(0);
		this.total_valor_formalizaciones_normales = BigDecimal.ZERO;
		this.total_valor_formalizaciones_pago_anticipado = BigDecimal.ZERO;
		this.total_valor_futuras = BigDecimal.ZERO;
		this.total_valor_anuladas = BigDecimal.ZERO;
		
		for (ReportEmissionDTO reportEmissionDTO : this.detailsResults) {
			if(reportEmissionDTO.getTipo().equals("FUTURA")){
				this.detailsFuturas.add(reportEmissionDTO);
				this.total_futuras = this.total_futuras +  reportEmissionDTO.getCantidad_emisiones();
				this.total_valor_futuras = this.total_valor_futuras.add(reportEmissionDTO.getTotal_emision());
			}else if(reportEmissionDTO.getTipo().equals("FORMALIZACIONES PAGO ANTICIPADO")){
				this.detailsFormalizacionesPagoAnticipado.add(reportEmissionDTO);
				this.total_formalizaciones_pago_anticipado = this.total_formalizaciones_pago_anticipado +  reportEmissionDTO.getCantidad_emisiones();
				this.total_valor_formalizaciones_pago_anticipado = this.total_valor_formalizaciones_pago_anticipado.add(reportEmissionDTO.getTotal_emision());
			}else if(reportEmissionDTO.getTipo().equals("FORMALIZACIONES NORMALES")){
				this.detailsFormalizacionesNormales.add(reportEmissionDTO);
				this.total_formalizaciones_normales = this.total_formalizaciones_normales +  reportEmissionDTO.getCantidad_emisiones();
				this.total_valor_formalizaciones_normales = this.total_valor_formalizaciones_normales.add(reportEmissionDTO.getTotal_emision());
			}else if(reportEmissionDTO.getTipo().equals("ANULADAS")){
				this.detailsAnuladas.add(reportEmissionDTO);
				this.total_anuladas = this.total_anuladas +  reportEmissionDTO.getCantidad_emisiones();
				this.total_valor_anuladas = this.total_valor_anuladas.add(reportEmissionDTO.getTotal_emision());
			}
			
		}
		
		if(this.allResults.size()>0 || this.detailsAnuladas.size()>0 || this.detailsFormalizacionesNormales.size()>0 || this.detailsFormalizacionesPagoAnticipado.size()>0 || this.detailsFuturas.size()>0){
			this.renderPrint = Boolean.TRUE;
		}
	}
}