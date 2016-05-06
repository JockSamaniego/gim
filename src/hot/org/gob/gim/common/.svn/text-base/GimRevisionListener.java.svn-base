package org.gob.gim.common;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.gob.gim.common.action.Gim;
import org.gob.gim.common.action.UserSession;
import org.hibernate.envers.RevisionListener;
import org.jboss.seam.contexts.Context;
import org.jboss.seam.contexts.Contexts;

import ec.gob.gim.common.model.Revision;
import ec.gob.gim.security.model.User;

public class GimRevisionListener implements RevisionListener {
    public void newRevision(Object revisionEntity) {
    	System.out.println("GimRevisionListener -----> STARTING NEW REVISION METHOD");
    	String username = "externalUser";
    	Context appContext = Contexts.getApplicationContext();
    	if(appContext != null){
    		 //Gim gim = (Gim) appContext.get(Gim.class);
    		 //HttpSession session = gim.getHttpSession();
    		 User user = getSessionUser(); //(User) session.getAttribute("user");
    		 username = user.getName();
    	}
    	Revision revision = (Revision) revisionEntity;
    	revision.setTimestamp(new Date());
    	revision.setUsername(username);
    	System.out.println("GimRevisionListener -----> ENDING NEW REVISION METHOD");
    }
    
    private User getSessionUser(){
    	System.out.println("GimRevisionListener -----> getSessionUser");
    	Gim gim = (Gim) Contexts.getApplicationContext().get(Gim.class);
    	HttpSession session = gim.getHttpSession();
    	User user = null;
    	if(session != null){
    		System.out.println("GimRevisionListener -----> user got from HttpSession");
    		user = (User) session.getAttribute("user");
    	} else {
    		System.out.println("GimRevisionListener -----> user got from UserSession");
    		UserSession userSession = (UserSession) Contexts.getSessionContext().get(UserSession.class);
    		user = userSession.getUser();
    	}
    	System.out.println("GimRevisionListener -----> user got: "+user.getName());
    	return user;
    }
    
}
