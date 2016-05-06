/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gob.gim.coercive.action;

/**
 *
 * @author Usuario
 */
public class DataModelConsultNotification {

    public DataModelConsultNotification(String identificationNumber, String nameNotifier, long numberNotification ) {
        this.nameNotifier = nameNotifier;
        this.numberNotification = numberNotification;
        this.identificationNumber = identificationNumber;
    }
      
    private String nameNotifier;
    private long numberNotification;
    private String identificationNumber;

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    
    
    public long getNumberNotification() {
        return numberNotification;
    }

    public void setNumberNotification(long numberNotification) {
        this.numberNotification = numberNotification;
    }

    public String getNameNotifier() {
        return nameNotifier;
    }

    public void setNameNotifier(String nameNotifier) {
        this.nameNotifier = nameNotifier;
    }
}
