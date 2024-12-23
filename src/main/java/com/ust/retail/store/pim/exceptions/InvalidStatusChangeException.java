package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidStatusChangeException extends ApplicationException {
	private static final long serialVersionUID = 1L;
	private final String errorCode;

	public InvalidStatusChangeException() {
		super("There are no records available to change the status");
		this.errorCode = "PIM-INV-ADJUSTMENT-004";
	}

	public String getErrorCode() {
		return errorCode;
	}

}
