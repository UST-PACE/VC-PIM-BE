package com.ust.retail.store.bistro.model.recipes;

import com.ust.retail.store.bistro.commons.catalogs.MealCategoryCatalog;
import com.ust.retail.store.bistro.dto.recipes.AddOnDTO;
import com.ust.retail.store.bistro.dto.recipes.RecipeDTO;
import com.ust.retail.store.bistro.dto.recipes.RecipeDetailDTO;
import com.ust.retail.store.bistro.dto.recipes.RecipeFinancialInfoDTO;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "bistro_recipes")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class RecipeModel extends Audits implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recipe_id")
	private Long recipeId;

	@Lob
	@Column(name = "prep_instructions")
	private String prepInstructions;

	@Column(name = "buffer_price_percentage", nullable = false)
	private Double bufferPricePercentage;

	@Column(name = "selling_price", nullable = false)
	private Double sellingPrice;

	@Column(name = "indirect_cost", nullable = false)
	private Double indirectCost;

	@Column(name = "cooking_time", nullable = false)
	private Integer cookingTime;

	@Column(name = "display_externally")
	private boolean displayExternally;

	@Column(name = "contains_text", length = 150)
	private String containsText;

	@Column(name = "ingredients_text", length = 700)
	private String ingredientsText;
	
	@Column(name = "tax_percentage")
	private Double taxPercentage;
	
	@Column(name = "upc_tax_percentage_active")
	private Boolean upcTaxPercentageActive;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upc_master_id", referencedColumnName = "upc_master_id")
	private UpcMasterModel relatedUpcMaster;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "meal_temp_id", referencedColumnName = "catalog_id")
	private CatalogModel mealTemperature; // HOT COLD

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "preparation_area_id", referencedColumnName = "catalog_id")
	private CatalogModel preparationArea;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "selling_unit_id", referencedColumnName = "catalog_id")
	private CatalogModel sellingUnit;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "food_classification_id", referencedColumnName = "catalog_id")
	private CatalogModel foodClassification;

	@OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RecipeDetailModel> details;

	@OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RecipeAddonModel> addOns;


	public RecipeModel(Long recipeId) {
		this.recipeId = recipeId;
	}

	public RecipeModel(Long recipeId, Long userId) {
		this.recipeId = recipeId;
		super.userCreate = new UserModel(userId);
	}

	public RecipeModel(Long recipeId,
					   Long mealTemperatureId,
					   Long preparationAreaId,
					   Long sellingUnitId,
					   String prepInstructions,
					   Double bufferPricePercentage,
					   Double sellingPrice,
					   Double indirectCost,
					   Integer cookingTime,
					   boolean displayExternally,
					   Long categoryId,
					   Long userId) {
		super();
		this.recipeId = recipeId;
		this.mealTemperature = new CatalogModel(mealTemperatureId);
		this.preparationArea = new CatalogModel(preparationAreaId);
		this.sellingUnit = new CatalogModel(sellingUnitId);
		this.prepInstructions = prepInstructions;
		this.bufferPricePercentage = bufferPricePercentage;
		this.sellingPrice = sellingPrice;
		this.indirectCost = indirectCost;
		this.cookingTime = cookingTime;
		this.displayExternally = displayExternally;
		this.foodClassification = new CatalogModel(categoryId);
		super.userCreate = new UserModel(userId);

	}

	public void addItem(RecipeDetailDTO recipeDetailDTO, Long userId) {
		if (details == null) {
			details = new ArrayList<>();
		}

		details.add(recipeDetailDTO.createModel(this, userId));
	}

	public void addInstructions(String prepInstructions) {
		this.prepInstructions = prepInstructions;
	}

	public void addLabelTexts(String containsText, String ingredientsText) {
		this.containsText = containsText;
		this.ingredientsText = ingredientsText;
	}

	public void addAddOn(AddOnDTO addOnDTO, Long userCreatedId) {

		if (addOns == null) {
			addOns = new ArrayList<>();
		}

		addOns.add(new RecipeAddonModel(this, addOnDTO.getRecipeAddOnId(),
				addOnDTO.getUpcMasterId(), addOnDTO.getQty(), addOnDTO.getSellingPrice(), addOnDTO.getMaxRequests(), userCreatedId));

	}

	public RecipeModel setRelatedUpcMaster(UpcMasterModel relatedUpcMaster) {
		this.relatedUpcMaster = relatedUpcMaster;
		return this;
	}

	public void setDisplayExternally(boolean displayExternally) {
		this.displayExternally = displayExternally;
	}

	public void updateInformation(RecipeDTO recipeDTO) {
		this.mealTemperature = new CatalogModel(recipeDTO.getMealTemperatureId());
		this.preparationArea = new CatalogModel(recipeDTO.getPreparationAreaId());
		this.foodClassification = new CatalogModel(recipeDTO.getFoodClassificationId());
		this.cookingTime = recipeDTO.getCookingTime();
		this.displayExternally = recipeDTO.isDisplayExternally();

		if (Objects.equals(recipeDTO.getFoodClassificationId(), MealCategoryCatalog.TOPPING)) {
			this.relatedUpcMaster.setToppingFields(recipeDTO);
		}
	}

	public void updateSalePrice(Double salePrice, Long storeNumId) {
		this.relatedUpcMaster.updateSalePriceForStore(salePrice, storeNumId);
	}

	public Double getAverageSalePrice() {
		return relatedUpcMaster.getSalePrice();
	}

	public void updateFinancialInformation(RecipeFinancialInfoDTO financialInfoDTO, Long userId) {
		this.indirectCost = Optional.ofNullable(financialInfoDTO.getIndirectCost()).orElse(0d);
		this.bufferPricePercentage = Optional.ofNullable(financialInfoDTO.getBufferOnPrice()).orElse(0d);
		this.taxPercentage = financialInfoDTO.getTaxPercentage();
		this.upcTaxPercentageActive = financialInfoDTO.getUpcTaxPercentageActive();
		
		this.relatedUpcMaster.updateStorePrices(financialInfoDTO.getStorePrices().stream()
				.filter(sp -> Objects.nonNull(sp.getSalePrice()))
				.map(sp -> sp.createModel(userId))
				.collect(Collectors.toList()));
	}
}
