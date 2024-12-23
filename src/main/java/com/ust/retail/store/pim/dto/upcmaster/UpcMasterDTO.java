package com.ust.retail.store.pim.dto.upcmaster;

import java.util.*;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import com.ust.retail.store.pim.common.catalogs.UpcMasterTypeCatalog;
import com.ust.retail.store.pim.dto.catalog.FoodOptionDTO;
import com.ust.retail.store.pim.dto.tax.TaxDTO;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterSellingChannelModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpcMasterDTO extends BaseFilterDTO {
	// General information
	@Null(message = "UPC Master Id should not be present when creating.", groups = {OnCreate.class})
	@NotNull(message = "UPC Master Id is mandatory when updating.", groups = {OnUpdate.class})
	private Long upcMasterId;

	@NotNull(message = "Principal UPC is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private String principalUPC;

	private Long upcMasterTypeId;

	private String upcMasterType;

	private String sku;

	@NotNull(message = "Brand Owner ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long brandOwnerId;
	private String brandOwnerName;

	@NotNull(message = "Country of Origin ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long countryOfOriginId;
	private String countryOfOriginName;

	// Product information
	@NotNull(message = "Product Group ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long productGroupId;
	private String productGroupName;

	@NotNull(message = "Product Category ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long productCategoryId;
	private String productCategoryName;

	@NotNull(message = "Product Subcategory ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long productSubcategoryId;
	private String productSubcategoryName;

	@NotNull(message = "Product Item ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long productItemId;
	private String productItemName;

	@NotNull(message = "Product Type ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long productTypeId;
	private String productTypeName;

	@NotNull(message = "Product Main Entry Type ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long mainEntryTypeId;
	private String mainEntryTypeName;

	private Long packageTypeId;

	@NotNull(message = "Product Name is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	@Size(min = 3, max = 50, message = "Product Name must have between 3 and 50 characters", groups = {OnCreate.class,
			OnUpdate.class})
	private String productName;

	@Size(max = 100, message = "Product Description must have at most 100 characters", groups = {OnCreate.class,
			OnUpdate.class})
	private String productDescription;

	private String menuBoardImage;

	private String websiteImage;

	private String kioskImage;

	private String appImage;

	private Double productWeight;

	private Double productLength;

	private Double productHeight;

	private Double productWidth;

	// Product Financial Information
	private List<StorePriceDTO> storePrices;
	private List<TaxDTO> applicableTaxes;

	// Product Additional Information
	private boolean soldInBulk;

	@NotNull(message = "Content per Unit is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Double contentPerUnit;

	@NotNull(message = "Content per Unit UOM ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long contentPerUnitUomId;

	private String contentPerUnitUomDesc;

	@NotNull(message = "Inventory Unit ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long inventoryUnitId;

	private String inventoryUnitDesc;

	@NotNull(message = "Buying Unit ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long productTypeBuyingUnitId;

	private String productTypeBuyingUnitDesc;

	@NotNull(message = "Selling Unit ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long productTypeSellingUnitId;

	private String productTypeSellingUnitDesc;

	private Integer shelfLifeWh;

	private Integer shelfLifeShipment;

	private Integer shelfLifeCustomer;

	@Size(max = 15, message = "Image URL must have at most 15 characters", groups = {OnCreate.class, OnUpdate.class})
	private String planogramLocation;

	@NotNull(message = "Stock Min is mandatory.", groups = {OnUpdate.class, OnUpdate.class})
	private Double stockMin;

	private Long upcMasterStatusId;

	private String upcMasterStatusDesc;
	private boolean batchRequired;

	private boolean expirationDateRequired;

	private boolean ageRestricted;

	private boolean returnReducesInventory;
	private boolean imageTrained;

	private NutritionalInformationDTO nutritionalInformation;

	private List<UpcAdditionalFeeFilterResultDTO> additionalFees = new ArrayList<>();
	private List<UpcSellingChannelDTO> sellingChannels = new ArrayList<>();

	private Double taxPercentage;

	private Double upcDiscount;
	
	private Boolean upcTaxPercentageActive;

	private boolean isDrinkConfOptionEnabled;
	
	private String alias;
	
	@NotNull(message = "Portion Qty must be not null")
	private Integer portionQty;
	
	private Boolean vcItem;
	
	private FoodOptionDTO foodOption;
	
	@NotNull(message = "Product Upc Type ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long upcProductTypeId;
	private String upcProductTypeName;

	private String servedWith;

	private String coreName;
	private Double recipeNumber;
	private String packageColor;

	private String productImage1;
	private String productImage2;
	private String productImage3;
	private String productImage4;

	private Date createdAt;
	private Date updateAt;
	public UpcMasterDTO parseToDTO(UpcMasterModel model) {
		this.upcMasterId = model.getUpcMasterId();
		this.principalUPC = model.getPrincipalUpc();
		this.upcMasterTypeId = model.getUpcMasterType().getCatalogId();
		this.upcMasterType = model.getUpcMasterType().getCatalogOptions();
		this.sku = model.getSku();
		this.brandOwnerId = model.getBrandOwner().getBrandOwnerId();
		this.brandOwnerName = model.getBrandOwner().getBrandOwnerName();
		this.productTypeId = model.getProductType().getCatalogId();
		this.productTypeName = model.getProductType().getCatalogOptions();
		this.mainEntryTypeId = model.getMainEntryType().getCatalogId();
		this.mainEntryTypeName = model.getMainEntryType().getCatalogOptions();
		this.countryOfOriginId = model.getCountryOfOrigin().getCatalogId();
		this.countryOfOriginName = model.getCountryOfOrigin().getCatalogOptions();

		this.productGroupId = model.getProductGroup().getProductGroupId();
		this.productGroupName = model.getProductGroup().getProductGroupName();
		this.productCategoryId = model.getProductCategory().getProductCategoryId();
		this.productCategoryName = model.getProductCategory().getProductCategoryName();
		this.productSubcategoryId = model.getProductSubcategory().getProductSubcategoryId();
		this.productSubcategoryName = model.getProductSubcategory().getProductSubcategoryName();
		this.productItemId = model.getProductItem().getProductItemId();
		this.productItemName = model.getProductItem().getProductItemName();
		this.packageTypeId = model.getPackageType().getCatalogId();
		this.productName = model.getProductName();
		this.productDescription = model.getProductDescription();
		this.menuBoardImage = model.getMenuBoardImageUrl();
		this.websiteImage = model.getWebsiteImageUrl();
		this.kioskImage = model.getKioskImageUrl();
		this.appImage = model.getAppImageUrl();
		this.productImage1 =model.getProductImage1();
		this.productImage2 =model.getProductImage2();
		this.productImage3 =model.getProductImage3();
		this.productImage4 =model.getProductImage4();
		this.productWeight = model.getProductWeight();
		this.productLength = model.getProductLength();
		this.productHeight = model.getProductHeight();
		this.productWidth = model.getProductWidth();

		this.soldInBulk = model.isSoldInBulk();
		this.contentPerUnit = model.getContentPerUnit();
		this.contentPerUnitUomId = model.getContentPerUnitUom().getCatalogId();
		this.contentPerUnitUomDesc = model.getContentPerUnitUom().getCatalogOptions();
		this.inventoryUnitId = model.getInventoryUnit().getCatalogId();
		this.inventoryUnitDesc = model.getInventoryUnit().getCatalogOptions();
		this.productTypeBuyingUnitId = model.getProductTypeBuyingUnit().getCatalogId();
		this.productTypeBuyingUnitDesc = model.getProductTypeBuyingUnit().getCatalogOptions();
		this.productTypeSellingUnitId = model.getProductTypeSellingUnit().getCatalogId();
		this.productTypeSellingUnitDesc = model.getProductTypeSellingUnit().getCatalogOptions();
		this.shelfLifeWh = model.getShelfLifeWh();
		this.shelfLifeShipment = model.getShelfLifeShipment();
		this.shelfLifeCustomer = model.getShelfLifeCustomer();
		this.planogramLocation = model.getPlanogramLocation();
		this.stockMin = model.getStockMin();
		this.batchRequired = model.isBatchRequired();
		this.expirationDateRequired = model.isExpirationDateRequired();
		this.ageRestricted = model.isAgeRestricted();
		this.imageTrained = model.isImageTrained();
		this.returnReducesInventory = model.isReturnReducesInventory();
		this.upcMasterStatusId = model.getUpcMasterStatus().getCatalogId();
		this.upcMasterStatusDesc = model.getUpcMasterStatus().getCatalogOptions();

		this.servedWith =model.getServedWith();
		this.coreName=model.getCoreName();
		this.recipeNumber=model.getRecipeNumber();
		this.packageColor=model.getPackageColor();
		this.createdAt =model.getCreatedAt();
		this.updateAt=model.getUpdatedAt();

		this.nutritionalInformation = new NutritionalInformationDTO().parseToDTO(model.getUpcNutritionInformationModel());
		this.additionalFees = model.getUpcAdditionalFees().stream()
				.map(af -> new UpcAdditionalFeeFilterResultDTO().modelToDTO(af))
				.collect(Collectors.toList());

		Set<UpcMasterSellingChannelModel> upcSellingChannels = Optional.ofNullable(model.getUpcSellingChannels()).orElse(new HashSet<>());
		this.sellingChannels = upcSellingChannels.stream()
				.filter(sc -> Objects.nonNull(sc.getChannel()))
				.map(sc -> new UpcSellingChannelDTO().modelToDTO(sc))
				.sorted(Comparator.comparing(UpcSellingChannelDTO::getChannelId))
				.collect(Collectors.toList());
		
		this.taxPercentage = model.getTaxPercentage();
		this.upcDiscount = model.getUpcDiscount();
		this.upcTaxPercentageActive = model.getUpcTaxPercentageActive();
		
		this.alias = model.getAlias();
		this.portionQty = model.getPortionQty();
		this.vcItem = model.getVcItem();
		this.foodOption = new FoodOptionDTO().parseToDTO(model.getFoodOption());
		this.upcProductTypeId = model.getUpcProductType().getCatalogId();
		this.upcProductTypeName = model.getUpcProductType().getCatalogOptions();
		return this;
	}

	public UpcMasterModel createModel(Long userId) {
		
		UpcMasterModel model = new UpcMasterModel(
				upcMasterId,
				principalUPC,
				sku,
				brandOwnerId,
				countryOfOriginId,
				UpcMasterTypeCatalog.PIM_TYPE,

				productGroupId,
				productCategoryId,
				productSubcategoryId,
				productItemId,
				productTypeId,
				mainEntryTypeId,
				packageTypeId,
				productName,
				productDescription,
				productWidth,
				productWeight,
				productLength,
				productHeight,

				productTypeBuyingUnitId,
				productTypeSellingUnitId,
				upcMasterStatusId,

				soldInBulk,
				contentPerUnit,
				contentPerUnitUomId,
				inventoryUnitId,
				shelfLifeWh,
				shelfLifeShipment,
				shelfLifeCustomer,
				planogramLocation,
				stockMin,
				batchRequired,
				expirationDateRequired,
				ageRestricted,
				returnReducesInventory,
				taxPercentage,
				upcDiscount,
				userId,
				portionQty,
				alias,
				vcItem,
				upcProductTypeId,
				servedWith,
				coreName,
				recipeNumber,
				packageColor

		);

		model.setUpcNutritionInformationModel(this.nutritionalInformation.parseToModel().setUpcMaster(model))
				.setUpcSellingChannels(this.sellingChannels.stream()
						.map(sc -> sc.parseToModel(sc).setUpcMaster(model))
						.collect(Collectors.toSet()));

		Optional.ofNullable(this.storePrices).orElse(List.of())
				.forEach(sp -> model.addStorePrice(sp.createModel(userId)));

		return model;
	}

	public UpcMasterDTO setStorePrices(List<StorePriceDTO> storePrices) {
		this.storePrices = storePrices;

		return this;
	}

	public UpcMasterDTO setApplicableTaxes(List<TaxDTO> applicableTaxes) {
		this.applicableTaxes = applicableTaxes;

		return this;
	}
}
