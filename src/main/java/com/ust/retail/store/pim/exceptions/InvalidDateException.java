package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidDateException extends ApplicationException {
	private static final long serialVersionUID = 1L;
	private final String errorCode;

	public InvalidDateException(String date) {
		super(String.format("la fecha %s, no es válida", date));
		this.errorCode = "AHC00014";
	}

	public String getErrorCode() {
		return errorCode;
	}
}
