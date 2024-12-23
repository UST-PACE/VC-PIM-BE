package com.ust.retail.store.pim.dto.inventory.adjustment.screens;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.common.annotations.OnAdjustInventory;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.dto.inventory.Item;
import com.ust.retail.store.pim.model.inventory.InventoryAdjustmentModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InventoryAdjustmentDTO {

	@NotNull(message = "Product Inventory Adjustment id is mandatory", groups = { OnAdjustInventory.class, OnUpdate.class })
	private Long inventoryAdjustmentId;

	private Long inventoryAdjustmentDetailId;

	@NotNull(message = "Store Location Id is mandatory", groups = { OnCreate.class, OnAdjustInventory.class })
	private Long storeLocationId;

	@Valid
	private List<InventoryAdjustmentShrinkageDTO> shrinkages;

	@Valid
	@NotNull(message = "Item details are Mandatory.", groups = { OnAdjustInventory.class})
	private Item item;
	
	@Valid
	private List<Item> shrinkageItems;
	
	
	public InventoryAdjustmentModel updateModel(InventoryAdjustmentModel adjustment, Long userCreatedId) {

		for (InventoryAdjustmentShrinkageDTO currentShrinkage : shrinkages) {
			adjustment.addShrinkage(currentShrinkage.getUpcMasterId(), currentShrinkage.getShrinkageReasonId(),
					currentShrinkage.getQty(), currentShrinkage.getEvidence(),userCreatedId);

		}

		adjustment.addDetail(inventoryAdjustmentDetailId, item.getUpcMasterId(), item.getQty(), userCreatedId);
		
		return adjustment;
	}

	public InventoryAdjustmentDTO(Long inventoryAdjustmentId) {
		super();
		this.inventoryAdjustmentId = inventoryAdjustmentId;
	}

}
