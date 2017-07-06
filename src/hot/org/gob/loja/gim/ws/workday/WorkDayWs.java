/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gob.loja.gim.ws.workday;

import org.gob.loja.gim.ws.service.WorkDay;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.income.model.Till;
import ec.gob.gim.income.model.TillPermission;
import ec.gob.gim.parking.model.Journal;
import ec.gob.gim.security.model.Role;
import ec.gob.gim.security.model.User;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.annotation.Resource;
import javax.faces.context.ExternalContext;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.gob.gim.common.PasswordManager;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.Gim;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.loja.gim.ws.dto.ServiceRequest;
import org.gob.loja.gim.ws.dto.Statement;
import org.gob.loja.gim.ws.exception.InvalidUser;
import org.gob.loja.gim.ws.exception.CashierOpen;
import org.gob.loja.gim.ws.exception.DateNoAvalible;
import org.gob.loja.gim.ws.exception.ExistsDuplicateUsers;
import org.gob.loja.gim.ws.exception.InterestRateNoDefined;
import org.gob.loja.gim.ws.exception.TaxesNoDefined;
import org.gob.loja.gim.ws.service.PaymentService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

/**
 * rfam 2017-07-06
 * se comenta no se esta usando
 */
//@WebService
//        (serviceName = "WorkDayWs")
public class WorkDayWs {

  /*  @Resource
    WebServiceContext wsContext;*/

    @EJB
    private WorkDay service;

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

    }

    private void InvalidateSession() {
        final MessageContext mc = this.wsContext.getMessageContext();
        HttpServletRequest sr = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);

        if (sr != null && sr instanceof HttpServletRequest) {
            final HttpServletRequest hsr = (HttpServletRequest) sr;
            hsr.getSession(true).invalidate();
            hsr.getSession().setMaxInactiveInterval(1);
        }

    }*/

}
