package org.gob.loja.gim.ws.dto.preemission.response;

public class PreemissionServiceResponse {
	private Boolean error;
	private String errorMessage;
	private Long emissionOrderId;
	private Long bondId;

	public PreemissionServiceResponse() {
		super();
		this.error = Boolean.TRUE;
		this.errorMessage = null;
		this.emissionOrderId = null;
		this.bondId = null;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Long getEmissionOrderId() {
		return emissionOrderId;
	}

	public void setEmissionOrderId(Long emissionOrderId) {
		this.emissionOrderId = emissionOrderId;
	}

	public Long getBondId() {
		return bondId;
	}

	public void setBondId(Long bondId) {
		this.bondId = bondId;
	}

	@Override
	public String toString() {
		return "PreemissionServiceResponse [error=" + error + ", errorMessage="
				+ errorMessage + ", emissionOrderId=" + emissionOrderId
				+ ", bondId=" + bondId + "]";
	}

}
