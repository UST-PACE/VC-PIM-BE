package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PurchaseOrderUpdateException extends ApplicationException {

	private static final long serialVersionUID = 1L;

	public PurchaseOrderUpdateException(String purchaseOrderNum, String status) {
		super(String.format("Purchase order [%s] has already been marked as '%s' and can't be modified", purchaseOrderNum, status));
		this.errorCode = "PIM-PO-UPDATE-001";
	}
}
