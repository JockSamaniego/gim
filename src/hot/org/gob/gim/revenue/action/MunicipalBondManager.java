package org.gob.gim.revenue.action;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.income.action.ReceiptPrintingManager;
import org.gob.gim.income.facade.IncomeService;
import org.gob.gim.revenue.facade.RevenueService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.framework.EntityController;

import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.income.model.Deposit;
import ec.gob.gim.income.model.LegalStatus;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.MunicipalBondType;

@Name("municipalBondManager")
@Scope(ScopeType.CONVERSATION)
public class MunicipalBondManager extends EntityController {

	private static final long serialVersionUID = 1L;
	/** CRITERIOS DE BUSQUEDA **/
	private Entry entry;

	private String entryCode;

	private String criteriaEntry;

	private MunicipalBondStatus municipalBondStatus;

	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	private Date startDate;

	private Date endDate;

	private Resident resident;

	private Resident residentForLoad;

	private String criteria;

	private String identificationNumber;

	private Boolean isCertificate = null;

	public static String REVENUE_SERVICE_NAME = "/gim/RevenueService/local";

	/** FIN CRITERIOS **/

	private SystemParameterService systemParameterService;

	private RevenueService revenueService;

	protected MunicipalBondType municipalBondType = MunicipalBondType.EMISSION_ORDER;

	private Boolean allBondsSelected;

	private MunicipalBond municipalBond;

	private Long municipalBondNumber = 0L;

	private MunicipalBondStatus pendingBondStatus;
	private MunicipalBondStatus preEmitBondStatus;
	private MunicipalBondStatus reversedBondStatus;
	private MunicipalBondStatus rejectedBondStatus;
	private MunicipalBondStatus blockedBondStatus;
	private MunicipalBondStatus canceledBondStatus;
	private MunicipalBondStatus paidBondStatus;
	private MunicipalBondStatus paidBondStatusExternalChannel;
	private MunicipalBondStatus inPaymentAgreementBondStatus;
	private MunicipalBondStatus futureBondStatus;

	private List<Entry> entries;

	private List<Long> listForReverse = new ArrayList<Long>();
	private List<Long> listForBlocked = new ArrayList<Long>();
	private List<Long> listForEmited = new ArrayList<Long>();

	private List<Resident> residents;

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	@In(create = true)
	ReceiptPrintingManager receiptPrintingManager;

	private List<MunicipalBond> municipalBonds;

	private List<Long> otherDatesMunicipalBonds;

	private String observation;
	
	//rfam 2020-02-13 ML-UMTTTSV-L-FM-050-2020
	private String judicialProcessNumber;

	private static final String EJBQL = "SELECT municipalBond "
			+ "FROM MunicipalBond municipalBond "
			+ "LEFT JOIN FETCH municipalBond.resident resident "
			+ "LEFT JOIN FETCH municipalBond.municipalBondStatus municipalBondStatus "
			+ "LEFT JOIN FETCH municipalBond.entry entry "
			+ "LEFT JOIN FETCH municipalBond.receipt receipt " + " WHERE ";

	private static String JOIN_CLAUSE = " AND ";

	List<MunicipalBond> municipalBondsFormalizing;

	public MunicipalBondManager() {

		systemParameterService = ServiceLocator.getInstance().findResource(
				SystemParameterService.LOCAL_NAME);
		revenueService = ServiceLocator.getInstance().findResource(
				RevenueService.LOCAL_NAME);
		loadBondStatus();
	}

	private String completeEntryCode(String code) {
		StringBuffer codeBuffer = new StringBuffer(code.trim());
		while (codeBuffer.length() < 5) {
			codeBuffer.insert(0, '0');
		}
		return codeBuffer.toString();
	}

	@SuppressWarnings("unchecked")
	@Transactional(TransactionPropagationType.NEVER)
	public Entry findEntryByCode(String code) {
		code = this.completeEntryCode(code);
		Query query = getEntityManager().createNamedQuery("Entry.findByCode");
		query.setParameter("code", code);
		List<Entry> results = (List<Entry>) query.getResultList();
		if (!results.isEmpty())
			return results.get(0);
		return null;
	}

