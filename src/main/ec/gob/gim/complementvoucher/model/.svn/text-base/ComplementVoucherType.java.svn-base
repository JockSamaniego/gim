package ec.gob.gim.complementvoucher.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Person;

/**
 * @author manuel
 * @version 1.0
 * @created 17-abr-2015 12:40:33
 */
@Audited
@Entity
@TableGenerator(name = "ComplementVoucherTypeGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "ComplementVoucherType", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { @NamedQuery(name = "ComplementVoucherType.findAll", query = "SELECT cvt FROM ComplementVoucherType cvt order by cvt.name"),
		 				@NamedQuery(name = "ComplementVoucherType.findByCode", query = "SELECT cvt FROM ComplementVoucherType cvt where cvt.code=:code order by cvt.name"),
		 				@NamedQuery(name = "ComplementVoucherType.findInCode", query = "SELECT cvt FROM ComplementVoucherType cvt where cvt.code in :list order by cvt.name")}
)
public class ComplementVoucherType {

	@Id
	@GeneratedValue(generator = "ComplementVoucherTypeGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length = 50, nullable = false)
	private String name;
	
	@Column(nullable = false)
	private Boolean requiresAproval;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private VersionVoucher versionVoucher;
	
	@Column(length = 10, nullable = false)
	private String currentVersionVoucher;
	
	@Column(length = 50, nullable = false)
	private String code;
	
	@OneToMany(mappedBy = "complementVoucherType", cascade = {CascadeType.ALL}) 
	private List<TypeEmissionPoint> types;	
	
	
	
	public ComplementVoucherType() {

	}

	public void finalize() throws Throwable {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.toUpperCase();
	}


	public Boolean getRequiresAproval() {
		return requiresAproval;
	}

	public void setRequiresAproval(Boolean requiresAproval) {
		this.requiresAproval = requiresAproval;
	}

	public VersionVoucher getVersionVoucher() {
		return versionVoucher;
	}

	public void setVersionVoucher(VersionVoucher versionVoucher) {
		this.versionVoucher = versionVoucher;
	}

	public String getCurrentVersionVoucher() {
		return currentVersionVoucher;
	}

	public void setCurrentVersionVoucher(String currentVersionVoucher) {
		this.currentVersionVoucher = currentVersionVoucher;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public List<TypeEmissionPoint> getTypes() {
		return types;
	}

	public void setTypes(List<TypeEmissionPoint> types) {
		this.types = types;
	}
}// end ComplementVoucherType