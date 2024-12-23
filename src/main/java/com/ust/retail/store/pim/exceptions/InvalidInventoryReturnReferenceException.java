package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidInventoryReturnReferenceException extends ApplicationException {
	private static final long serialVersionUID = 1L;
	private final String errorCode;

	public InvalidInventoryReturnReferenceException(Long inventoryReturnId) {
		super(String.format("Invalid Inventory return id %s", inventoryReturnId));
		this.errorCode = "PIM-INV-ADJUSTMENT-005";
	}

	public String getErrorCode() {
		return errorCode;
	}

}
