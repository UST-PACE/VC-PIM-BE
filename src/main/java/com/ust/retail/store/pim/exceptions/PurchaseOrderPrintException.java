package com.ust.retail.store.pim.exceptions;

public class PurchaseOrderPrintException extends ApplicationException {

	private static final long serialVersionUID = 1L;

	public PurchaseOrderPrintException(String message) {
		super(message);
		this.errorCode = "PIM-PO-PRINT-001";
	}
}
