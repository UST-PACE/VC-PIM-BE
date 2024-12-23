package com.ust.retail.store.pim.engine.inventory;

import com.ust.retail.store.pim.dto.inventory.Item;
import com.ust.retail.store.pim.dto.inventory.QtyOperationResultDTO;
import com.ust.retail.store.pim.exceptions.SalesInventoryException;
import com.ust.retail.store.pim.model.inventory.InventoryModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SaleItemInventory extends InventoryEngine {
	public final Long OPERATION_MODULE = 10003L;

	@Value("${pim.enable.infinite.inventory}")
	private Boolean ENABLE_INFINITE_INVENTORY;
	
	@Override
	protected Long getOperationType(Item inventoryInfoDTO) {
		return OPERATION_TYPE_SALE;
	}

	@Override
	protected QtyOperationResultDTO getItemFinalQty(InventoryModel inventoryModel, Double newQty) {
		UpcMasterModel upcMaster = inventoryModel.getUpcMaster();
		Long sellingUnit = upcMaster.getProductTypeSellingUnit().getCatalogId();
		Long inventoryUnit = upcMaster.getInventoryUnit().getCatalogId();

		double finalQty = 0 ;
		if(!ENABLE_INFINITE_INVENTORY) {
			finalQty = inventoryModel.getQty() - unitConverter.convert(sellingUnit, inventoryUnit, newQty);
		}else {
			finalQty = inventoryModel.getQty();
		}
		

		if (finalQty < 0) {
			throw new SalesInventoryException(upcMaster.getProductName(),
					upcMaster.getPrincipalUpc());
		}

		return new QtyOperationResultDTO(inventoryModel.getQty(), finalQty);
	}

	@Override
	protected QtyOperationResultDTO getItemFinalQty(Double currentQty, Double newQty, boolean isShrinkage) {
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
