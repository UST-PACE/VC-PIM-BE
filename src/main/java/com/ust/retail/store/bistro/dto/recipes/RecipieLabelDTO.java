package com.ust.retail.store.bistro.dto.recipes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecipieLabelDTO {
	
	private RecipeDTO recipeDetails;
    private String storeName;
    private Double salePrice;
    private String storeAddress;
    
}
