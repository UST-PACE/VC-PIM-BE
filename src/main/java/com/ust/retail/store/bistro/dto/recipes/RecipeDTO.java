package com.ust.retail.store.bistro.dto.recipes;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ust.retail.store.bistro.commons.annotations.OnInstructions;
import com.ust.retail.store.bistro.commons.annotations.OnLabelTexts;
import com.ust.retail.store.bistro.commons.annotations.OnNutritionalValues;
import com.ust.retail.store.bistro.commons.catalogs.MealCategoryCatalog;
import com.ust.retail.store.bistro.commons.catalogs.MealTemperatureCatalog;
import com.ust.retail.store.bistro.commons.catalogs.RecipePreparationAreaCatalog;
import com.ust.retail.store.bistro.model.recipes.RecipeModel;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.dto.upcmaster.NutritionalInformationDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcMasterDTO;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;

import lombok.Getter;

@Getter
public class RecipeDTO {

	@NotNull(message = "recipeId is Mandatory.", groups = {OnUpdate.class, OnInstructions.class, OnNutritionalValues.class})
	private Long recipeId;

	private String recipeName;

	@NotNull(message = "prepInstructions is Mandatory.", groups = {OnInstructions.class})
	private String prepInstructions;

	private Double bufferPricePercentage;

	private Double indirectCost;

	@NotNull(message = "cookingTime is Mandatory.", groups = {OnUpdate.class, OnCreate.class})
	private Integer cookingTime;

	private boolean displayExternally;

	@NotNull(message = "mealTemperatureId is Mandatory.", groups = {OnUpdate.class, OnCreate.class})
	private Long mealTemperatureId;

	@NotNull(message = "preparationAreaId is Mandatory.", groups = {OnUpdate.class, OnCreate.class})
	private Long preparationAreaId;

	private Double contentPerUnit;
	private Long contentPerUnitUomId;
	private Long inventoryUnitId;

	private Long sellingUnitId;

	private String sellingUnitName;

	@NotNull(message = "foodClassificationId is Mandatory.", groups = {OnUpdate.class, OnCreate.class})
	private Long foodClassificationId;

	@Size(max = 150, message = "Text for Contains  must have at most 150 characters", groups = {OnLabelTexts.class})
	private String containsText;

	@Size(max = 700, message = "Text for Ingredients must have at most 700 characters", groups = {OnLabelTexts.class})
	private String ingredientsText;

	@Valid
	@NotNull(message = "nutritionalInfo is Mandatory.", groups = {OnNutritionalValues.class})
	private NutritionalInformationDTO nutritionalInfo;

	private UpcMasterDTO relatedUpcMaster;

	private Double taxPercentage;
	private Boolean upcTaxPercentageActive;


	/*DEFAULT CONSTRUCTOR WHEN UPC MASTER IS NEW TO RECIPE */
	public RecipeModel createModel(UpcMasterModel upcMaster) {
		return new RecipeModel(
				null,
				MealTemperatureCatalog.HOT,
				RecipePreparationAreaCatalog.KITCHEN,
				upcMaster.getProductTypeSellingUnit().getCatalogId(),
				"",
				0d,
				0d,
				0d,
				0,
				true,
				MealCategoryCatalog.DISH,
				upcMaster.getUserCreate().getUserId())
				.setRelatedUpcMaster(upcMaster);
	}

	public RecipeDTO parseToDTO(RecipeModel model) {
		this.recipeId = model.getRecipeId();
		this.recipeName = model.getRelatedUpcMaster().getProductName();
		this.mealTemperatureId = model.getMealTemperature().getCatalogId();
		this.preparationAreaId = model.getPreparationArea().getCatalogId();
		this.foodClassificationId = model.getFoodClassification().getCatalogId();
		this.sellingUnitId = model.getSellingUnit().getCatalogId();
		this.sellingUnitName = model.getSellingUnit().getCatalogOptions();
		this.cookingTime = model.getCookingTime();
		this.displayExternally = model.isDisplayExternally();
		this.prepInstructions = model.getPrepInstructions();

		this.indirectCost = model.getIndirectCost();
		this.bufferPricePercentage = model.getBufferPricePercentage();

		this.containsText = model.getContainsText();
		this.ingredientsText = model.getIngredientsText();

		this.relatedUpcMaster = new UpcMasterDTO().parseToDTO(model.getRelatedUpcMaster());

		this.nutritionalInfo = new NutritionalInformationDTO().parseToDTO(model.getRelatedUpcMaster().getUpcNutritionInformationModel());
		
		this.taxPercentage = model.getTaxPercentage();
		this.upcTaxPercentageActive = model.getUpcTaxPercentageActive();
		
		return this;
	}

	public RecipeDTO parseToSimpleDTO(RecipeModel model) {
		this.recipeId = model.getRecipeId();
		this.recipeName = model.getRelatedUpcMaster().getProductName();
		this.relatedUpcMaster = new UpcMasterDTO().parseToDTO(model.getRelatedUpcMaster());
		return this;
	}
}
