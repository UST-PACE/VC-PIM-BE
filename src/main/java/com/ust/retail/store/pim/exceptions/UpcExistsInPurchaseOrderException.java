package com.ust.retail.store.pim.exceptions;

public class UpcExistsInPurchaseOrderException extends ApplicationException {
	private static final long serialVersionUID = 1L;

	private final String upc;
	private final String productName;
	private final Long purchaseOrderId;
	private final Long purchaseOrderDetailId;

	public UpcExistsInPurchaseOrderException(String upc, String productName, Long purchaseOrderId, Long purchaseOrderDetailId) {
		super(String.format("UPC: '%s' exists in Purchase Order : '%s' with Detail ID : '%s'", upc, purchaseOrderId, purchaseOrderDetailId));
		this.upc = upc;
		this.productName = productName;
		this.purchaseOrderId = purchaseOrderId;
		this.purchaseOrderDetailId = purchaseOrderDetailId;
		this.errorCode = "PIM-UEO-001";
	}

	public String getUpc() {
		return upc;
	}

	public String getProductName() {
		return productName;
	}

	public Long getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public Long getPurchaseOrderDetailId() {
		return purchaseOrderDetailId;
	}
}
