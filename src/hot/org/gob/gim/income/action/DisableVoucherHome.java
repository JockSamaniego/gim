package org.gob.gim.income.action;

import java.io.IOException;
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
	private List<ReceiptDTO> compensationList=new ArrayList<ReceiptDTO>();;
	private Long mbStatusCompensationId;
	private Long mbStatusPendingId;
	private MunicipalBondStatus pendingStatus;
	private MunicipalBondStatus compensationStatus;
	private String observation;
	private BigDecimal total=BigDecimal.ZERO;
	@In
    UserSession userSession;
	
	
  	public void loadDefaultDates() {
  		  		
		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		}
		
		pendingStatus = findStatus("MUNICIPAL_BOND_STATUS_ID_PENDING");
		compensationStatus = findStatus("MUNICIPAL_BOND_STATUS_ID_COMPENSATION");
		
		//cargar las fechas por defecto del mes
		Calendar current = Calendar.getInstance();
		Calendar aux = current;
		current.set(Calendar.DAY_OF_MONTH, 1);		
		this.startDate = current.getTime();				
		current.set(Calendar.DAY_OF_MONTH, current.getActualMaximum(Calendar.DAY_OF_MONTH));
		this.endDate = current.getTime();	
		
	}
  	
  	@SuppressWarnings("unchecked")
	public void searchBonds() {		
		String query = "select  NEW ec.gob.gim.income.model.ReceiptDTO( r.id, res.name,  " + 
  						"r.date as emission, " + 
  						"r.sriAuthorizationDate as authorizationDate, " + 
  						"r.receiptNumber as sequential,  "+
  						"res.identificationNumber as identification, "+
  						"mb.id as bondid, "+
  						"mb.paidTotal as total) " + 
  						"from Receipt r " + 
  						"join r.municipalBond mb " +
  						"join mb.resident res " + 
  						"where mb.municipalBondStatus.id = :compensationStatus " + 
  						"and  r.date  between  :startDate  and  :endDate " + 
  						"and r.isCompensation =:compensation "+
  						"and r.receiptNumber like :sequential "+
  						"order by r.date";
  		
  		Query q = getEntityManager().createQuery(query);

  		q.setParameter("compensationStatus", this.mbStatusCompensationId);
  		q.setParameter("startDate", this.startDate);
  		q.setParameter("endDate", this.endDate);
  		q.setParameter("compensation", Boolean.TRUE);
  		q.setParameter("sequential", "%001-022%");
  		this.compensationList = q.getResultList();  
 
  		
  		for (ReceiptDTO receiptDTO : compensationList) {
  			total = total.add(receiptDTO.getTotal());
		}
  	}
  	
  	@Transactional
  	public void reverseBonds() {
  		
  		List<String> documentNumbers = new ArrayList<String>();
  		for (ReceiptDTO receiptDTO : compensationList) {
  			MunicipalBond mb = getEntityManager().find(MunicipalBond.class, receiptDTO.getBondid());
  			Receipt receipt = getEntityManager().find(Receipt.class, receiptDTO.getId());
  			/* 			
  			//statusChange
  			StatusChange newsch = createStatusChange(mb);	
  			getEntityManager().persist(newsch);  		
  			
  			//actualizar Municipalbond
  			mb.setMunicipalBondStatus(pendingStatus);
  			getEntityManager().merge(mb);
  			
  			//actualizar receipt
  			receipt.setMunicipalBond(null);
  			receipt.setReversedMunicipalBond(mb);
  			receipt.setAuthorizationNumber("");
  			receipt.setSriAccessKey("0000000000000000000000000000000000000000000000000");
  			receipt.setStatus(FinancialStatus.VOID);
  			receipt.setStatusElectronicReceipt(StatusElectronicReceipt.VOID);
  			getEntityManager().merge(receipt);
  			*/
  			
  			documentNumbers.add(receipt.getReceiptNumber().replaceAll("-", ""));
  			//update gimprod.municipalbond set municipalbondstatus_id=3 where id=7282116;
  			//delete from gimprod.compensationreceipt where receipt_id=3302824;	
  			//update gimprod.receipt set municipalbond_id=null, reversedmunicipalbond_id=7282116, 
  			//       sriaccesskey='0000000000000000000000000000000000000000000000000', authorizationnumber='',
  			//       status='VOID', statuselectronicreceipt='VOID' where receiptnumber in ('001-022-000059272');         
  			
		}
  		
  		//invocar al servicio web de facturas
  		String rucMunicipio = "1160000240001";
  		String input = createParameters(rucMunicipio, documentNumbers);  /*
  			URL url;
			try {
				url = new URL(
				        "http://localhost:8080/CustomerDB/webresources/co.com.mazf.ciudad");
				
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	  		    conn.setRequestMethod("POST");
	  		    conn.setDoOutput(true);
	  		    conn.setRequestProperty("Accept", "application/json");
	  		    
	  		    if (conn.getResponseCode() != 200) {
	  		    	throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
	  		    }
	  		    
	  		  	String input = createParameters(rucMunicipio, documentNumbers); 
	  		  
	  		  	OutputStream os = conn.getOutputStream();
	  		  	os.write(input.getBytes());
	  		  	os.flush();
	  		  	
	  		  	
			} catch (MalformedURLException e) { 
				e.printStackTrace();
			} catch (IOException e) { 
				e.printStackTrace();
			}
  			
  		  	*/
  		
  	}
  	
  	private String createParameters(String ruc, List<String> documentNumbers) {
  		StringBuffer msg = new StringBuffer("{");
  		msg.append("\"ruc\":");  	
  		msg.append(ruc);  		
  		msg.append(",");
  		msg.append("\"documentNumbers\": [ ");
  		String replace = documentNumbers.toString().replace(", ", ",").replaceAll("[\\[.\\]]", "");
  		
  		System.out.println(replace);
  		msg.append(")]");
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
}