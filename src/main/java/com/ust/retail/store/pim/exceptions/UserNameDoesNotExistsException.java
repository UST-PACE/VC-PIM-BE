package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserNameDoesNotExistsException extends ApplicationException {
	private static final long serialVersionUID = 1L;
	private final String errorCode;

	public UserNameDoesNotExistsException(String userName) {
		super(String.format("User %s does not exist.", userName));
		this.errorCode = "AHC00018";
	}

	public String getErrorCode() {
		return errorCode;
	}

}
