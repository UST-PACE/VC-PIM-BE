package com.ust.retail.store.pim.dto.inventory;

import com.ust.retail.store.pim.model.inventory.InventoryModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class InventoryProductDTO {

	private InventoryModel inventoryModel;
	private UpcMasterModel upcMasterModel;
	
}
