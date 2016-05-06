package org.gob.loja.gim.ws.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.loja.gim.ws.dto.ServiceRequest;
import org.gob.loja.gim.ws.exception.InvalidUser;

import ec.gob.gim.security.model.User;
import ec.gob.gim.waterservice.model.Consumption;
import ec.gob.gim.waterservice.model.WaterMeterStatus;
import ec.gob.gim.wsrest.dto.ConsumptionPackage;
import ec.gob.gim.wsrest.dto.DtoConsumption;
import ec.gob.gim.wsrest.dto.DtoRoute;
import ec.gob.gim.wsrest.dto.RoutePackage;

@Stateless(name="ReadingService")
@Interceptors({SecurityInterceptor.class})
public class ReadingServiceBean implements ReadingService{

	@PersistenceContext
	EntityManager em;
	
	@SuppressWarnings("unchecked")
	public RoutePackage findRoutesByUser(ServiceRequest request) throws InvalidUser {
		Query query;
		query = em.createNamedQuery("User.findByUsername");
		query.setParameter("name", request.getUsername());
		User user = (User) query.getSingleResult();
		query = null;
		query = em.createNativeQuery("SELECT DISTINCT r.id, r.name, c.year, c.month, rp.readingman_id "
				+ "from gimprod.consumption c inner join gimprod.watersupply ws on ws.id = c.watersupply_id "
				+ "inner join gimprod.route r on r.id = ws.route_id "
				+ "inner join gimprod.routeperiod rp on r.id = rp.route_id "
				+ "where c.readingdate is null and transferred = false and rp.readingman_id = " + user.getId());
		List<DtoRoute> dtoRouteList = new ArrayList<DtoRoute>();
		List<Object[]> results = query.getResultList();
		
		for(Object[] row : results){
			DtoRoute dtoRoute = new DtoRoute();
			dtoRoute.setRouteId(Long.valueOf(row[0].toString()));
			dtoRoute.setRouteName((String)row[1]);
			dtoRoute.setRouteYear((Integer)row[2]);
			dtoRoute.setRouteMonth((Integer)row[3]);
			dtoRoute.setUserId(Long.valueOf(row[4].toString()));
			
			dtoRouteList.add(dtoRoute);
		}
		
		RoutePackage routePack = new RoutePackage();
		routePack.setDtoRouteList(dtoRouteList);
		return routePack;
	}


	@SuppressWarnings("unchecked")
	@Override
	public ConsumptionPackage findConsumptions(ServiceRequest request,
			DtoRoute dtoRoute) throws InvalidUser {
		Query query;
		query = em.createNamedQuery("Consumption.findDtoConsumptionsByRoute");
		query.setParameter("year", dtoRoute.getRouteYear());
		query.setParameter("month", dtoRoute.getRouteMonth());
		query.setParameter("routeId", dtoRoute.getRouteId());
		List<DtoConsumption> consumptions = new ArrayList<DtoConsumption>();
		consumptions = query.getResultList();
		
		ConsumptionPackage conPack = new ConsumptionPackage();
		conPack.setDtoConsumptionList(consumptions);
		
		return conPack;
	}


