package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SalesInventoryException extends ApplicationException {
	private static final long serialVersionUID = 1L;
	private final String errorCode;

	public SalesInventoryException(String productName, String principalSKU) {
		super(String.format("Product %s with SKU %s cannot be sold - OOS.", productName, principalSKU));
		this.errorCode = "PIM-INV-SALES-001";
	}

	public String getErrorCode() {
		return errorCode;
	}

}
