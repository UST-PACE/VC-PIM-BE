package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidUPCException extends ApplicationException {
	private static final long serialVersionUID = 1L;
	private final String errorCode;

	public InvalidUPCException(String code) {
		super(String.format("Code number %s is not valid.", code));
		this.errorCode = "PIM-INV-002";
	}

	public InvalidUPCException(Long code) {
		this(code.toString());
	}

	public String getErrorCode() {
		return errorCode;
	}

}
