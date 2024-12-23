package com.ust.retail.store.bistro.exception;

import com.ust.retail.store.pim.exceptions.ApplicationException;

public class UnitConvertException extends ApplicationException {

	private static final long serialVersionUID = 1L;
	private final String errorCode;

	public UnitConvertException(Long unitFromId, Long unitToId) {
        super(String.format("Given units are not compatible (from: %s; to: %s), weight/liquid must transform to a feasible unit", unitFromId, unitToId));
        this.errorCode = "PIM-CONVERT-001" ;
    }

	public String getErrorCode() {
		return errorCode;
	}

}
