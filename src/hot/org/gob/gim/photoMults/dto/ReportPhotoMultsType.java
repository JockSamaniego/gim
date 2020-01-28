package org.gob.gim.photoMults.dto;

public enum ReportPhotoMultsType {
	PHOTO_MULTS_EMIT(new String("FOTO MULTAS EMITIDAS"), "Emisión"), 
	PHOTO_MULTS_PRE_EMIT(new String("FOTO MULTAS PRE EMITIDAS"), "Pre-Emisión"), 
	PHOTO_MULTS_LOW(new String("FOTO MULTAS DADAS DE BAJA"), "Baja"), 
	PHOTO_MULTS_PAID(new String("FOTO MULTAS PAGADAS"),"Pago"), 
	PHOTO_MULTS_EXTEMPORARY(new String("FOTO MULTAS EXTEMPORANEAS"), "Registro"), ;

	String label;
	String labelDate;

	private ReportPhotoMultsType(String label, String labelDate) {
		this.label = label;
		this.labelDate = labelDate;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabelDate() {
		return labelDate;
	}

	public void setLabelDate(String labelDate) {
		this.labelDate = labelDate;
	}
	
}
