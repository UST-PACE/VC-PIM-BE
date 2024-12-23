package com.ust.retail.store.pim.dto.inventory;

import com.ust.retail.store.pim.common.bases.BaseFilterDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InventoryFiltersDTO extends BaseFilterDTO {

	private Long storeNumberId;
	private Long storeLocationId;
	
	private String storeNumberName;
	private String storeLocationName;
	private String productName;
	private String principalUpc;
	private Double qty;
	private String units;
	
	
	public InventoryFiltersDTO(
			Long storeNumberId,
			String storeNumberName,
			Long storeLocationId,
			String storeLocationName,
			String productName,
			String principalUpc,
			Double qty,
			String units) {
		this.storeNumberId = storeNumberId;
		this.storeNumberName = storeNumberName;
		this.storeLocationId = storeLocationId;
		this.storeLocationName = storeLocationName;
		this.productName = productName;
		this.principalUpc = principalUpc;
		this.qty = qty;
		this.units = units;
	}


	public InventoryFiltersDTO(String storeNumberName,
							   String productName,
							   String principalUpc,
							   Double qty,
							   String units) {
		this.storeNumberName = storeNumberName;
		this.storeLocationName = "all";
		this.productName = productName;
		this.principalUpc = principalUpc;
		this.qty = qty;
		this.units = units;
	}
	
	
}
