package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ItemReceivingException extends ApplicationException {
	private static final long serialVersionUID = 1L;
	private final String errorCode;

	public ItemReceivingException(String sku) {
		super(String.format("SKU %s has been already scanned.", sku));
		this.errorCode = "PIM-INV-RECEIVING-001";
	}

	public String getErrorCode() {
		return errorCode;
	}

}
