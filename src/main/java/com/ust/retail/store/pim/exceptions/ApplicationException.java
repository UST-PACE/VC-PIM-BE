package com.ust.retail.store.pim.exceptions;

public class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	protected String errorCode;

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(String message, String errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
