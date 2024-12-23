package com.ust.retail.store.pim.dto.external;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.model.catalog.FoodOptionModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class ExternalFoodOptionDTO {

	private Long foodOptionId;

	private String foodOptionCatalogueName;

	public ExternalFoodOptionDTO parseToDTO(FoodOptionModel model) {
		if (model == null) {
			return null;
		}
		this.foodOptionId = model.getFoodOptionId();
		this.foodOptionCatalogueName = model.getFoodOptionCatalogueName();
		return this;
	}
}
