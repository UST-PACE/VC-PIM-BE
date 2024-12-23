package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidTxnNumberAuthorizationException extends ApplicationException {
	private static final long serialVersionUID = 1L;
	private final String errorCode;

	public InvalidTxnNumberAuthorizationException(Long txn) {
		super(String.format("TXN number %s is not valid for authorization.", txn));
		this.errorCode = "PIM-INV-TXN-0002";
	}

	public String getErrorCode() {
		return errorCode;
	}

}
