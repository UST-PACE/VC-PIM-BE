package com.ust.retail.store.pim.service.purchaseorder;

public interface PurchaseOrderNumberGenerator {
	String generateRevision(String currentRevision);
	String generateNumber();
	boolean isFirstRevision(String initialPurchaseOrderNum);
}
