package ec.gob.gim.common.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.gob.gim.common.IdentificationNumberUtil;
import org.gob.gim.common.exception.IdentificationNumberExistsException;
import org.gob.gim.common.exception.IdentificationNumberSizeException;
import org.gob.gim.common.exception.IdentificationNumberWrongException;
import org.gob.gim.common.exception.InvalidIdentificationNumberException;
import org.gob.gim.common.exception.InvalidIdentificationNumberFinishedException;
import org.gob.loja.gim.ws.dto.Taxpayer;
import org.hibernate.envers.Audited;

import ec.gob.gim.commercial.model.Business;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:40
 */
@Audited
@Entity
@DiscriminatorValue(value = "N")
@NamedQueries(value = {
		@NamedQuery(name = "Person.findReadingMan", query = "SELECT person FROM Person person WHERE lower(person.identificationNumber) LIKE lower(concat(:criteria,'%')) or lower(person.lastName) like lower(concat(:criteria,'%'))"),
		@NamedQuery(name = "Person.findByIdentificationNumber", query = "select person from Person person left join fetch person.currentAddress where "
				+ "person.identificationNumber like :identificationNumber"),
		@NamedQuery(name = "Person.findByRoleName", query = "select distinct(p) from Person p join p.user u join u.roles r where r.name = :roleName ORDER BY p.name"),
		@NamedQuery(name = "Person.findByCriteria", query = "select person from Person person left join fetch person.currentAddress where "
				+ "person.identificationNumber like concat(:criteria,'%') or "
				+ "lower(person.name) like lower(concat(:criteria, '%')) "
				+ "order by person.name") })
public class Person extends Resident {

	public static final String YEAR = "YEAR";
	public static final String MONTH = "MONTH";
	public static final String DATE = "DATE";

	@Column(length = 50)
	private String firstName;

	@Column(length = 50)
	private String lastName;

	@Temporal(TemporalType.DATE)
	private Date birthday;

	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private Gender gender;

	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	private MaritalStatus maritalStatus;

	private Boolean isHandicaped;

	private BigDecimal handicapedPercentage;

	private Boolean isDead;

	private String country;
	
	private Boolean isForeign;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(length = 15)
	private String handicapedNumber;

	/*
	 * Relationship
	 */
	@OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
	private List<Business> managedBusinesses;

	public Person() {
		this.managedBusinesses = new ArrayList<Business>();
		isForeign = Boolean.FALSE;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName.toUpperCase().trim();
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName.toUpperCase().trim();
	}

	/**
	 * @return the birthday
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * @param birthday
	 *            the birthday to set
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * @return the maritalStatus
	 */
	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	/**
	 * @param maritalStatus
	 *            the maritalStatus to set
	 */
	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	/**
	 * @return the isHandicaped
	 */
	public Boolean getIsHandicaped() {
		if (isHandicaped == null)
			isHandicaped = Boolean.FALSE;
		return isHandicaped;
	}

	/**
	 * @param isHandicaped
	 *            the isHandicaped to set
	 */
	public void setIsHandicaped(Boolean isHandicaped) {
		this.isHandicaped = isHandicaped;
	}

	/**
	 * @return the handicapedNumber
	 */
	public String getHandicapedNumber() {
		return handicapedNumber;
	}

	/**
	 * @param handicapedNumber
	 *            the handicapedNumber to set
	 */
	public void setHandicapedNumber(String handicapedNumber) {
		this.handicapedNumber = handicapedNumber;
	}

	/**
	 * @return the managedBusinesses
	 */
	public List<Business> getManagedBusinesses() {
		return managedBusinesses;
	}

	/**
	 * @param managedBusinesses
	 *            the managedBusinesses to set
	 */
	public void setManagedBusinesses(List<Business> managedBusinesses) {
		this.managedBusinesses = managedBusinesses;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return this.getLastName() + " " + this.getFirstName();
	}

	/*
	 * public void assignName(){ this.setName(toString()); }
	 */

	public void add(Business managedBusiness) {
		if (!this.managedBusinesses.contains(managedBusiness)) {
			this.managedBusinesses.add(managedBusiness);
			managedBusiness.setManager(this);
		}
	}

	public void remove(Business managedBusiness) {
		boolean removed = this.managedBusinesses.remove(managedBusiness);
		if (removed)
			managedBusiness.setManager((Person) null);
	}

