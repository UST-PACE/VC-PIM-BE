package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InventoryAdjustmentProcessInProgressException extends ApplicationException {
	private static final long serialVersionUID = 1L;
	private final String errorCode;

	public InventoryAdjustmentProcessInProgressException(String subcategoriesIds) {
		super(String.format("Some selected categories are already in progress for inventory adjustment. Ids:%s", subcategoriesIds));
		this.errorCode = "PIM-INV-ADJUSTMENT-001";
	}

	public String getErrorCode() {
		return errorCode;
	}

}
