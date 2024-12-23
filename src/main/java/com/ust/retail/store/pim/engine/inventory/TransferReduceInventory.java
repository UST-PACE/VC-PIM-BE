package com.ust.retail.store.pim.engine.inventory;

import org.springframework.stereotype.Component;

import com.ust.retail.store.pim.dto.inventory.Item;
import com.ust.retail.store.pim.dto.inventory.QtyOperationResultDTO;
import com.ust.retail.store.pim.exceptions.InvalidInventoryTransferenceException;
import com.ust.retail.store.pim.model.inventory.InventoryModel;

@Component
public class TransferReduceInventory extends InventoryEngine {

	public final Long OPERATION_MODULE = 10004L;

	@Override
	protected Long getOperationType(Item inventoryInfoDTO) {
		return OPERATION_TYPE_REDUCE;
	}

	@Override
	protected QtyOperationResultDTO getItemFinalQty(Double currentQty, Double newQty, boolean isShrinkage) {

		Double finalQty = currentQty - newQty;

		if (finalQty < 0) {
			throw new InvalidInventoryTransferenceException(newQty, finalQty);
		}

		return new QtyOperationResultDTO(currentQty, finalQty, InventoryEngine.OPERATION_TYPE_REDUCE);
	}

	@Override
	protected QtyOperationResultDTO getItemFinalQty(InventoryModel inventoryModel, Double newQty) {
		return NOT_ACTIVE;
	}

	@Override
	protected Long getOperationModule() {
		return OPERATION_MODULE;
	}

	@Override
	protected boolean isAuthorizationRequired() {
		return false;
	}

	@Override
	protected Long getDefaultAuthorizationStatus() {
		return AUTHORIZATION_STATUS_NOT_REQUIRED;
	}


}
