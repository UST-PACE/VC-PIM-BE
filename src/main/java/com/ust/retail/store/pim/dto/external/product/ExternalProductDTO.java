package com.ust.retail.store.pim.dto.external.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ust.retail.store.bistro.dto.external.dish.ExternalNutritionalInformationDTO;
import com.ust.retail.store.common.dto.external.product.ExternalAdditionalFeeDTO;
import com.ust.retail.store.common.util.UnitConverter;
import com.ust.retail.store.pim.common.catalogs.ProductUnitCatalog;
import com.ust.retail.store.pim.dto.external.ExternalFoodOptionDTO;
import com.ust.retail.store.pim.dto.upcmaster.ApplicableTaxDTO;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.model.inventory.InventoryModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class ExternalProductDTO {
	private Long productId;
	private String sku;
	private String skuId;
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
	private Boolean imageTrained;
	private String productType;
	private Long mainEntryTypeId;
	private String mainEntryType;
	private boolean soldInBulk;
	private Double price;
	private Double sellingPrice;
	private String sellingUnit;
	private Double taxPercentage;
	private Double weightInGrams;
	private Double heightInCm;
	private Double widthInCm;
	private Double lengthInCm;
	private Boolean ageRestricted;
	private Boolean returnReducesInventory;
	private Long groupId;
	private Long categoryId;
	private String categoryName;
	private Long subcategoryId;
	private List<ExternalStoreLocationInventoryDTO> storeLocationInventoryList;
	private ExternalNutritionalInformationDTO nutritionalInformation;
	private Set<ApplicableTaxDTO> applicableTaxes;
	private boolean hasAdditionalFees;
	private List<ExternalAdditionalFeeDTO> additionalFees;
	private String alias;
	private Boolean vcItem;
	private Integer portionQty;
	private ExternalFoodOptionDTO foodOption;

	private Boolean upcTaxPercentageActive;

	private String servedWith;
	private String packageColor;
	private String coreName;
	private Double recipeNumber;
	private Double upcDiscount;
	private String upcProductTypeName;

	public ExternalProductDTO parseToDTO(UpcMasterModel m, Long storeNumId, UnitConverter unitConverter) {
		this.productId = m.getUpcMasterId();
		this.sku = m.getPrincipalUpc();
		this.skuId = m.getSku();
		this.productName = m.getProductName();
		this.productDescription = m.getProductDescription();
		this.menuBoardImage = m.getMenuBoardImageUrl();
		this.websiteImage = m.getWebsiteImageUrl();
		this.kioskImage = m.getKioskImageUrl();
		this.appImage = m.getAppImageUrl();
		this.image = m.getWebsiteImageUrl();
		this.imageTrained = m.isImageTrained();
		this.productType = m.getProductType().getCatalogOptions();
		this.mainEntryTypeId = m.getMainEntryType().getCatalogId();
		this.mainEntryType = m.getMainEntryType().getCatalogOptions();
		this.soldInBulk = m.isSoldInBulk();
		this.price = m.getSalePrice(storeNumId);
		this.sellingPrice = m.getSalePrice(storeNumId);
		if (this.soldInBulk) {
			this.sellingUnit = m.getProductTypeSellingUnit().getCatalogOptions();
		}
		this.weightInGrams = unitConverter.convert(ProductUnitCatalog.UNIT_OZ, ProductUnitCatalog.UNIT_G, m.getProductWeight());
		this.heightInCm = unitConverter.convert(ProductUnitCatalog.UNIT_IN, ProductUnitCatalog.UNIT_M, m.getProductHeight()) * 100;
		this.widthInCm = unitConverter.convert(ProductUnitCatalog.UNIT_IN, ProductUnitCatalog.UNIT_M, m.getProductWidth()) * 100;
		this.lengthInCm = unitConverter.convert(ProductUnitCatalog.UNIT_IN, ProductUnitCatalog.UNIT_M, m.getProductLength()) * 100;
		this.ageRestricted = m.isAgeRestricted();
		this.returnReducesInventory = m.isReturnReducesInventory();
		this.groupId = m.getProductGroup().getProductGroupId();
		this.categoryId = m.getProductCategory().getProductCategoryId();
		this.categoryName = m.getProductCategory().getProductCategoryName();
		this.subcategoryId = m.getProductSubcategory().getProductSubcategoryId();
		this.nutritionalInformation = new ExternalNutritionalInformationDTO().parseToDTO(m.getUpcNutritionInformationModel());
		this.additionalFees = m.getUpcAdditionalFees().stream()
				.filter(af -> Objects.equals(af.getStoreNumber().getStoreNumId(), storeNumId))
				.map(af -> new ExternalAdditionalFeeDTO(
						af.getStoreNumber().getStoreNumId(),
						af.getAdditionalFee().getFeeName(),
						af.getPrice()))
				.collect(Collectors.toList());
		this.hasAdditionalFees = !this.additionalFees.isEmpty();

		
		this.taxPercentage = m.getTaxPercentage();
		this.upcTaxPercentageActive = m.getUpcTaxPercentageActive() == null ? false : m.getUpcTaxPercentageActive();
		this.foodOption = new ExternalFoodOptionDTO().parseToDTO(m.getFoodOption());
		this.alias = m.getAlias();
		this.portionQty = m.getPortionQty();
		this.vcItem = m.getVcItem();

		this.servedWith = m.getServedWith();
		this.packageColor=m.getPackageColor();
		this.coreName=m.getCoreName();
		this.recipeNumber=m.getRecipeNumber();
		this.upcDiscount=m.getUpcDiscount();
		this.upcProductTypeName = m.getUpcProductType().getCatalogOptions();
		return this;
	}

	public ExternalProductDTO setStoreLocationInventory(List<InventoryModel> inventoryList, UnitConverter unitConverter) {
		this.storeLocationInventoryList = inventoryList.stream()
				.map(m -> new ExternalStoreLocationInventoryDTO().parseToDTO(m, unitConverter))
				.collect(Collectors.toUnmodifiableList());

		return this;
	}

	public ExternalProductDTO setApplicableTaxes(Set<ApplicableTaxDTO> applicableTaxes) {
		this.applicableTaxes = applicableTaxes;

		return this;
	}

	public void updatePrice(Double amount) {
		this.price = price * amount;
		this.price = Math.round(this.price * 100) / 100d;
	}
}
