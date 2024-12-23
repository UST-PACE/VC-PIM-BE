package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidOldPasswordException extends ApplicationException {
	private static final long serialVersionUID = 1L;
	private final String errorCode;

	public InvalidOldPasswordException() {
		super("Invalid password.");
		this.errorCode = "PIM-SECURITY-001";
	}

	public String getErrorCode() {
		return errorCode;
	}

}
