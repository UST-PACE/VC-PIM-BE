package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InventoryOutOfStockException extends ApplicationException {
	private static final long serialVersionUID = 1L;
	private final String errorCode;

	public InventoryOutOfStockException(Double inventoryQty, Double requestedQty) {
		super(String.format("Transaction Cannot be finished due a out of stock on inventory. requested qty %s - available qty %s", requestedQty, inventoryQty));
		this.errorCode = "PIM-INV-007";
	}

	public String getErrorCode() {
		return errorCode;
	}

}
