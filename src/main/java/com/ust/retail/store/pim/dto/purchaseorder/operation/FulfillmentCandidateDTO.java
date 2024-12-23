package com.ust.retail.store.pim.dto.purchaseorder.operation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FulfillmentCandidateDTO {
	private Long vendorMasterId;
	private Long storeNumId;
	private Long upcMasterId;
	private Double stockMin;
	private Long purchaseOrderId;
	private Long purchaseOrderDetailId;
	private Integer totalAmount;
	private Double qty;
	private Double qtyInUnits;

	public FulfillmentCandidateDTO(Long vendorMasterId,
								   Long storeNumId,
								   Long upcMasterId,
								   Double stockMin,
								   Long purchaseOrderId,
								   Long purchaseOrderDetailId,
								   Integer totalAmount,
								   Double qty) {
		this.vendorMasterId = vendorMasterId;
		this.storeNumId = storeNumId;
		this.upcMasterId = upcMasterId;
		this.stockMin = stockMin;
		this.purchaseOrderId = purchaseOrderId;
		this.purchaseOrderDetailId = purchaseOrderDetailId;
		this.totalAmount = totalAmount;
		this.qty = qty;
	}

	public void updateQtyInUnits(Double qtyInUnits) {
		this.qtyInUnits = qtyInUnits;
	}
}
