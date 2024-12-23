package com.ust.retail.store.pim.dto.inventory.adjustment.screens;

import java.util.ArrayList;
import java.util.List;

import com.ust.retail.store.pim.model.inventory.InventoryAdjustmentShrinkageModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InventoryAdjustmentItemDetailDTO {

	private Long inventoryAdjustmentDetailId;
	private Long upcMasterId;
	private String principalUPC;
	private String productName;
	private Double qty;

	private List<InventoryAdjustmentShrinkageDTO> shrinkages;

	public InventoryAdjustmentItemDetailDTO(Long inventoryAdjustmentDetailId,
											Long upcMasterId,
											String principalUPC,
											String productName,
											Double qty,
											List<InventoryAdjustmentShrinkageModel> shrinkages) {
		super();

		this.inventoryAdjustmentDetailId = inventoryAdjustmentDetailId;
		this.upcMasterId = upcMasterId;
		this.productName = productName;
		this.principalUPC = principalUPC;
		this.qty = qty;

		transformShrinkageToDTO(shrinkages);
	}

	private void transformShrinkageToDTO(List<InventoryAdjustmentShrinkageModel> shrinkages) {

		this.shrinkages = new ArrayList<>();

		for (InventoryAdjustmentShrinkageModel currentShrinkage : shrinkages) {

			if (currentShrinkage.getUpcMaster().getPrincipalUpc().equalsIgnoreCase(principalUPC)) {
				this.shrinkages.add(new InventoryAdjustmentShrinkageDTO(
						currentShrinkage.getInventoryAdjustmentShrinkageId(), 
						currentShrinkage.getUpcMaster().getUpcMasterId(),
						currentShrinkage.getUpcMaster().getPrincipalUpc(),
						currentShrinkage.getShrinkageReason().getCatalogId(),
						currentShrinkage.getShrinkageReason().getCatalogOptions(),
						currentShrinkage.getQty(),
						currentShrinkage.getEvidence()));
			}
		}
	}

}
