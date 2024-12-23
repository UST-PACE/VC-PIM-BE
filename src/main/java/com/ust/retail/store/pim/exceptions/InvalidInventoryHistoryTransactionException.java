package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidInventoryHistoryTransactionException extends ApplicationException {
	private static final long serialVersionUID = 1L;
	private final String errorCode;

	public InvalidInventoryHistoryTransactionException(Long inventoryHistoryId) {
		super(String.format("History id %s is not valid for authorization.", inventoryHistoryId));
		this.errorCode = "PIM-INV-004";
	}

	public String getErrorCode() {
		return errorCode;
	}

}
