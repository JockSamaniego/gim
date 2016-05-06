package org.gob.gim.common;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletRequest;

import org.gob.gim.common.action.Gim;
import org.gob.gim.common.action.UserSession;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.ResourceBundle;
import org.jboss.seam.security.Identity;

import ec.gob.gim.security.model.Permission;
import ec.gob.gim.security.model.SessionRecordType;
//import org.gob.gim.security.action.MenuGeneratorAction;

public class AuthorizationPhaseListener implements
		javax.faces.event.PhaseListener {

	private static final long serialVersionUID = 1L;
	private FacesContext context;

	@Override
	public void afterPhase(PhaseEvent event) {
	}

	private void disable(UIComponent component) {
		
		Iterator<UIComponent> children = component.getFacetsAndChildren();
		String value = (String)component.getAttributes().get("bypassDisabled");
		boolean bypassDisabled = value != null ? new Boolean(value) : false;

		if (children.hasNext()) {
			if (!bypassDisabled) {
				component.getAttributes().put("disabled", true);
				while (children.hasNext()) {
					UIComponent childComponent = children.next();
					disable(childComponent);
				}
			}			
		} else {
			component.getAttributes().put("disabled", true);
		}
	}
	
	private void redirect(String uri){
		try {
			context.getExternalContext().redirect(uri);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		
		if(Identity.instance().getPrincipal() != null){
			context = FacesContext.getCurrentInstance();
			String checkedUrl = ((HttpServletRequest) context.getExternalContext().getRequest()).getRequestURI();
			Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
			UserSession userSession = (UserSession) Contexts.getSessionContext().get(UserSession.class);
			
			Gim gim = (Gim) Contexts.getApplicationContext().get(Gim.class);
			if(!gim.isSessionActive(userSession.getUser())){
				String sessionClosedFromManager = ResourceBundle.instance().getString("security.sessionClosedFromManager");
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, sessionClosedFromManager, null));
				userSession.logout(SessionRecordType.FORCED_LOGOUT);
				//userSession.setLogoutType(SessionRecordType.FORCED_LOGOUT);
				//Identity.instance().logout();
				//userSession.postLogout();
				redirect("/gim/login.seam");
				return;
			}
			
			Permission permission = userSession.findPermission(checkedUrl.toLowerCase());
			
			if (permission == null) {
				if(!checkedUrl.contains("/error.")){
					sessionMap.put("requestedUrl", checkedUrl);
					redirect("/gim/error.seam");
				} else {
					String requestedUrl = (String)sessionMap.get("requestedUrl");
					
					String noPrivilegesDefinedTemplate = ResourceBundle.instance().getString("security.noPrivilegesDefinedForResource");
					String resourceName = (requestedUrl == null ? ResourceBundle.instance().getString("security.requested") : requestedUrl);
					System.out.println(resourceName);
					userSession.notifySecurityViolation(requestedUrl);
					
					String noPrivilegesDefinedMsg = MessageFormat.format(noPrivilegesDefinedTemplate, userSession.getPerson().getName(), resourceName);
					
					String incidentReportedMsg = ResourceBundle.instance().getString("security.incidentWillBeReported");
					
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, noPrivilegesDefinedMsg, null));
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, incidentReportedMsg, null));
					sessionMap.remove("requestedUrl");
				}
			} else {
				String permissionTypeName = permission.getPermissionType().name();
				if (permissionTypeName == null
						|| "read".equalsIgnoreCase(permissionTypeName)) {
					disable(event.getFacesContext().getViewRoot());
				}
			}
		}
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

}
