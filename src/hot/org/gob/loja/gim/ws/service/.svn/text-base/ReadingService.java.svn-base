package org.gob.loja.gim.ws.service;

import java.util.List;

import javax.ejb.Local;

import org.gob.loja.gim.ws.dto.ServiceRequest;
import org.gob.loja.gim.ws.exception.InvalidUser;

import ec.gob.gim.wsrest.dto.ConsumptionPackage;
import ec.gob.gim.wsrest.dto.DtoConsumption;
import ec.gob.gim.wsrest.dto.DtoRoute;
import ec.gob.gim.wsrest.dto.RoutePackage;

@Local
public interface ReadingService {
	
	public String LOCAL_NAME = "/gim/GimService/local";

	public RoutePackage findRoutesByUser(ServiceRequest request) throws InvalidUser;
	public ConsumptionPackage findConsumptions(ServiceRequest request, DtoRoute dtoRoute) throws InvalidUser;
	public ConsumptionPackage uploadConsumptions(ServiceRequest request, List<DtoConsumption> consumptions) throws InvalidUser;
	public ConsumptionPackage updateTransferred(ServiceRequest request, List<DtoConsumption> consumptions) throws InvalidUser;
	public RoutePackage uploadRoutes(ServiceRequest request, List<DtoRoute> routes);
	
}
