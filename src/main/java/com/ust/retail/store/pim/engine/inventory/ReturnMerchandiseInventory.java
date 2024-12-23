package com.ust.retail.store.pim.engine.inventory;

import com.ust.retail.store.pim.dto.inventory.Item;
import com.ust.retail.store.pim.dto.inventory.QtyOperationResultDTO;
import com.ust.retail.store.pim.model.inventory.InventoryModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import org.springframework.stereotype.Component;

@Component
public class ReturnMerchandiseInventory extends InventoryEngine {

	public static final Long OPERATION_MODULE = 10006L;

	@Override
	protected Long getOperationType(Item inventoryInfoDTO) {
		return OPERATION_TYPE_INCREASE;
	}

	@Override
	protected QtyOperationResultDTO getItemFinalQty(Double currentQty, Double newQty, boolean isShrinkage) {
		return NOT_ACTIVE;
	}

	@Override
	protected QtyOperationResultDTO getItemFinalQty(InventoryModel inventoryModel, Double newQty) {
		UpcMasterModel upcMaster = inventoryModel.getUpcMaster();
		Long sellingUnit = upcMaster.getProductTypeSellingUnit().getCatalogId();
		Long inventoryUnit = upcMaster.getInventoryUnit().getCatalogId();

		double finalQty = inventoryModel.getQty() + unitConverter.convert(sellingUnit, inventoryUnit, newQty);

		return new QtyOperationResultDTO(inventoryModel.getQty(), finalQty);
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
