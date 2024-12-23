package com.ust.retail.store.bistro.dto.external.dish;

import com.ust.retail.store.bistro.model.recipes.RecipeSubstitutionModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ExternalDishSubstitutionDTO {
	private Long substitutionId;
	private String baseIngredientSku;
	private String baseIngredientName;
	private String baseIngredientDisplayName;
	private String substituteIngredientSku;
	private String substituteIngredientName;
	private String substituteIngredientDisplayName;
	private Double price;

	public ExternalDishSubstitutionDTO parseToDTO(RecipeSubstitutionModel substitution) {
		this.substitutionId = substitution.getRecipeSubstitutionId();
		this.baseIngredientName = substitution.getIngredient().getUpcMaster().getProductName();
		this.baseIngredientSku = substitution.getIngredient().getUpcMaster().getPrincipalUpc();
		this.baseIngredientDisplayName = substitution.getIngredient().getExcludeName();

		this.substituteIngredientName = substitution.getSubstituteUpcMaster().getProductName();
		this.substituteIngredientSku = substitution.getSubstituteUpcMaster().getPrincipalUpc();
		this.substituteIngredientDisplayName = substitution.getDisplayName();

		this.price = substitution.getSellingPrice();

		return this;
	}
}
