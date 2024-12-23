package com.ust.retail.store.pim.model.upcmaster;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.ust.retail.store.pim.dto.upcmaster.NutritionalInformationDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "upc_nutrition_details")
@Getter
@NoArgsConstructor
public class UpcNutritionInformationModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nutrition_detail_id")
	private Long nutritionDetailId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upc_master_id", referencedColumnName = "upc_master_id", unique = true)
	private UpcMasterModel upcMaster;

	@Column(name = "calories", columnDefinition = "double default 0")
	private Double calories;

	@Column(name = "fats")
	private Double fats;

	@Column(name = "carbohydrates")
	private Double carbohydrates;

	@Column(name = "protein")
	private Double protein;

	@Column(name = "sugar")
	private Double sugar;

	@Column(name = "flag_to_display")
	private String flagToDisplay;

	@Column(name = "min_range_kcalories")
	private Double minRangeKCalories;

	@Column(name = "max_range_kcalories")
	private Double maxRangeKCalories;

	@Column(name = "kilo_calories")
	private Double kiloCalories;


	@Column(name = "display_menu_board_calories", nullable = false)
	private boolean displayMenuBoardCalories;

	@Column(name = "display_ecommerce_calories", nullable = false)
	private boolean displayEcommerceCalories;

	@Column(name = "display_kiosk_calories", nullable = false)
	private boolean displayKioskCalories;

	@Column(name = "display_app_calories", nullable = false)
	private boolean displayAppCalories;

	@Column(name = "display_menu_board_fats", nullable = false)
	private boolean displayMenuBoardFats;

	@Column(name = "display_ecommerce_fats", nullable = false)
	private boolean displayEcommerceFats;

	@Column(name = "display_kiosk_fats", nullable = false)
	private boolean displayKioskFats;

	@Column(name = "display_app_fats", nullable = false)
	private boolean displayAppFats;

	@Column(name = "display_menu_board_Carbohydrates", nullable = false)
	private boolean displayMenuBoardCarbohydrates;

	@Column(name = "display_ecommerce_Carbohydrates", nullable = false)
	private boolean displayEcommerceCarbohydrates;

	@Column(name = "display_kiosk_Carbohydrates", nullable = false)
	private boolean displayKioskCarbohydrates;

	@Column(name = "display_app_Carbohydrates", nullable = false)
	private boolean displayAppCarbohydrates;

	@Column(name = "display_menu_board_protein", nullable = false)
	private boolean displayMenuBoardProtein;

	@Column(name = "display_ecommerce_protein", nullable = false)
	private boolean displayEcommerceProtein;

	@Column(name = "display_kiosk_protein", nullable = false)
	private boolean displayKioskProtein;

	@Column(name = "display_app_protein", nullable = false)
	private boolean displayAppProtein;

	@Column(name = "display_menu_board_sugar", nullable = false)
	private boolean displayMenuBoardSugar;

	@Column(name = "display_Ecommerce_sugar", nullable = false)
	private boolean displayEcommerceSugar;

	@Column(name = "display_kiosk_sugar", nullable = false)
	private boolean displayKioskSugar;

	@Column(name = "display_app_sugar", nullable = false)
	private boolean displayAppSugar;

	@Column(name = "display_menu_board_kcalories", nullable = false)
	private boolean displayMenuBoardKcalories;

	@Column(name = "display_Ecommerce_kcalories", nullable = false)
	private boolean displayEcommerceKcalories;

	@Column(name = "display_kiosk_kcalories", nullable = false)
	private boolean displayKioskKcalories;

	@Column(name = "display_app_kcalories", nullable = false)
	private boolean displayAppKcalories;

	public UpcNutritionInformationModel(Long nutritionDetailId,
										Double calories,
										Double fats,
										Double carbohydrates,
										Double protein,
										Double sugar,
										Double kiloCalories,
										String flagToDisplay,
										Double minRangeKCalories,
										Double maxRangeKCalories,
										boolean displayMenuBoardCalories,
										boolean displayEcommerceCalories,
										boolean displayKioskCalories,
										boolean displayAppCalories,
										boolean displayMenuBoardFats,
										boolean displayEcommerceFats,
										boolean displayKioskFats,
										boolean displayAppFats,
										boolean displayMenuBoardCarbohydrates,
										boolean displayEcommerceCarbohydrates,
										boolean displayKioskCarbohydrates,
										boolean displayAppCarbohydrates,
										boolean displayMenuBoardProtein,
										boolean displayEcommerceProtein,
										boolean displayKioskProtein,
										boolean displayAppProtein,
										boolean displayMenuBoardSugar,
										boolean displayEcommerceSugar,
										boolean displayKioskSugar,
										boolean displayAppSugar,
										boolean displayMenuBoardKcalories,
										boolean displayEcommerceKcalories,
										boolean displayKioskKcalories,
										boolean displayAppKcalories) {
		this.nutritionDetailId = nutritionDetailId;
		this.calories = calories;
		this.fats = fats;
		this.carbohydrates = carbohydrates;
		this.protein = protein;
		this.sugar = sugar;
		this.kiloCalories=kiloCalories;
		this.flagToDisplay = flagToDisplay;
		this.minRangeKCalories = minRangeKCalories;
		this.maxRangeKCalories =maxRangeKCalories;
		this.displayMenuBoardCalories = displayMenuBoardCalories;
		this.displayEcommerceCalories = displayEcommerceCalories;
		this.displayKioskCalories = displayKioskCalories;
		this.displayAppCalories = displayAppCalories;
		this.displayMenuBoardFats = displayMenuBoardFats;
		this.displayEcommerceFats = displayEcommerceFats;
		this.displayKioskFats = displayKioskFats;
		this.displayAppFats = displayAppFats;
		this.displayMenuBoardCarbohydrates = displayMenuBoardCarbohydrates;
		this.displayEcommerceCarbohydrates = displayEcommerceCarbohydrates;
		this.displayKioskCarbohydrates = displayKioskCarbohydrates;
		this.displayAppCarbohydrates = displayAppCarbohydrates;
		this.displayMenuBoardProtein = displayMenuBoardProtein;
		this.displayEcommerceProtein = displayEcommerceProtein;
		this.displayKioskProtein = displayKioskProtein;
		this.displayAppProtein = displayAppProtein;
		this.displayMenuBoardSugar = displayMenuBoardSugar;
		this.displayEcommerceSugar = displayEcommerceSugar;
		this.displayKioskSugar = displayKioskSugar;
		this.displayAppSugar = displayAppSugar;
		this.displayMenuBoardKcalories = displayMenuBoardKcalories;
		this.displayEcommerceKcalories = displayEcommerceKcalories;
		this.displayKioskKcalories = displayKioskKcalories;
		this.displayAppKcalories = displayAppKcalories;
	}

	public void parseFromDTO(NutritionalInformationDTO nutritionalInfo) {
		this.nutritionDetailId = nutritionalInfo.getNutritionDetailId();
		this.calories = nutritionalInfo.getCalories();
		this.fats = nutritionalInfo.getFats();
		this.carbohydrates = nutritionalInfo.getCarbohydrates();
		this.protein = nutritionalInfo.getProtein();
		this.sugar = nutritionalInfo.getSugar();
		this.kiloCalories = nutritionalInfo.getKiloCalories();
		this.flagToDisplay=nutritionalInfo.getFlagToDisplay();
		this.minRangeKCalories = nutritionalInfo.getMinRangeKCalories();
		this.maxRangeKCalories =nutritionalInfo.getMaxRangeKCalories();

		this.displayMenuBoardCalories = nutritionalInfo.isDisplayMenuBoardCalories();
		this.displayEcommerceCalories = nutritionalInfo.isDisplayEcommerceCalories();
		this.displayKioskCalories = nutritionalInfo.isDisplayKioskCalories();
		this.displayAppCalories = nutritionalInfo.isDisplayAppCalories();

		this.displayMenuBoardFats = nutritionalInfo.isDisplayMenuBoardFats();
		this.displayEcommerceFats = nutritionalInfo.isDisplayEcommerceFats();
		this.displayKioskFats = nutritionalInfo.isDisplayKioskFats();
		this.displayAppFats = nutritionalInfo.isDisplayAppFats();

		this.displayMenuBoardCarbohydrates = nutritionalInfo.isDisplayMenuBoardCarbohydrates();
		this.displayEcommerceCarbohydrates = nutritionalInfo.isDisplayEcommerceCarbohydrates();
		this.displayKioskCarbohydrates = nutritionalInfo.isDisplayKioskCarbohydrates();
		this.displayAppCarbohydrates = nutritionalInfo.isDisplayAppCarbohydrates();

		this.displayMenuBoardProtein = nutritionalInfo.isDisplayMenuBoardProtein();
		this.displayEcommerceProtein = nutritionalInfo.isDisplayEcommerceProtein();
		this.displayKioskProtein = nutritionalInfo.isDisplayKioskProtein();
		this.displayAppProtein = nutritionalInfo.isDisplayAppProtein();

		this.displayMenuBoardSugar = nutritionalInfo.isDisplayMenuBoardSugar();
		this.displayEcommerceSugar = nutritionalInfo.isDisplayEcommerceSugar();
		this.displayKioskSugar = nutritionalInfo.isDisplayKioskSugar();
		this.displayAppSugar = nutritionalInfo.isDisplayAppSugar();

		this.displayMenuBoardKcalories = nutritionalInfo.isDisplayMenuBoardKcalories();
		this.displayEcommerceKcalories = nutritionalInfo.isDisplayEcommerceKcalories();
		this.displayKioskKcalories = nutritionalInfo.isDisplayKioskKcalories();
		this.displayAppKcalories = nutritionalInfo.isDisplayAppKcalories();
	}

	public UpcNutritionInformationModel setUpcMaster(UpcMasterModel upcMaster) {
		this.upcMaster = upcMaster;
		return this;
	}
}
