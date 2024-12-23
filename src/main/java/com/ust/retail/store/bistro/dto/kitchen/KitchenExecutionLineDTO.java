package com.ust.retail.store.bistro.dto.kitchen;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KitchenExecutionLineDTO {
	private Long recipeId;
	private String recipeName;
	private boolean productionEnabled;
}
