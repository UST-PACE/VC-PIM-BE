package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidPurchaseOrderException extends ApplicationException {
	private static final long serialVersionUID = 1L;

	public InvalidPurchaseOrderException(String purchaseOrderNumber) {
		super(String.format("Purchase Order : '%s' does not exists", purchaseOrderNumber));
		this.errorCode = "PIM-PO-001";
	}


}
