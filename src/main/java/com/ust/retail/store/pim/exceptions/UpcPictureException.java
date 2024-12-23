package com.ust.retail.store.pim.exceptions;

import lombok.Getter;

@Getter
public class UpcPictureException extends ApplicationException {

	private static final long serialVersionUID = 1L;
	
	private final Long upcMasterId;
	private final Exception exception;

	public UpcPictureException(Long upcMasterId, Exception e) {
		super(String.format("Could not update picture for UPC Master '%s': %s", upcMasterId, e.getMessage()));

		this.upcMasterId = upcMasterId;
		this.exception = e;
		this.errorCode = "PIM-UPC-PIC-001";
	}
}
