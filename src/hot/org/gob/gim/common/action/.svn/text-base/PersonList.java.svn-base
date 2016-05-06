package org.gob.gim.common.action;

import ec.gob.gim.common.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("personList")
public class PersonList extends EntityQuery<Person> {

	private static final String EJBQL = "select person from Person person";

	private static final String[] RESTRICTIONS = {
			"lower(person.firstName) like lower(concat(#{personList.person.firstName},'%'))",
			"lower(person.handicapedNumber) like lower(concat(#{personList.person.handicapedNumber},'%'))",
			"lower(person.lastName) like lower(concat(#{personList.person.lastName},'%'))",};

	private Person person = new Person();

	public PersonList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Person getPerson() {
		return person;
	}
}
