package org.gob.gim.binnaclecrv.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.Query;

import org.gob.gim.common.DateUtils;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.binnaclecrv.model.ArrivalHistoryBinnacleCRV;
import ec.gob.gim.binnaclecrv.model.BinnacleCRV;

@Name("reportBinnacleCRVHome")
public class ReportBinnacleCRVHome extends EntityHome<BinnacleCRV> {

	private static final long serialVersionUID = -137033714034103801L;

	private static String TODOS = "Todos";
	private static String RETENIDOS = "Retenidos";
	private static String LIBERADOS = "Liberados";
	
	private List<String> listVehicles;
	private String vehicle;

	private boolean isFirstTime = true;

	private Date nowDate;
	private Date startDate;
	private Date endDate;
	private List<BinnacleCRV> list = new ArrayList<BinnacleCRV>();
	ArrivalHistoryBinnacleCRV arrival = null;
	private String dateSelected = "ingreso";

	@In
	FacesMessages facesMessages;

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the listVehicles
	 */
	public List<String> getListVehicles() {
		return listVehicles;
	}

	/**
	 * @param listVehicles the listVehicles to set
	 */
	public void setListVehicles(List<String> listVehicles) {
		this.listVehicles = listVehicles;
	}

	/**
	 * @return the vehicle
	 */
	public String getVehicle() {
		return vehicle;
	}

	/**
	 * @param vehicle the vehicle to set
	 */
	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}
	
	/**
	 * @return the dateSelected
	 */
	public String getDateSelected() {
		return dateSelected;
	}

	/**
	 * @param dateSelected the dateSelected to set
	 */
	public void setDateSelected(String dateSelected) {
		this.dateSelected = dateSelected;
	}

	/**
	 * @return the list
	 */
	public List<BinnacleCRV> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<BinnacleCRV> list) {
		this.list = list;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		if (!isFirstTime) {
			return;
		}
		initialData();
		isFirstTime = false;
	}
	
	public void initialData() {
		list.clear();
		listVehicles = new ArrayList<String>();
		listVehicles.add(TODOS);
		listVehicles.add(RETENIDOS);
		listVehicles.add(LIBERADOS);
		vehicle = TODOS;
		Query query = getEntityManager().createNativeQuery("SELECT min(a.arrivaldate) " +
				"FROM gimprod.arrivalhistorybinnaclecrv a");
		startDate = (Date) query.getSingleResult();
		endDate = new GregorianCalendar().getTime();
		nowDate = new GregorianCalendar().getTime();
	}
	
	public void clearList() {
		list.clear();
	}

	@SuppressWarnings("unchecked")
	public void generateReportVehicles(){
		if (startDate.after(nowDate)) {
			facesMessages.add("La fecha inicial no puede ser mayor a hoy.");
			return;
		}
		if (endDate.after(nowDate)) {
			facesMessages.add("La fecha final no puede ser mayor a hoy.");
			return;
		}
		list.clear();
		String sql = "";
		Query query = null;
		if (vehicle.equals(TODOS)){
			sql = "select b from BinnacleCRV b " +
					"join b.arrivalHistoryBinnacleCRVs a where ";
			if (dateSelected.equalsIgnoreCase("ingreso"))
			    sql = sql + " a.arrivalDate >= :startDate and " + "a.arrivalDate <= :endDate ";
			else
			    sql = sql + " a.exitDate >= :startDate and " + "a.exitDate <= :endDate ";
			sql = sql + "order by b.serialNumber asc";
		} else if (vehicle.equals(RETENIDOS)){
			sql = "select b from BinnacleCRV b " +
    				"join b.arrivalHistoryBinnacleCRVs a where ";
			if (dateSelected.equalsIgnoreCase("ingreso"))
			    sql = sql + " a.arrivalDate >= :startDate and " + "a.arrivalDate <= :endDate ";
			else
			    sql = sql + " a.exitDate >= :startDate and " + "a.exitDate <= :endDate ";
			sql = sql + "order by b.serialNumber asc";
		} else if (vehicle.equals(LIBERADOS)){
			sql = "select b from BinnacleCRV b " +
					"join b.arrivalHistoryBinnacleCRVs a where ";
			if (dateSelected.equalsIgnoreCase("ingreso"))
			    sql = sql + " a.arrivalDate >= :startDate and " + "a.arrivalDate <= :endDate ";
			else
			    sql = sql + " a.exitDate >= :startDate and " + "a.exitDate <= :endDate ";
			sql = sql + "order by b.serialNumber asc";
		};
		if (sql.length() > 0){
			query = getEntityManager().createQuery(sql);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			list = query.getResultList();
		}
	}
	
	public int calculateDays(BinnacleCRV binnacleCRV) {
		int days = 0;
		arrival = binnacleCRV.getLastArrivalHistoryBinnacleCRV();
		if (arrival != null)
			if (arrival.getExitDate() == null)
				days = DateUtils.daysBetweenDates(nowDate, arrival.getArrivalDate()) + 1;
			else
				days = DateUtils.daysBetweenDates(arrival.getExitDate(), arrival.getArrivalDate()) + 1;
		return days;
	}
	
}
