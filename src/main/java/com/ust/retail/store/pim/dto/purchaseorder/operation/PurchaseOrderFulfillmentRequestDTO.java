package com.ust.retail.store.pim.dto.purchaseorder.operation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PurchaseOrderFulfillmentRequestDTO {
	private Long vendorMasterId;
	private Long storeNumId;
	private Long upcMasterId;
	private Long purchaseOrderId;
	private Long purchaseOrderDetailId;
	private boolean newOrder;
	private boolean detailToUpdate;
	private boolean toFulfill;
	private boolean detailToRemove;
	private int amountToRequest;
}
