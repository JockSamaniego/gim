package org.gob.gim.coercive.action;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;

import org.gob.gim.common.DateUtils;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityController;
import org.jboss.seam.log.Log;

import ec.gob.gim.coercive.model.DuePortfolioDTO;

@Name("duePortfolioHome")
@Scope(ScopeType.CONVERSATION)
public class DuePortfolioHome extends EntityController {

	/**
	 * 	
	 */
	private static final long serialVersionUID = 1L;
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	private static final String PENDING_BOND_STATUS_ID = "MUNICIPAL_BOND_STATUS_ID_PENDING";
	
	private SystemParameterService systemParameterService;
	
	@Logger 
	Log logger;
	
	@In
	FacesMessages facesMessages;
	
	private Date startDate;	
	private Date endDate;
	
	private Long pendingMunicipalBondStatus;
	
	private List<DuePortfolioDTO> duePortfolioDTOs;
	
	private BigDecimal total;
	
	private Boolean isFirstTime = Boolean.TRUE;
	
	public void loadValues(){
		if(!isFirstTime) return;
		Calendar ca = Calendar.getInstance();
		startDate = ca.getTime();
		endDate = ca.getTime();
		loadPendingBondStatus();
		isFirstTime = Boolean.FALSE;
	}
	
	public void loadPendingBondStatus(){
		if(pendingMunicipalBondStatus != null) return;
		systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
		pendingMunicipalBondStatus = systemParameterService.findParameter(PENDING_BOND_STATUS_ID);
	}
	
	private String dataForReport(List<DuePortfolioDTO> list){
		String data = list.toString();
//		Separar con una salto entre lineas en lugar de comas (,) a los elementos del List
		data = data.replace(',', '\n');
//		Reemplazar el inicio del toString del List
		data = data.replace('[', ' ');
//		Reemplazar los * por las comas (,)
		data = data.replace('*', ',');
//		Reemplazar el final del toString del List
		data = data.replace(']', ' ');		
		return data ;
	}
	
	public void download() throws IOException {	     
	          
	     try {
	    	 
	    	  String data = dataForReport(duePortfolioDTOs);
	          
	          HttpServletResponse response = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
	          
	          response.reset();
	          response.setContentType("text/csv");          
	          String name = "CarteraVencida-" + DateUtils.formatDate(Calendar.getInstance().getTime()) + ".csv";
	          response.addHeader("Content-Disposition","attachment;filename=" + name);
	               
	          response.setContentLength(data.getBytes().length);
	               
	          OutputStream writer = response.getOutputStream();       
	          
	          writer.write(data.getBytes());	               
	          
	          writer.flush();
	          writer.close();
	          
	          FacesContext.getCurrentInstance().responseComplete();               
	          
	     } catch (Exception e) {
	    	 addFacesMessageFromResourceBundle("common.noResultsReturned");	         
	     } 
	          
	}
	
	public void generateReport(){		
		duePortfolioDTOs = findDuePortfolio(startDate, endDate);
		total = sumTotal(duePortfolioDTOs);
	}
	
	private BigDecimal sumTotal(List<DuePortfolioDTO> duePortfolioDTOs){
		BigDecimal aux = BigDecimal.ZERO;
		for(DuePortfolioDTO d: duePortfolioDTOs){
			aux = aux.add(d.getValue());
		}
		return aux;
	}
	
	private List<Long> bondStatuesForDuePortfolio(){
		List<Long> list = new ArrayList<Long>();
		list.add(pendingMunicipalBondStatus);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	private List<DuePortfolioDTO> findDuePortfolio(Date startDate, Date endDate){		
		Query query = getEntityManager().createNamedQuery("MunicipalBond.findDuePortfolioDTO");
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("statusIds", bondStatuesForDuePortfolio());
		return query.getResultList();
	}

	public List<DuePortfolioDTO> getDuePortfolioDTOs() {
		return duePortfolioDTOs;
	}

	public void setDuePortfolioDTOs(List<DuePortfolioDTO> duePortfolioDTOs) {
		this.duePortfolioDTOs = duePortfolioDTOs;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
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
	
	public String valuesToReport(){
		return dataForReport(duePortfolioDTOs);
	}

	public Boolean getIsFirstTime() {
		return isFirstTime;
	}

	public void setIsFirstTime(Boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}
	
}