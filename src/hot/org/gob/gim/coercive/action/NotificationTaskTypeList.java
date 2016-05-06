package org.gob.gim.coercive.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.coercive.model.NotificationTaskType;

@Name("notificationTaskTypeList")
public class NotificationTaskTypeList extends EntityQuery<NotificationTaskType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7829139376689892812L;

	private static final String EJBQL = "select notificationTaskType from NotificationTaskType notificationTaskType";

	private static final String[] RESTRICTIONS = {
		"(lower(notificationTaskType.name) like lower(concat(#{notificationTaskTypeList.criteria},'%')) or " +
		"lower(notificationTaskType.description) like lower(concat(:el1,'%')) ", 
		"notificationTaskType.sequence != #{notificationTaskTypeList.sequence == 1 ? 0 : 1}"};
	
	private String criteria;
	
	private Long sequence;
	
	private NotificationTaskType notificationTaskType = new NotificationTaskType();

	public NotificationTaskTypeList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
		setOrderDirection("ASC");
		setOrderColumn("name");
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public NotificationTaskType getNotificationTaskType() {
		return notificationTaskType;
	}

	public void setNotificationTaskType(NotificationTaskType notificationTaskType) {
		this.notificationTaskType = notificationTaskType;
	}

	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}


}
