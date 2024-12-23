package com.ust.retail.store.bistro.dto.external.dish;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.bistro.dto.recipes.DrinksConfExternalApiDTO;
import com.ust.retail.store.bistro.model.recipes.RecipeDetailModel;
import com.ust.retail.store.bistro.model.recipes.RecipeModel;
import com.ust.retail.store.common.dto.external.product.ExternalAdditionalFeeDTO;
import com.ust.retail.store.pim.dto.upcmaster.ApplicableTaxDTO;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class ExternalDishDTO {
	private Long productId;
	private String sku;
	private String productName;
	private String productDescription;
	private String menuBoardImage;
	private String websiteImage;
	private String kioskImage;
	private String appImage;
	/**
	 * @deprecated use websiteImage
	 */
	private String image;
	private String productType;
	private Long sectionTypeId;
	private String sectionType;
	private Double price;
	private Double sellingPrice;
	private Double weightInGrams;
	private Double heightInCm;
	private Double widthInCm;
	private Double lengthInCm;
	private Boolean ageRestricted;
	private Boolean returnReducesInventory;
	private Boolean display;
	private Long groupId;
	private Long categoryId;
	private Long subcategoryId;
	private Integer eta;
	private Long preparationAreaId;
	private String preparationArea;
	private ExternalNutritionalInformationDTO nutritionalInformation;

	private Boolean hasAddOns;
	private List<ExternalDishAddOnDTO> addOnList;

	private boolean hasExcludes;
	private List<ExternalDishExcludeDTO> excludeList;

	private boolean availableForSale;

	private boolean hasAdditionalFees;
	private List<ExternalAdditionalFeeDTO> additionalFees;
	private Set<ApplicableTaxDTO> applicableTaxes;

	private Double taxPercentage;
	private Boolean upcTaxPercentageActive;
	
	private DrinksConfExternalApiDTO drinksConfiguration; 
	
	public ExternalDishDTO parseToDTO(RecipeModel m, Long storeId, boolean availableForSale) {
		UpcMasterModel relatedUpcMaster = m.getRelatedUpcMaster();
		this.productId = relatedUpcMaster.getUpcMasterId();
		this.sku = relatedUpcMaster.getPrincipalUpc();
		this.productName = relatedUpcMaster.getProductName();
		this.productDescription = relatedUpcMaster.getProductDescription();
		this.menuBoardImage = relatedUpcMaster.getMenuBoardImageUrl();
		this.websiteImage = relatedUpcMaster.getWebsiteImageUrl();
		this.kioskImage = relatedUpcMaster.getKioskImageUrl();
		this.appImage = relatedUpcMaster.getAppImageUrl();
		this.image = relatedUpcMaster.getWebsiteImageUrl();
		this.productType = relatedUpcMaster.getUpcMasterType().getCatalogOptions();
//		this.sectionTypeId = relatedUpcMaster.getSectionType().getCatalogId();
//		this.sectionType = relatedUpcMaster.getSectionType().getCatalogOptions();
		this.price = relatedUpcMaster.getSalePrice(storeId);
		this.sellingPrice = relatedUpcMaster.getSalePrice(storeId);
		this.weightInGrams = relatedUpcMaster.getProductWeight();
		this.heightInCm = relatedUpcMaster.getProductHeight();
		this.widthInCm = relatedUpcMaster.getProductWidth();
		this.lengthInCm = relatedUpcMaster.getProductLength();
		this.ageRestricted = relatedUpcMaster.isAgeRestricted();
		this.returnReducesInventory = relatedUpcMaster.isReturnReducesInventory();
		this.display = m.isDisplayExternally();
		this.groupId = relatedUpcMaster.getProductGroup().getProductGroupId();
		this.categoryId = relatedUpcMaster.getProductCategory().getProductCategoryId();
		this.subcategoryId = relatedUpcMaster.getProductSubcategory().getProductSubcategoryId();

		this.eta = m.getCookingTime();
		this.preparationAreaId = m.getPreparationArea().getCatalogId();
		this.preparationArea = m.getPreparationArea().getCatalogOptions();
		this.availableForSale = availableForSale;

		this.nutritionalInformation = new ExternalNutritionalInformationDTO()
				.parseToDTO(relatedUpcMaster.getUpcNutritionInformationModel());

		if (!Optional.ofNullable(m.getAddOns()).orElse(List.of()).isEmpty()) {
			this.hasAddOns = true;
			this.addOnList = m.getAddOns().stream()
					.map(a -> new ExternalDishAddOnDTO().parseToDTO(a))
					.collect(Collectors.toUnmodifiableList());
		} else {
			this.hasAddOns = false;
		}

		this.excludeList = m.getDetails().stream()
				.filter(RecipeDetailModel::isToExclude)
				.map(i -> new ExternalDishExcludeDTO().parseToDTO(i))
				.collect(Collectors.toUnmodifiableList());
		this.hasExcludes = !this.excludeList.isEmpty();

		this.additionalFees = relatedUpcMaster.getUpcAdditionalFees().stream()
				.filter(af -> Objects.equals(af.getStoreNumber().getStoreNumId(), storeId))
				.map(af -> new ExternalAdditionalFeeDTO(
						af.getStoreNumber().getStoreNumId(),
						af.getAdditionalFee().getFeeName(),
						af.getPrice()))
				.collect(Collectors.toList());
		this.hasAdditionalFees = !this.additionalFees.isEmpty();

		this.taxPercentage = m.getTaxPercentage();
		this.upcTaxPercentageActive = m.getUpcTaxPercentageActive();
		return this;
	}

	public ExternalDishDTO setApplicableTaxes(Set<ApplicableTaxDTO> applicableTaxes) {
		this.applicableTaxes = applicableTaxes;

		return this;
	}
	
	public ExternalDishDTO setDrinksConfiguration(DrinksConfExternalApiDTO drinksConfiguration) {
		
		this.drinksConfiguration = drinksConfiguration;
		return this;
	}
}
