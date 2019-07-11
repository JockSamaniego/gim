package org.gob.gim.waterservice.action;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import ec.gob.gim.waterservice.model.*;
import ec.gob.gim.waterservice.model.dto.ResidentsInRouteDTO;
import ec.gob.gim.waterservice.model.dto.WaterMeterDisabledDTO;
import ec.gob.gim.waterservice.model.dto.WaterServiceHistoryDTO;
import org.gob.gim.common.NativeQueryResultsMapper;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("waterMeterHome")
public class WaterMeterHome extends EntityHome<WaterMeter> {

	@In(create = true)
	WaterMeterStatusHome waterMeterStatusHome;

	public void setWaterMeterId(Long id) {
		setId(id);
	}

	public Long getWaterMeterId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		WaterMeterStatus waterMeterStatus = waterMeterStatusHome.getDefinedInstance();
		if (waterMeterStatus != null) {
			getInstance().setWaterMeterStatus(waterMeterStatus);
		}
	}

	public boolean isWired() {
		return true;
	}

	public WaterMeter getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	//para consultas especiales
	//Jock Samaniego
	//04-06-2019
	
	private String serviceNumber;

	public String getServiceNumber() {
		return serviceNumber;
	}

	public void setServiceNumber(String serviceNumber) {
		this.serviceNumber = serviceNumber;
	}
	
	
	public List<WaterServiceHistoryDTO> findWaterServiceHistory(){
		List<WaterServiceHistoryDTO> history = new ArrayList<WaterServiceHistoryDTO>();
		if(serviceNumber!=null && !serviceNumber.equals("")){
			String query = "select wsa.routeorder, wsa.servicenumber, res.identificationnumber, res2.identificationnumber, "
					+ "rev.timestamp, rev.username "
					+ "from gimaudit.watersupply_aud wsa "
					+ "inner join revision rev on wsa.rev = rev.id "
					+ "LEFT JOIN resident res on wsa.serviceowner_id = res.id "
					+ "LEFT JOIN resident res2 on wsa.recipeowner_id = res2.id "
					+ "where wsa.servicenumber = :serviceNumber "
					+ "order by rev.timestamp ASC";
			Query q = getEntityManager().createNativeQuery(query);
			q.setParameter("serviceNumber", Integer.parseInt(serviceNumber));
			history = NativeQueryResultsMapper.map(q.getResultList(), WaterServiceHistoryDTO.class);
		}	
		return history;
	}
	
	private String route_id;

	public String getRoute_id() {
		return route_id;
	}

	public void setRoute_id(String route_id) {
		this.route_id = route_id;
	}
	
	public List<WaterMeterDisabledDTO> findWaterMeterDisabled(){
		List<WaterMeterDisabledDTO> disabled = new ArrayList<WaterMeterDisabledDTO>();
		if(route_id!=null && !route_id.equals("")){
			String query = "SELECT qry.serviceNumber, qry.falsos, qry.verdaderos, qry.totalCount "
					+ "FROM "
					+ "(select ws.servicenumber as serviceNumber,  "
					+ "count(nullif(wm.isactive, true)) falsos, "
					+ "count(nullif(wm.isactive, false)) verdaderos, "
					+ "count(wm.isactive) totalCount "
					+ "from watersupply ws "
					+ "inner join watermeter wm on ws.id = wm.watersupply_id "
					+ "where ws.iscanceled = false "
					+ "and ws.route_id = :route_id "
					+ "group by ws.servicenumber "
					+ "order by falsos desc, verdaderos asc)qry "
					+ "WHERE qry.verdaderos = 0";
			Query q = getEntityManager().createNativeQuery(query);
			q.setParameter("route_id", Long.parseLong(route_id));
			disabled = NativeQueryResultsMapper.map(q.getResultList(), WaterMeterDisabledDTO.class);	
		}	
		return disabled;
	}
	
	private String route_id2;
	private String waterYear;
	private String waterMonth;
	
	public String getRoute_id2() {
		return route_id2;
	}

	public void setRoute_id2(String route_id2) {
		this.route_id2 = route_id2;
	}

	public String getWaterYear() {
		return waterYear;
	}

	public void setWaterYear(String waterYear) {
		this.waterYear = waterYear;
	}

	public String getWaterMonth() {
		return waterMonth;
	}

	public void setWaterMonth(String waterMonth) {
		this.waterMonth = waterMonth;
	}

	public List<ResidentsInRouteDTO> findResidentsInRoute(){
		List<ResidentsInRouteDTO> residents = new ArrayList<ResidentsInRouteDTO>();
		if((route_id2!=null && !route_id2.equals(""))&&(waterYear!=null && !waterYear.equals(""))&&(waterMonth!=null && !waterMonth.equals("0"))){
			String query = "select id, servicenumber, routeorder from gimprod.watersupply where route_id = :route_id2 "
					+ "and id not in ( "
					+ "select consumption.watersupply_id from gimprod.consumption "
					+ "inner join gimprod.watersupply on consumption.watersupply_id=watersupply.id "
					+ "where watersupply.route_id = :route_id2 and month = :month and year= :year "
					+ ") and watersupply.iscanceled=false "
					+ "order by routeorder ";		
			Query q = getEntityManager().createNativeQuery(query);
			q.setParameter("route_id2", Long.parseLong(route_id2));
			q.setParameter("year", Integer.parseInt(waterYear));
			q.setParameter("month", Integer.parseInt(waterMonth));
			residents = NativeQueryResultsMapper.map(q.getResultList(), ResidentsInRouteDTO.class);	
		}	
		return residents;
	}
	
}