	public void searchEntry() {
		if (entryCode != null && entryCode.length() > 0) {
			Entry entry = findEntryByCode(entryCode);
			if (entry != null) {
				this.entry = entry;
				this.setEntry(entry);
				if (entry.getAccount() == null) {
					setEntryCode(entry.getCode());
				} else {
					setEntryCode(entry.getAccount().getAccountCode());
				}
			}
		} else {
			this.entry = null;
		}

	}

	public void searchEntryByCriteria() {
		if (this.criteriaEntry != null && !this.criteriaEntry.isEmpty()) {
			entries = findByCriteria(criteriaEntry);
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(TransactionPropagationType.NEVER)
	public List<Entry> findByCriteria(String criteria) {
		Query query = getEntityManager().createNamedQuery(
				"Entry.findByCriteria");
		query.setParameter("criteria", criteria);
		return query.getResultList();
	}

	public void clearSearchEntryPanel() {
		this.setCriteriaEntry(null);
		entries = null;
	}

	public List<MunicipalBond> getMunicipalBonds() {
		return municipalBonds;
	}

	public void setMunicipalBonds(List<MunicipalBond> municipalBonds) {
		this.municipalBonds = municipalBonds;
	}

	public void loadMunicipalBond(Long municipalBondId) {
		municipalBonds = new ArrayList<MunicipalBond>();
		if (municipalBondId != null) {
			IncomeService incomeService = ServiceLocator.getInstance()

			.findResource(IncomeService.LOCAL_NAME);
			MunicipalBond municipalBond = incomeService
					.loadForPrinting(municipalBondId);
			municipalBonds.add(municipalBond);
			residentForLoad = municipalBond.getResident();
		}
	}

	public void entrySelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Entry entry = (Entry) component.getAttributes().get("entry");
		this.setEntry(entry);
		if (entry.getAccount() == null) {
			setEntryCode(entry.getCode());
		} else {
			setEntryCode(entry.getAccount().getAccountCode());
		}
	}

	public String getEntryCode() {
		return entryCode;
	}

	public void setEntryCode(String entryCode) {
		this.entryCode = entryCode;
	}

	public String getCriteriaEntry() {
		return criteriaEntry;
	}

	public void setCriteriaEntry(String criteriaEntry) {
		this.criteriaEntry = criteriaEntry;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
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

	@End
	public String findMunicipalBonds() {
		//macartuche
		this.isFuture=Boolean.FALSE;
		//fin
		
		if (resident != null || entry != null || startDate != null
				|| endDate != null || municipalBondStatus != null

				|| municipalBondNumber > 0) {
			Long residentId = resident != null ? resident.getId() : null;

			Long municipalBondStatusId = municipalBondStatus != null ? municipalBondStatus
					.getId() : null;
			Long entryId = entry != null ? entry.getId() : null;
			Long mbNumber = municipalBondNumber > 0 ? municipalBondNumber : 0;
			getDataModel().setCriteria(residentId, entryId, startDate, endDate,
					municipalBondStatusId, mbNumber, this.bondsWasInAgreement);
			getDataModel().setRowCount(getDataModel().getObjectsNumber());
		} else {
			addFacesMessageFromResourceBundle("municipalBond.invalidCriteriaSet");
		}
		return null;
	}

	@End
	public String findMunicipalBondsFutureEmission() {
		//System.out				.println("---------------***********LLega al buscador de obligaciones municipales futuras-----------*************");
		if (resident != null || entry != null) {
			Long residentId = resident != null ? resident.getId() : null;
			Long municipalBondStatusId = futureBondStatus.getId();
			Long entryId = entry != null ? entry.getId() : null;

			/*System.out.println("resident Id:" + residentId);
			System.out.println("entry Id:" + entryId);
			System.out.println("municipal status future:"
					+ municipalBondStatusId);*/

			getDataModel().setCriteria(residentId, entryId,
					municipalBondStatusId, true);
			getDataModel().setRowCount(getDataModel().getObjectsNumber());
		} else {
			addFacesMessageFromResourceBundle("municipalBond.invalidCriteriaSet");
		}
		return null;
	}

	public String loadMunicipalBondsFormalizeEmission() {
		//System.out.println("---------------***********LLega al load formalizar-----------*************");

		Long municipalBondStatusId = futureBondStatus.getId();

		//System.out.println("municipal status future:" + municipalBondStatusId);
		Date now = new Date();
		this.municipalBondsFormalizing = getMunicipalBondsFormalizing(municipalBondStatusId, now);
		return null;
	}

	public List<MunicipalBond> getMunicipalBondsFormalizing(Long municipalBondStatusId, Date now) {
		// TODO imolementar aqui logica buscar emisiones formalizar
		StringBuilder queryBuilder = new StringBuilder(EJBQL);

		if (municipalBondStatusId != null) {
			queryBuilder.append("municipalBond.municipalBondStatus.id = "
					+ municipalBondStatusId);
			queryBuilder.append(JOIN_CLAUSE);
		}

		if (now != null) {

			queryBuilder.append(":now>= municipalBond.emisionDate ");
		}

		queryBuilder
				.append(" ORDER BY municipalBond.entry, municipalBond.id desc");

		/*System.out
				.println("QUERY GENERADO findMunicipalBondsFomalizing----> \n\n\n"
						+ queryBuilder.toString());*/

		String stringQuery = queryBuilder.toString();

		// Integer maxResults =
		// systemParameterService.findParameter("MAX_QUERY_RESULTS");
		Query query = getEntityManager().createQuery(stringQuery);
		query.setParameter("now", now, TemporalType.DATE);

		List<MunicipalBond> mbs = query.getResultList();

		return mbs;
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

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public MunicipalBondStatus getMunicipalBondStatus() {
		return municipalBondStatus;
	}

	public void setMunicipalBondStatus(MunicipalBondStatus municipalBondStatus) {
		this.municipalBondStatus = municipalBondStatus;
	}

	public Long getMunicipalBondNumber() {
		return municipalBondNumber;
	}

	public void setMunicipalBondNumber(Long municipalBondNumber) {
		this.municipalBondNumber = municipalBondNumber;
	}

	@SuppressWarnings("unchecked")
	@Transactional(TransactionPropagationType.NEVER)
	public void searchResidentByCriteria() {
		// System.out.println("SEARCH RESIDENT BY CRITERIA " + this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery(
					"Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			setResidents(query.getResultList());
		}
	}

	@Transactional(TransactionPropagationType.NEVER)
	public void searchResident() {
		// System.out.println("RESIDENT CHOOSER CRITERIA... "
		// + this.identificationNumber);
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			// System.out.println("RESIDENT CHOOSER ACTION " +
			// resident.getName());
			setResident(resident);
			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void residentSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes()
				.get("resident");
		setResident(resident);
		this.setIdentificationNumber(resident.getIdentificationNumber());
	}

	public void clearSearchResidentPanel() {
		this.setCriteria(null);
		setResidents(null);
	}

	public Boolean getIsCertificate() {
		return isCertificate;
	}

	public void setIsCertificate(Boolean isCertificate) {
		this.isCertificate = isCertificate;
	}

	public String printCertificate(Long municipalBondId) {
		isCertificate = Boolean.TRUE;
		if (revenueCharge == null)
			loadCharge();
		print(municipalBondId);
		return "printCertificate";
	}

	public Charge getCharge(String systemParameter) {
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(

			SYSTEM_PARAMETER_SERVICE_NAME);
		Charge charge = systemParameterService.materialize(Charge.class,
				systemParameter);
		return charge;
	}

	private Charge revenueCharge;

	public Charge getRevenueCharge() {
		return revenueCharge;
	}

	public void setRevenueCharge(Charge revenueCharge) {
		this.revenueCharge = revenueCharge;
	}

	public Delegate getRevenueDelegate() {
		return revenueDelegate;
	}

	public void setRevenueDelegate(Delegate revenueDelegate) {
		this.revenueDelegate = revenueDelegate;
	}

	private Delegate revenueDelegate;

	public void loadCharge() {
		revenueCharge = getCharge("DELEGATE_ID_REVENUE");
		if (revenueCharge != null) {
			for (Delegate d : revenueCharge.getDelegates()) {
				if (d.getIsActive())
					revenueDelegate = d;
			}

		}
	}

	public String print(Long municipalBondId) {
		IncomeService incomeService = ServiceLocator.getInstance()

		.findResource(IncomeService.LOCAL_NAME);
		MunicipalBond municipalBond = incomeService
				.loadForPrinting(municipalBondId);
		/*System.out.println("RECOVERED IN BACKING BEAN ---> "
				+ municipalBond.getDeposits().size());*/
		Set<Deposit> deposits = municipalBond.getDeposits();
		if (deposits.size() > 0) {
			Deposit depositToPrint = (Deposit) Arrays.asList(deposits.toArray()).get(deposits.size() - 1);
			depositToPrint.setMunicipalBond(municipalBond);

			Long printingsNumber = new Long(municipalBond.getPrintingsNumber());
			Long[] printings = { printingsNumber };
			receiptPrintingManager.setPrintings(printings);
			receiptPrintingManager.setIsCertificate(isCertificate);
			receiptPrintingManager.controlReprintsCreditTitles(municipalBond.getPrintingsNumber(), this.printForRedeemed);
			String result = receiptPrintingManager.print(depositToPrint);

			/*System.out.println("RESULTADO ----> " + result + " "
					+ receiptPrintingManager);*/
			if (isCertificate == null || !isCertificate)
				incomeService.updateReprintings(municipalBond.getId());
			isCertificate = false;
			return result;
		} else {
			addFacesMessageFromResourceBundle("MunicipalBond.noDepositsFound");
			return null;
		}
	}

	public String printOriginal(Long municipalBondId) {
		IncomeService incomeService = ServiceLocator.getInstance()

		.findResource(IncomeService.LOCAL_NAME);
		MunicipalBond municipalBond = incomeService
				.loadForPrinting(municipalBondId);
		Set<Deposit> deposits = municipalBond.getDeposits();
		if (municipalBond.getDeposits().size() > 0) {
			Deposit depositToPrint = (Deposit) Arrays.asList(deposits.toArray()).get(deposits.size() - 1);
			municipalBond.setDeposits(deposits);
			depositToPrint.setMunicipalBond(municipalBond);

			Long[] printings = { 0L, -1L };
			receiptPrintingManager.setPrintings(printings);

			receiptPrintingManager.setIsCertificate(Boolean.FALSE);
			receiptPrintingManager.controlReprintsCreditTitles(municipalBond.getPrintingsNumber(), this.printForRedeemed);
			String result = receiptPrintingManager.print(depositToPrint);
			return result;
		} else {
			addFacesMessageFromResourceBundle("MunicipalBond.noDepositsFound");
			return null;
		}
	}

	public void adjustAllSelection(Boolean currentSelection) {
		if (!currentSelection) {
			allBondsSelected = Boolean.FALSE;
		}
	}

	private MunicipalBondDataModel getDataModel() {

		MunicipalBondDataModel dataModel = (MunicipalBondDataModel) Contexts
				.getConversationContext().get(MunicipalBondDataModel.class);
		//System.out.println("Data model en el getDataModel" + dataModel);
		return dataModel;
	}

	public void selectAllBonds() {
		for (MunicipalBond bond : getDataModel().getMunicipalBonds()) {
			bond.setIsSelected(getAllBondsSelected());
		}
	}

	public Boolean getAllBondsSelected() {
		return allBondsSelected;
	}

	public void setAllBondsSelected(Boolean allBondsSelected) {
		this.allBondsSelected = allBondsSelected;
	}

	private void loadBondStatus() {
		pendingBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PENDING");
		reversedBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_REVERSED");
		rejectedBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_REJECTED");
		preEmitBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PREEMIT");
		blockedBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_BLOCKED");
		canceledBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_VOID");
		paidBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PAID");
		paidBondStatusExternalChannel = systemParameterService.materialize(
				MunicipalBondStatus.class,
				"MUNICIPAL_BOND_STATUS_ID_PAID_FROM_EXTERNAL_CHANNEL");
		inPaymentAgreementBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class,
				"MUNICIPAL_BOND_STATUS_ID_IN_PAYMENT_AGREEMENT");
		futureBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class,
				"MUNICIPAL_BOND_STATUS_ID_FUTURE_ISSUANCE");
	}

