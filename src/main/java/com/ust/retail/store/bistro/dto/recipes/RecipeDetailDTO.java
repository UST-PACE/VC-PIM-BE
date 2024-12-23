package com.ust.retail.store.bistro.dto.recipes;

import com.ust.retail.store.bistro.model.recipes.RecipeDetailModel;
import com.ust.retail.store.bistro.model.recipes.RecipeModel;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnRemove;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Getter
public class RecipeDetailDTO {

	@NotNull(message = "recipeDetailId is Mandatory.", groups = {OnUpdate.class, OnRemove.class})
	private Long recipeDetailId;

	@NotNull(message = "recipeId is Mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long recipeId;

	private String name;

	@NotNull(message = "qty is Mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Double qty;

	@NotNull(message = "unitId is Mandatory", groups = {OnCreate.class, OnUpdate.class})
	private Long unitId;

	@NotNull(message = "ingredientTypeId is Mandatory", groups = {OnCreate.class, OnUpdate.class})
	private Long ingredientTypeId;

	private String unitDesc;

	private String ingredientTypeDesc;

	private Double cost;

	private boolean topping;

	private boolean withSubstitutions;

	@Valid
	@NotNull(message = "upcMasterId is Mandatory.", groups = {OnCreate.class})
	private Long upcMasterId;
	private String upc;

	private boolean toExclude;
	private String excludeName;

	public RecipeDetailModel merge(RecipeDetailModel model) {

		model.updateQty(qty);
		model.updateUnit(unitId);
		model.updateIngredientType(ingredientTypeId);
		model.setExclusionData(toExclude, excludeName);

		return model;
	}

	public RecipeDetailDTO parseToDTO(RecipeDetailModel detail) {

		UpcMasterModel detailProduct = detail.getUpcMaster();
		this.recipeDetailId = detail.getRecipeDetailId();
		this.recipeId = detail.getRecipe().getRecipeId();
		this.name = detailProduct.getProductName();
		this.unitId = detail.getUnit().getCatalogId();
		this.unitDesc = detail.getUnit().getCatalogOptions();
		this.ingredientTypeId = detail.getIngredientType().getCatalogId();
		this.ingredientTypeDesc = detail.getIngredientType().getCatalogOptions();
		this.topping = detail.isTopping();
		this.upcMasterId = detailProduct.getUpcMasterId();
		this.upc = detailProduct.getPrincipalUpc();
		this.qty = detail.getQty();
		this.toExclude = detail.isToExclude();
		this.excludeName = detail.getExcludeName();
		this.withSubstitutions = !Optional.ofNullable(detail.getSubstitutions()).orElse(List.of()).isEmpty();

		return this;
	}

	public void updateCost(Double qty, Double ingredientCost) {
		this.cost = ingredientCost * qty;
		this.cost = Math.round(this.cost * 100) / 100d;
	}

	public RecipeDetailModel createModel(RecipeModel recipe, Long userId) {
		return new RecipeDetailModel(recipe,
				this.recipeDetailId,
				this.qty,
				this.unitId,
				this.ingredientTypeId,
				this.topping,
				this.toExclude,
				this.excludeName,
				this.upcMasterId,
				userId);
	}
}