	@SuppressWarnings("unchecked")
	@Override
	public ConsumptionPackage uploadConsumptions(ServiceRequest request,
			List<DtoConsumption> consumptions) throws InvalidUser {
		List<Long> ids = new ArrayList<Long>();
		Map<Long, Consumption> consumptionMap = new HashMap<Long, Consumption>();
		for (DtoConsumption dtoCon : consumptions) {
			ids.add(dtoCon.getConsumptionId());
			System.out.println("<<<R>>>Consumo: "+dtoCon.getConsumptionId()+" Amount: "+dtoCon.getAmount());
		}
		Query query;
		query = em.createNamedQuery("Consumption.findConsumptionsByIds");
		query.setParameter("ids", ids);
		List<Consumption> consumptionsServer = new ArrayList<Consumption>();
		consumptionsServer = query.getResultList();
		for (Consumption consumption : consumptionsServer) {
			consumptionMap.put(consumption.getId(), consumption);
		}
		Calendar dateLocal = new GregorianCalendar();
		Consumption consumption = new Consumption();
		for (DtoConsumption dtoCon : consumptions) {
			consumption = consumptionMap.get(dtoCon.getConsumptionId());
			if (!dtoCon.isAverage()){
				consumption.setAmount(new BigDecimal(dtoCon.getAmount()));
				consumption.setCurrentReading(dtoCon.getCurrentReading());
			} else{
				consumption.setAmount(findAverage(dtoCon.getServiceCode()));
				consumption.setCurrentReading(consumption.getAmount().longValue() + consumption.getPreviousReading());
				WaterMeterStatus wms = findWaterMeterState(dtoCon.getWaterMeterState());
				if (wms.getId() != null) consumption.setWaterMeterStatus(wms);
			}
			consumption.setReadingDate(dtoCon.getReadingDate());
			consumption.setDateReceived(dateLocal.getTime());
			consumption.setObservations(dtoCon.getObs());
			consumption.setLatitude(dtoCon.getLatitudeReading());
			consumption.setLongitude(dtoCon.getLongitudeReading());
			consumption.setDateReceived(dateLocal.getTime());
			consumption.setReceived(true);
			dtoCon.setReceived(true);
			em.merge(consumption);
		}
		em.flush();
		ConsumptionPackage pack = new ConsumptionPackage();
		pack.setDtoConsumptionList(consumptions);
		return pack;
	}

	public BigDecimal findAverage(int serviceCode){
		BigDecimal value = null;
		Query query;
		query = em.createNamedQuery("Consumption.findAverageByServiceCode");
		query.setParameter("serviceCode", serviceCode);
		value = (BigDecimal) query.getSingleResult();
		if (value == null)
			return BigDecimal.ZERO;
		else{
			value = value.setScale(2, RoundingMode.HALF_UP);
			return value;
		}
	}
	
	public WaterMeterStatus findWaterMeterState(String wmsName){
		Query query;
		query = em.createNamedQuery("WaterMeterStatus.findByName");
		query.setParameter("wmsName", wmsName);
		WaterMeterStatus wms = (WaterMeterStatus) query.getSingleResult();
		return wms;
	}

	@SuppressWarnings("unchecked")
	public ConsumptionPackage updateTransferred(ServiceRequest request,
			List<DtoConsumption> consumptions) throws InvalidUser {
		List<Long> ids = new ArrayList<Long>();
		Map<Long, Consumption> consumptionMap = new HashMap<Long, Consumption>();
		for (DtoConsumption dtoCon : consumptions) {
			ids.add(dtoCon.getConsumptionId());
		}
		Query query;
		query = em.createNamedQuery("Consumption.findConsumptionsByIds");
		query.setParameter("ids", ids);
		List<Consumption> consumptionsServer = new ArrayList<Consumption>();
		consumptionsServer = query.getResultList();
		for (Consumption consumption : consumptionsServer) {
			consumptionMap.put(consumption.getId(), consumption);
		}
		Calendar dateLocal = new GregorianCalendar();
		Consumption consumption = new Consumption();
		for (DtoConsumption dtoCon : consumptions) {
			consumption = consumptionMap.get(dtoCon.getConsumptionId());
			consumption.setTransferred(true);
			consumption.setDateTransferred(dateLocal.getTime());
			em.merge(consumption);
		}
		em.flush();
		ConsumptionPackage pack = new ConsumptionPackage();
		pack.setDtoConsumptionList(consumptions);
		return pack;
	}

	public RoutePackage uploadRoutes(ServiceRequest request, List<DtoRoute> routes){
		RoutePackage pack = new RoutePackage();
		for (DtoRoute dtoRoute : routes) {
			System.out.println("<<<R>>> uploadRoutes: "+dtoRoute.getRouteId()+" "+dtoRoute.getRouteMonth());
		}
		pack.setDtoRouteList(routes);
		return pack;
	}
}
