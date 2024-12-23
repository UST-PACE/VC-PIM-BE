package com.ust.retail.store.pim.dto.inventory.returns.screen;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class InventoryReturnItemDetailDTO {

	private Long inventoryProductReturnDetailId;
	private Long upcMasterId;
	private String productName;
	private String principalUPC;
	private String batchNumber;
	private Double qty;

	private Long returnReasonId;
	private String returnReasonDesc;

	public InventoryReturnItemDetailDTO(Long inventoryProductReturnDetailId,
										Long upcMasterId,
										String productName,
										String principalUPC,
										String batchNumber,
										Double qty,
										Long returnReasonId,
										String returnReasonDesc) {
		super();

		this.inventoryProductReturnDetailId = inventoryProductReturnDetailId;
		this.productName = productName;
		this.principalUPC = principalUPC;
		this.batchNumber = batchNumber;
		this.qty = qty;
		this.returnReasonId = returnReasonId;
		this.returnReasonDesc = returnReasonDesc;
		this.upcMasterId = upcMasterId;
	}

}
