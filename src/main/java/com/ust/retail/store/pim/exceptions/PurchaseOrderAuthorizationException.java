package com.ust.retail.store.pim.exceptions;

public class PurchaseOrderAuthorizationException extends ApplicationException {

	private static final long serialVersionUID = 1L;

	public PurchaseOrderAuthorizationException() {
		super("User can not approve");
		this.errorCode = "PIM-PO-AUTH-001";
	}
}
