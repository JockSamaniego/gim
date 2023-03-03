package ec.gob.gim.income.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.security.model.User;

/**
 * Certificado de Valores Pendientes
 * @author Ronald Paladines Celi
 * @version 1.0
 * @created 19-Oct-2022
 */
@Audited
@Entity
@Table 
@TableGenerator(
     name="PendingCertificateGenerator",
     table="IdentityGenerator",
     pkColumnName="name",
     valueColumnName="value",
     pkColumnValue="PendingCertificate",
     initialValue=1, allocationSize=1
)

public class PendingCertificate implements Serializable{

    private static final long serialVersionUID = 7135739421250523673L;

    @Id
    @GeneratedValue(generator="PendingCertificateGenerator",strategy=GenerationType.TABLE)
    private Long id;
    
	@Column(length=40)
    private String memoNumber;

	@Column(columnDefinition = "TEXT")
    private String jsonData;
    
    private BigDecimal total;
    
    private boolean totalEntries;

    @Temporal(TemporalType.TIMESTAMP)
    private Date systemDate;

    @ManyToOne(cascade=CascadeType.ALL)
    private Resident resident;    
    
    @ManyToOne(cascade=CascadeType.ALL)
    private User responsable;    
    
    public PendingCertificate(){
        total = BigDecimal.ZERO;
        totalEntries = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemoNumber() {
        return memoNumber;
    }

    public void setMemoNumber(String memoNumber) {
        this.memoNumber = memoNumber;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public boolean isTotalEntries() {
        return totalEntries;
    }

    public void setTotalEntries(boolean totalEntries) {
        this.totalEntries = totalEntries;
    }

    public Date getSystemDate() {
        return systemDate;
    }

    public void setSystemDate(Date systemDate) {
        this.systemDate = systemDate;
    }

    public Resident getResident() {
        return resident;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
    }

    public User getResponsable() {
        return responsable;
    }

    public void setResponsable(User responsable) {
        this.responsable = responsable;
    }

}