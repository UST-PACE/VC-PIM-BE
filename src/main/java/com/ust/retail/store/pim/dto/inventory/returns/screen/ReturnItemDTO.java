package com.ust.retail.store.pim.dto.inventory.returns.screen;

import javax.validation.constraints.NotNull;

import com.ust.retail.store.pim.common.annotations.OnReturn;
import com.ust.retail.store.pim.dto.inventory.Item;

import lombok.Getter;

@Getter
public class ReturnItemDTO {
	
	@NotNull(message = "Inventory Product Id is Mandatory.", groups = { OnReturn.class})
	private Long inventoryProductReturnId;

	private Long inventoryProductReturnDetailId;

	@NotNull(message = "Return Reason Id is Mandatory.", groups = { OnReturn.class})
	private Long returnReasonId;
	
	@NotNull(message = "Store Location Id is Mandatory.", groups = { OnReturn.class})
	private Long storeLocationId;
	
	@NotNull(message = "Qty is Mandatory.", groups = { OnReturn.class})
	private Double qty;
	
	@NotNull(message = "code UPC is Mandatory.", groups = { OnReturn.class})
	private String code;
	
	private String batchNum;

	private Item item;
	
}
