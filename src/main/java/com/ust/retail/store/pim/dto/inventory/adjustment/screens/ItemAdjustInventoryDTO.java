package com.ust.retail.store.pim.dto.inventory.adjustment.screens;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemAdjustInventoryDTO {
	
	private Long inventoryId;
	private Long upcMasterId;
	private Long storeLocationId;
	private String principalUPC;
	private String productName;

	private InventoryAdjustmentItemDetailDTO previousDetail;

}
