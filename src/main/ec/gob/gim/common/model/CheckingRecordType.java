package ec.gob.gim.common.model;

import org.hibernate.envers.Audited;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:27
 */
@Audited
public enum CheckingRecordType {
	REGISTERED,
	CHECKED,
	AUDITED,
	APPROVED,
	NAME_CHANGE,
	DOMAIN_TRANSFER,
	DELETED
}