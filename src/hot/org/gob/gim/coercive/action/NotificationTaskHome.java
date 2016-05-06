package org.gob.gim.coercive.action;

import java.util.Calendar;
import java.util.Date;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.coercive.model.NotificationTask;

@Name("notificationTaskHome")
@Scope(ScopeType.CONVERSATION)
public class NotificationTaskHome extends EntityHome<NotificationTask> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2899071844037443506L;

	@Logger 
	Log logger;
	
	private NotificationTask notificationTask;
	

	public void setNotificationTaskId(Long id) {
		setId(id);
	}

	public Long getNotificationTaskId() {
		return (Long) getId();
	}

	@Override
	protected NotificationTask createInstance() {
		Date now = Calendar.getInstance().getTime();
		notificationTask = new NotificationTask();
		notificationTask.setCreationDate(now);
		return notificationTask;
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

	public NotificationTask getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
}
