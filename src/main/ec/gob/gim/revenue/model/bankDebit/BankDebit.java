package ec.gob.gim.revenue.model.bankDebit;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.waterservice.model.WaterSupply;

/**
 * @author rene
 * @date 2019-05-22
 * @summary Clase para Debitos Bancarios
 */
@Audited
@Entity
@TableGenerator(name = "BankDebitGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "BankDebit", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { @NamedQuery(name = "BankDebit.findById", query = "select ban from BankDebit ban join fetch ban.waterSupply ws join fetch ws.recipeOwner res where ban.id=:bankDebitId") })
public class BankDebit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "BankDebitGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "waterSupply_id", nullable = false, referencedColumnName = "id")
	private WaterSupply waterSupply;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accountType_itm_id", nullable = false, referencedColumnName = "id")
	private ItemCatalog accountType;

	private String accountNumber;

	private String accountHolder;

	private Boolean active;

	@Version
	private Long version = 0L;

	public BankDebit() {
		this.active = Boolean.TRUE;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public WaterSupply getWaterSupply() {
		return waterSupply;
	}

	public void setWaterSupply(WaterSupply waterSupply) {
		this.waterSupply = waterSupply;
	}

	public ItemCatalog getAccountType() {
		return accountType;
	}

	public void setAccountType(ItemCatalog accountType) {
		this.accountType = accountType;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BankDebit)) {
			return false;
		}
		BankDebit other = (BankDebit) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		if (id == null) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "BankDebit [id=" + id + ", waterSupply=" + waterSupply
				+ ", accountType=" + accountType + ", accountNumber="
				+ accountNumber + ", accountHolder=" + accountHolder
				+ ", active=" + active + ", version=" + version + "]";
	}

}