	public boolean isCurrentDay(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		Calendar aux = Calendar.getInstance();
		aux.set(Calendar.HOUR_OF_DAY, 0);
		aux.set(Calendar.MINUTE, 0);
		aux.set(Calendar.SECOND, 0);
		aux.set(Calendar.MILLISECOND, 0);

		if (ca.getTime().equals(aux.getTime()))

			return true;

		return false;
	}

	private Boolean isFuture=Boolean.FALSE;
	private Boolean isVoid=Boolean.FALSE;
	
	//macartuche
	//metodo auxiliar de boton anulacion
	public void putVoidBond(MunicipalBond municipalBond,
			MunicipalBondStatus municipalBondStatus) {
		isVoid = Boolean.TRUE;
		updateStatus(municipalBond, municipalBondStatus);
		//encerar variables
		isVoid = Boolean.FALSE;		
	}
	//fin
	
	public void updateStatus(MunicipalBond municipalBond,
			MunicipalBondStatus municipalBondStatus) {
		/*System.out.println("GZ -----> UpdateStatus executed for "
				+ municipalBond.getId() + " FOR STATUS " + municipalBondStatus);*/
		//macartuche
		//verificar si ha estado en estado futura(solo en boton anular) caso contrario no es posible anular 
		//System.out.println("==============================================>"+isVoid);
		if(isVoid){
			
			Long futureStatus = systemParameterService
					.findParameter("MUNICIPAL_BOND_STATUS_ID_FUTURE_ISSUANCE");
			
			//auditoria
			Query qaudit = getEntityManager().createNativeQuery("select count(*) from gimaudit.municipalbond_aud "
					+ " where municipalbondstatus_id="+futureStatus
					+ " and  id="+municipalBond.getId());
			Integer auditTotal= ((BigInteger)qaudit.getSingleResult()).intValue();
			//statuschange
			Query qsch = getEntityManager().createNativeQuery("select count(*) from gimprod.statuschange  "
					+ " where municipalbondstatus_id="+futureStatus
					+ " and municipalbond_id="+municipalBond.getId());
			Integer statusChTotal= ((BigInteger)qsch.getSingleResult()).intValue();
			
			
			if(auditTotal >0 || statusChTotal > 0){
				isFuture = Boolean.TRUE;
				return;
			}
		}else{
			isFuture = Boolean.FALSE;
		}
			
		//fin macartuche
		
		List<Long> selectedIds = new ArrayList<Long>();
		selectedIds.add(municipalBond.getId());
		updateStatus(selectedIds, municipalBondStatus);

	}

