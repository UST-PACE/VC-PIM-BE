package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class VendorCreditsInventoryException extends ApplicationException {
	private static final long serialVersionUID = 1L;
	private final String errorCode;

	public VendorCreditsInventoryException(Double currentInventoryQty, Double requestedReturnQty) {
		super(String.format("There is not enough inventory to return. Current inventory %s - requested return %s.",
				currentInventoryQty, requestedReturnQty));
		this.errorCode = "PIM-INV-ADJUSTMENT-003";
	}

	public String getErrorCode() {
		return errorCode;
	}

}
