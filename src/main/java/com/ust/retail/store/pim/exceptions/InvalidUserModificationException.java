package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidUserModificationException extends ApplicationException {
	private static final long serialVersionUID = 1L;

	public InvalidUserModificationException() {
		super("Insufficient privileges to modify user");
		this.errorCode = "AHC00030";
	}
}
