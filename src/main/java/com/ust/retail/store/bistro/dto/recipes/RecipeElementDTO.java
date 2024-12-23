package com.ust.retail.store.bistro.dto.recipes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecipeElementDTO {
	private Long upcMasterId;
	private String upc;
	private String name;
	private Long inventoryUnitId;
	private Double salePrice;
	private boolean topping;
}
