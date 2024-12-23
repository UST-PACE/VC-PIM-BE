package com.ust.retail.store.bistro.exception;

import com.ust.retail.store.pim.exceptions.ApplicationException;

public class BarcodeGenerationException extends ApplicationException {

	private static final long serialVersionUID = 1L;

	public BarcodeGenerationException(Exception e) {
		super(String.format("An exception occurred during the barcode generation: %s", e.getMessage()));
		this.errorCode = "PIM-BARCODE-001";
	}
}
