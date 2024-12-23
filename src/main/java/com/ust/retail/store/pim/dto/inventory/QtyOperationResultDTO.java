package com.ust.retail.store.pim.dto.inventory;

import com.ust.retail.store.pim.engine.inventory.InventoryEngine;

import lombok.Getter;

@Getter
public class QtyOperationResultDTO {

	private Double previewsQty;
	private Double finalQty;
	private Long operationResultId;

	public QtyOperationResultDTO(Double previewsQty, Double finalQty) {
		super();
		this.previewsQty = previewsQty;
		this.finalQty = finalQty;

		if (previewsQty == finalQty) {
			this.operationResultId = InventoryEngine.OPERATION_TYPE_NO_CHANGE;
		} else if (finalQty > previewsQty) {
			this.operationResultId = InventoryEngine.OPERATION_TYPE_INCREASE;
		} else {
			this.operationResultId = InventoryEngine.OPERATION_TYPE_SHRINKAGE;
		}
	}

	public QtyOperationResultDTO(Double previewsQty, Double finalQty, Long operationResultId) {
		super();
		this.previewsQty = previewsQty;
		this.finalQty = finalQty;

		this.operationResultId = operationResultId;
	}

}