	public Map<String, Integer> getAge() {
		Calendar now = Calendar.getInstance();

		Calendar birthDay = Calendar.getInstance();
		if (this.birthday != null)
			birthDay.setTime(this.birthday);

		if (birthDay.after(now))
			return null;

		Integer year = now.get(Calendar.YEAR);
		Integer month = now.get(Calendar.MONTH);
		Integer day = now.get(Calendar.DATE);

		// PARA LOS DIAS
		if (day < birthDay.get(Calendar.DATE)) {
			int ndays = now.getActualMaximum(Calendar.DAY_OF_MONTH);
			day = (day + ndays) - birthDay.get(Calendar.DATE);
			// como pedi un mes dias tengo que devolver al mes actual
			month--;
		} else
			day = day - birthDay.get(Calendar.DATE);

		if (month < birthDay.get(Calendar.MONTH)) {
			month = (month + 12) - birthDay.get(Calendar.MONTH);
			// como pedi un anio en meses tengo que devolver al anio actual
			year--;
		} else
			month = month - birthDay.get(Calendar.MONTH);

		if (year > birthDay.get(Calendar.YEAR)) {
			// now.add(Calendar.YEAR, -(birthDate.get(Calendar.YEAR)));
			year = year - birthDay.get(Calendar.YEAR);
		} else {
			year = 0;
		}
		// return new GregorianCalendar(year, month, day);
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put(YEAR, year);
		map.put(MONTH, month);
		map.put(DATE, day);

		return map;
	}

	public String getAgeToString() {
		Map<String, Integer> ageFull = this.getAge();
		if (ageFull != null) {
			Integer year = ageFull.get(YEAR);
			Integer month = ageFull.get(MONTH);
			Integer day = ageFull.get(DATE);

			StringBuffer dateStr = new StringBuffer(year + " a\u00f1o(s), ");
			dateStr.append(month + " mes(es), " + day + " d\u00eda(s)");
			return dateStr.toString();
		} else {
			return "Aun no ha nacido...";
		}
	}

	public Boolean getIsDead() {
		return isDead;
	}

	public void setIsDead(Boolean isDead) {
		this.isDead = isDead;
	}

	public BigDecimal getHandicapedPercentage() {
		return handicapedPercentage;
	}

	public void setHandicapedPercentage(BigDecimal handicapedPercentage) {
		this.handicapedPercentage = handicapedPercentage;
	}

	public static Person createInstance(Taxpayer taxpayer) {
		Person person = new Person();

		if (taxpayer.getIdentificationNumber().length() < 10) {
			person.setIdentificationType(IdentificationType.PASSPORT);
		} else {
			person.setIdentificationType(IdentificationType.NATIONAL_IDENTITY_DOCUMENT);
		}
		person.setFirstName(taxpayer.getFirstName());
		person.setLastName(taxpayer.getLastName());
		person.setEmail(taxpayer.getEmail());
		person.setIdentificationNumber(taxpayer.getIdentificationNumber());
		// Por defecto todos masculinos
		person.setGender(Gender.MALE);
		person.setName(person.toString());
		person.setCountry("Ecuador");
		// Direccion Actual
		person.getCurrentAddress().setStreet(taxpayer.getStreet());
		person.getCurrentAddress().setPhoneNumber(taxpayer.getPhoneNumber());
		person.getCurrentAddress().setCity("Loja");
		person.getCurrentAddress().setCountry("Ecuador");

		return person;
	}

	public Boolean isIdentificationNumberValid(String identificationNumber)
			throws IdentificationNumberSizeException,
			IdentificationNumberWrongException,
			InvalidIdentificationNumberException,
			InvalidIdentificationNumberFinishedException,
			IdentificationNumberExistsException {

		if (getIdentificationType() == IdentificationType.NATIONAL_IDENTITY_DOCUMENT) {
			return IdentificationNumberUtil.getInstance()
					.isNationalIdentityNumberValid(identificationNumber);
		}
		if (getIdentificationType() == IdentificationType.TAXPAYER_DOCUMENT) {
			return IdentificationNumberUtil.getInstance()
					.isTaxpayerNumberValid(identificationNumber);
		}
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Person)) {
			return false;
		}

		Person other = (Person) obj;
		if (this.getId() != null) {
			if (!this.getId().equals(other.getId())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		return result;
	}

	public Boolean getIsForeign() {
		return isForeign;
	}

	public void setIsForeign(Boolean isForeign) {
		this.isForeign = isForeign;
	}

}// end Person