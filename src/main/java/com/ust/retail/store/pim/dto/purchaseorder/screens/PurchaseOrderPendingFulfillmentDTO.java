package com.ust.retail.store.pim.dto.purchaseorder.screens;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PurchaseOrderPendingFulfillmentDTO {
	private Long purchaseOrderId;
	private String purchaseOrderNumber;
	private Date requestDate;
	private Date eta;
	private Long storeNumId;
	private String storeName;
}
