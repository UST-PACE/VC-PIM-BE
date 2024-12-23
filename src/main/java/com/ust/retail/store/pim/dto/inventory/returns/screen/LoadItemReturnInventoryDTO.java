package com.ust.retail.store.pim.dto.inventory.returns.screen;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoadItemReturnInventoryDTO {
	
	private Long inventoryId;
	private String principalUPC;
	private Long storeLocationId;
	private String productName;
	private Long upcMasterId;
	private InventoryReturnItemDetailDTO previousDetail;

	public LoadItemReturnInventoryDTO(Long inventoryId,
									  String principalUPC,
									  Long storeLocationId,
									  String productName,
									  Long upcMasterId,
									  InventoryReturnItemDetailDTO previousDetail) {
		super();
		this.inventoryId = inventoryId;
		this.principalUPC = principalUPC;
		this.storeLocationId = storeLocationId;
		this.productName = productName;
		this.upcMasterId = upcMasterId;
		this.previousDetail = previousDetail;
	}
	
	

}
