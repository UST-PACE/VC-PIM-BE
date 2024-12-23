package com.ust.retail.store.pim.dto.inventory.adjustment.authorization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class InventoryAdjustmentAuthorizationDetailDTO {
	private Long adjustmentDetailId;
	private String categoryName;
	private String productName;
	private String principalUpc;
	private String productType;
	private Double qty;
	private String inventoryUnit;
	private Double shrinkageQty;
	private String shrinkageReason;
	private String evidence;
	private Long statusId;
	private String status;
	private Long txnNum;
	private Long inventoryHistoryId;
}
