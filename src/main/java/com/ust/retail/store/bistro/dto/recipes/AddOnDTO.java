package com.ust.retail.store.bistro.dto.recipes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.bistro.model.recipes.RecipeAddonModel;
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
public class AddOnDTO {

	@Null(message = "recipeAddOnId is not valid for creation.", groups = { OnCreate.class })
	@NotNull(message = "recipeAddOnId is Mandatory.", groups = { OnUpdate.class })
	private Long recipeAddOnId;

	@NotNull(message = "recipeId is Mandatory.", groups = { OnCreate.class })
	private Long recipeId;

	@NotNull(message = "upcMasterId is Mandatory.", groups = { OnCreate.class })
	private Long upcMasterId;

	@NotNull(message = "qty is Mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Double qty;

	@NotNull(message = "sellingPrice is Mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Double sellingPrice;

	@NotNull(message = "maxRequests is Mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Integer maxRequests;

	private Double costPerUnit;

	/* NOT FOR FORM INPUT */
	private String productName;
	private String sellingUnitDesc;
	
	public RecipeAddonModel merge(RecipeAddonModel addon) {
		addon.setQty(this.qty);
		addon.setSellingPrice(this.sellingPrice);
		addon.setMaxRequests(this.maxRequests);
		return addon;
	}

	public AddOnDTO parseToDTO(RecipeAddonModel addon, Double costPerUnit) {
		this.recipeAddOnId = addon.getRecipeAddonId();
		this.recipeId = addon.getRecipe().getRecipeId();
		this.upcMasterId = addon.getUpcMaster().getUpcMasterId();
		this.qty = addon.getQty();
		this.sellingPrice = addon.getSellingPrice();
		this.maxRequests = addon.getMaxRequests();
		
		this.costPerUnit = costPerUnit;
		this.sellingUnitDesc = addon.getUpcMaster().getProductTypeSellingUnit().getCatalogOptions();
		this.productName = addon.getUpcMaster().getProductName();

		this.recipeAddOnId = addon.getRecipeAddonId();
		return this;
	}
}
