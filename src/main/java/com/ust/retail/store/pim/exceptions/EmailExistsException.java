package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailExistsException extends ApplicationException {
	private static final long serialVersionUID = 1L;
	private final String errorCode;

	public EmailExistsException(String email) {
		super(String.format("El correo %s, ya existe.", email));
		this.errorCode = "AHC00015";
	}

	public String getErrorCode() {
		return errorCode;
	}

}
