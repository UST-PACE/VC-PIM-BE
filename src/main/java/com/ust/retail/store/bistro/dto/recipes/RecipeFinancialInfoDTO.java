package com.ust.retail.store.bistro.dto.recipes;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RecipeFinancialInfoDTO {
	private Long recipeId;
	private Double indirectCost;
	private Double bufferOnPrice;
	private List<RecipeStorePriceDTO> storePrices;
	
	private Double taxPercentage;
	private Boolean upcTaxPercentageActive;
}
