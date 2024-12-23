package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GeniUpcValidationException extends ApplicationException {
	private static final long serialVersionUID = 1L;
	private final String errorCode;

	public GeniUpcValidationException(Exception e) {
		super(e.getMessage());
		this.errorCode = "GENI-EX-0001";
	}

	public String getErrorCode() {
		return errorCode;
	}

}
