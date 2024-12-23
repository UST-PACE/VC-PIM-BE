package com.ust.retail.store.pim.dto.external.product;

import com.ust.retail.store.common.util.UnitConverter;
import com.ust.retail.store.pim.model.inventory.InventoryModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ExternalStoreLocationInventoryDTO {
	private Long storeLocationId;
	private Double qty;

	public ExternalStoreLocationInventoryDTO parseToDTO(InventoryModel m) {
		this.storeLocationId = m.getStoreLocation().getStoreLocationId();
		this.qty = m.getQty();

		return this;
	}

	public ExternalStoreLocationInventoryDTO parseToDTO(InventoryModel m, UnitConverter unitConverter) {
		this.storeLocationId = m.getStoreLocation().getStoreLocationId();
		this.qty = unitConverter.convert(
				m.getUpcMaster().getInventoryUnit().getCatalogId(),
				m.getUpcMaster().getProductTypeSellingUnit().getCatalogId(),
				m.getQty());
		return this;
	}
}
