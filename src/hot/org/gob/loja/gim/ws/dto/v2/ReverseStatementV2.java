package org.gob.loja.gim.ws.dto.v2;

public class ReverseStatementV2 {
	
	private String code;
	private String message;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "StatementV2 [code=" + code + ", message=" + message + "]";
	}

}
