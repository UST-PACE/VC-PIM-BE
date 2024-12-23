package com.ust.retail.store.bistro.dto.recipes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.bistro.model.recipes.RecipeDetailModel;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OptionalIngredientDTO {

	@NotNull(message = "recipeDetailId is Mandatory.", groups = {OnCreate.class, OnUpdate.class })
	private Long recipeDetailId;

	private String upc;

	private String name;

	@NotNull(message = "displayName is Mandatory.", groups = {OnCreate.class, OnUpdate.class })
	private String displayName;

	public OptionalIngredientDTO parseToDTO(RecipeDetailModel model) {
		this.recipeDetailId = model.getRecipeDetailId();
		this.upc = model.getUpcMaster().getPrincipalUpc();
		this.name = model.getUpcMaster().getProductName();
		this.displayName = model.getExcludeName();
		return this;
	}
}
