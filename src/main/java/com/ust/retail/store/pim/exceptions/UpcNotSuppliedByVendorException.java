package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UpcNotSuppliedByVendorException extends ApplicationException {
	private static final long serialVersionUID = 1L;

	private final Long upcMasterId;
	private final String upc;
	private final Long vendorMasterId;

	public UpcNotSuppliedByVendorException(String upc, Long vendorMasterId) {
		super(String.format("UPC: '%s' not supplied by Vendor : '%s'", upc, vendorMasterId));
		this.upcMasterId = null;
		this.upc = upc;
		this.vendorMasterId = vendorMasterId;
		this.errorCode = "PIM-SNS-001";
	}

	public UpcNotSuppliedByVendorException(Long upcMasterId, Long vendorMasterId) {
		super(String.format("UPC Master Id : '%s' not supplied by Vendor : '%s'", upcMasterId, vendorMasterId));
		this.upc = null;
		this.upcMasterId = upcMasterId;
		this.vendorMasterId = vendorMasterId;
		this.errorCode = "PIM-SNS-001";
	}

	public Long getUpcMasterId() {
		return upcMasterId;
	}

	public String getUpc() {
		return upc;
	}

	public Long getVendorMasterId() {
		return vendorMasterId;
	}
}
