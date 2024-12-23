package com.ust.retail.store.pim.dto.upcmaster;

import java.util.Optional;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.ust.retail.store.bistro.commons.annotations.OnNutritionalValues;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.model.upcmaster.UpcNutritionInformationModel;

import lombok.Getter;

@Getter
public class NutritionalInformationDTO {

	@Null(message = "Nutrition Detail Id should not be present when creating.", groups = { OnCreate.class })
	private Long nutritionDetailId;

	@NotNull(message = "calories is Mandatory.", groups = {OnCreate.class, OnUpdate.class, OnNutritionalValues.class})
	private Double calories;

	private Double fats;

	private Double carbohydrates;

	private Double protein;

	private Double sugar;

	private Double kiloCalories;

	private String flagToDisplay;

	private Double minRangeKCalories;

	private Double maxRangeKCalories;

	private boolean displayMenuBoardCalories;

	private boolean displayEcommerceCalories;

	private boolean displayKioskCalories;

	private boolean displayAppCalories;

	private boolean displayMenuBoardFats;

	private boolean displayEcommerceFats;

	private boolean displayKioskFats;

	private boolean displayAppFats;

	private boolean displayMenuBoardCarbohydrates;

	private boolean displayEcommerceCarbohydrates;

	private boolean displayKioskCarbohydrates;

	private boolean displayAppCarbohydrates;

	private boolean displayMenuBoardProtein;

	private boolean displayEcommerceProtein;

	private boolean displayKioskProtein;

	private boolean displayAppProtein;

	private boolean displayMenuBoardSugar;

	private boolean displayEcommerceSugar;

	private boolean displayKioskSugar;

	private boolean displayAppSugar;

	private boolean displayMenuBoardKcalories;

	private boolean displayEcommerceKcalories;

	private boolean displayKioskKcalories;

	private boolean displayAppKcalories;

	public NutritionalInformationDTO parseToDTO(UpcNutritionInformationModel model) {

		UpcNutritionInformationModel nullSafeModel = Optional.ofNullable(model).orElse(new UpcNutritionInformationModel());
		this.nutritionDetailId = nullSafeModel.getNutritionDetailId();
		this.calories = nullSafeModel.getCalories();
		this.fats = nullSafeModel.getFats();
		this.carbohydrates = nullSafeModel.getCarbohydrates();
		this.protein = nullSafeModel.getProtein();
		this.sugar = nullSafeModel.getSugar();
		this.kiloCalories = nullSafeModel.getKiloCalories();
		this.flagToDisplay = nullSafeModel.getFlagToDisplay();
		this.minRangeKCalories = nullSafeModel.getMinRangeKCalories();
		this.maxRangeKCalories =nullSafeModel.getMaxRangeKCalories();
		this.displayMenuBoardCalories = nullSafeModel.isDisplayMenuBoardCalories();
		this.displayEcommerceCalories = nullSafeModel.isDisplayEcommerceCalories();
		this.displayKioskCalories = nullSafeModel.isDisplayKioskCalories();
		this.displayAppCalories = nullSafeModel.isDisplayAppCalories();
		this.displayMenuBoardFats = nullSafeModel.isDisplayMenuBoardFats();
		this.displayEcommerceFats = nullSafeModel.isDisplayEcommerceFats();
		this.displayKioskFats = nullSafeModel.isDisplayKioskFats();
		this.displayAppFats = nullSafeModel.isDisplayAppFats();
		this.displayMenuBoardCarbohydrates = nullSafeModel.isDisplayMenuBoardCarbohydrates();
		this.displayEcommerceCarbohydrates = nullSafeModel.isDisplayEcommerceCarbohydrates();
		this.displayKioskCarbohydrates = nullSafeModel.isDisplayKioskCarbohydrates();
		this.displayAppCarbohydrates = nullSafeModel.isDisplayAppCarbohydrates();
		this.displayMenuBoardProtein = nullSafeModel.isDisplayMenuBoardProtein();
		this.displayEcommerceProtein = nullSafeModel.isDisplayEcommerceProtein();
		this.displayKioskProtein = nullSafeModel.isDisplayKioskProtein();
		this.displayAppProtein = nullSafeModel.isDisplayAppProtein();
		this.displayMenuBoardSugar = nullSafeModel.isDisplayMenuBoardSugar();
		this.displayEcommerceSugar = nullSafeModel.isDisplayEcommerceSugar();
		this.displayKioskSugar = nullSafeModel.isDisplayKioskSugar();
		this.displayAppSugar = nullSafeModel.isDisplayAppSugar();
		this.displayMenuBoardKcalories = nullSafeModel.isDisplayMenuBoardKcalories();
		this.displayEcommerceKcalories = nullSafeModel.isDisplayEcommerceKcalories();
		this.displayKioskKcalories = nullSafeModel.isDisplayKioskKcalories();
		this.displayAppKcalories = nullSafeModel.isDisplayAppKcalories();
		return this;
	}

	public UpcNutritionInformationModel parseToModel() {
		return new UpcNutritionInformationModel(
				this.nutritionDetailId,
				this.calories,
				this.fats,
				this.carbohydrates,
				this.protein,
				this.sugar,
				this.kiloCalories,
				this.flagToDisplay,
				this.minRangeKCalories,
				this.maxRangeKCalories,
				this.displayMenuBoardCalories,
				this.displayEcommerceCalories,
				this.displayKioskCalories,
				this.displayAppCalories,
				this.displayMenuBoardFats,
				this.displayEcommerceFats,
				this.displayKioskFats,
				this.displayAppFats,
				this.displayMenuBoardCarbohydrates,
				this.displayEcommerceCarbohydrates,
				this.displayKioskCarbohydrates,
				this.displayAppCarbohydrates,
				this.displayMenuBoardProtein,
				this.displayEcommerceProtein,
				this.displayKioskProtein,
				this.displayAppProtein,
				this.displayMenuBoardSugar,
				this.displayEcommerceSugar,
				this.displayKioskSugar,
				this.displayAppSugar,
				this.displayMenuBoardKcalories,
				this.displayEcommerceKcalories,
				this.displayKioskKcalories,
				this.displayAppKcalories
		);
	}
}
