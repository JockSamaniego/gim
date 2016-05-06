package ec.gob.gim.common.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.gob.gim.common.GimRevisionListener;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Entity
@RevisionEntity(GimRevisionListener.class)
@NamedQueries(value = {
		@NamedQuery(name = "Revision.findBetweenDates", 
				query = "SELECT revision FROM Revision revision "
				+ "WHERE cast(revision.timestamp as date) BETWEEN :startDate AND :endDate")
})

public class Revision {
	@Id
    @GeneratedValue
    @RevisionNumber
    private Long id;

    @RevisionTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    
    private String username;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int hashCode() {
        return (int) id.intValue();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
        	return true;
        }
        
        if (!(obj instanceof Revision)) {
        	return false;
        }
        Revision revision = (Revision) obj;
        return revision.getId().longValue() == id.longValue();
    }
}
