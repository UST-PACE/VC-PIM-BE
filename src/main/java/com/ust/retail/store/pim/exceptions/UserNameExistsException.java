package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserNameExistsException extends ApplicationException {
	private static final long serialVersionUID = 1L;
	private final String errorCode;

	public UserNameExistsException(String userName) {
		super(String.format("El nombre de usuario %s, ya existe.", userName));
		this.errorCode = "AHC00016";
	}

	public String getErrorCode() {
		return errorCode;
	}

}
