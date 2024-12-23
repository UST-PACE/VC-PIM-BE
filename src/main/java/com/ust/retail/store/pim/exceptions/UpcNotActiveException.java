package com.ust.retail.store.pim.exceptions;

import lombok.Getter;

@Getter
public class UpcNotActiveException extends ApplicationException {
	private static final long serialVersionUID = 1L;

	private final String upc;

	public UpcNotActiveException(String upc) {
		super(String.format("UPC: '%s' not not active", upc));
		this.upc = upc;
		this.errorCode = "PIM-UNA-001";
	}
}
