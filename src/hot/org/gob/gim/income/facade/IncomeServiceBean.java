/**
 * 
 */
package org.gob.gim.income.facade;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.CrudService;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.exception.CreditNoteValueNotValidException;
import org.gob.gim.exception.InvoiceNumberOutOfRangeException;
import org.gob.gim.exception.NotActiveWorkdayException;
import org.gob.gim.exception.ReceiptAuthorizationDoesNotExistException;
import org.gob.gim.exception.ReverseAmongPaymentsIsNotAllowedException;
import org.gob.gim.exception.ReverseNotAllowedException;
import org.gob.gim.revenue.exception.EntryDefinitionNotFoundException;
import org.gob.gim.revenue.service.MunicipalBondService;
import org.gob.loja.gim.ws.dto.FutureBond;

import com.google.common.base.Joiner;

import ec.common.sridocuments.v110.factura.Factura;
//import ec.common.sridocuments.v110.factura.QueryBilling;
//import ec.common.sridocuments.v110.factura.ResultSet;
//import ec.common.sridocuments.v110.factura.SQLException;
import ec.common.sridocuments.v110.factura.XmlTransform;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.common.model.FinancialStatus;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.income.model.Branch;
import ec.gob.gim.income.model.CompensationReceipt;
import ec.gob.gim.income.model.CreditNote;
import ec.gob.gim.income.model.Deposit;
import ec.gob.gim.income.model.LegalStatus;
import ec.gob.gim.income.model.Payment;
import ec.gob.gim.income.model.PaymentAgreement;
import ec.gob.gim.income.model.PaymentFraction;
import ec.gob.gim.income.model.PaymentMethod;
import ec.gob.gim.income.model.PaymentType;
import ec.gob.gim.income.model.Receipt;
import ec.gob.gim.income.model.ReceiptAuthorization;
import ec.gob.gim.income.model.ReceiptType;
import ec.gob.gim.income.model.StatusElectronicReceipt;
import ec.gob.gim.income.model.Tax;
import ec.gob.gim.income.model.TaxpayerRecord;
import ec.gob.gim.income.model.Till;
import ec.gob.gim.income.model.Workday;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.MunicipalBondType;
import ec.gob.gim.security.model.MunicipalbondAux;
import ec.gob.loja.client.clients.ElectronicClient;
import ec.gob.loja.client.model.DataWS;
import ec.gob.loja.client.utility.FileUtilities;

/**
 * @author wilman
 * @author RonaldPC
 *
 */
@Stateless(name = "IncomeService")
public class IncomeServiceBean implements IncomeService {

	@EJB
	CrudService crudService;

	@EJB
	SystemParameterService systemParameterService;

	@PersistenceContext
	EntityManager entityManager;

	@EJB
	MunicipalBondService municipalBondService;

	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public InterestRate findInterestRateById(Long interestRateId) {
	 * Map<String, Long> parameters = new HashMap<String, Long>();
	 * parameters.put("id", interestRateId);
	 * 
	 * List<InterestRate> resultList =
	 * (List<InterestRate>)crudService.findWithNamedQuery(
	 * "InterestRate.findById", parameters, 1); if (! resultList.isEmpty())
	 * return resultList.get(0); return null; }
	 */

	private Deposit getLastDeposit(MunicipalBond municipalBond) {
		Deposit deposit = null;
		Set<Deposit> deposits = municipalBond.getDeposits();
		for (Deposit d : deposits) {
			if (deposit == null || deposit.getId().intValue() < d.getId().intValue()) {
				deposit = d;
			}
		}
		return deposit;
	}

	@Override
	public void calculatePayment(MunicipalBond municipalBond, boolean isForPay, boolean applyDiscount)
			throws EntryDefinitionNotFoundException {
		Date now = Calendar.getInstance().getTime();
		this.calculatePayment(municipalBond, now, isForPay, applyDiscount);
	}

