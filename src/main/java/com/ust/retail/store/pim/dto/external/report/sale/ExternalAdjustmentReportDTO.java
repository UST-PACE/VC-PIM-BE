package com.ust.retail.store.pim.dto.external.report.sale;

import com.ust.retail.store.pim.model.inventory.InventoryHistoryModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ExternalAdjustmentReportDTO {

	private String upc;
	private Double adjustmentQty;
	private Double onHand;
	private Double averageCost;

	public ExternalAdjustmentReportDTO parseToDTO(InventoryHistoryModel model) {
		this.upc = model.getInventory().getUpcMaster().getPrincipalUpc();
		this.adjustmentQty = model.getOperationQty();
		this.onHand = model.getFinalQty();
		return this;
	}

}
