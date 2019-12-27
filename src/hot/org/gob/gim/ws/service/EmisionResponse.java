package org.gob.gim.ws.service;

import org.gob.loja.gim.ws.dto.EmisionDetail;

/**
 * 
 * @author rfam
 * @since 2019-10-25
 */
public class EmisionResponse {

	/**
	 * mensaje detallado
	 */
	private String message;
	/**
	 * ok error
	 */
	private String status;

	private Long emisionOrderId;
	
	/**
	 * detalle con la informacipon de la foto multa, la misma que se presenta en el log
	 */
	private String detail;
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getEmisionOrderId() {
		return emisionOrderId;
	}

	public void setEmisionOrderId(Long emisionOrderId) {
		this.emisionOrderId = emisionOrderId;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		//String s = street != null ? street : "";
		String s ="\t=> status: " + status +" "
				+ "\t=> message: " + message +" "
				+ "\t=> emisionOrderId: " + emisionOrderId != null ? emisionOrderId.toString() : "-"
				+ "\t=> detail: " + detail;
		return s;
	}

}
