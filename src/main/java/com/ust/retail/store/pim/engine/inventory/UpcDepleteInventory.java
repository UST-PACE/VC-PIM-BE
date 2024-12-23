package com.ust.retail.store.pim.engine.inventory;

import org.springframework.stereotype.Component;

import com.ust.retail.store.pim.dto.inventory.Item;
import com.ust.retail.store.pim.dto.inventory.QtyOperationResultDTO;
import com.ust.retail.store.pim.model.inventory.InventoryModel;

@Component
public class UpcDepleteInventory extends InventoryEngine {
	public static final Long OPERATION_MODULE = 10001L;


	@Override
	protected Long getOperationType(Item inventoryInfoDTO) {
		return OPERATION_TYPE_SET;
	}

	@Override
	protected Long getOperationModule() {
		return OPERATION_MODULE;
	}


	@Override
	protected QtyOperationResultDTO getItemFinalQty(Double currentQty, Double newQty, boolean isShrinkage) {
		Double finalQty = newQty;

		if (isShrinkage) {
			finalQty = currentQty - newQty;
		}

		return new QtyOperationResultDTO(currentQty, finalQty);
	}


	@Override
	protected QtyOperationResultDTO getItemFinalQty(InventoryModel inventoryModel, Double newQty) {
		return NOT_ACTIVE;
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
