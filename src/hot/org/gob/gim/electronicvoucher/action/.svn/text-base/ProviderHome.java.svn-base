package org.gob.gim.electronicvoucher.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.common.model.IdentificationType;
import ec.gob.gim.complementvoucher.model.Provider;
import ec.gob.loja.client.clients.UserClient;
import ec.gob.loja.client.model.Message;
import ec.gob.loja.client.model.UserWS;
import java.util.logging.Level;
import javax.faces.application.FacesMessage;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.core.ResourceBundle;

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
            UserWS userws = new UserWS();
            userws.setIdentification(instance.getIdentificationNumber());
            userws.setName(instance.getName());
            userws.setSurname(instance.getLastname());
            userws.setActive(Boolean.TRUE);
            userws.setEmail(instance.getEmail());
            userws.setPassword(instance.getIdentificationNumber());
            userws.setPhone("");
            this.sendToService(userws);
            addFacesMessageFromResourceBundle("update.mail.sri");
            //To change body of generated methods, choose Tools | Templates.
        } catch (Exception ex) {
            System.out.println("persist sri >>> error >>>>> " + ex.getStackTrace().toString());
            ex.printStackTrace();
            getFacesMessages().addFromResourceBundle(FacesMessage.SEVERITY_ERROR, "noUpdate.mail.sri");
        }
        return r;
    }

    @Override
    public String update() {
        String r = super.update();
//        try{
//            System.out.println("instance.getElectronicVoucher()" 
//                + instance.getElectronicVoucher());
//            System.out.println("instance.getElectronicVoucher().getResident()." 
//                + instance.getElectronicVoucher().getResident());
//            System.out.println("instance.getElectronicVoucher().getResident().getCurrentAddress();" 
//                + instance.getElectronicVoucher().getResident().getCurrentAddress());
//            System.out.println("instance.getElectronicVoucher().getResident().getCurrentAddress().getPhoneNumber());" 
//                + instance.getElectronicVoucher().getResident().getCurrentAddress().getPhoneNumber());
//        }catch(Exception ex){}
        
        try {
            UserWS userws = new UserWS();
            userws.setIdentification(instance.getIdentificationNumber());
            userws.setName(instance.getName());
            userws.setSurname(instance.getLastname());
            userws.setActive(Boolean.TRUE);
            userws.setEmail(instance.getEmail());
            userws.setPassword(instance.getIdentificationNumber());
            try {
                userws.setPhone(instance.getElectronicVoucher().getResident().getCurrentAddress().getPhoneNumber());
            } catch (Exception ex) {
                System.out.println(" setPhone sri >>> error >>>>> <<<<<<");
                ex.printStackTrace();
                userws.setPhone("");
            }
            this.sendToService(userws);
            addFacesMessageFromResourceBundle("update.mail.sri");
            //To change body of generated methods, choose Tools | Templates.
        } catch (Exception ex) {
            System.out.println("update sri >>> error >>>>> " + ex.getStackTrace().toString());
            ex.printStackTrace();
            getFacesMessages().addFromResourceBundle(FacesMessage.SEVERITY_ERROR, "noUpdate.mail.sri");
        }//To change body of generated methods, choose Tools | Templates.
        return r;
    }

    private UserWS sendToService(UserWS input) throws Exception {
        log.info("BASE_URI_SRI >>>>> " + ResourceBundle.instance().getString("BASE_URI_SRI"));
        String BASE_URI = ResourceBundle.instance().getString("BASE_URI_SRI");
        UserClient client = new UserClient(BASE_URI);
        log.info("UserClient client >>>>> <<<<<<");
        UserWS response;
        response = client.saveUser_XML(input, UserWS.class);
        System.out.println("Estado >>>>>>>>>> " + response.getState());

        if (response.getMessageList() != null) {
            List<Message> mensajes = response.getMessageList();
            for (Message mensaje : mensajes) {
                System.out.println(mensaje.getType() + "\t" + mensaje.getIdentifier() + "\t" + mensaje.getMessage() + "\t" + mensaje.getAdditionalInformation());
            }
        }
        return response;
    }

}
