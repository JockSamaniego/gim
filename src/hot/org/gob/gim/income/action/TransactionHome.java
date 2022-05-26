package org.gob.gim.income.action;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.income.model.Payment;

@Name("transactionHome")
public class TransactionHome extends EntityHome<Payment> {

    private static final long serialVersionUID = 5269335956457694018L;
    
    @In
    FacesMessages facesMessages;

    private Long transactionId;
    private String criteria = "";
    private Payment payment;
    
    public void setPaymentId(Long id) {
        setId(id);
    }

    public Long getPaymentId() {
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
    
    /**
     * @return the transactionId
     */
    public Long getTransactionId() {
        return transactionId;
    }

    /**
     * @param transactionId the transactionId to set
     */
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * @return the criteria
     */
    public String getCriteria() {
        return criteria;
    }

    /**
     * @param criteria the criteria to set
     */
    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    /**
     * @return the payment
     */
    public Payment getPayment() {
        return payment;
    }

    /**
     * @param payment the payment to set
     */
    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void addMessages(){
        String message = Interpolator.instance().interpolate("Fallo en el número de transacción", new Object[0]);
        facesMessages.addToControl("",
                org.jboss.seam.international.StatusMessage.Severity.ERROR,
                message);
    }
    
    public boolean isWired() {
        return true;
    }

    public Payment getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    public void searchTransaction(){
        if (criteria == "") return;
        try{
            transactionId = Long.parseLong(criteria);
        } catch (Exception e){
            addMessages();
            clear();
        }
        criteria = transactionId.toString();
        payment = getEntityManager().find(Payment.class, transactionId);
        if (payment == null){
            addMessages();
        }
    }
    
    public void clear(){
        transactionId = 0L;
        criteria = "";
        payment = null;
    }
    
}
