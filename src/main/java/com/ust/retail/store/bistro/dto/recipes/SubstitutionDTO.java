package com.ust.retail.store.bistro.dto.recipes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.bistro.model.recipes.RecipeDetailModel;
import com.ust.retail.store.bistro.model.recipes.RecipeSubstitutionModel;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubstitutionDTO {

	@Null(message = "recipeSubstitutionId is not valid for creation.", groups = {OnCreate.class})
	@NotNull(message = "recipeSubstitutionId is Mandatory.", groups = {OnUpdate.class})
	private Long recipeSubstitutionId;

	@NotNull(message = "recipeDetailId is Mandatory.", groups = {OnCreate.class})
	private Long recipeDetailId;
	private String recipeDetailName;

	@NotNull(message = "substitutionUpcMasterId is Mandatory.", groups = {OnCreate.class})
	private Long substitutionUpcMasterId;
	private String substitutionIngredientName;
	private String substitutionDisplayName;

	@NotNull(message = "qty is Mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Double qty;

	@NotNull(message = "substitutionUnitId is Mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long substitutionUnitId;
	private String substitutionUnitDesc;

	@NotNull(message = "sellingPrice is Mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Double sellingPrice;

	private Double costPerUnit;


	public SubstitutionDTO parseToDTO(RecipeSubstitutionModel substitution, Double costPerUnit) {

		this.recipeSubstitutionId = substitution.getRecipeSubstitutionId();
		this.recipeDetailId = substitution.getIngredient().getRecipeDetailId();
		this.recipeDetailName = substitution.getIngredient().getUpcMaster().getProductName();

		this.substitutionUpcMasterId = substitution.getSubstituteUpcMaster().getUpcMasterId();
		this.substitutionIngredientName = substitution.getSubstituteUpcMaster().getProductName();
		this.substitutionDisplayName = substitution.getDisplayName();

		this.qty = substitution.getQty();

		this.substitutionUnitId = substitution.getUnit().getCatalogId();
		this.substitutionUnitDesc = substitution.getUnit().getCatalogOptions();

		this.sellingPrice = substitution.getSellingPrice();
		this.costPerUnit = costPerUnit;

		return this;
	}

	public RecipeSubstitutionModel createModel(RecipeDetailModel ingredient, Long userCreatedId) {
		return new RecipeSubstitutionModel(
				ingredient,
				recipeSubstitutionId,
				substitutionUpcMasterId,
				substitutionDisplayName,
				qty,
				substitutionUnitId,
				sellingPrice,
				userCreatedId);
	}
}
