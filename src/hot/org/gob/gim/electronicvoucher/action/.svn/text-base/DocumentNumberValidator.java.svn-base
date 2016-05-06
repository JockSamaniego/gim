/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gob.gim.electronicvoucher.action;

import ec.gob.gim.complementvoucher.model.ElectronicVoucher;
import java.io.Serializable;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.faces.Validator;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import java.util.regex.Matcher;

/**
 *
 * @author Usuario
 */
@Name("documentNumberValidator")
@Validator
@BypassInterceptors
public class DocumentNumberValidator extends EntityHome<ElectronicVoucher> implements javax.faces.validator.Validator,
        Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3876319398131645955L;
    Log log = Logging.getLog(DocumentNumberValidator.class);

//    private ElectronicVoucher instance;
//    public ElectronicVoucher getInstance() {
//        return instance;
//    }
    public DocumentNumberValidator() {
    }

//    public void setInstance(ElectronicVoucher instance) {
//        this.instance = instance;
//    }
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DocumentNumberValidator(ElectronicVoucher instance) {
        this.instance = instance;
    }

    public void validate(FacesContext context, UIComponent component,
            Object value) throws ValidatorException {

        
        String voucher = (String) value;
         
        log.info("validating....!");
        getInstance();
        System.out.println("true>>>>> this.instance.getId()");
        this.instance.setDocumentNumber("000-000-123456789");

        if (!voucher.equals(this.instance.getDocumentNumber())) {
            System.out.println("true>>>>> voucher");
            //regex = "^[0-9]{3}-[0-9]{3}-[0-9]{9}$", message = "Debe usar el formato xxx-xxx-xxxxxxxxx")
            Pattern pattern = Pattern.compile(Interpolator.instance().interpolate("#{messages['electronicVoucher.bildPatterError']}", new Object[0]));
            boolean found = false;
            Matcher matcher = pattern.matcher(voucher);
            while (matcher.find()) {
//                System.out.printf("I found the text" +
//                    " \"%s\" starting at " +
//                    "index %d and ending at index %d.%n",
//                    matcher.group(),
//                    matcher.start(),
//                    matcher.end());
                found = true;
            }
            if (found) {
                System.out.println("true>>>>> found");
                ((UIInput) component).setValid(true);
            } else {
                ((UIInput) component).setValid(false);
//            FacesMessage message = new FacesMessage();
                System.out.println("error");
                message = Interpolator.instance().interpolate("#{messages['electronicVoucher.bildPatterErrorMsg']}", new Object[0]);
                throw new ValidatorException(new FacesMessage(message));
            }

        } else {
            ((UIInput) component).setValid(false);
//            FacesMessage message = new FacesMessage();
            System.out.println("error");
            message = Interpolator.instance().interpolate("#{messages['electronicVoucher.bildError']}", new Object[0]);
            throw new ValidatorException(new FacesMessage(message));
        }
    }
}
