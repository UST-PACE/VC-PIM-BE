package com.ust.retail.store.bistro.dto.kitchen;

import com.ust.retail.store.bistro.model.wastage.WastageDetailModel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
class KitchenExecutionWastageDetailDTO {
	private Long ingredientUpcMasterId;
	private String ingredientName;
	private Double wasteAmount;
	private Long wasteUnitId;
	private String wasteUnitName;

	public KitchenExecutionWastageDetailDTO parseToDTO(WastageDetailModel model) {
		this.ingredientUpcMasterId = model.getIngredient().getUpcMasterId();
		this.ingredientName = model.getIngredient().getProductName();
		this.wasteAmount = model.getWastedAmount();
		this.wasteUnitId = model.getWastedUnit().getCatalogId();
		this.wasteUnitName = model.getWastedUnit().getCatalogOptions();
		return this;
	}
}
