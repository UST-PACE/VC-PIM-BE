package com.ust.retail.store.bistro.exception;

import com.ust.retail.store.pim.exceptions.ApplicationException;

public class InvalidStatusDrinkCatalogException extends ApplicationException {

    private static final long serialVersionUID = 1L;
    private final String errorCode;

    public InvalidStatusDrinkCatalogException(Long statusId) {
        super(String.format("Invalid status (%s)", statusId));
        this.errorCode = "PIM-DRINK-001" ;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
