package org.gob.loja.gim.ws.production;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.gob.loja.gim.ws.dto.ServiceRequest;
import org.gob.loja.gim.ws.exception.InvalidUser;
import org.gob.loja.gim.ws.service.ReadingService;

import ec.gob.gim.wsrest.dto.ConsumptionPackage;
import ec.gob.gim.wsrest.dto.DtoConsumption;
import ec.gob.gim.wsrest.dto.DtoRoute;
import ec.gob.gim.wsrest.dto.RoutePackage;

/**
 * rfam 2017-07-06
 * se comenta no se esta usando
 */
//@WebService
public class ReadingsUMAPAL {
	/*@EJB
	private ReadingService service;

	@Resource
	WebServiceContext wsContext;

	@WebMethod
	public RoutePackage findRoutesByUser(ServiceRequest request) throws InvalidUser{
		RoutePackage routeObj = new RoutePackage();
		try {
			routeObj = service.findRoutesByUser(request);
		} catch (InvalidUser e) {
			InvalidateSession();
			throw e;
		}
		InvalidateSession();
		return routeObj;
	}

	@WebMethod
	public RoutePackage uploadRoutes(ServiceRequest request, List<DtoRoute> routes) throws InvalidUser{
		RoutePackage routeObj = new RoutePackage();
		routeObj = service.uploadRoutes(request, routes);
		InvalidateSession();
		return routeObj;
	}

	@WebMethod
	public ConsumptionPackage findConsumptionsByRoute(ServiceRequest request, DtoRoute dtoRoute) throws InvalidUser{
		ConsumptionPackage consumptionPack = new ConsumptionPackage();
		try {
			consumptionPack = service.findConsumptions(request, dtoRoute);
		} catch (InvalidUser e) {
			InvalidateSession();
			throw e;
		}
		InvalidateSession();
		return consumptionPack;
	}

	@WebMethod
	public ConsumptionPackage uploadConsumptionsByRoute(ServiceRequest request, List<DtoConsumption> consumptions) throws InvalidUser{
		ConsumptionPackage consumptionPackage = new ConsumptionPackage();
		consumptionPackage = service.uploadConsumptions(request, consumptions);
		InvalidateSession();
		return consumptionPackage;
	}

	@WebMethod
	public ConsumptionPackage updateTransferred(ServiceRequest request, List<DtoConsumption> consumptions) throws InvalidUser{
		ConsumptionPackage consumptionPackage = new ConsumptionPackage();
		consumptionPackage = service.updateTransferred(request, consumptions);
		InvalidateSession();
		return consumptionPackage;
	}

	private void InvalidateSession(){
		final MessageContext mc = this.wsContext.getMessageContext();
	    HttpServletRequest sr = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);

	    if (sr != null && sr instanceof HttpServletRequest) {
	        final HttpServletRequest hsr = (HttpServletRequest) sr;
	        hsr.getSession(true).invalidate();
	        hsr.getSession().setMaxInactiveInterval(1);
	    }

	}*/

}
