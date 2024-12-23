package com.ust.retail.store.pim.dto.inventory.adjustment.authorization.operation;

import javax.validation.constraints.NotNull;

import com.ust.retail.store.pim.common.annotations.OnAuthorize;
import com.ust.retail.store.pim.common.annotations.OnReject;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class InventoryAdjustmentAuthorizationRequestLineDTO {
	
	@NotNull(message = "TXN Num is mandatory.", groups = {OnAuthorize.class, OnReject.class})
	private Long txnNum;
	
	@NotNull(message = "Adjustment Detail ID is mandatory.", groups = {OnAuthorize.class, OnReject.class})
	private Long adjustmentDetailId;
}
