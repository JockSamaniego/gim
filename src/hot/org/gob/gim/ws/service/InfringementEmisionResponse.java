package org.gob.gim.ws.service;

import org.gob.loja.gim.ws.dto.InfringementEmisionDetail;
import org.gob.loja.gim.ws.dto.ant.BondDTO;

public class InfringementEmisionResponse {
	/**
	 * mensaje detallado
	 */
	private String message;
	/**
	 * ok error
	 */
	private String status;

	private Long municipalBondId;
	private Long municipalBondNumber;
	
	private BondDTO bond;

	/**
	 * detalle con la informacion que llego por el servicio web
	 */
	private String detail;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		// String s = street != null ? street : "";
		String s = "\t=> status: " + status + " " + "\t=> message: " + message
				+ " " + "\t=> municipalBondId: " + municipalBondId != null ? municipalBondId
				.toString() : "-" + "\t=> municipalBondNumber: "
				+ municipalBondNumber != null ? municipalBondNumber.toString()
				: "-" + "\t=> detail: " + detail;
		return s;
	}

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

	public Long getMunicipalBondId() {
		return municipalBondId;
	}

	public void setMunicipalBondId(Long municipalBondId) {
		this.municipalBondId = municipalBondId;
	}

	public Long getMunicipalBondNumber() {
		return municipalBondNumber;
	}

	public void setMunicipalBondNumber(Long municipalBondNumber) {
		this.municipalBondNumber = municipalBondNumber;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	/**
	 * @return the bond
	 */
	public BondDTO getBond() {
		return bond;
	}

	/**
	 * @param bond the bond to set
	 */
	public void setBond(BondDTO bond) {
		this.bond = bond;
	}

}
