package org.gob.loja.gim.ws.service;

import org.gob.loja.gim.ws.service.*;
import java.util.List;

import javax.ejb.Local;

import org.gob.loja.gim.ws.dto.ServiceRequest;
import org.gob.loja.gim.ws.exception.InvalidUser;

import ec.gob.gim.wsrest.dto.ConsumptionPackage;
import ec.gob.gim.wsrest.dto.DtoConsumption;
import ec.gob.gim.wsrest.dto.DtoRoute;
import ec.gob.gim.wsrest.dto.RoutePackage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.gob.loja.gim.ws.exception.CashierOpen;
import org.gob.loja.gim.ws.exception.DateNoAvalible;
import org.gob.loja.gim.ws.exception.ExistsDuplicateUsers;
import org.gob.loja.gim.ws.exception.InterestRateNoDefined;
import org.gob.loja.gim.ws.exception.TaxesNoDefined;

@Local
public interface WorkDay {
	
	public String LOCAL_NAME = "/gim/GimService/local";

	public Boolean workDay(ServiceRequest request, HttpServletRequest hrs) throws  InvalidUser, DateNoAvalible, InterestRateNoDefined, TaxesNoDefined, CashierOpen, ExistsDuplicateUsers, Exception;
	
	
}
