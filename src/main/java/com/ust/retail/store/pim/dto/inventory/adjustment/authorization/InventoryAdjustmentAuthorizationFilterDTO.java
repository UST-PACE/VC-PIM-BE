package com.ust.retail.store.pim.dto.inventory.adjustment.authorization;

import com.ust.retail.store.pim.common.bases.BaseFilterDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class InventoryAdjustmentAuthorizationFilterDTO extends BaseFilterDTO {
	private Long brandOwnerId;
	private Long productTypeId;
	private Long productGroupId;
	private Long productCategoryId;
	private String productName;
	private Long statusId;
}
