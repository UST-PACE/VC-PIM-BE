package com.ust.retail.store.pim.dto.report;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RecipeAddOnCsvDumpDTO {
	private String recipeUpc;
	private String recipeName;
	private String addOnUpc;
	private String addOnName;
	private String addOnVendor;
	private Double qty;
	private String uom;
	private Double sellingPrice;
	private String brand;
	private String status;
	@JsonIgnore
	private Long addOnUpcMasterId;

	private Double cost;

	public RecipeAddOnCsvDumpDTO(String recipeUpc,
								 String recipeName,
								 String addOnUpc,
								 String addOnName,
								 String addOnVendor,
								 Double qty,
								 String uom,
								 Double sellingPrice,
								 String brand,
								 String status,
								 Long addOnUpcMasterId) {
		this.recipeUpc = recipeUpc;
		this.recipeName = recipeName;
		this.addOnUpc = addOnUpc;
		this.addOnName = addOnName;
		this.addOnVendor = addOnVendor;
		this.qty = qty;
		this.uom = uom;
		this.sellingPrice = sellingPrice;
		this.brand = brand;
		this.status = status;
		this.addOnUpcMasterId = addOnUpcMasterId;
	}

	public void setCost(Double ingredientCost) {
		this.cost = ingredientCost * qty;
		this.cost = Math.round(this.cost * 100) / 100d;
	}
}
