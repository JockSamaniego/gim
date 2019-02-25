package org.gob.gim.income.action;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.codehaus.jackson.map.ObjectMapper;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.common.model.FinancialStatus;
import ec.gob.gim.income.model.Receipt;
import ec.gob.gim.income.model.ReceiptDTO;
import ec.gob.gim.income.model.StatusElectronicReceipt;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.StatusChange;
import ec.gob.gim.waterservice.model.ConsumptionState;

@Name("disableVoucherHome")
public class DisableVoucherHome extends EntityHome<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	private SystemParameterService systemParameterService;
	public static String REVENUE_SERVICE_NAME = "/gim/RevenueService/local";

	private Date startDate;
	private Date endDate;
	private List<ReceiptDTO> compensationList = new ArrayList<ReceiptDTO>();;
	private Long mbStatusCompensationId;
	private Long mbStatusPendingId;
	private MunicipalBondStatus pendingStatus;
	private MunicipalBondStatus compensationStatus;
	private String observation;
	private BigDecimal total = BigDecimal.ZERO;
	private String ruc = "";
	private String receipt = "";
	private String URI_ELEC_INVOICE;
	private String message="";

	@In
	UserSession userSession;

	public void loadDefaultDates() {

		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		}

		pendingStatus = findStatus("MUNICIPAL_BOND_STATUS_ID_PENDING");
		compensationStatus = findStatus("MUNICIPAL_BOND_STATUS_ID_COMPENSATION");
		URI_ELEC_INVOICE = systemParameterService.findParameter("ELECTRONIC_INVOICE_BASE_URI");
		// cargar las fechas por defecto del mes
		Calendar current = Calendar.getInstance();
		Calendar aux = current;
		current.set(Calendar.DAY_OF_MONTH, 1);
		this.startDate = current.getTime();
		current.set(Calendar.DAY_OF_MONTH, current.getActualMaximum(Calendar.DAY_OF_MONTH));
		this.endDate = current.getTime(); 	
	}

	@SuppressWarnings("unchecked")
	public void searchBonds() {
		total = BigDecimal.ZERO;
		this.observation="";
		this.message="";
		String query = "select  NEW ec.gob.gim.income.model.ReceiptDTO( r.id, res.name,  " + "r.date as emission, "
				+ "r.sriAuthorizationDate as authorizationDate, " + "r.receiptNumber as sequential, "
				+ "res.identificationNumber as identification, " + "r.statusElectronicReceipt as status,  "
				+ "mb.id as bondid, " 
				+ "mb.paidTotal as total,"
				+ "mb.number as number) " + "from Receipt r " + "join r.municipalBond mb "
				+ "join mb.resident res " + "where mb.municipalBondStatus.id = :compensationStatus "
				+ "and  r.date  between  :startDate  and  :endDate " + "and r.isCompensation =:compensation "
				+ "and r.receiptNumber like :sequential ";
		if (!ruc.trim().isEmpty()) {
			query += "and res.identificationNumber =:identification ";
		}

		if (!receipt.trim().isEmpty()) {
			query += "and r.receiptNumber =:billing ";
		}

		query += "order by r.date";

		Query q = getEntityManager().createQuery(query);

		q.setParameter("compensationStatus", compensationStatus.getId());
		q.setParameter("startDate", this.startDate);
		q.setParameter("endDate", this.endDate);
		q.setParameter("compensation", Boolean.TRUE);
		q.setParameter("sequential", "%001-022%");
		if (!ruc.trim().isEmpty()) {
			q.setParameter("identification", this.ruc);
		}

		if (!receipt.trim().isEmpty()) {
			q.setParameter("billing", this.receipt);
		}

		this.compensationList = q.getResultList();

		for (ReceiptDTO receiptDTO : compensationList) {
			total = total.add(receiptDTO.getTotal());
		}
	}

	@SuppressWarnings("unchecked")
	public void reverseBonds() {
		this.message="";
 
		List<String> documentNumbers = new ArrayList<String>();
		this.message="";
		// HACER EN GIM
		joinTransaction();
		int contGim=0;
		for (ReceiptDTO receiptDTO : compensationList) {
			MunicipalBond mb = getEntityManager().find(MunicipalBond.class, receiptDTO.getBondid());
			Receipt receipt = getEntityManager().find(Receipt.class, receiptDTO.getId());

			StatusChange newsch = createStatusChange(mb);
			getEntityManager().persist(newsch);
						
			// actualizar Municipalbond 
			mb.setMunicipalBondStatus(pendingStatus);
			getEntityManager().merge(mb);
			 			
			// actualizar receipt 
			receipt.setMunicipalBond(null);
			receipt.setReversedMunicipalBond(mb);
			receipt.setAuthorizationNumber("");
			receipt.setSriAccessKey("0000000000000000000000000000000000000000000000000");
			receipt.setStatus(FinancialStatus.VOID);
			receipt.setStatusElectronicReceipt(StatusElectronicReceipt.VOID);
			getEntityManager().merge(receipt);					
			
			documentNumbers.add("\"" + receipt.getReceiptNumber().replaceAll("-", "") + "\"");
			contGim++;
		}
		getEntityManager().flush(); 

		// HACER EN FACTURACION ELECTRONICA
		String rucMunicipio = "1160000240001";
		String input = createParameters(rucMunicipio, documentNumbers);
 
		String inputString = null;
		int responseCode = 0;
		URL url;
		try {
			url = new URL(URI_ELEC_INVOICE+"/ec.gob.loja.service.electronicinvoice/disableVouchers");

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");

			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(input);
			wr.flush();
			wr.close();

			responseCode = conn.getResponseCode();
			System.out.println("POST " + responseCode);
			StringBuffer responseJson = new StringBuffer();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((inputString = in.readLine()) != null) {
				responseJson.append(inputString);
			}
			@SuppressWarnings("unused")
			Map<String, String> responseServer = new ObjectMapper().readValue(responseJson.toString(), HashMap.class);
			String status = responseServer.get("respuesta");
			if (status.equals("ok")) {
				System.out.println(responseServer.get("modificados"));
				String modified = responseServer.get("modificados");
				message +="REVERSADOS EN GIM: "+contGim+" \n\n";
				message +="REVERSADOS EN FACTURACIÓN ELECTRÓNICA: "+modified+" \n";
			}
			
			System.out.println(message);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	private String createParameters(String ruc, List<String> documentNumbers) {

		String replace = documentNumbers.toString().replace(", ", ",").replaceAll("[\\[.\\]]", "");
		String identification = userSession.getUser().getResident().getIdentificationNumber();

		StringBuffer msg = new StringBuffer("{");
		msg.append("\"ruc\":");
		msg.append("\"" + ruc + "\"");
		msg.append(",");
		msg.append("\"identification\":");
		msg.append("\"" + identification + "\"");
		msg.append(",");
		msg.append("\"documentNumbers\": [ ");
		msg.append(replace);
		msg.append("]");
		msg.append("}");

		return msg.toString();
	}

	public MunicipalBondStatus findStatus(String status) {
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		return systemParameterService.materialize(MunicipalBondStatus.class, status);
	}

	public StatusChange createStatusChange(MunicipalBond mb) {
		StatusChange scNew = new StatusChange();
		scNew.setExplanation(this.observation);
		scNew.setUser(userSession.getUser());
		scNew.setPreviousBondStatus(mb.getMunicipalBondStatus());
		scNew.setMunicipalBondStatus(pendingStatus);
		scNew.setMunicipalBond(mb);
		return scNew;

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

	public List<ReceiptDTO> getCompensationList() {
		return compensationList;
	}

	public void setCompensationList(List<ReceiptDTO> compensationList) {
		this.compensationList = compensationList;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Long getMbStatusCompensationId() {
		return mbStatusCompensationId;
	}

	public void setMbStatusCompensationId(Long mbStatusCompensationId) {
		this.mbStatusCompensationId = mbStatusCompensationId;
	}

	public Long getMbStatusPendingId() {
		return mbStatusPendingId;
	}

	public void setMbStatusPendingId(Long mbStatusPendingId) {
		this.mbStatusPendingId = mbStatusPendingId;
	}

	public MunicipalBondStatus getPendingStatus() {
		return pendingStatus;
	}

	public void setPendingStatus(MunicipalBondStatus pendingStatus) {
		this.pendingStatus = pendingStatus;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getURI_ELEC_INVOICE() {
		return URI_ELEC_INVOICE;
	}

	public void setURI_ELEC_INVOICE(String uRI_ELEC_INVOICE) {
		URI_ELEC_INVOICE = uRI_ELEC_INVOICE;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}