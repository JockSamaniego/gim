package org.gob.gim.validator;

/**
 * @author wilman
 */

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.core.Interpolator;
import org.richfaces.model.UploadItem;

@Name("ruleValidator")
@BypassInterceptors
@org.jboss.seam.annotations.faces.Validator
public class RuleValidator implements
		javax.faces.validator.Validator, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;

	/*@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		// TODO Auto-generated method stub
		if (value != null) {
			String code = value.toString().trim();
			
			try{
				Class.forName(code);
			}catch(ClassNotFoundException cnfe){
				message = Interpolator.instance().interpolate(
						"#{messages['entryDefinition.wrongRule']}",
						new Object[0]);
				throw new ValidatorException(new FacesMessage(message));
			}
			
			

		} else {
			message = Interpolator.instance().interpolate(
					"#{messages['entryDefinition.requiredRule']}",
					new Object[0]);
			throw new ValidatorException(new FacesMessage(message));
		}

	}*/
	
	
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		// TODO Auto-generated method stub
		if (value != null) {
			UploadItem item = (UploadItem)value;
			KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			InputStream is = new ByteArrayInputStream(item.getData());
			builder.add(ResourceFactory.newInputStreamResource(is), ResourceType.DRL);
			
			if (builder.hasErrors()){
				Exception e = new RuntimeException(builder.getErrors().toString());
				/*message = Interpolator.instance().interpolate(
						"#{messages['entryDefinition.wrongRule']}",
						new Object[0]);*/
				message = e.getMessage();
				throw new ValidatorException(new FacesMessage(message));
	        }
			
			
		}else {
			message = Interpolator.instance().interpolate(
					"#{messages['entryDefinition.requiredRule']}",
					new Object[0]);
			throw new ValidatorException(new FacesMessage(message));
		}
	}
}
