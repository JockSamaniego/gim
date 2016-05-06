/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gob.gim.coercive.action;

import java.math.BigDecimal;

/**
 *
 * @author Usuario
 */
public class NotificationTotalDescendenteOrdenDTO {
	
	public String getNumberNotification() {
		return numberNotification;
	}
	public void setNumberNotification(String numberNotification) {
		this.numberNotification = numberNotification;
	}
	public String getNameBound() {
		return nameBound;
	}
	public void setNameBound(String nameBound) {
		this.nameBound = nameBound;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public String numberNotification;
	public String nameBound;
	public BigDecimal value;
	

    public NotificationTotalDescendenteOrdenDTO(String numberNotification, String nameBound, BigDecimal value ) {
        this.numberNotification = numberNotification;
        this.nameBound = nameBound;
        this.value = value;
    }
      
   
}