	public MunicipalBond getMunicipalBond() {
		return municipalBond;
	}

	public void changeSelectedMunicipalBond(MunicipalBond municipalBond,
			boolean selected) {
		municipalBond.setIsSelected(!selected);
	}

	public void setMunicipalBond(MunicipalBond municipalBond) {
		this.municipalBond = municipalBond;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public void updateStatus(MunicipalBondStatus status) {
		updateStatus(getSelected(), status);

	}

	public void updateReversedSelected(MunicipalBondStatus status) {
		updateStatus(listForReverse, status);
	}

	public void emitAllFuture() {
		try {

			RevenueService revenueService = ServiceLocator.getInstance()
					.findResource(REVENUE_SERVICE_NAME);

			List<MunicipalBond> listadoSeleccionadas = new ArrayList<MunicipalBond>();
			// System.out.println("Tamaño de modelo:"+getDataModel().getMunicipalBonds().size());
			
			Long municipalBondStatusId = futureBondStatus.getId();

			//System.out.println("municipal status future:" + municipalBondStatusId);
			Date now = new Date();
			
			
			for (MunicipalBond bond : getMunicipalBondsFormalizing(municipalBondStatusId, now)) {
				//System.out.println("ID bond model:" + bond.getId());
				Query query = getEntityManager().createNamedQuery(
						"MunicipalBond.findById");
				query.setParameter("municipalBondId", bond.getId());
				listadoSeleccionadas.add((MunicipalBond) query
						.getSingleResult());
			}

			/*System.out.println("Litado a enviar a change:"
					+ listadoSeleccionadas.size());*/
			String explanation = systemParameterService
					.findParameter("STATUS_CHANGE_FOMALIZE_EMISSION_EXPLANATION");
			revenueService
					.changeFutureToPendign(listadoSeleccionadas,
							userSession.getUser(), userSession.getPerson(),
							explanation);
		} catch (Exception e) {
			//System.out.println(e.getStackTrace());
			e.printStackTrace();
		}
	}

	public void emitFutureSelected() {
		try {
			RevenueService revenueService = ServiceLocator.getInstance()
					.findResource(REVENUE_SERVICE_NAME);

			// System.out.println("Listado de obligaciones seleccionadas:"
			// + getSelected());
			List<Long> listSelected = getSelected();
			//System.out.println("Tamaño de listado de selecionadas:"
					//+ listSelected.size());
			List<MunicipalBond> listadoSeleccionadas = new ArrayList<MunicipalBond>();
			// System.out.println("Tamaño de modelo:"+getDataModel().getMunicipalBonds().size());

			for (Long id : listSelected) {
				/*System.out.println("Dentro del for 1");
				System.out.println("ID Selected:" + id);*/
				for (MunicipalBond bond : getDataModel().getMunicipalBonds()) {
					/*System.out.println("Dentro del for 2");
					System.out.println("ID bond model:" + bond.getId());*/
					if (bond.getId().equals(id)) {
						Query query = getEntityManager().createNamedQuery(
								"MunicipalBond.findById");
						query.setParameter("municipalBondId", bond.getId());
						listadoSeleccionadas.add((MunicipalBond) query
								.getSingleResult());
						break;
					}
				}
			}

			/*System.out.println("Litado a enviar a change:"
					+ listadoSeleccionadas.size());*/
			String explanation = systemParameterService
					.findParameter("STATUS_CHANGE_FUTURE_EMISSION_EXPLANATION");
			revenueService
					.changeFutureToPendign(listadoSeleccionadas,
							userSession.getUser(), userSession.getPerson(),
							explanation);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}

	public void updateBlockedSelected(MunicipalBondStatus status) {
		updateStatus(listForBlocked, status);
	}

	public void changeListForReverse() {
		listForReverse.clear();
		for (MunicipalBond bond : getDataModel().getMunicipalBonds()) {
			if (bond.getIsSelected() != null && bond.getIsSelected()) {
				/*System.out
						.println("changeListForReverse>>>>>>>>>>>>>>>>>>sizeValor: "
								+ getDataModel().getMunicipalBonds().size());
				System.out
						.println("changeListForReverse>>>>>>>>>>>>>>>>>>bond.getIdValor: "
								+ bond.getId());*/
				listForReverse.add(bond.getId());
				/*System.out
						.println("changeListForReverse>>>>>>>>>>>>>>>>>>listForReverse2Valor: "
								+ listForReverse.toString());*/
				// bond.setIsSelected(Boolean.FALSE);
			}
		}

	}

	public void changeListForBlocked() {
		listForBlocked.clear();
		for (MunicipalBond bond : getDataModel().getMunicipalBonds()) {
			if (bond.getIsSelected() != null && bond.getIsSelected()) {
				listForBlocked.add(bond.getId());
				// bond.setIsSelected(Boolean.FALSE);
			}

		}

	}

	private void updateStatus(List<Long> selected, MunicipalBondStatus status) {
		//System.out.println("GZ -----> Invoking RevenueService " + status);
		revenueService.update(selected, status.getId(), userSession.getUser()
				.getId(), observation, judicialProcessNumber);
		findMunicipalBonds();
	}

	private List<Long> getSelected() {
		List<Long> selectedIds = new ArrayList<Long>();
		for (MunicipalBond bond : getDataModel().getMunicipalBonds()) {
			if (bond.getIsSelected() != null && bond.getIsSelected()) {
				selectedIds.add(bond.getId());
				bond.setIsSelected(Boolean.FALSE);
			}
		}
		return selectedIds;
	}

	private List<Long> currentMunicipalBonds;

	public List<Long> getCurrentMunicipalBonds() {
		return currentMunicipalBonds;
	}

	public void setCurrentMunicipalBonds(List<Long> currentMunicipalBonds) {
		this.currentMunicipalBonds = currentMunicipalBonds;
	}

	public List<Long> getOtherDatesMunicipalBonds() {
		return otherDatesMunicipalBonds;
	}

	public void setOtherDatesMunicipalBonds(List<Long> otherDatesMunicipalBonds) {
		this.otherDatesMunicipalBonds = otherDatesMunicipalBonds;
	}

	public void clearSearcherCriteria() {
		entry = null;
		entryCode = null;
		identificationNumber = null;
		resident = null;
		municipalBondStatus = null;
		startDate = null;
		endDate = null;
		municipalBondNumber = 0L;
		listForReverse.clear();
		listForBlocked.clear();
		findMunicipalBonds();
	}

	public MunicipalBondStatus getPreEmitBondStatus() {
		return preEmitBondStatus;
	}

	public void setPreEmitBondStatus(MunicipalBondStatus preEmitBondStatus) {
		this.preEmitBondStatus = preEmitBondStatus;
	}

	public MunicipalBondStatus getPendingBondStatus() {
		return pendingBondStatus;
	}

	public void setPendingBondStatus(MunicipalBondStatus pendingBondStatus) {
		this.pendingBondStatus = pendingBondStatus;
	}

	public MunicipalBondStatus getBlockedBondStatus() {
		return blockedBondStatus;
	}

	public void setBlockedBondStatus(MunicipalBondStatus blockedBondStatus) {
		this.blockedBondStatus = blockedBondStatus;
	}

	public MunicipalBondStatus getCanceledBondStatus() {
		return canceledBondStatus;
	}

	public void setCanceledBondStatus(MunicipalBondStatus canceledBondStatus) {
		this.canceledBondStatus = canceledBondStatus;
	}

	public MunicipalBondStatus getPaidBondStatus() {
		return paidBondStatus;
	}

	public void setPaidBondStatus(MunicipalBondStatus paidBondStatus) {
		this.paidBondStatus = paidBondStatus;
	}

	public MunicipalBondStatus getPaidBondStatusExternalChannel() {
		return paidBondStatusExternalChannel;
	}

	public void setPaidBondStatusExternalChannel(
			MunicipalBondStatus paidBondStatusExternalChannel) {
		this.paidBondStatusExternalChannel = paidBondStatusExternalChannel;
	}

	@SuppressWarnings("unchecked")
	@Factory("bondStatuses")
	@Transactional(TransactionPropagationType.NEVER)
	public List<MunicipalBondStatus> findMunicipalBondStatuses() {
		Query query = getEntityManager().createNamedQuery(
				"MunicipalBondStatus.findAll");
		return query.getResultList();
	}

	/*
	 * private Boolean getHasIncomeBossRole() { return
	 * userSession.hasRole(UserSession.ROLE_NAME_INCOME_BOSS); }
	 * 
	 * private Boolean getHasRevenueBossRole() { return
	 * userSession.hasRole(UserSession.ROLE_NAME_REVENUE_BOSS); }
	 */
	// UserSession.ROLE_NAME_REVENUE_CERTIFICATE
	public Boolean hasRole(String roleKey) {
		String role = systemParameterService.findParameter(roleKey);
		if (role != null) {
			return userSession.getUser().hasRole(role);
		}
		return false;
	}

	public String showWarned(MunicipalBond municipalBond) {
		if (municipalBond.getLegalStatus() != LegalStatus.ACCEPTED) {
			return "redFont";
		}

		if (municipalBond.getMunicipalBondStatus().getId().longValue() == inPaymentAgreementBondStatus
				.getId().longValue()) {
			return "orangeFont";
		}
		return "";
	}

	public boolean sameDates(Date d, Date d1) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(d);
		cal2.setTime(d1);
		boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2
						.get(Calendar.DAY_OF_YEAR);
		return sameDay;
	}

	public Resident getResidentForLoad() {
		return residentForLoad;
	}

	public void setResidentForLoad(Resident residentForLoad) {
		this.residentForLoad = residentForLoad;
	}

	public MunicipalBondStatus getReversedBondStatus() {
		return reversedBondStatus;
	}

	public void setReversedBondStatus(MunicipalBondStatus reversedBondStatus) {
		this.reversedBondStatus = reversedBondStatus;
	}

	public MunicipalBondStatus getRejectedBondStatus() {
		return rejectedBondStatus;
	}

	public void setRejectedBondStatus(MunicipalBondStatus rejectedBondStatus) {
		this.rejectedBondStatus = rejectedBondStatus;
	}

	public void setForReverse(MunicipalBond mb) {
		this.municipalBond = mb;
		observation = "";
		judicialProcessNumber = null;
		//System.out.println("Reseteaa observation/////////SetForReverse");
	}

	public void setForBlocked(MunicipalBond mb) {
		this.municipalBond = mb;
		observation = "";
		//System.out.println("Reseteaa observation/////////SetForBlocked");
	}

	public void setForRejectedReverse(MunicipalBond mb) {
		this.municipalBond = mb;
		observation = "";
		//System.out				.println("Reseteaa observation/////////setForRejectedReverse");
	}

	/**
	 * Nueva implementacion: pasar toda la lista de Obligaciones Municipales que
	 * estan en estado "bloqueados" a estado "de Baja"
	 */

	public static List<Long> listForReverseAll = new ArrayList<Long>();

	private String observacionManager;

	public String getObservacionManager() {
		return observacionManager;
	}

	public void setObservacionManager(String observacionManager) {
		this.observacionManager = observacionManager;
	}

	public List<MunicipalBond> getMunicipalBondsFormalizing() {
		return municipalBondsFormalizing;
	}

	public void setMunicipalBondsFormalizing(
			List<MunicipalBond> municipalBondsFormalizing) {
		this.municipalBondsFormalizing = municipalBondsFormalizing;
	}

	public String updateReversedSelectedAll() {
		reversedBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_REVERSED");
		/*System.out
				.println("updateReversedSelectedAll>>>>>>>>>>>>>>>>>>reversedBondStatusValor: "
						+ reversedBondStatus);
		System.out.println("updateReversedSelectedAll -----> statusValor "
				+ reversedBondStatus);
		System.out
				.println("updateReversedSelectedAll -----> observacionManagerValor "
						+ observacionManager);*/
		revenueService.update(listForReverseAll, reversedBondStatus.getId(),
				userSession.getUser().getId(), observacionManager, judicialProcessNumber);
		return "persisted";
	}
	
	public int getNumberMunicipalBondsFormalize(){
		return this.municipalBondsFormalizing.size();
	}

	public Boolean getIsFuture() {
		return isFuture;
	}

	public void setIsFuture(Boolean isFuture) {
		this.isFuture = isFuture;
	}

	public Boolean getIsVoid() {
		return isVoid;
	}

	public void setIsVoid(Boolean isVoid) {
		this.isVoid = isVoid;
	}
	
	//para controlar re-impresion por canje de documento
	//Jock Samaniego
	//07-01-2020
	
	private Boolean printForRedeemed = Boolean.FALSE;
	
	public String reprintedForRedeemed(MunicipalBond mb){
		this.printForRedeemed = Boolean.TRUE;
		//Query query = getEntityManager().createNamedQuery("MunicipalBond.findById");
		//query.setParameter("municipalBondId", mb_id);
		//MunicipalBond mb = (MunicipalBond) query.getSingleResult();
		if(mb.getDocumentIsRedeemed() == null || mb.getDocumentIsRedeemed()==Boolean.FALSE){			
			//mb.setDocumentIsRedeemed(Boolean.TRUE);
			MunicipalBond municipalBond = getEntityManager().getReference(MunicipalBond.class, mb.getId());
			municipalBond.setDocumentIsRedeemed(Boolean.TRUE);
			municipalBond.setExchangeDate(new Date());
			getEntityManager().merge(municipalBond);
			getEntityManager().flush();
			return this.printOriginal(mb.getId());
		}
		return null;
	}

	public String getJudicialProcessNumber() {
		return judicialProcessNumber;
	}

	public void setJudicialProcessNumber(String judicialProcessNumber) {
		this.judicialProcessNumber = judicialProcessNumber;
	}
	
	// Para visualizar obligacines que estuvieron en convenio
	// Jock Samaniego
	
	private Boolean bondsWasInAgreement = Boolean.FALSE;

	public Boolean getBondsWasInAgreement() {
		return bondsWasInAgreement;
	}

	public void setBondsWasInAgreement(Boolean bondsWasInAgreement) {
		this.bondsWasInAgreement = bondsWasInAgreement;
	}
	
	
	
}