package org.gob.gim.action;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import org.jboss.seam.annotations.Name;

@Name("ELEvaluator")
public class ELEvaluator {
	
	public void evaluateMethodBinding(String el) {
		System.out.println("=====> INGRESO a evaluateMethodBinding() con param: " + el);
		ValueExpression ve = FacesContext.getCurrentInstance().getApplication().getExpressionFactory()
			.createValueExpression(FacesContext.getCurrentInstance().getELContext(), "#{" + el + "}", Object.class);
		ve.getValue(FacesContext.getCurrentInstance().getELContext());
	}

}
