package ec.gob.gim.common.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

@Audited
@Entity
@TableGenerator(
		 name="AttachmentGenerator",
		 table="IdentityGenerator",
		 pkColumnName="name",
		 valueColumnName="value",
		 pkColumnValue="Attachment",
		 initialValue=1, allocationSize=1
	)
public class Attachment {

	@Id
	@GeneratedValue(generator="AttachmentGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private String name;
	
	private long size;
	
	private String contentType;
	
	//@Lob No usar Lob si se esta trabajando en postgresql
	@Column(length = 2147483647)
	@Basic(fetch = FetchType.LAZY)
	private byte[] data;

	public Long getId() { 
		return this.id; 
	}
	public void setId(Long id) { 
		this.id = id; 
	}

	public String getName() { 
		return this.name;	
	}
	public void setName(String name) { 
		this.name = name; 
	}

	public long getSize() { 
		return this.size; 
	}
	public void setSize(long size) { 
		this.size = size; 
	}
	
	public String getContentType() { 
		return this.contentType; 
	}
	public void setContentType(String contentType) { 
		this.contentType = contentType; 
	}
	
	public byte[] getData() { 
		return this.data; 
	}
	public void setData(byte[] data) { 
		this.data = data; 
	}
}