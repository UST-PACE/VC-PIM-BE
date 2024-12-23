package com.ust.retail.store.bistro.dto.external.dish;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.model.upcmaster.UpcNutritionInformationModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class ExternalNutritionalInformationDTO {

	private Double calories;

	private Double fats;

	private Double carbohydrates;

	private Double protein;

	private Double sugar;

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

	private Double kiloCalories;

	private Double minRangeKCalories;

	private Double maxRangeKCalories;

	private boolean displayMenuBoardKcalories;

	private boolean displayEcommerceKcalories;

	private boolean displayKioskKcalories;

	private boolean displayAppKcalories;


	public ExternalNutritionalInformationDTO parseToDTO(UpcNutritionInformationModel model) {
		UpcNutritionInformationModel nullSafeModel = Optional.ofNullable(model).orElse(new UpcNutritionInformationModel());
		this.calories = nullSafeModel.getCalories();
		this.fats = nullSafeModel.getFats();
		this.carbohydrates = nullSafeModel.getCarbohydrates();
		this.protein = nullSafeModel.getProtein();
		this.sugar = nullSafeModel.getSugar();
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
		this.kiloCalories = nullSafeModel.getKiloCalories();
		this.minRangeKCalories =nullSafeModel.getMinRangeKCalories();
		this.maxRangeKCalories = nullSafeModel.getMaxRangeKCalories();
		this.displayMenuBoardKcalories = nullSafeModel.isDisplayMenuBoardKcalories();
		this.displayEcommerceKcalories =nullSafeModel.isDisplayEcommerceKcalories();
		this.displayKioskKcalories = nullSafeModel.isDisplayKioskKcalories();
		this.displayAppKcalories =nullSafeModel.isDisplayAppKcalories();
		return this;
	}
}
