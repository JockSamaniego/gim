package ec.gob.gim.security.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.envers.Audited;

@Audited
@Entity
@TableGenerator(
	 name="RoleGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Role",
	 initialValue=1, allocationSize=1
)
@NamedQueries({
		@NamedQuery(name="Role.findByUserId", 
					query="SELECT o FROM Role o LEFT JOIN FETCH o.users u WHERE u.id = :userId"),
		@NamedQuery(name="Role.findAll", query="select o from Role o")
})
public class Role {
	@Id
	@GeneratedValue(generator = "RoleGenerator", strategy = GenerationType.TABLE)
	private Long id;
	private String name;
	
	@Column(length = 2147483647, columnDefinition="TEXT")
	private String description;
	
	@ManyToMany
	private List<User> users;
	
	@OneToMany(mappedBy="role",cascade=javax.persistence.CascadeType.ALL)
	@Cascade(CascadeType.DELETE_ORPHAN)
	private List<Permission> permissions;
	
	public Role() {
		users = new ArrayList<User>();
		permissions =  new ArrayList<Permission>();
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
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public List<User> getUsers() {
		Collections.sort(users);
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	} 
	
	public void add(User user){
		if(user!=null && !users.contains(user)){
			this.users.add(user);
			user.add(this);
		}
	}
	
	public void remove(User user){
		if(user!=null && users.contains(user)){
			this.users.remove(user);
			user.remove(this);
		}
	}
	
	public void add(Permission per){
		if(per !=null && !permissions.contains(per)){
			this.permissions.add(per);
			per.setRole(this);
		}
	}
	
	public void remove(Permission per){
		if(per != null && permissions.contains(per)){
			this.permissions.remove(per);
			per.setRole(null);
			per.setAction(null);
		}
	}
}
