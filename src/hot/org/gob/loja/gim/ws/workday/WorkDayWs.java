/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gob.loja.gim.ws.workday;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.gob.loja.gim.ws.dto.ServiceRequest;
import org.gob.loja.gim.ws.exception.CashierOpen;
import org.gob.loja.gim.ws.exception.DateNoAvalible;
import org.gob.loja.gim.ws.exception.ExistsDuplicateUsers;
import org.gob.loja.gim.ws.exception.InterestRateNoDefined;
import org.gob.loja.gim.ws.exception.InvalidUser;
import org.gob.loja.gim.ws.exception.TaxesNoDefined;

/**
 * rfam 2017-07-06
 * se comenta no se esta usando
 */
//@WebService
//        (serviceName = "WorkDayWs")
public class WorkDayWs {

    @Resource
    WebServiceContext wsContext;

    @EJB
//    private WorkDay service;

    /**
     * This is a sample web service operation
     * @param request
     * @throws org.gob.loja.gim.ws.exception.DateNoAvalible
     * @throws org.gob.loja.gim.ws.exception.InvalidUser
     * @throws org.gob.loja.gim.ws.exception.InterestRateNoDefined
     * @throws org.gob.loja.gim.ws.exception.TaxesNoDefined
     * @throws org.gob.loja.gim.ws.exception.CashierOpen
     * @throws org.gob.loja.gim.ws.exception.ExistsDuplicateUsers
     */
/*    @WebMethod
//        (operationName = "WorkDay")
    public void WorkDay(ServiceRequest request) throws DateNoAvalible, InvalidUser, InterestRateNoDefined, TaxesNoDefined, CashierOpen, ExistsDuplicateUsers, Exception {
        Boolean routeObj = Boolean.FALSE;
        try {

            final MessageContext mc = this.wsContext.getMessageContext();
            HttpServletRequest sr = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);

            HttpServletRequest hsr = null;

            if (sr != null && sr instanceof HttpServletRequest) {
                hsr = (HttpServletRequest) sr;
                routeObj = service.workDay(request, hsr);
            }

            
        }catch (DateNoAvalible e) {
            InvalidateSession();
            e.printStackTrace();
            throw e;
        }catch (ExistsDuplicateUsers e) {
            InvalidateSession();
            e.printStackTrace();
            throw e;
        } catch (InvalidUser e) {
            InvalidateSession();
            e.printStackTrace();
            throw e;
        }catch (InterestRateNoDefined e) {
            InvalidateSession();
            e.printStackTrace();
            throw e;
        }catch (TaxesNoDefined e) {
            InvalidateSession();
            e.printStackTrace();
            throw e;
        }catch (CashierOpen e) {
            InvalidateSession();
            e.printStackTrace();
            throw e;
        }catch (Exception e) {
            InvalidateSession();
            e.printStackTrace();
            throw e;
        }
        InvalidateSession();

    }*/
    
    public void openWorkDay(ServiceRequest request) throws DateNoAvalible, InvalidUser, InterestRateNoDefined, TaxesNoDefined, CashierOpen, ExistsDuplicateUsers, Exception {
    	//service.
    }

    private void InvalidateSession() {
        final MessageContext mc = this.wsContext.getMessageContext();
        HttpServletRequest sr = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);

        if (sr != null && sr instanceof HttpServletRequest) {
            final HttpServletRequest hsr = (HttpServletRequest) sr;
            hsr.getSession(true).invalidate();
            hsr.getSession().setMaxInactiveInterval(1);
        }

    }

}
