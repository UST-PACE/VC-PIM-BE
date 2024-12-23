package com.ust.retail.store.pim.engine.inventory;

import org.springframework.stereotype.Component;

import com.ust.retail.store.pim.dto.inventory.Item;
import com.ust.retail.store.pim.dto.inventory.QtyOperationResultDTO;
import com.ust.retail.store.pim.exceptions.InventoryOutOfStockException;
import com.ust.retail.store.pim.model.inventory.InventoryModel;

@Component
public class BistroWasteWholeDishInventory extends InventoryEngine{

	public final Long OPERATION_MODULE =10005L;
	
	@Override
	protected Long getOperationType(Item inventoryInfoDTO) {
		return OPERATION_TYPE_WASTE_WHOLE_DISH;
	}

	@Override
	protected QtyOperationResultDTO getItemFinalQty(Double currentQty, Double newQty, boolean isShrinkage) {
		
		Double finalQty = currentQty - newQty;
		
		if(finalQty<0) {
			throw new InventoryOutOfStockException(currentQty,newQty);
		}
		
		return new QtyOperationResultDTO(currentQty,finalQty);
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
