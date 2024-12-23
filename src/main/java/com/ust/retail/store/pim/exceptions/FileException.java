package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FileException extends ApplicationException {
	private static final long serialVersionUID = 1L;
	private final String errorCode;

	public FileException(String msg) {
		super(msg);
		this.errorCode = "ARC00002";
	}

	public String getErrorCode() {
		return errorCode;
	}

}
