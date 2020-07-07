package org.gob.gim.electronicvoucher.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.dto.UserWS;
import org.gob.gim.common.service.ResidentService;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.common.model.IdentificationType;
import ec.gob.gim.complementvoucher.model.Provider;


@Name("providerHome")
public class ProviderHome extends EntityHome<Provider> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Logger
    Log log;

    public void setProviderId(Long id) {
        setId(id);
    }

    public Long getProviderId() {
        return (Long) getId();
    }

    public void load() {
        if (isIdDefined()) {
            wire();
        }
    }

    public void wire() {
        getInstance(); 
    }

    public boolean isWired() {
        return true;
    }

    public Provider getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    public List<IdentificationType> getIdentificationTypes() {
        List<IdentificationType> identificationTypes = new ArrayList<IdentificationType>();
        identificationTypes.add(IdentificationType.TAXPAYER_DOCUMENT);
        identificationTypes.add(IdentificationType.NATIONAL_IDENTITY_DOCUMENT);
        identificationTypes.add(IdentificationType.PASSPORT);
        return identificationTypes;
    }

    @Override
    public String persist() {

        String r = super.persist();
        try {
        	ResidentService residentService = ServiceLocator.getInstance().findResource(ResidentService.LOCAL_NAME);
            UserWS userws = new UserWS();
            userws.setIdentification(instance.getIdentificationNumber());
            userws.setName(instance.getName());
            userws.setSurname(instance.getLastname());
            userws.setActive(Boolean.TRUE);
            userws.setEmail(instance.getEmail());
            userws.setPassword(instance.getIdentificationNumber());
            userws.setPhone("");
           
            residentService.updateUserIntoEBilling(userws);
            addFacesMessageFromResourceBundle("update.mail.sri");
            //To change body of generated methods, choose Tools | Templates.
        } catch (Exception ex) {
            //System.out.println("persist sri >>> error >>>>> " + ex.getStackTrace().toString());
            ex.printStackTrace();
            getFacesMessages().addFromResourceBundle(FacesMessage.SEVERITY_ERROR, "noUpdate.mail.sri");
        }
        return r;
    }

    @Override
    public String update() {
        String r = super.update();
        ResidentService residentService = ServiceLocator.getInstance().findResource(ResidentService.LOCAL_NAME);
        
        try {
            UserWS userws = new UserWS();
            userws.setIdentification(instance.getIdentificationNumber());
            userws.setName(instance.getName());
            userws.setSurname(instance.getLastname());
            userws.setActive(Boolean.TRUE);
            userws.setEmail(instance.getEmail());
            userws.setPassword(instance.getIdentificationNumber());
            userws.setPhone("");
             
            residentService.updateUserIntoEBilling(userws);
            addFacesMessageFromResourceBundle("update.mail.sri");
            //To change body of generated methods, choose Tools | Templates.
        } catch (Exception ex) {
            //System.out.println("update sri >>> error >>>>> " + ex.getStackTrace().toString());
            ex.printStackTrace();
            getFacesMessages().addFromResourceBundle(FacesMessage.SEVERITY_ERROR, "noUpdate.mail.sri");
        }//To change body of generated methods, choose Tools | Templates.
        return r;
    }
}
