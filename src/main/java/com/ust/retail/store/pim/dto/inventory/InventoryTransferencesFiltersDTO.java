package com.ust.retail.store.pim.dto.inventory;

import java.util.Date;

import com.ust.retail.store.pim.common.bases.BaseFilterDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryTransferencesFiltersDTO extends BaseFilterDTO{

	private Long inventoryTransferId;
	private String principalUpc;
	private Double qty;
	private Long storeLocationToFromId;
	private Long storeLocationToId;
	private Date createdAt;

}
