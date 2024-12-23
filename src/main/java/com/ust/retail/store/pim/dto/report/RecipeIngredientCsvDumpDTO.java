package com.ust.retail.store.pim.dto.report;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RecipeIngredientCsvDumpDTO {
	private String recipeUpc;
	private String recipeName;
	private String ingredientUpc;
	private String ingredientName;
	private String ingredientVendor;
	private Double qty;
	private String uom;
	private String brand;

	@JsonIgnore
	private Long ingredientUpcMasterId;
	@JsonIgnore
	private Double ingredientProductContentPerUnit;
	@JsonIgnore
	private Long ingredientProductContentPerUnitUomId;
	@JsonIgnore
	private Long ingredientUnitId;
	@JsonIgnore
	private boolean isTopping;

	private Double cost;

	public RecipeIngredientCsvDumpDTO(String recipeUpc,
									  String recipeName,
									  String ingredientUpc,
									  String ingredientName,
									  String ingredientVendor,
									  Double qty,
									  String uom,
									  String brand,
									  Long ingredientUpcMasterId,
									  Double ingredientProductContentPerUnit,
									  Long ingredientProductContentPerUnitUomId,
									  Long ingredientUnitId,
									  boolean isTopping) {
		this.recipeUpc = recipeUpc;
		this.recipeName = recipeName;
		this.ingredientUpc = ingredientUpc;
		this.ingredientName = ingredientName;
		this.ingredientVendor = ingredientVendor;
		this.qty = qty;
		this.uom = uom;
		this.brand = brand;
		this.ingredientUpcMasterId = ingredientUpcMasterId;
		this.ingredientProductContentPerUnit = ingredientProductContentPerUnit;
		this.ingredientProductContentPerUnitUomId = ingredientProductContentPerUnitUomId;
		this.ingredientUnitId = ingredientUnitId;
		this.isTopping = isTopping;
	}

	public void setCost(Double ingredientCost) {
		this.cost = ingredientCost * qty;
		this.cost = Math.round(this.cost * 100) / 100d;
	}
}