	@Override//iva12%
	public void calculatePayment(MunicipalBond municipalBond,
			Date paymentServiceDate, boolean isForPay, boolean applyDiscount)
			throws EntryDefinitionNotFoundException {
		Object[] objects = null;
		calculatePayment(municipalBond, paymentServiceDate, isForPay, applyDiscount, objects);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void calculatePayment(Date paymentDate, List<Long> municipalBondIds, boolean isForPay, boolean applyDiscount)
			throws EntryDefinitionNotFoundException {
		// List<MunicipalBond> municipalBonds =
		// findMunicipalBonds(municipalBondIds);
		List<MunicipalBond> municipalBonds = findMunicipalBondsPaymentPlatform(municipalBondIds);
		for (MunicipalBond municipalBond : municipalBonds) {
			this.calculatePayment(municipalBond, paymentDate, isForPay, applyDiscount);
		}
	}

	@SuppressWarnings("unchecked")
	private List<MunicipalBond> findMunicipalBonds(List<Long> ids) {
		Query query = entityManager.createNamedQuery("MunicipalBond.findByIds");
		query.setParameter("municipalBondIds", ids);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	private List<MunicipalBond> findMunicipalBondsPaymentPlatform(List<Long> ids) {
		// Query query =
		// entityManager.createNamedQuery("MunicipalBond.findByIds");
		Query query = entityManager.createNamedQuery("MunicipalBond.findByIdsPaymentPlatform");
		query.setParameter("municipalBondIds", ids);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	private MunicipalBondStatus findMunicipalBondStatusById(Long id) {
		Query query = entityManager.createNamedQuery("MunicipalBondStatus.findById");
		query.setParameter("id", id);
		List<MunicipalBondStatus> list = query.getResultList();
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	@Override  //iva12%
	public void calculatePayment(List<MunicipalBond> municipalBonds,
			Date paymentServiceDate, boolean isForPay, boolean applyDiscount)
			throws EntryDefinitionNotFoundException { 
		for (MunicipalBond municipalBond : municipalBonds) {
			this.calculatePayment(municipalBond, paymentServiceDate, isForPay, applyDiscount);
		}
	}

	@SuppressWarnings("unused")
	private List<Long> convertTaxListToTaxIdList(List<Tax> taxes) {
		List<Long> idList = new ArrayList<Long>();
		for (Tax tax : taxes) {
			idList.add(tax.getId());
		}
		return idList;
	}

	@Override
	public void calculatePayment(MunicipalBond municipalBond, boolean isForPay, boolean applyDiscount, Object... facts)
			throws EntryDefinitionNotFoundException {
		Date now = Calendar.getInstance().getTime();
		this.calculatePayment(municipalBond, now, isForPay, applyDiscount, facts);

	}

	@Override//iva12%
	public void calculatePayment(MunicipalBond municipalBond,
			Date paymentServiceDate, boolean isForPay, boolean applyDiscount,
			Object... facts) throws EntryDefinitionNotFoundException {

		// System.out.println("IncomeServiceBean -----> BEGINS CALCULATE
		// PAYMENT");
		municipalBond.setBalance(municipalBond.getValue());
		Deposit lastDeposit = this.getLastDeposit(municipalBond);
		if (lastDeposit != null) {
			municipalBond.setBalance(lastDeposit.getBalance());
		}
		municipalBondService.calculatePayment(municipalBond, paymentServiceDate, lastDeposit, !isForPay, !isForPay,
				applyDiscount, null);
		/*
		 * System.out
		 * .println("\n\n\n\n\nBASE IMPONIBLE EN IncomeService -----> TAXABLE "
		 * + municipalBond.getTaxableTotal() + " TAXES TOTAL " +
		 * municipalBond.getTaxesTotal());
		 */
	}

	@Override
	public void calculatePayment(List<MunicipalBond> municipalBonds, Date paymentServiceDate, boolean isForPay,
			boolean applyDiscount, Object... facts) throws EntryDefinitionNotFoundException {
		for (MunicipalBond municipalBond : municipalBonds) {
			this.calculatePayment(municipalBond, paymentServiceDate, isForPay, applyDiscount, facts);
		}
	}

	public void setAsPrinted(List<Long> printedDepositIds) {
		if (printedDepositIds != null && printedDepositIds.size() > 0) {
			Query query = entityManager.createNamedQuery("Deposit.setAsPrinted");
			query.setParameter("printedDepositIds", printedDepositIds);
			query.executeUpdate();
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void updateReprintings(Long municipalBondId) {
		if (municipalBondId != null) {
			MunicipalBond municipalBond = entityManager.getReference(MunicipalBond.class, municipalBondId);
			municipalBond.setPrintingsNumber(municipalBond.getPrintingsNumber() + 1);
			entityManager.flush();
			/*
			 * Query query =
			 * entityManager.createNamedQuery("MunicipalBond.updateReprintings")
			 * ; query.setParameter("municipalBondId", municipalBondId);
			 * query.executeUpdate();
			 */
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void updateMunicipalBonds(List<MunicipalBond> bonds) {
		for (MunicipalBond mb : bonds) {
			entityManager.merge(mb);
			entityManager.flush();
		}

	}

	public void saveForCompensationPayment(List<MunicipalBond> municipalBonds,
			Long tillId) throws Exception {
		//System.out.println("\n\nInicia grabacion para pago por compensacion");
		Workday workday = findActiveWorkday();
		Long COMPENSATION_STATUS_ID = systemParameterService.findParameter(COMPENSATION_BOND_STATUS);
		for (MunicipalBond municipalBond : municipalBonds) {
			setToNextStatus(municipalBond, COMPENSATION_STATUS_ID, tillId, workday.getDate());
		}
	}

	private Deposit generateDeposit(MunicipalBond municipalBond,
			Date paymentDate, Person cashier, Long tillId, String externalTransactionId) {
		Payment payment = new Payment();
		payment.setDate(paymentDate);
		payment.setTime(paymentDate);
		payment.setCashier(cashier);
		payment.setStatus(FinancialStatus.VALID);
		payment.setValue(municipalBond.getPaidTotal());
		payment.setExternalTransactionId(externalTransactionId);

		Query query = entityManager.createNamedQuery("Till.findById");
		query.setParameter("tillId", tillId);
		Till till = (Till) query.getSingleResult();
		if (till.isTillBank()) {
			payment.getPaymentFractions().get(0).setPaidAmount(municipalBond.getPaidTotal());
			payment.getPaymentFractions().get(0).setReceivedAmount(municipalBond.getPaidTotal());
		}
		Deposit deposit = new Deposit();
		deposit.setBalance(BigDecimal.ZERO);
		deposit.setCapital(municipalBond.getValue());
		deposit.setConcept(municipalBond.getEntry().getName());
		deposit.setDate(paymentDate);
		deposit.setInterest(municipalBond.getInterest());
		deposit.setIsPrinted(Boolean.FALSE);
		deposit.setNumber(1);
		deposit.setPaidTaxes(municipalBond.getTaxesTotal());
		deposit.setStatus(FinancialStatus.VALID);
		deposit.setTime(paymentDate);
		deposit.setValue(municipalBond.getPaidTotal());

		municipalBond.add(deposit);
		payment.add(deposit);

		return deposit;
	}

	// @tag recaudacionCoactiva
	// agregar el tipo de pago Interes/recargo/impuesto
	@SuppressWarnings("unchecked")
	public void save(Date paymentDate, List<Long> municipalBondIds,
			Person cashier, Long tillId, String externalTransactionId, String paymentMethod) throws Exception {
		Query query = entityManager
				.createNamedQuery("MunicipalBond.findSimpleByIds");
		query.setParameter("municipalBondIds", municipalBondIds);
		List<MunicipalBond> paidMunicipalBonds = query.getResultList();

		List<Deposit> deposits = new LinkedList<Deposit>();
		for (MunicipalBond municipalBond : paidMunicipalBonds) {
			Deposit deposit = generateDeposit(municipalBond, paymentDate,
					cashier, tillId, externalTransactionId);
			deposits.add(deposit);
		}
		save(deposits, null, tillId, paymentMethod);
	}

	public void deactivateCreditNotes(List<PaymentFraction> paymentFractions) {
		//System.out.println("DEACTIVATING CREDIT NOTES");
		for (PaymentFraction fraction : paymentFractions) {
			if (fraction.getPaymentType() == PaymentType.CREDIT_NOTE) {
				// CreditNote creditNote = fraction.getCreditNote();
				CreditNote creditNote = entityManager.getReference(
						CreditNote.class, fraction.getCreditNote().getId());
				/*System.out.println("CHECKING CREDIT NOTE "
						+ fraction.getCreditNote().getId() + " AVAILABLE "
						+ creditNote.getAvailableAmount() + " PAID "
						+ fraction.getPaidAmount());*/
				creditNote.setAvailableAmount(creditNote.getAvailableAmount()
						.subtract(fraction.getPaidAmount()));
				if (creditNote.getAvailableAmount().compareTo(BigDecimal.ZERO) == 0) {
					/*System.out
							.println("CREDIT NOTE "
									+ fraction.getCreditNote().getId()
									+ " DEACTIVATED");*/
					creditNote.setIsActive(Boolean.FALSE);
				}
				entityManager.persist(creditNote);
			}
		}
	}

	// @tag recaudacionCoactiva
	// agregar el tipo de pago Interes/recargo/impuesto
	public void save(List<Deposit> deposits, Long paymentAgreementId, Long tillId, String paymentMethod) throws Exception {
		// System.out.println("\n\nInicia grabacion");
		Long PAID_STATUS_ID = systemParameterService.findParameter(PAID_BOND_STATUS);
		
		Long SUBSCRIPTION_STATUS_ID = systemParameterService.findParameter(SUBSCRIPTION_BOND_STATUS);
		for (Deposit deposit : deposits) {
			MunicipalBond municipalBond = deposit.getMunicipalBond();
			/*System.out.println("BASE IMPONIBLE EN PaymentHome -----> TAXABLE "
					+ municipalBond.getTaxableTotal() + " TAXES TOTAL "
					+ municipalBond.getTaxesTotal());*/
			entityManager.persist(deposit);

			// @author macartuche
			// @date 2016-06-21
			// @tag recaudacionesCoactivas
			createMunicipalBondsAux(deposit, municipalBond, paymentMethod);
			
			//agregado para abonos
			if(paymentMethod.equals(PaymentMethod.SUBSCRIPTION.name())) {
				
				Query query = entityManager.createQuery("Select m from MunicipalBond m where id=:id");
				query.setParameter("id", municipalBond.getId());				
				MunicipalBond municipalBondUpdate = (MunicipalBond) query.getSingleResult();
				
				
				query = entityManager.createNamedQuery("MunicipalBondStatus.findById");
				query.setParameter("id", SUBSCRIPTION_STATUS_ID);
				MunicipalBondStatus subscriptionBondStatus = (MunicipalBondStatus) query.getSingleResult();

				municipalBondUpdate.setBalance(deposit.getBalance());
				municipalBondUpdate.setMunicipalBondStatus(subscriptionBondStatus);
				entityManager.merge(municipalBondUpdate);
			}
			//fin abonos
			
			if (deposit.getBalance().compareTo(BigDecimal.ZERO) == 0) {
				setToNextStatus(municipalBond, PAID_STATUS_ID, tillId, deposit.getDate());
			}
		}
		Date rightNow = new Date();
		deactivatePaymentAgreement(paymentAgreementId, rightNow);
		// System.out.println("Termina grabacion");
	}

	private void createMunicipalBondsAux(Deposit dep, MunicipalBond mb, String paymentMethod) {
		BigDecimal sum = BigDecimal.ZERO;
		BigDecimal itemValue = BigDecimal.ZERO;
		BigDecimal depositValue = BigDecimal.ZERO;
		Boolean itemIsPayed = false;

		if (dep.getInterest().compareTo(BigDecimal.ZERO) > 0) {
			sum = sumAccumulatedInterest(mb.getId(), false, "VALID", "I", paymentMethod);
			itemValue = mb.getInterest();
			depositValue = dep.getInterest();
			if (sum != null && sum.compareTo(BigDecimal.ZERO) >= 0) {
				BigDecimal temp = depositValue.add(sum);
				if (temp.compareTo(itemValue) >= 0)
					itemIsPayed = true;
			} else if (sum == null && depositValue.compareTo(itemValue) >= 0 && mb.getPaymentAgreement() != null) {
				itemIsPayed = true;
			}else if(sum == null && depositValue.compareTo(itemValue) >= 0 && paymentMethod.equals(PaymentMethod.SUBSCRIPTION.name())) {
				itemIsPayed = true;
			}

			if (mb.getPaymentAgreement() != null || paymentMethod.equals(PaymentMethod.SUBSCRIPTION.name())) {
				//si item esta pagado poner valor a true
				if(itemIsPayed)
					updateMunicipalbondAux(mb, "I");
				
				MunicipalbondAux munAux = createBondAux(dep, mb, itemIsPayed, "I", paymentMethod);
				entityManager.persist(munAux);
			}
		}

		sum = BigDecimal.ZERO;
		itemValue = BigDecimal.ZERO;
		depositValue = BigDecimal.ZERO;
		itemIsPayed = false;

		if (dep.getCapital().compareTo(BigDecimal.ZERO) > 0) {
			sum = sumAccumulatedInterest(mb.getId(), false, "VALID", "C", paymentMethod);
			itemValue = mb.getBalance();
			depositValue = dep.getCapital();
			/*
			if (sum != null && sum.compareTo(BigDecimal.ZERO) >= 0) {
				BigDecimal temp = depositValue.add(sum);
				if (temp.compareTo(itemValue) >= 0)
					itemIsPayed = true;
			} else if (sum == null && depositValue.compareTo(itemValue) >= 0 && mb.getPaymentAgreement() != null) {
				itemIsPayed = true;
			}*/
			if( dep.getBalance().compareTo(BigDecimal.ZERO) == 0 && mb.getPaymentAgreement() != null ) {
				itemIsPayed = true;
			}
			
			if(paymentMethod.equals(PaymentMethod.SUBSCRIPTION.name()) && dep.getBalance().compareTo(BigDecimal.ZERO)==0) {
				itemIsPayed = true;
			}

			if (mb.getPaymentAgreement() != null || paymentMethod.equals(PaymentMethod.SUBSCRIPTION.name())) {
				MunicipalbondAux munAux = createBondAux(dep, mb, itemIsPayed, "C", paymentMethod);
				entityManager.persist(munAux);
			}
		}

		sum = BigDecimal.ZERO;
		itemValue = BigDecimal.ZERO;
		depositValue = BigDecimal.ZERO;
		itemIsPayed = false; ////////////////////////////////////////////////////////////////////

		if (dep.getPaidTaxes().compareTo(BigDecimal.ZERO) > 0) {
			sum = sumAccumulatedInterest(mb.getId(), false, "VALID", "T", paymentMethod);
			itemValue = mb.getTaxesTotal();
			depositValue = dep.getPaidTaxes();
			if (sum != null && sum.compareTo(BigDecimal.ZERO) >= 0) {
				BigDecimal temp = depositValue.add(sum);
				if (temp.compareTo(itemValue) >= 0)
					itemIsPayed = true;
			} else if (sum == null && depositValue.compareTo(itemValue) >= 0 && mb.getPaymentAgreement() != null) {
				itemIsPayed = true;
			}else if(sum == null && depositValue.compareTo(itemValue) >= 0 && paymentMethod.equals(PaymentMethod.SUBSCRIPTION.name())) {
				itemIsPayed = true;
			}

			if (mb.getPaymentAgreement() != null || paymentMethod.equals(PaymentMethod.SUBSCRIPTION.name())) {
				if(itemIsPayed)
					updateMunicipalbondAux(mb, "T");
				MunicipalbondAux munAux = createBondAux(dep, mb, itemIsPayed, "T", paymentMethod);
				entityManager.persist(munAux);
			}
		}

		sum = BigDecimal.ZERO;
		itemValue = BigDecimal.ZERO;
		depositValue = BigDecimal.ZERO;
		itemIsPayed = false;

		if (dep.getSurcharge().compareTo(BigDecimal.ZERO) > 0) {
			sum = sumAccumulatedInterest(mb.getId(), false, "VALID", "S", paymentMethod);
			itemValue = mb.getSurcharge();
			depositValue = dep.getSurcharge();
			if (sum != null && sum.compareTo(BigDecimal.ZERO) >= 0) {
				BigDecimal temp = depositValue.add(sum);
				if (temp.compareTo(itemValue) >= 0)
					itemIsPayed = true;
			} else if (sum == null && depositValue.compareTo(itemValue) >= 0 && mb.getPaymentAgreement() != null) {
				itemIsPayed = true;
			}else if(sum == null && depositValue.compareTo(itemValue) >= 0 && paymentMethod.equals(PaymentMethod.SUBSCRIPTION.name())) {
				itemIsPayed = true;
			}

			if (mb.getPaymentAgreement() != null || paymentMethod.equals(PaymentMethod.SUBSCRIPTION.name())) {
				if(itemIsPayed)
					updateMunicipalbondAux(mb, "S");
				
				MunicipalbondAux munAux = createBondAux(dep, mb, itemIsPayed, "S", paymentMethod);
				entityManager.persist(munAux);
			}
		}
	}
	
	/**
	 * INteres o recargos luego de pagar parte del capital 
	 * @test
	 * @param bond
	 * @param type
	 */
	private void updateMunicipalbondAux(MunicipalBond bond, String type){
		String query = "Select mb from MunicipalbondAux mb"
				+ " where mb.municipalbond=:bond and "
				+ " mb.type=:type and "
//				+ " mb.coveritem=:coveritem and "
				+ " mb.anotherItem is null  ";
		
		Query q = entityManager.createQuery(query);
		q.setParameter("bond", bond);
		q.setParameter("type", type);
//		q.setParameter("coveritem", true);
		List<MunicipalbondAux> list = (List<MunicipalbondAux>)q.getResultList();
		for (MunicipalbondAux municipalbondAux : list) {
			municipalbondAux.setAnotherItem(true);
			entityManager.merge(municipalbondAux);
		}
	}

	private Receipt findActiveReceipt(Long receiptId) {
		Query query = entityManager.createNamedQuery("Receipt.findById");
		query.setParameter("receiptId", receiptId);
		return (Receipt) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public Map<Long, Branch> fillMapBranch() {
		// System.out.println("\n<<<R>>> branch: ");
		Map<Long, Branch> mapBranch = new HashMap<Long, Branch>();
		// System.out.println("\n<<<R>>> branch: " + entityManager);
		Query query = entityManager.createNamedQuery("Branch.findAll");
		List<Branch> list = new ArrayList<Branch>();
		list = query.getResultList();
		for (Branch branch : list) {
			// System.out.println("\n<<<R>>> branch: " + branch.getId());
			mapBranch.put(branch.getId(), branch);
		}
		return mapBranch;
	}

	public DataWS authorizedElectronicReceipt(Receipt receipt) throws Exception {
		String sriEnvironment = systemParameterService.findParameter(ELECTRONIC_INVOICE_ENVIRONMENT);
		JAXBContext jaxbContext = JAXBContext.newInstance(Factura.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		String xml_dir = systemParameterService
				.findParameter(ELECTRONIC_INVOICE_XML_DIR);
		String dirName = System.getProperty("user.home") + File.separator
				+ xml_dir;
		//System.out.println("Output sent to " + dirName);
		new File(dirName).mkdirs();

		String xmlFileName = dirName + File.separator + receipt.toString()
				+ ".xml";
		//System.out.println("File Saved: " + xmlFileName);
		File output = new File(xmlFileName);
		ElectronicServiceBean elecbean = new ElectronicServiceBean();
		// elecbean.fillMapBranch();
		// agregado macartuche , ya que da error al consultar en el
		// electronicBean
		elecbean.setMapBranch(fillMapBranch());

		// agregado macartuche
		XmlTransform.CFO = getCFO();
		Factura factura = XmlTransform.transform(receipt, sriEnvironment, elecbean.getMapBranch());
		marshaller.marshal(factura, output);

		byte[] document;
		DataWS dataWS = new DataWS();
		try {
			document = FileUtilities.convertir_file_to_ByteArray(output);
			String checksum = FileUtilities.checkSumApacheCommons(xmlFileName);
			dataWS.setXmlFile(document);
			dataWS.setRucCompany(receipt.getMunicipalBond().getInstitution()
					.getNumber());
			//System.out.println("cliente checksum ");
			dataWS.setCheksum(checksum);
			dataWS = sendToService(dataWS);
			new File(xmlFileName).delete();
		} catch (IOException ex) {
			Logger.getLogger(IncomeServiceBean.class.getName()).log(Level.SEVERE, null, ex);
		}

		return dataWS;
	}

	/**
	 * Obtener el director(a) financiero actual CFO => siglas en ingles
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getCFO() {
		Query query = entityManager
				.createQuery("Select d from Delegate d " + "where d.charge.id=:id and d.isActive =:active");

		query.setParameter("id", 10L);
		query.setParameter("active", Boolean.TRUE);
		List<Delegate> delegates = query.getResultList();
		String cfoName = "";
		if (delegates.size() > 0) {
			cfoName = delegates.get(0).getName();
		}

		return cfoName;
	}

	public DataWS sendToService(DataWS input) {
		String uriElectronicService = systemParameterService.findParameter(ELECTRONIC_INVOICE_BASE_URI);
		ElectronicClient client = new ElectronicClient(uriElectronicService);
		DataWS response;
		response = client.reception_XML(input, DataWS.class);
		/*System.out.println("Access key " + response.getAccesKey()
				+ " Autorizado en SRI: " + response.getAuthorized()
				+ " Ambiente Contingencia: "
				+ response.getContingencyEnvironment());*/
		return response;
	}

	private void setToNextStatus(MunicipalBond municipalBond, Long nextStatusId, Long tillId, Date paymentDate)
			throws Exception {
		if (municipalBond != null && municipalBond.getId() != null) {
			MunicipalBondStatus paidStatus = systemParameterService.materialize(MunicipalBondStatus.class,
					PAID_BOND_STATUS);		
			Long statusId;
			MunicipalBondStatus statusIdPrevious;
			Branch branch = findBranchByTillId(tillId);
			Till till = findTillById(tillId);
			if (nextStatusId == paidStatus.getId()) {
				statusIdPrevious = municipalBond.getMunicipalBondStatus();
				if (branch.getIsPaidByCompensation()) {
					MunicipalBondStatus mbs = systemParameterService.materialize(MunicipalBondStatus.class,
							COMPENSATION_BOND_STATUS);
					statusId = mbs.getId();
					municipalBond.setMunicipalBondStatus(mbs);
				} else {
					statusId = systemParameterService.findParameter(PAID_BOND_STATUS);
					municipalBond.setMunicipalBondStatus(paidStatus);

				}

				// macartuche
				// obtener el interees de la factura electronica y actual de
				// pago
				// @tag InteresCeroInstPub
				// COMENTADO YA QUE SE USA INTERES=0 A INST_PUB
				// Receipt rec = municipalBond.getReceipt();
				// if(rec!=null){
				// Query q = entityManager.createQuery("Select cr from
				// CompensationReceipt cr where cr.receipt=:receipt");
				// q.setParameter("receipt", rec);
				// List<CompensationReceipt> list = q.getResultList();
				// if(!list.isEmpty()){
				// CompensationReceipt cr = list.get(0);
				// BigDecimal residue =
				// municipalBond.getInterest().subtract(cr.getInterest());
				// if(residue.compareTo(BigDecimal.ZERO)==1){
				// cr.setResidualInterest(residue);
				// }
				// System.out.println("Se ha registrado el residuo de:
				// "+residue);
				// entityManager.merge(cr);
				// }
				// }

				// Se realiza calculo de la liquidacion final
				municipalBond.setBalance(BigDecimal.ZERO);
				municipalBond.setInterest(municipalBond.getInterestTotal());
				municipalBond.setDiscount(municipalBond.getDiscount());
				municipalBond.setSurcharge(municipalBond.getSurcharge());
				municipalBond.setTaxableTotal(municipalBond.getTaxableTotal());
				municipalBond.setNonTaxableTotal(municipalBond.getNonTaxableTotal());
				municipalBond.setTaxesTotal(municipalBond.getTaxesTotal());
				municipalBond.setPaidTotal(municipalBond.findPaidTotal());
				municipalBond.setLiquidationDate(paymentDate);
				municipalBond.setLiquidationTime(paymentDate);
				municipalBond.setPrintingsNumber(1);
			} else {
				statusIdPrevious = municipalBond.getMunicipalBondStatus();
				municipalBond.setMunicipalBondStatus(findMunicipalBondStatusById(nextStatusId));
				statusId = nextStatusId;
			}

			Query query = entityManager.createNamedQuery("MunicipalBondStatus.findById");
			query.setParameter("id", statusId);
			MunicipalBondStatus newBondStatus = (MunicipalBondStatus) query.getSingleResult();

			municipalBond.setMunicipalBondStatus(newBondStatus);
			entityManager.merge(municipalBond);

			Boolean IS_AUTO_EMMITER = systemParameterService
					.findParameter(ENABLE_RECEIPT_GENERATION);
			Boolean IS_ELECTRONIC_INVOICE_ENABLE = systemParameterService
					.findParameter(ELECTRONIC_INVOICE_ENABLE);
			//System.out.println("AUTOEMITER=>" + IS_AUTO_EMMITER);

			if (IS_AUTO_EMMITER) {
				// System.out.println("SIIII=>");
				if (municipalBond.getEntry().getIsTaxable()) {
					// System.out.println("SIIII=>");
					if (municipalBond.getReceipt() == null) {
						Receipt receipt = createReceipt(municipalBond, branch.getNumber(), till.getNumber(),
								paymentDate);
						// System.out.println("===========>" +
						// till.getNumber());

						// PREGUNTAR SI ES POR COMPENSACION AGREGAR UN CAMPO MAS
						// EN LA FACTURA
						// macartuche
						if (municipalBond.getMunicipalBondStatus().getId() == 7L) {
							receipt.setCompensation(true);
						}

						// receipt.setStatusElectronicReceipt(StatusElectronicReceipt.AUTOIMPRESOR);
						entityManager.persist(receipt);
						municipalBond.setReceipt(receipt);
						if (IS_ELECTRONIC_INVOICE_ENABLE) {
							// System.out
							// .println("isElectronicInvoiceEnable==================>"
							// + till.isElectronicInvoiceEnable());

							if (till.isElectronicInvoiceEnable()) {
								// System.out
								// .println("isEmissionElectronicOnLine==================>"
								// + till.isEmissionElectronicOnLine());
								if (till.isEmissionElectronicOnLine()) {

									DataWS authorizedReceiptWS = authorizedElectronicReceipt(receipt);
									// System.out.println("<<<R>>> Messages SRI:
									// "
									// +
									// authorizedReceiptWS.getMessageList().toString());
									/*System.out.println("AUTHORIZADO?"
											+ authorizedReceiptWS
													.getAuthorized());*/
									if (authorizedReceiptWS.getAuthorized()) {
										receipt.setAuthorizationNumber(authorizedReceiptWS.getAuthorizationNumber());
										receipt.setSriAuthorizationDate(authorizedReceiptWS.getAuthorizationDate());
										receipt.setStatusElectronicReceipt(StatusElectronicReceipt.AUTHORIZED);
									} else {
										if (authorizedReceiptWS.getContingencyEnvironment()) {
											receipt.setAuthorizationNumber("");
											receipt.setSriAuthorizationDate(paymentDate);
											receipt.setStatusElectronicReceipt(StatusElectronicReceipt.CONTINGENCY);
										} else {
											receipt.setAuthorizationNumber("");
											receipt.setSriAuthorizationDate(paymentDate);
											receipt.setStatus(FinancialStatus.VOID);
											receipt.setStatusElectronicReceipt(StatusElectronicReceipt.VOID);
										}
									}

									if (authorizedReceiptWS.getAuthorized()) {
										receipt.setSriAccessKey(authorizedReceiptWS.getAccesKey());
										receipt.setSriContingencyEnvironment(
												authorizedReceiptWS.getContingencyEnvironment());
										receipt.setStatusElectronicReceipt(StatusElectronicReceipt.AUTHORIZED);
										entityManager.merge(receipt);
										entityManager.flush();

										// No realizar el calculo de interes
										// para instituciones publicas
										// createCompensationReceipt
										// @author macartuche
										// @date 2016-06-27 17:25
										// @tag InteresCeroInstPub
										// createCompensationReceipt(receipt,municipalBond);

									} else {
										if (authorizedReceiptWS.getContingencyEnvironment()) {
											receipt.setSriAccessKey(authorizedReceiptWS.getAccesKey());
											receipt.setSriContingencyEnvironment(
													authorizedReceiptWS.getContingencyEnvironment());
											receipt.setStatusElectronicReceipt(StatusElectronicReceipt.CONTINGENCY);
											entityManager.merge(receipt);
											entityManager.flush();
										} else {
											deletePaymentFromMunicipalBond(municipalBond.getId(), paymentDate);
											municipalBond.setMunicipalBondStatus(statusIdPrevious);
											municipalBond.setLiquidationDate(null);
											municipalBond.setLiquidationTime(null);
											entityManager.merge(municipalBond);
											receipt.setStatus(FinancialStatus.VOID);
											receipt.setStatusElectronicReceipt(StatusElectronicReceipt.VOID);
											receipt.setReversedMunicipalBond(municipalBond);
											receipt.setMunicipalBond(null);
											entityManager.merge(receipt);
											entityManager.flush();
											throw new Exception();
										}
									}

								} else {
									receipt.setSriAccessKey("0000000000000000000000000000000000000000000000000");
									receipt.setSriContingencyEnvironment(false);
									receipt.setAuthorizationNumber("");
									receipt.setSriAuthorizationDate(paymentDate);
									receipt.setStatusElectronicReceipt(StatusElectronicReceipt.PENDING);
									entityManager.merge(receipt);
									entityManager.flush();
								}
							}
						}
					} else {
						Long receiptId = municipalBond.getReceipt().getId();
						Receipt receipt = findActiveReceipt(receiptId);
						municipalBond.setReceipt(receipt);

						// @author macartuche
						// @date 2016-06-27 17:26
						// @tag InteresCeroInstPub
						// No realizar el calculo de interes para instituciones
						// publicas

						// if(!receipt.getStatusElectronicReceipt().equals(StatusElectronicReceipt.AUTOIMPRESOR)){
						//// verificar si la factura ya fue autorizada
						// anteriormente
						// if (receipt.getStatusElectronicReceipt() ==
						// StatusElectronicReceipt.AUTHORIZED) {
						//// createCompensationReceipt
						// createCompensationReceipt(receipt, municipalBond);
						// }
						// }
					}
				}
			}
		}
	}

	// @author macartuche
	// @date 2016-06-27 17:25
	// @tag InteresCeroInstPub
	// No realizar el calculo de interes para instituciones publicas
	// @SuppressWarnings("unchecked")
	// private void createCompensationReceipt(Receipt receipt,
	// MunicipalBond municipalBond) {
	// // grabar el interes que tiene la
	// // factura electronica
	// //crear solo en el caso de que no exista
	// Query q = entityManager.createQuery("Select cr from CompensationReceipt
	// cr "
	// + "where cr.serviceDate=:serviceDate and "
	// + "cr.groupingCode=:groupingCode and "
	// + "cr.receipt=:receipt");
	// q.setParameter("serviceDate", municipalBond.getServiceDate());
	// q.setParameter("groupingCode", municipalBond.getGroupingCode());
	// q.setParameter("receipt", receipt);
	// List<CompensationReceipt> crlist = q.getResultList();
	// if(crlist.isEmpty()){
	// CompensationReceipt compensationDetail = new CompensationReceipt();
	// compensationDetail.setServiceDate(municipalBond.getServiceDate());
	// compensationDetail.setGroupingCode(municipalBond.getGroupingCode());
	// compensationDetail.setInterest(municipalBond.getInterest());
	// compensationDetail.setReceipt(receipt);
	// compensationDetail.setAvailable(Boolean.TRUE);
	// compensationDetail.setIsPaid(Boolean.FALSE);
	// entityManager.merge(compensationDetail);
	// entityManager.flush();
	// }
	// }

	private void deletePaymentFromMunicipalBond(Long municipalBondId, Date date) {
		Long paymentId = new Long(0);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String sql = "select payment_id from deposit where municipalbond_id = "
				+ municipalBondId + " and date='" + df.format(date) + "';";
		//System.out.println("<<<R>>> sql: " + sql);
		Query query = entityManager.createNativeQuery(sql);
		paymentId = (Long) query.getSingleResult();
		query = null;
		if (paymentId > 0) {
			sql = "delete from paymentfraction where payment_id = " + paymentId;
			//System.out.println("<<<R>>> sql: " + sql);
			query = entityManager.createNativeQuery(sql);
			query.executeUpdate();
			query = null;
			sql = "delete from deposit where payment_id = " + paymentId;
			//System.out.println("<<<R>>> sql: " + sql);
			query = entityManager.createNativeQuery(sql);
			query.executeUpdate();
			query = null;
			sql = "delete from payment where payment_id = " + paymentId;
			//System.out.println("<<<R>>> sql: " + sql);
			query = entityManager.createNativeQuery(sql);
			query.executeUpdate();
			query = null;
		}
	}

	private Receipt createReceipt(MunicipalBond municipalBond, Integer branchNumber, Integer tillNumber,
			Date paymentDate) throws Exception {
		Entry entry = municipalBond.getEntry();
		try {
			Long INITIAL_RECEIPT_NUMBER = systemParameterService.findParameter("INITIAL_RECEIPT_NUMBER");
			Long FINAL_RECEIPT_NUMBER = systemParameterService.findParameter("FINAL_RECEIPT_NUMBER");
			Boolean IS_ELECTRONIC_RECEIPT = systemParameterService.findParameter(ELECTRONIC_INVOICE_ENABLE);

			ReceiptType receiptType = findReceiptType(entry.getId());
			entry.setReceiptType(receiptType);
			Long receiptTypeId = receiptType.getId();

			ReceiptAuthorization authorization = findReceiptAuthorization(entry, paymentDate);

			Long number = findReceiptValue(authorization.getTaxpayerRecord().getId(), receiptTypeId, branchNumber,
					tillNumber);

			if (number > FINAL_RECEIPT_NUMBER) {
				resetSequence(authorization.getTaxpayerRecord().getId(), receiptTypeId, branchNumber, tillNumber,
						INITIAL_RECEIPT_NUMBER);
				number = INITIAL_RECEIPT_NUMBER;
			}

			if (number >= INITIAL_RECEIPT_NUMBER && number <= FINAL_RECEIPT_NUMBER) {
				Receipt receipt = new Receipt();
				receipt.setDate(paymentDate);
				receipt.setReceiptAuthorization(authorization);
				receipt.setAuthorizationNumber(authorization.getAuthorizationNumber());
				receipt.setElectronicReceipt(IS_ELECTRONIC_RECEIPT);
				receipt.setSequential(number);
				receipt.setBranch(branchNumber);
				receipt.setTill(tillNumber);
				receipt.setStatus(FinancialStatus.VALID);
				receipt.setMunicipalBond(municipalBond);
				receipt.setReceiptType(entry.getReceiptType());
				receipt.setStatusElectronicReceipt(StatusElectronicReceipt.NONE);
				receipt.setSriAccessKey("0000000000000000000000000000000000000000000000000");
				receipt.setReceiptNumber(receipt.toString());
				return receipt;
			} else {
				throw new InvoiceNumberOutOfRangeException(number);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * private Long findNextReceiptValue(Long taxpayerRecordId, Long
	 * receiptTypeId, Integer branchNumber, Integer tillNumber){
	 * System.out.println("LXGK-SRI -----> TRYING TO GET A SEQUENCE");
	 * 
	 * return nextReceiptNumber; }
	 */

	private Long findReceiptValue(Long taxpayerRecordId, Long receiptTypeId, Integer branchNumber, Integer tillNumber) {
		Query query = entityManager.createNativeQuery("SELECT nextval('"
				+ buildSequenceName(taxpayerRecordId, receiptTypeId, branchNumber, tillNumber) + "')");
		return ((BigInteger) query.getResultList().get(0)).longValue();
	}

	private String buildSequenceName(Long taxpayerRecordId, Long receiptTypeId, Integer branchNumber,
			Integer tillNumber) {
		// WARNING: Los identificadores en Postgresql son siempre en MINUSCULAS
		return "auth_" + taxpayerRecordId + "_" + receiptTypeId + "_" + branchNumber + "_" + tillNumber;
	}

	private void resetSequence(Long taxpayerRecordId, Long receiptTypeId, Integer branchNumber, Integer tillNumber,
			Long initialValue) {
		String sequenceName = buildSequenceName(taxpayerRecordId, receiptTypeId, branchNumber, tillNumber);
		String alterSentence = "ALTER SEQUENCE " + sequenceName + " RESTART WITH " + initialValue;
		Query query = entityManager.createNativeQuery(alterSentence);
		query.executeUpdate();
	}

	// TODO Colocarlo como un named native query con parametros?
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void createSequence(Long taxpayerRecordId, Long receiptTypeId,
			Integer branchNumber, Integer tillNumber) {
		Long initialReceiptNumber = systemParameterService
				.findParameter("INITIAL_RECEIPT_NUMBER");
		Long finalReceiptNumber = systemParameterService
				.findParameter("FINAL_RECEIPT_NUMBER");
		String sequenceName = buildSequenceName(taxpayerRecordId,
				receiptTypeId, branchNumber, tillNumber);
		//System.out.println("LXGK-SRI -----> Sequence CHECKIN sequence "
				//+ sequenceName);
		String stripedSequenceName = sequenceName.substring(
				sequenceName.indexOf('.') + 1).toLowerCase();
		if (!sequenceNameExists(stripedSequenceName)) {
			String createSentence = "CREATE SEQUENCE " + sequenceName + " START " + initialReceiptNumber + " MINVALUE "
					+ initialReceiptNumber + " MAXVALUE " + finalReceiptNumber;
			Query query = entityManager.createNativeQuery(createSentence);
			query.executeUpdate();
			//System.out.println("LXGK-SRI -----> Sequence created ");
			return;
		}
		System.out.println("LXGK-SRI -----> Sequence already exists, skipping creation");
	}

	@SuppressWarnings("unchecked")
	private Boolean sequenceNameExists(String sequenceName) {
		String checkSequenceQuery = "SELECT c.relname FROM pg_class c WHERE c.relkind = 'S' AND c.relname='"
				+ sequenceName + "'";
		;
		Query query = entityManager.createNativeQuery(checkSequenceQuery);
		List<String> sequenceNames = query.getResultList();
		if (sequenceNames != null && sequenceNames.size() > 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	private ReceiptType findReceiptType(Long entryId) {
		Query query = entityManager.createNamedQuery("Entry.findReceiptTypeByEntryId");
		query.setParameter("entryId", entryId);
		ReceiptType receiptType = (ReceiptType) query.getSingleResult();
		return receiptType;
	}

	private ReceiptAuthorization findReceiptAuthorization(Entry entry, Date paymentDate)
			throws ReceiptAuthorizationDoesNotExistException {
		TaxpayerRecord taxpayerRecord = municipalBondService.findTaxpayerRecord(entry.getId());

		Query query = entityManager.createNamedQuery("ReceiptAuthorization.findActiveByTaxpayerRecordId");
		query.setParameter("taxpayerRecordId", taxpayerRecord.getId());
		query.setParameter("date", paymentDate);
		// query.setParameter("receiptTypeId", receiptType.getId());
		try {
			ReceiptAuthorization receiptAuthorization = (ReceiptAuthorization) query.getSingleResult();
			return receiptAuthorization;
		} catch (Exception e) {
			throw new ReceiptAuthorizationDoesNotExistException();
		}
	}

	private void deactivatePaymentAgreement(Long paymentAgreementId, Date rightNow) {
		if (paymentAgreementId != null) {
			Query query = entityManager.createNamedQuery("PaymentAgreement.deactivate");
			query.setParameter("isActive", Boolean.FALSE);
			query.setParameter("closingDate", rightNow);
			query.setParameter("closingTime", rightNow);
			query.setParameter("paymentAgreementId", paymentAgreementId);
			query.executeUpdate();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Deposit> findDepositsForReverse(Long residentId, Long cashierId) {
		if (residentId != null) {
			Query query = entityManager.createNamedQuery("Deposit.findDepositsByResidentIdAndCashierAndDate");
			query.setParameter("residentId", residentId);
			query.setParameter("cashierId", cashierId);
			query.setParameter("date", new Date());
			return query.getResultList();
		}
		return null;
	}

	public void reverse(List<Long> depositIds, String concept, Resident userReversed)
			throws ReverseNotAllowedException, ReverseAmongPaymentsIsNotAllowedException {
		List<Payment> payments = findPayments(depositIds);
		// System.out.println("REVERSE -----> Deposits "+depositIds+" size
		// "+depositIds.size());
		// System.out.println("REVERSE -----> Payments selected
		// "+payments.size());
		if (payments.size() > 1) {
			throw new ReverseAmongPaymentsIsNotAllowedException();
		}

		List<Deposit> deposits = findDeposits(depositIds);
		// System.out.println("REVERSE -----> Deposits "+deposits+" size
		// "+deposits.size());
		Payment payment = payments.get(0);
		List<PaymentFraction> paymentFractions = findPaymentFractions(payment.getId());
		// System.out.println("REVERSE -----> paymentFractions
		// "+paymentFractions+" size "+paymentFractions.size());

		BigDecimal totalToReverse = BigDecimal.ZERO;
		for (Deposit deposit : deposits) {
			totalToReverse = totalToReverse.add(deposit.getValue());
		}

		BigDecimal reversableAmount = BigDecimal.ZERO;
		BigDecimal nonCashAmount = BigDecimal.ZERO;
		List<PaymentFraction> fractions = new LinkedList<PaymentFraction>();
		Boolean isFullPaymentReverse = payment.getDeposits().size() == deposits.size();
		for (PaymentFraction fraction : paymentFractions) {
			if (isFullPaymentReverse || fraction.getPaymentType() == PaymentType.CASH) {
				reversableAmount = reversableAmount.add(fraction.getPaidAmount());
				fractions.add(fraction);
			} else {
				if (fraction.getPaymentType() != PaymentType.CASH) {
					nonCashAmount = nonCashAmount.add(fraction.getPaidAmount());
				}
			}
		}

		// System.out.println("REVERSE -----> totalToReverse "+totalToReverse);
		// System.out.println("REVERSE -----> reversableAmount
		// "+reversableAmount);
		// System.out.println("REVERSE -----> nonCashAmount "+nonCashAmount);
		// System.out.println("REVERSE -----> Fractions involved "+fractions+"
		// size "+fractions.size());

		// Se verifica si es posible realizar la reversa

		if (reversableAmount.compareTo(totalToReverse) < 0) {
			if (nonCashAmount.add(reversableAmount).compareTo(totalToReverse) == 0) {
				fractions = paymentFractions;
				isFullPaymentReverse = Boolean.TRUE;
			} else {
				throw new ReverseNotAllowedException();
			}
		}

		payment.setValue(payment.getValue().subtract(totalToReverse));
		if (isFullPaymentReverse) {
			payment.setStatus(FinancialStatus.VOID);
		}

		BigDecimal remaining = new BigDecimal(totalToReverse.doubleValue());
		for (PaymentFraction fraction : fractions) {
			if (isFullPaymentReverse || remaining.compareTo(fraction.getPaidAmount()) > 0) {
				if (fraction.getPaymentType() == PaymentType.CREDIT_NOTE) {
					Long creditNoteId = fraction.getCreditNote().getId();
					reactivateCreditNote(creditNoteId, fraction.getPaidAmount());
				}
				fraction.setPaidAmount(BigDecimal.ZERO);
				fraction.setReceivedAmount(BigDecimal.ZERO);
			} else {
				fraction.setPaidAmount(fraction.getPaidAmount().subtract(remaining));
			}
			remaining = remaining.subtract(fraction.getPaidAmount());
		}
		// PREVIOUS ALGORITHM
		Long PENDING_STATUS_ID = systemParameterService.findParameter(PENDING_BOND_STATUS);
		changeMunicipalBondsStatus(depositIds, PENDING_STATUS_ID);
		changeDepositStatus(depositIds, concept, FinancialStatus.VOID, userReversed);
		entityManager.persist(payment);

	}

	@SuppressWarnings("unchecked")
	private List<PaymentFraction> findPaymentFractions(Long paymentId) {
		Query query = entityManager.createNamedQuery("PaymentFraction.findByPaymentId");
		query.setParameter("paymentId", paymentId);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	private List<Deposit> findDeposits(List<Long> depositIds) {
		Query query = entityManager.createNamedQuery("Deposit.findByIds");
		query.setParameter("depositIds", depositIds);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	private List<Payment> findPayments(List<Long> depositIds) {
		Query query = entityManager.createNamedQuery("Deposit.findPaymentsByDepositIds");
		query.setParameter("depositIds", depositIds);
		return query.getResultList();
	}

	public void eliminateReverse(Long depositId, Resident userReversed) {
		System.out.println("REHABILITAR EN SB ---> " + depositId);
		List<Long> depositIds = new ArrayList<Long>();
		depositIds.add(depositId);
		Long PAID_STATUS_ID = systemParameterService.findParameter(PAID_BOND_STATUS);
		try {
			changeMunicipalBondsStatus(depositIds, PAID_STATUS_ID);
		} catch (ReverseNotAllowedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		changeDepositStatus(depositIds, null, FinancialStatus.VALID, userReversed);
	}

	private Branch findBranchByTillId(Long tillId) {
		Query query = entityManager.createNamedQuery("Till.findBranchByTillId");
		query.setParameter("tillId", tillId);
		Branch branch = (Branch) query.getSingleResult();
		return branch;
	}

	private Till findTillById(Long tillId) {
		Query query = entityManager.createNamedQuery("Till.findById");
		query.setParameter("tillId", tillId);
		Till till = (Till) query.getSingleResult();
		return till;
	}

	@SuppressWarnings("unchecked")
	private void changeMunicipalBondsStatus(List<Long> depositIds, Long municipalBondStatusId)
			throws ReverseNotAllowedException {
		Query query = entityManager.createNativeQuery(
				"SELECT municipalBond_id FROM Deposit d WHERE d.id IN (:depositIds) and d.balance = 0");
		query.setParameter("depositIds", depositIds);
		List<BigInteger> municipalBonds = query.getResultList();
		List<Long> municipalBondIds = new ArrayList<Long>();
		Long PENDING_STATUS_ID = systemParameterService.findParameter(PENDING_BOND_STATUS);
		Long PAYMENT_AGREEMENT_STATUS_ID = systemParameterService.findParameter(IN_PAYMENT_AGREEMENT_BOND_STATUS);
		//rfam 2018-05-07 pagos en abonos
		//Long SUBSCRIPTION_STATUS_ID = systemParameterService.findParameter(SUBSCRIPTION_BOND_STATUS);
		// boolean IS_ELECTRONIC_INVOIVE_ENABLE = systemParameterService
		// .findParameter(ELECTRONIC_INVOICE_ENABLE);
		for (BigInteger biid : municipalBonds) {
			Long municipalBondId = biid.longValue();
			municipalBondIds.add(municipalBondId);

			if (municipalBondStatusId.longValue() == PENDING_STATUS_ID.longValue()) {
				// if (IS_ELECTRONIC_INVOIVE_ENABLE)
				// throw new ReverseNotAllowedException();
				// else
				setReceiptAsVoid(municipalBondId);
			} else {
				setReceiptAsValid(municipalBondId);
			}
		}
		if (municipalBondIds.size() > 0) {
			//todo verificar q se ha
			persistChangeStatus(municipalBondIds, municipalBondStatusId, PENDING_STATUS_ID,
					PAYMENT_AGREEMENT_STATUS_ID);
		}
		
		//rfam reverso abonos 2018-05-08
		query = entityManager.createNativeQuery(
				"SELECT d.municipalBond_id " + 
				"FROM gimprod.Deposit d " + 
				"inner join gimprod.municipalbond mb on d.municipalbond_id = mb.id " + 
				"WHERE d.id IN (:depositIds) and mb.municipalbondstatus_id = 14");
		query.setParameter("depositIds", depositIds);
		List<BigInteger> subscriptionBonds = query.getResultList(); 
		List<Long> subscriptionBondsIds = new ArrayList<Long>();	
		
		for (BigInteger biid : subscriptionBonds) {
			Long municipalBondId = biid.longValue();
			subscriptionBondsIds.add(municipalBondId);			
		}
		
		if (subscriptionBondsIds.size() > 0) {
			persistChangeStatus(subscriptionBondsIds, municipalBondStatusId, PENDING_STATUS_ID,
					PAYMENT_AGREEMENT_STATUS_ID);
		}
	}

	@SuppressWarnings("unchecked")
	private void persistChangeStatus(List<Long> municipalBondIds, Long municipalBondStatusId, Long PENDING_STATUS_ID,
			Long PAYMENT_AGREEMENT_STATUS_ID) {
		Query query = entityManager.createNamedQuery("MunicipalBondStatus.findById");
		query.setParameter("id", municipalBondStatusId);
		MunicipalBondStatus newBondStatus = (MunicipalBondStatus) query.getSingleResult();

		query.setParameter("id", PAYMENT_AGREEMENT_STATUS_ID);
		MunicipalBondStatus paymentAgreementStatus = (MunicipalBondStatus) query.getSingleResult();
		
		//rfam 2018-05-07 pagos en abonos
		Long SUBSCRIPTION_STATUS_ID = systemParameterService.findParameter(SUBSCRIPTION_BOND_STATUS);
		query.setParameter("id", SUBSCRIPTION_STATUS_ID);
		MunicipalBondStatus subscriptiontStatus = (MunicipalBondStatus) query.getSingleResult();
				
		query = entityManager.createNamedQuery("MunicipalBond.findByIdsToChangeStatus");
		query.setParameter("municipalBondIds", municipalBondIds);
		List<MunicipalBond> municipalBonds = query.getResultList();
		for (MunicipalBond mb : municipalBonds) {
			MunicipalBondStatus currentStatus = mb.getMunicipalBondStatus();
			mb.setMunicipalBondStatus(newBondStatus);
			if (newBondStatus.getId().longValue() == PENDING_STATUS_ID.longValue()) {
				mb.setLiquidationDate(null);
				mb.setLiquidationTime(null);
				if (mb.getPaymentAgreement() != null) {
					mb.setMunicipalBondStatus(paymentAgreementStatus);
					reactivatePaymentAgreement(mb.getPaymentAgreement().getId());
				}
				mb.setPrintingsNumber(0);
				
				//para el caso q liquide y tenga abonos, debe retornar a abono
				if(!isSubscriptionBondReverseToPending(mb.getId().longValue())) {
					mb.setMunicipalBondStatus(subscriptiontStatus);
				}
				
				// identificar q es un abono rfam 2018-05-07
				// e identificar si se pone a 3 o conserva el mismo porq ya tiene abonos previos
				/*if (currentStatus.getId().longValue() == SUBSCRIPTION_STATUS_ID) {
					if(!isSubscriptionBondReverseToPending(mb.getId().longValue())) {
						mb.setMunicipalBondStatus(subscriptiontStatus);
					}
				}*/
			} else {
				mb.setLiquidationDate(null);
				mb.setLiquidationTime(null);
			}
		}
	}
	/**
	 * Determina el estado para el reverso de una obligacion de pago en abono
	 * @param mb
	 * @return
	 */
	private Boolean isSubscriptionBondReverseToPending(Long mbId) {
		String sql = "select count(dep) from deposit dep where dep.municipalbond_id = :mbId and dep.status = :status";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("mbId", mbId);
		query.setParameter("status", "VALID");
		BigInteger count = (BigInteger) query.getSingleResult();
		if (count.intValue() > 1) {
			// se queda en 14
			return false;
		} else {
			// pasa a pendiente
			return true;
		}
	}

	private void reactivatePaymentAgreement(Long paymentAgreementId) {
		Query query = entityManager.createNamedQuery("PaymentAgreement.reactivate");
		query.setParameter("paymentAgreementId", paymentAgreementId);
		query.executeUpdate();
	}

	private void reactivateCreditNote(Long creditNoteId, BigDecimal availableAmount) {
		Query query = entityManager.createNamedQuery("CreditNote.reactivate");
		query.setParameter("creditNoteId", creditNoteId);
		query.setParameter("availableAmount", availableAmount);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	private void setReceiptAsVoid(Long municipalBondId) {
		/*
		 * Query query = entityManager.createNamedQuery("Receipt.setAsVoid");
		 * query.setParameter("municipalBondId", municipalBondId);
		 * query.executeUpdate();
		 */
		Query query = entityManager.createNamedQuery("Receipt.findByMunicipalBondId");
		query.setParameter("municipalBondId", municipalBondId);
		List<Receipt> receipts = query.getResultList();

		if (receipts.size() > 0) {
			Receipt receipt = receipts.get(0);
			MunicipalBond municipalBond = receipt.getMunicipalBond();
			receipt.setReversedMunicipalBond(municipalBond);
			receipt.setMunicipalBond(null);
			receipt.setStatus(FinancialStatus.VOID);
			receipt.setStatusElectronicReceipt(StatusElectronicReceipt.VOID);
		}
	}

	@SuppressWarnings("unchecked")
	private void setReceiptAsValid(Long municipalBondId) {
		/*
		 * Query query = entityManager.createNamedQuery("Receipt.setAsValid");
		 * query.setParameter("municipalBondId", municipalBondId);
		 * query.executeUpdate();
		 */

		Query query = entityManager.createNamedQuery("Receipt.findReversedByMunicipalBondId");
		query.setParameter("municipalBondId", municipalBondId);
		List<Receipt> receipts = query.getResultList();
		if (receipts.size() > 0) {
			Receipt receipt = receipts.get(0);
			MunicipalBond municipalBond = receipt.getReversedMunicipalBond();
			receipt.setReversedMunicipalBond(null);
			receipt.setMunicipalBond(municipalBond);
			receipt.setStatus(FinancialStatus.VALID);
		}
	}

	private void changeDepositStatus(List<Long> depositIds, String concept, FinancialStatus status,
			Resident userReversed) {
		String queryName = "Deposit.setAsVoid";
		if (status == FinancialStatus.VALID) {
			queryName = "Deposit.setAsValid";
		}
		Query query = entityManager.createNamedQuery(queryName);
		query.setParameter("concept", concept);
		query.setParameter("status", status);
		query.setParameter("depositIds", depositIds);

		Calendar cal = new GregorianCalendar();
		query.setParameter("reversedDate", cal.getTime());
		query.setParameter("reversedTime", cal.getTime());
		//System.out.println("load resident user");
		query.setParameter("reversedResident", userReversed);
		query.executeUpdate();
	}

	private List<Long> getMunicipalBondIds(List<MunicipalBond> municipalBonds) {
		List<Long> municipalBondIds = new ArrayList<Long>();
		for (MunicipalBond mb : municipalBonds) {
			municipalBondIds.add(mb.getId());
		}
		return municipalBondIds;
	}

	private BigDecimal getMaximumTotal(List<Long> municipalBondIds) {
		Query query = entityManager
				.createNativeQuery("SELECT SUM(m.paidTotal) FROM MunicipalBond m WHERE m.id IN (:municipalBondIds)");
		query.setParameter("municipalBondIds", municipalBondIds);
		/*System.out.println("ESCALAR CLASS ----> "
				+ query.getResultList().get(0));*/
		BigDecimal total = (BigDecimal) (query.getResultList().get(0));
		return total;
	}

	public void createCreditNote(CreditNote creditNote, LegalStatus legalStatus)
			throws CreditNoteValueNotValidException {
		List<Long> municipalBondIds = getMunicipalBondIds(creditNote.getMunicipalBonds());
		if (municipalBondIds != null && municipalBondIds.size() > 0) {
			BigDecimal maximumTotal = getMaximumTotal(municipalBondIds);

			/*System.out.println("MAXIMUM TOTAL ----> " + maximumTotal);
			System.out.println("CREDIT NOTE VALUE ----> "
					+ creditNote.getValue());
			System.out.println("COMPARE TO "
					+ maximumTotal.compareTo(creditNote.getValue()));*/

			if (maximumTotal.compareTo(creditNote.getValue()) >= 0) {
				saveCreditNote(creditNote, municipalBondIds, legalStatus);
			} else {
				throw new CreditNoteValueNotValidException();
			}
		} else {
			entityManager.persist(creditNote);
		}
	}

	private void saveCreditNote(CreditNote creditNote, List<Long> municipalBondIds, LegalStatus legalStatus) {
		entityManager.persist(creditNote);
		entityManager.flush();

		Query query = entityManager.createNamedQuery("MunicipalBond.updateLegalStatus");
		query.setParameter("municipalBondIds", municipalBondIds);
		query.setParameter("legalStatus", legalStatus);
		query.setParameter("creditNoteId", creditNote.getId());
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<Deposit> findDeposits(Long municipalBondId) {
		Query query = entityManager.createNamedQuery("Deposit.findByMunicipalBondId");
		query.setParameter("municipalBondId", municipalBondId);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<MunicipalBond> findPendingBonds(Long residentId) {
		SystemParameterService systemParameterService = ServiceLocator.getInstance()
				.findResource(SystemParameterService.LOCAL_NAME);
		Long pendingMunicipalBondStatusId = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_PENDING");
		Long compensationMunicipalBondStatusId = systemParameterService
				.findParameter("MUNICIPAL_BOND_STATUS_ID_COMPENSATION");

		List<Long> statuses = new ArrayList<Long>();
		statuses.add(pendingMunicipalBondStatusId);
		statuses.add(compensationMunicipalBondStatusId);

		Query query = entityManager.createNamedQuery("MunicipalBond.findByResidentIdAndTypeAndStatus");
		query.setParameter("residentId", residentId);
		query.setParameter("municipalBondType", MunicipalBondType.CREDIT_ORDER);
		query.setParameter("municipalBondStatusIds", statuses);
		
		System.out.println(residentId);
		System.out.println(statuses);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<MunicipalBond> findPendingBondsSubscriptions(Long residentId) {
		SystemParameterService systemParameterService = ServiceLocator.getInstance()
				.findResource(SystemParameterService.LOCAL_NAME);
		Long subscriptionMunicipalBondStatusId = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_SUBSCRIPTION");

		List<Long> statuses = new ArrayList<Long>();
		statuses.add(subscriptionMunicipalBondStatusId);

		Query query = entityManager.createNamedQuery("MunicipalBond.findByResidentIdAndTypeAndStatus");
		query.setParameter("residentId", residentId);
		query.setParameter("municipalBondType", MunicipalBondType.CREDIT_ORDER);
		query.setParameter("municipalBondStatusIds", statuses);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<MunicipalBond> findOnlyPendingBonds(Long residentId) {
		SystemParameterService systemParameterService = ServiceLocator.getInstance()
				.findResource(SystemParameterService.LOCAL_NAME);
		Long pendingMunicipalBondStatusId = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_PENDING");

		List<Long> statuses = new ArrayList<Long>();
		statuses.add(pendingMunicipalBondStatusId);

		Query query = entityManager.createNamedQuery("MunicipalBond.findByResidentIdAndTypeAndStatus");
		query.setParameter("residentId", residentId);
		query.setParameter("municipalBondType", MunicipalBondType.CREDIT_ORDER);
		query.setParameter("municipalBondStatusIds", statuses);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<MunicipalBond> findOnlyPendingAndInAgreementBonds(Long residentId) {
		SystemParameterService systemParameterService = ServiceLocator.getInstance()
				.findResource(SystemParameterService.LOCAL_NAME);
		Long pendingMunicipalBondStatusId = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_PENDING");
		Long agreementMunicipalBondStatusId = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_IN_PAYMENT_AGREEMENT");
		//rfam 2018-05-03
		Long subscriptionMunicipalBondStatusId = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_SUBSCRIPTION");
		

		// Long futureMunicipalBondStatusId =
		// systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_FUTURE_EMISION");

		List<Long> statuses = new ArrayList<Long>();
		statuses.add(pendingMunicipalBondStatusId);
		statuses.add(agreementMunicipalBondStatusId);
		statuses.add(subscriptionMunicipalBondStatusId);

		Query query = entityManager.createNamedQuery("MunicipalBond.findByResidentIdAndTypeAndStatus");
		query.setParameter("residentId", residentId);
		query.setParameter("municipalBondType", MunicipalBondType.CREDIT_ORDER);
		query.setParameter("municipalBondStatusIds", statuses);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<MunicipalBond> findPendingBondsBetweenDates(Long residentId, Long entryId, Date startDate,
			Date endDate) {
		SystemParameterService systemParameterService = ServiceLocator.getInstance()
				.findResource(SystemParameterService.LOCAL_NAME);
		Long pendingMunicipalBondStatusId = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_PENDING");
		Long compensationMunicipalBondStatusId = systemParameterService
				.findParameter("MUNICIPAL_BOND_STATUS_ID_COMPENSATION");

		List<Long> statuses = new ArrayList<Long>();
		statuses.add(pendingMunicipalBondStatusId);
		statuses.add(compensationMunicipalBondStatusId);

		Query query = entityManager
				.createNamedQuery("MunicipalBond.findByResidentIdEntryIdAndTypeAndStatusBetweenDates");
		query.setParameter("residentId", residentId);
		query.setParameter("entryId", entryId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("municipalBondType", MunicipalBondType.CREDIT_ORDER);
		query.setParameter("municipalBondStatusIds", statuses);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<MunicipalBond> findOnlyPendingBonds(Long residentId, Long entryId) {
		SystemParameterService systemParameterService = ServiceLocator.getInstance()
				.findResource(SystemParameterService.LOCAL_NAME);
		Long pendingMunicipalBondStatusId = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_PENDING");

		List<Long> statuses = new ArrayList<Long>();
		statuses.add(pendingMunicipalBondStatusId);

		Query query = entityManager.createNamedQuery("MunicipalBond.findByResidentIdEntryIdAndTypeAndStatus");
		query.setParameter("residentId", residentId);
		query.setParameter("entryId", entryId);
		query.setParameter("municipalBondType", MunicipalBondType.CREDIT_ORDER);
		query.setParameter("municipalBondStatusIds", statuses);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<MunicipalBond> findOnlyPendingAndInAgreementBonds(Long residentId, Long entryId) {
		SystemParameterService systemParameterService = ServiceLocator.getInstance()
				.findResource(SystemParameterService.LOCAL_NAME);
		Long pendingMunicipalBondStatusId = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_PENDING");
		Long agreementMunicipalBondStatusId = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_IN_PAYMENT_AGREEMENT");
		//rfam 2018-05-03
		Long subscriptionMunicipalBondStatusId = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_SUBSCRIPTION");

		// Long futureMunicipalBondStatusId =
		// systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_FUTURE_EMISION");

		List<Long> statuses = new ArrayList<Long>();
		statuses.add(pendingMunicipalBondStatusId);
		statuses.add(agreementMunicipalBondStatusId);
		statuses.add(subscriptionMunicipalBondStatusId);

		Query query = entityManager.createNamedQuery("MunicipalBond.findByResidentIdEntryIdAndTypeAndStatus");
		query.setParameter("residentId", residentId);
		query.setParameter("entryId", entryId);
		query.setParameter("municipalBondType", MunicipalBondType.CREDIT_ORDER);
		query.setParameter("municipalBondStatusIds", statuses);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<MunicipalBond> findPendingBondsBetweenDates(Long residentId, Date startDate, Date endDate) {
		SystemParameterService systemParameterService = ServiceLocator.getInstance()
				.findResource(SystemParameterService.LOCAL_NAME);
		Long pendingMunicipalBondStatusId = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_PENDING");
		Long compensationMunicipalBondStatusId = systemParameterService
				.findParameter("MUNICIPAL_BOND_STATUS_ID_COMPENSATION");

		List<Long> statuses = new ArrayList<Long>();
		statuses.add(pendingMunicipalBondStatusId);
		statuses.add(compensationMunicipalBondStatusId);

		Query query = entityManager.createNamedQuery("MunicipalBond.findByResidentIdAndTypeAndStatusBetweenDates");
		query.setParameter("residentId", residentId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("municipalBondType", MunicipalBondType.CREDIT_ORDER);
		query.setParameter("municipalBondStatusIds", statuses);
		return query.getResultList();
	}

	public void save(PaymentAgreement paymentAgreement, List<Long> municipalBondIds) {
		Long inPaymentAgreementMunicipalBondStatusId = systemParameterService
				.findParameter(IN_PAYMENT_AGREEMENT_BOND_STATUS);
		entityManager.persist(paymentAgreement);
		//System.out.println("LXGK -----> " + paymentAgreement.getId());
		Query query = entityManager
				.createNamedQuery("MunicipalBond.updatePaymentAgreement");
		query.setParameter("municipalBondIds", municipalBondIds);
		query.setParameter("paymentAgreementId", paymentAgreement.getId());
		query.setParameter("municipalBondStatusId", inPaymentAgreementMunicipalBondStatusId);
		query.executeUpdate();

	}

	@SuppressWarnings("unchecked")
	public MunicipalBond loadForPrinting(Long municipalBondId) {
		//System.out.println("LOADING FOR PRITING");
		Query query = entityManager
				.createNamedQuery("Deposit.findByMunicipalBondIdForPrinting");
		query.setParameter("municipalBondId", municipalBondId);
		List<Deposit> deposits = (List<Deposit>) query.getResultList();
		/*System.out.println("RECOVERED ---> " + deposits.size());
		System.out.println("END LOADING FOR PRITING");*/
		if (deposits.size() > 0) {
			return deposits.get(deposits.size() - 1).getMunicipalBond();
		}
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public MunicipalBond loadMunicipalBond(Long municipalBondId) {
		Query query = entityManager.createNamedQuery("MunicipalBond.findById");
		query.setParameter("municipalBondId", municipalBondId);
		MunicipalBond mb = (MunicipalBond) query.getSingleResult();
		return mb;
	}

	public TaxpayerRecord findDefaultInstitution() {
		return municipalBondService.findDefaultInstitution();
	}

	public Workday findActiveWorkday() throws NotActiveWorkdayException {
		Query query = entityManager.createNamedQuery("Workday.findCurrentWorkday");
		try {
			Workday workday = (Workday) query.getSingleResult();
			return workday;
		} catch (Exception e) {
			throw new NotActiveWorkdayException();
		}

	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<Receipt> findReceipts(Date startDate, Date endDate, FinancialStatus finantialStatus) {
		String queryName = "Receipt.findByDateAndStatus";
		if (finantialStatus.name().equalsIgnoreCase(FinancialStatus.VOID.name())) {
			queryName = "Receipt.findReversedByDateAndStatus";
		}
		Query query = entityManager.createNamedQuery(queryName);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("status", finantialStatus);
		return query.getResultList();
	}

	// @author macartuche
	// @date 2015-5-21
	// Creacion de secuencias para los comprobantes complementarios
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void createSequenceComplementVoucher(String sequenceName) {
		Long initialReceiptNumber = systemParameterService.findParameter("INITIAL_RECEIPT_NUMBER");
		Long finalReceiptNumber = systemParameterService.findParameter("FINAL_RECEIPT_NUMBER");
		if (!sequenceNameExists(sequenceName)) {
			String createSentence = "CREATE SEQUENCE " + sequenceName + " START " + initialReceiptNumber + " MINVALUE "
					+ initialReceiptNumber + " MAXVALUE " + finalReceiptNumber;
			Query query = entityManager.createNativeQuery(createSentence);
			query.executeUpdate();
		}
	}

	// @author macartuche
	// @date 2015-05-25
	// Siguiente valor de la secuencia
	public Long generateNextValue(String sequenceName) throws InvoiceNumberOutOfRangeException {
		Long INITIAL_RECEIPT_NUMBER = systemParameterService.findParameter("INITIAL_RECEIPT_NUMBER");
		Long FINAL_RECEIPT_NUMBER = systemParameterService.findParameter("FINAL_RECEIPT_NUMBER");

		Query query = entityManager.createNativeQuery("SELECT nextval('" + sequenceName + "')");
		Long number = ((BigInteger) query.getResultList().get(0)).longValue();

		if (number >= INITIAL_RECEIPT_NUMBER && number <= FINAL_RECEIPT_NUMBER) {
			return number;
		} else {
			throw new InvoiceNumberOutOfRangeException(number);
		}

	}

	@Override
	public List<FutureBond> findFutureBonds(Long residentId) {
		/*SystemParameterService systemParameterService = ServiceLocator.getInstance()
				.findResource(SystemParameterService.LOCAL_NAME);
		Long futureMunicipalBondStatusId = systemParameterService
				.findParameter("MUNICIPAL_BOND_STATUS_ID_FUTURE_EMISION");

		List<Long> statuses = new ArrayList<Long>();
		statuses.add(futureMunicipalBondStatusId);

		Query query = entityManager.createNamedQuery("MunicipalBond.findByResidentIdAndTypeAndStatus");
		query.setParameter("residentId", residentId);
		query.setParameter("municipalBondType", MunicipalBondType.CREDIT_ORDER);
		query.setParameter("municipalBondStatusIds", statuses);
		return query.getResultList();*/
		
		
		Long futureBondStatusId = systemParameterService.findParameter(IncomeServiceBean.FUTURE_BOND_STATUS);
		Query query = entityManager.createNamedQuery("Bond.findFutureByStatusAndResidentId");
		query.setParameter("residentId", residentId);
		query.setParameter("municipalBondType", MunicipalBondType.CREDIT_ORDER);
		query.setParameter("pendingBondStatusId", futureBondStatusId);
		return query.getResultList();
	}

	// @author
	// @tag recaudacionCoactivas
	// @date 2016-07-06T12:29
	// realizar el reverso en municipalbondaux - abonos compensaciones
	@SuppressWarnings("unchecked")
	@Override
	public void reversePaymentAgreements(List<Long> depositIds) throws ReverseNotAllowedException {

		Query bonds = entityManager.createQuery("Select distinct mba.municipalbond.id "
				+ "from MunicipalbondAux mba where mba.deposit.id in :list and mba.status='VALID' ");
		bonds.setParameter("list", depositIds);
		List<Long> bondIds = (List<Long>) bonds.getResultList();
		if(!bondIds.isEmpty()) {
			//reversar al estado anterior los rubros de interes y recargo
			String types[] = {"I", "S"};
			for (Long bondId : bondIds) {
				for(String type : types) {
					String query = "Select mba from MunicipalbondAux mba "
							+ "where mba.municipalbond.id=:bondID  and mba.status='VALID' and mba.type=:type order by mba.id desc";
					Query q = entityManager.createQuery(query);
					q.setParameter("type", type);
					q.setParameter("bondID", bondId);
					
					List<MunicipalbondAux> auxs = q.getResultList();
					List<Long> mbaIds = new ArrayList<Long>();
					int cont =0;
					for(MunicipalbondAux mba: auxs) {						
						if( (mba.getAnotherItem()!= null && mba.getAnotherItem() == true) && 
							(mba.getCoveritem()!=null && mba.getCoveritem()==true) && cont > 0) {
							break;
						}else {
							mbaIds.add(mba.getId());
						}
						cont++;
					}
					
					if(!mbaIds.isEmpty()) {
						Joiner joiner = Joiner.on(",");
						String list = joiner.join(mbaIds);
						String updateOlderAux = "Update MunicipalbondAux set anotheritem=null where id in ("+list+")";
						q = entityManager.createNativeQuery(updateOlderAux);
						q.executeUpdate();	
					}
				}
			}
		}

		Query updateAux = entityManager.createNamedQuery("MunicipalbondAux.setAsVoid");
		updateAux.setParameter("status", "VOID");
		updateAux.setParameter("depositList", depositIds);
		int state = updateAux.executeUpdate();
	
	}

	/**
	 * @author macartuche
	 * @date 2016-07-08T15:33
	 * @tag recaudacionCoactivas Obtener una lista de municipalbondAux por el
	 *      estado, municipalbondId y si cubre o no el interes
	 */
	public List<MunicipalbondAux> getBondsAuxByIdAndStatus(Long municipalbondId, Boolean coverInterest, String status,
			String type, String paymentType) {

		String query = "Select mba from MunicipalbondAux mba "
				+ "where mba.municipalbond.id=:munid and mba.status=:status" + " and mba.type=:type";
		if (coverInterest != null) {
			query += " and mba.coveritem=:cover ";
		}
		
		if(type=="I" || type=="S"){
			query += " and mba.anotherItem is null ";
		}
		
		query += "and mba.typepayment=:paymentType ";

		Query interestIsPayedQuery = entityManager.createQuery(query);
		interestIsPayedQuery.setParameter("munid", municipalbondId);
		interestIsPayedQuery.setParameter("status", status);
		interestIsPayedQuery.setParameter("type", type);
		interestIsPayedQuery.setParameter("paymentType", paymentType);
		
		if (coverInterest != null) {
			interestIsPayedQuery.setParameter("cover", coverInterest);
		}

		return interestIsPayedQuery.getResultList();
	}

	/**
	 * @author macartuche
	 * @date 2016-07-08T15:40:05
	 * @tag recaudacionCoactivas Suma de los intereses que se acumulan por
	 *      abonos menores al interes
	 */
	public BigDecimal sumAccumulatedInterest(Long municipalbondId, Boolean coverInterest, String status, String type, String paymentType) {

		String query = " Select SUM(mba.payValue) from MunicipalbondAux mba "
				+ " where mba.municipalbond.id=:munid and " + " mba.coveritem=:cover and " + " mba.status=:status and"
				+ " mba.type=:type ";
		
		if( type == "I" || type == "S" ){
			query += "and mba.anotherItem is null ";
		}
		
		query += "and mba.typepayment=:typepayment ";
		Query sumInterest = entityManager.createQuery(query);
		sumInterest.setParameter("munid", municipalbondId);
		sumInterest.setParameter("cover", coverInterest);
		sumInterest.setParameter("status", status);
		sumInterest.setParameter("type", type);
		sumInterest.setParameter("typepayment", paymentType);
		
		return (BigDecimal) sumInterest.getSingleResult();
	}

	/**
	 * @author macartuche
	 * @date 2016-07-08T15:52
	 * @tag recaudacionCoactivas
	 * @param deposit
	 *            Deposit
	 * @param municipalBond
	 *            MunicipalBond
	 * @param coverInterest
	 *            Boolean
	 * @return MunicipalbondAux
	 */
	private MunicipalbondAux createBondAux(Deposit deposit, MunicipalBond municipalBond, Boolean coverInterest,
			String type, String paymentMethod) {
		MunicipalbondAux munAux = new MunicipalbondAux();
		
		//@author macartuche
		//pagos abonos
		munAux.setTypepayment(paymentMethod);
		//fin pago abonos

		BigDecimal valuePayed = BigDecimal.ZERO;
		BigDecimal valueCover = BigDecimal.ZERO;
		switch (type.charAt(0)) {
		case 'I':
			valuePayed = deposit.getInterest();
			valueCover = municipalBond.getInterest();
			break;
		case 'S':
			valuePayed = deposit.getSurcharge();
			valueCover = municipalBond.getSurcharge();
			break;
		case 'T':
			valuePayed = deposit.getPaidTaxes();
			valueCover = municipalBond.getTaxesTotal();
			break;
		case 'C':
			valuePayed = deposit.getCapital();
			valueCover = municipalBond.getBalance();
			break;

		}
		munAux.setPayValue(valuePayed);
		munAux.setBalance(valueCover);
		munAux.setCoveritem(coverInterest);
		munAux.setMunicipalbond(municipalBond);
		munAux.setLiquidationDate(deposit.getDate());
		munAux.setLiquidationTime(deposit.getTime());
		munAux.setType(type);

		if(coverInterest)
			munAux.setAnotherItem(coverInterest);
		
		// DEPOSITO
		munAux.setDeposit(deposit);
		munAux.setStatus("VALID");
		return munAux;
	}

	/**
	 * @author macartuche Poner a true que la compensacion ha sido pagada
	 */
	public void compensationPayment(List<Deposit> deposits) {

		if (deposits != null && !deposits.isEmpty()) {
			for (Deposit deposit : deposits) {
				Long estado = deposit.getMunicipalBond().getMunicipalBondStatus().getId().longValue();
				if(estado == 6){ //en compensacion
					//System.out.println("Ingresaaaaaa");
					Receipt receipt = deposit.getMunicipalBond().getReceipt();
					if( receipt != null){
						
						//System.out.println("Ingresaaaaaa con factura");
						Query query = entityManager.createQuery("Select cr from CompensationReceipt cr"
								+ " where cr.receipt.id=:receiptid");
						query.setParameter("receiptid", receipt.getId());
						List<CompensationReceipt> compensationReceiptList = query.getResultList();

						if (!compensationReceiptList.isEmpty()) {
							CompensationReceipt compensation = compensationReceiptList.get(0);
							compensation.setIsPaid(Boolean.TRUE);
							entityManager.merge(compensation);
							entityManager.flush();
						}
					}

				}
			}
		}
	}

	// Jock Samaniego.. 21/09/2016
	public void updatePaymentAgreement(PaymentAgreement paymentAgreement) {
		entityManager.merge(paymentAgreement);
	}
	
	
	public boolean checkIsPayed(Long municipalbondid, String type) {
		Query q = entityManager.createQuery("Select mba from MunicipalbondAux mba "
				+ "WHERE mba.type=:type and mba.status='VALID' and mba.coveritem=:cover and "
				+ "mba.anotherItem=:cover and mba.municipalbond.id=:id");	 
		q.setParameter("type", type);
		q.setParameter("cover", true);
		q.setParameter("id", municipalbondid);
		return !q.getResultList().isEmpty();
	}
}