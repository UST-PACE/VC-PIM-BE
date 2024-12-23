package com.ust.retail.store.bistro.dto.recipes;

import com.ust.retail.store.bistro.model.recipes.RecipeModel;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RecipeFilterDTO extends BaseFilterDTO {

	private Long recipeId;
	private String recipeName;
	private Long foodClassificationId;
	private String foodClassificationDesc; //NO USE IN SCREEN FILTERS
	private Long mealTemperatureId;
	private String mealTemperatureDesc; //NO USE IN SCREEN FILTERS
	private Long preparationAreaId;
	private String preparationAreaDesc; //NO USE IN SCREEN FILTERS
	private boolean displayExternally;

	public RecipeFilterDTO parseToDTO(RecipeModel m) {
		this.recipeId = m.getRecipeId();
		this.recipeName = m.getRelatedUpcMaster().getProductName();
		this.foodClassificationId = m.getFoodClassification().getCatalogId();
		this.foodClassificationDesc = m.getFoodClassification().getCatalogOptions();
		this.mealTemperatureId = m.getMealTemperature().getCatalogId();
		this.mealTemperatureDesc = m.getMealTemperature().getCatalogOptions();
		this.preparationAreaId = m.getPreparationArea().getCatalogId();
		this.preparationAreaDesc = m.getPreparationArea().getCatalogOptions();
		this.displayExternally = m.isDisplayExternally();
		return this;
	}
}
