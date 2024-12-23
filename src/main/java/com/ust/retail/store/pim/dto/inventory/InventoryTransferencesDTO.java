package com.ust.retail.store.pim.dto.inventory;

import com.ust.retail.store.pim.model.inventory.InventoryTransferModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryTransferencesDTO {
//TODO PONER VALIDACIONES
	private Long inventoryTransferId;
	private String principalUpc;
	private Double qty;
	private Long storeLocationFromId;
	private Long storeLocationToId;
	private Long upcMasterId;

	public InventoryTransferModel createModel(Long upcMasterId, Long userId) {
		return new InventoryTransferModel(this.inventoryTransferId, this.qty, upcMasterId, this.storeLocationFromId,
				this.storeLocationToId, userId);
	}

	public InventoryTransferencesDTO parseToDTO(InventoryTransferModel transferenceModel,String principalUpc,Long upcMasterId) {
		this.inventoryTransferId = transferenceModel.getInventoryTransferId();
		this.principalUpc = principalUpc;
		this.qty = transferenceModel.getQty();
		this.storeLocationFromId = transferenceModel.getStoreLocationFrom().getStoreLocationId();
		this.storeLocationToId = transferenceModel.getStoreLocationTo().getStoreLocationId();
		this.upcMasterId = upcMasterId;

		return this;
	}
	
	public InventoryTransferencesDTO parseToDTO(InventoryTransferModel transferenceModel) {
		this.inventoryTransferId = transferenceModel.getInventoryTransferId();
		this.principalUpc = transferenceModel.getUpcMaster().getPrincipalUpc();
		this.qty = transferenceModel.getQty();
		this.storeLocationFromId = transferenceModel.getStoreLocationFrom().getStoreLocationId();
		this.storeLocationToId = transferenceModel.getStoreLocationTo().getStoreLocationId();
		this.upcMasterId = transferenceModel.getUpcMaster().getUpcMasterId();

		return this;
	}
}
