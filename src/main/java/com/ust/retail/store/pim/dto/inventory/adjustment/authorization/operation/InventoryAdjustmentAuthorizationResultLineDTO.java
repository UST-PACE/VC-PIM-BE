package com.ust.retail.store.pim.dto.inventory.adjustment.authorization.operation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class InventoryAdjustmentAuthorizationResultLineDTO {
	private Long txnNum;
	private Long returnDetailId;
	private boolean success;
	private String message;
}
