package com.ust.retail.store.pim.dto.inventory.adjustment.screens;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryAdjustmentShrinkageDTO {

	private Long inventoryAdjustmentShrinkageId;

	private Long upcMasterId;

	private String principalUPC;
	
	private Long shrinkageReasonId;
	
	private String shrinkageDesc;
	
	private Double qty;
	
	private String evidence;
	
}
