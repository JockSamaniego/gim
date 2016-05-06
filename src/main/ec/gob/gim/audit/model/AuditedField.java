package ec.gob.gim.audit.model;

public class AuditedField {
	private Boolean isReported;
	private String name;
	private String label;
	
	public AuditedField() {
		isReported = Boolean.FALSE;
		name="";
	}
	
	public Boolean getIsReported() {
		return isReported;
	}


	public void setIsReported(Boolean isReported) {
		this.isReported = isReported;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {		
		return obj != null ? name.equalsIgnoreCase(obj.toString()) : false;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
