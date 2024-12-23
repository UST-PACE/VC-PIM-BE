package com.ust.retail.store.pim.model.upcmaster;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ust.retail.store.bistro.dto.recipes.RecipeDTO;
import com.ust.retail.store.pim.dto.upcmaster.NutritionalInformationDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcMasterDTO;
import com.ust.retail.store.pim.model.catalog.BrandOwnerModel;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.model.catalog.FoodOptionModel;
import com.ust.retail.store.pim.model.catalog.ProductCategoryModel;
import com.ust.retail.store.pim.model.catalog.ProductGroupModel;
import com.ust.retail.store.pim.model.catalog.ProductItemModel;
import com.ust.retail.store.pim.model.catalog.ProductSubcategoryModel;
import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
		name = "upc_master",
		uniqueConstraints = {
				@UniqueConstraint(name = "uq_product_name", columnNames = {"upc_master_type_id", "product_name"}),
				@UniqueConstraint(name = "uq_principal_upc", columnNames = {"principal_upc"}),
				@UniqueConstraint(columnNames = { "alias" })
		})
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class UpcMasterModel extends Audits implements Serializable {

	private static final long serialVersionUID = 2739179592458819148L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "upc_master_id")
	private Long upcMasterId;

	@NotBlank
	@Column(name = "product_name", nullable = false, length = 50)
	private String productName;

	@Column(name = "product_description", length = 100)
	private String productDescription;

	@Column(name = "menu_board_image_url", length = 500)
	private String menuBoardImageUrl;

	@Column(name = "website_image_url", length = 500)
	private String websiteImageUrl;

	@Column(name = "kiosk_image_url", length = 500)
	private String kioskImageUrl;

	@Column(name = "app_image_url", length = 500)
	private String appImageUrl;

	@Column(name = "product_image_1", length = 500)
	private String productImage1;

	@Column(name = "product_image_2", length = 500)
	private String productImage2;

	@Column(name = "product_image_3", length = 500)
	private String productImage3;

	@Column(name = "product_image_4", length = 500)
	private String productImage4;

	@Column(name = "tax_percentage")
	private Double taxPercentage;

	@Column(name = "upc_discount")
	private Double upcDiscount;

	@Column(name = "upc_tax_percentage_active")
	private Boolean upcTaxPercentageActive;

	@Column(name = "principal_upc")
	private String principalUpc;

	@Column(name = "sku")
	private String sku;

	@Column(name = "product_weight")
	private Double productWeight;

	@Column(name = "product_length")
	private Double productLength;

	@Column(name = "product_height")
	private Double productHeight;

	@Column(name = "product_width")
	private Double productWidth;

	@Column(name = "shelf_life_wh")
	private Integer shelfLifeWh;

	@Column(name = "shelf_life_shipment")
	private Integer shelfLifeShipment;

	@Column(name = "shelf_life_customer")
	private Integer shelfLifeCustomer;

	@Column(name = "stock_min")
	private Double stockMin;

	@Column(name = "planogram_location", length = 15)
	private String planogramLocation;

	@Column(name = "batch_required")
	private boolean batchRequired;

	@Column(name = "expiration_date_required")
	private boolean expirationDateRequired;

	@Column(name = "age_restricted")
	private boolean ageRestricted;

	@Column(name = "return_reduces_inventory")
	private boolean returnReducesInventory;

	@Column(name = "image_trained")
	private boolean imageTrained;

	@Column(name = "sold_in_bulk", columnDefinition = "boolean default false")
	private boolean soldInBulk;

	@Column(name = "content_per_unit")
	private Double contentPerUnit;
	
	@Column(name = "alias", length = 500)
	private String alias;
	
	@Column(name = "portion_qty", columnDefinition = "integer default 1")
	private Integer portionQty;
	
	@Column(name = "vc_item", columnDefinition = "boolean default false")
	private Boolean vcItem;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upc_master_type_id", referencedColumnName = "catalog_id")
	private CatalogModel upcMasterType;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_type_id", referencedColumnName = "catalog_id")
	private CatalogModel productType;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_type_buying_unit_id", referencedColumnName = "catalog_id")
	private CatalogModel productTypeBuyingUnit;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_type_selling_unit_id", referencedColumnName = "catalog_id")
	private CatalogModel productTypeSellingUnit;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "brand_owner_id", referencedColumnName = "brand_owner_id")
	private BrandOwnerModel brandOwner;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_of_origin_id", referencedColumnName = "catalog_id")
	private CatalogModel countryOfOrigin;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_group_id", referencedColumnName = "product_group_id")
	private ProductGroupModel productGroup;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_category_id", referencedColumnName = "product_category_id")
	private ProductCategoryModel productCategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_subcategory_id", referencedColumnName = "product_subcategory_id")
	private ProductSubcategoryModel productSubcategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_item_id", referencedColumnName = "product_item_id")
	private ProductItemModel productItem;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_type_id", referencedColumnName = "catalog_id")
	private CatalogModel packageType;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_per_unit_uom_id", referencedColumnName = "catalog_id")
	private CatalogModel contentPerUnitUom;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inventory_unit_id", referencedColumnName = "catalog_id")
	private CatalogModel inventoryUnit;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upc_master_status_id", referencedColumnName = "catalog_id")
	private CatalogModel upcMasterStatus;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "main_entry_type_id", referencedColumnName = "catalog_id")
	private CatalogModel mainEntryType;

	@OneToOne(mappedBy = "upcMaster", cascade = CascadeType.ALL)
	private UpcNutritionInformationModel upcNutritionInformationModel;

	@OneToMany(orphanRemoval = true, mappedBy = "upcMaster", cascade = CascadeType.ALL)
	private Set<UpcMasterSellingChannelModel> upcSellingChannels;

	@OneToMany(orphanRemoval = true, mappedBy = "upcMaster")
	private Set<UpcAdditionalFeeModel> upcAdditionalFees = new LinkedHashSet<>();

	@OneToMany(mappedBy = "upcMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<UpcStorePriceModel> storePrices = new HashSet<>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "food_option_id", referencedColumnName = "food_option_id")
	private FoodOptionModel foodOption;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upc_product_type_id", referencedColumnName = "catalog_id")
	private CatalogModel upcProductType;

	@Column(name = "serve_with", length = 500)
	private String servedWith;

	@Column(name = "core_name", length = 75)
	private String coreName;

	@Column(name = "recipe_number")
	private Double recipeNumber;

	@Column(name = "package_color", length=50)
	private String packageColor;



	public UpcMasterModel(Long upcMasterId) {
		this.upcMasterId = upcMasterId;
	}

	public UpcMasterModel(
			Long upcMasterId,
			String principalUpc,
			String sku,
			Long brandOwnerId,
			Long countryOfOriginId,
			Long upcMasterTypeId,

			Long productGroupId,
			Long productCategoryId,
			Long productSubcategoryId,
			Long productItemId,
			Long productTypeId,
			Long mainEntryTypeId,
			Long packageTypeId,
			String productName,
			String productDescription,
			Double productWidth,
			Double productWeight,
			Double productLength,
			Double productHeight,

			Long productTypeBuyingUnitId,
			Long productTypeSellingUnitId,
			Long upcMasterStatusId,

			boolean soldInBulk,
			Double contentPerUnit,
			Long contentPerUnitUomId,
			Long inventoryUnitId,
			Integer shelfLifeWh,
			Integer shelfLifeShipment,
			Integer shelfLifeCustomer,
			String planogramLocation,
			Double stockMin,
			boolean batchRequired,
			boolean expirationDateRequired,
			boolean ageRestricted,
			boolean returnReducesInventory,
			Double taxPercentage,
			Double upcDiscount,

			Long userCreatedId,
			Integer portionQty,
			String alias,
			Boolean vcItem,
			Long upcProductTypeId,
			String serveWith,
			String coreName,
			Double recipeNumber,
			String packageColor
	) {

		this.upcMasterId = upcMasterId;
		this.principalUpc = principalUpc;
		this.sku = sku;
		this.brandOwner = new BrandOwnerModel(brandOwnerId);
		this.countryOfOrigin = new CatalogModel(countryOfOriginId);
		this.upcMasterType = new CatalogModel(upcMasterTypeId);

		this.productGroup = new ProductGroupModel(productGroupId);
		this.productCategory = new ProductCategoryModel(productCategoryId);
		this.productSubcategory = new ProductSubcategoryModel(productSubcategoryId);
		this.productItem = new ProductItemModel(productItemId);
		this.productType = new CatalogModel(productTypeId);
		this.mainEntryType = new CatalogModel(mainEntryTypeId);
		this.packageType = new CatalogModel(packageTypeId);
		this.productName = productName;
		this.productDescription = productDescription;
		this.productWeight = productWeight;
		this.productLength = productLength;
		this.productHeight = productHeight;
		this.productWidth = productWidth;

		this.productTypeBuyingUnit = new CatalogModel(productTypeBuyingUnitId);
		this.productTypeSellingUnit = new CatalogModel(productTypeSellingUnitId);
		this.upcMasterStatus = new CatalogModel(upcMasterStatusId);

		this.soldInBulk = soldInBulk;
		this.contentPerUnit = contentPerUnit;
		this.contentPerUnitUom = new CatalogModel(contentPerUnitUomId);
		this.inventoryUnit = new CatalogModel(inventoryUnitId);
		this.shelfLifeWh = shelfLifeWh;
		this.shelfLifeShipment = shelfLifeShipment;
		this.shelfLifeCustomer = shelfLifeCustomer;
		this.planogramLocation = planogramLocation;
		this.stockMin = stockMin;
		this.batchRequired = batchRequired;
		this.expirationDateRequired = expirationDateRequired;
		this.ageRestricted = ageRestricted;
		this.returnReducesInventory = returnReducesInventory;

		this.taxPercentage = taxPercentage;
		this.upcDiscount = upcDiscount;
		super.userCreate = new UserModel(userCreatedId);
		
		this.portionQty = portionQty;
		this.alias = alias;
		this.vcItem = vcItem;
		this.upcProductType = new CatalogModel(upcProductTypeId);

		this.servedWith = serveWith;
		this.coreName=coreName;
		this.recipeNumber=recipeNumber;
		this.packageColor=packageColor;
	}

	public void updateMenuBoardImageUrl(String menuBoardImageUrl) {
		this.menuBoardImageUrl = menuBoardImageUrl;
	}

	public void updateWebsiteImage(String websiteImageUrl) {
		this.websiteImageUrl = websiteImageUrl;
	}

	public void updateKioskImageUrl(String kioskImageUrl) {
		this.kioskImageUrl = kioskImageUrl;
	}

	public void updateAppImageUrl(String appImageUrl) {
		this.appImageUrl = appImageUrl;
	}

	public void updateProductImage1(String productImage){
		this.productImage1 = productImage;
	}
	public void updateProductImage2(String productImage){
		this.productImage2 = productImage;
	}
	public void updateProductImage3(String productImage){
		this.productImage3 = productImage;
	}
	public void updateProductImage4(String productImage){
		this.productImage4 = productImage;
	}

	public void setImageAsTrained(boolean value) {
		this.imageTrained = value;
	}

	public void updateSku() {
		this.sku = String.valueOf(this.upcMasterId);
	}

	public void setToppingFields(RecipeDTO recipeDTO) {
		this.contentPerUnit = recipeDTO.getContentPerUnit();
		this.contentPerUnitUom = new CatalogModel(recipeDTO.getContentPerUnitUomId());
		this.inventoryUnit = new CatalogModel(recipeDTO.getInventoryUnitId());
	}

	public void updateUpcMasterStatus(Long upcMasterStatusId) {
		this.upcMasterStatus = new CatalogModel(upcMasterStatusId);
	}

	public UpcMasterModel setUpcNutritionInformationModel(UpcNutritionInformationModel upcNutritionInformationModel) {
		this.upcNutritionInformationModel = upcNutritionInformationModel;
		return this;
	}

	public void updateUpcNutritionInformationModel(NutritionalInformationDTO nutritionalInfo) {
 		if (this.upcNutritionInformationModel == null) {
			this.upcNutritionInformationModel = new UpcNutritionInformationModel().setUpcMaster(this);
		}
		this.upcNutritionInformationModel.parseFromDTO(nutritionalInfo);
	}

	public UpcMasterModel setUpcSellingChannels(Set<UpcMasterSellingChannelModel> sellingChannelModels) {
		if (this.upcSellingChannels == null) {
			this.upcSellingChannels = new HashSet<>();
		}

		this.upcSellingChannels.clear();
		this.upcSellingChannels.addAll(sellingChannelModels);

		return this;
	}

	public void addStorePrice(UpcStorePriceModel upcStorePriceModel) {
		upcStorePriceModel.setUpcMaster(this);
		getStorePrices().add(upcStorePriceModel);
	}

	public Double getSalePrice() {
		return getStorePrices().stream()
				.map(UpcStorePriceModel::getSalePrice)
				.filter(Objects::nonNull)
				.mapToDouble(Double::doubleValue)
				.average()
				.orElse(0d);
	}

	public Double getSalePrice(Long storeNumId) {
		return getStorePrices().stream()
				.filter(sp -> Objects.equals(sp.getStoreNumber().getStoreNumId(), storeNumId))
				.filter(sp-> sp.getSalePrice() != null)
				.mapToDouble(UpcStorePriceModel::getSalePrice)
				.findFirst()
				.orElse(0d);
	}

	public void updateInformation(UpcMasterDTO dto) {
		this.principalUpc = dto.getPrincipalUPC();
		this.brandOwner = new BrandOwnerModel(dto.getBrandOwnerId());
		this.countryOfOrigin = new CatalogModel(dto.getCountryOfOriginId());
		this.productGroup = new ProductGroupModel(dto.getProductGroupId());
		this.productCategory = new ProductCategoryModel(dto.getProductCategoryId());
		this.productSubcategory = new ProductSubcategoryModel(dto.getProductSubcategoryId());
		this.productItem = new ProductItemModel(dto.getProductItemId());
		this.productType = new CatalogModel(dto.getProductTypeId());
		this.mainEntryType = new CatalogModel(dto.getMainEntryTypeId());
		this.productName = dto.getProductName();
		this.productDescription = dto.getProductDescription();
		this.productWeight = dto.getProductWeight();
		this.productLength = dto.getProductLength();
		this.productHeight = dto.getProductHeight();
		this.productWidth = dto.getProductWidth();

		this.contentPerUnit = dto.getContentPerUnit();
		this.contentPerUnitUom = new CatalogModel(dto.getContentPerUnitUomId());
		this.inventoryUnit = new CatalogModel(dto.getInventoryUnitId());
		this.shelfLifeWh = dto.getShelfLifeWh();
		this.shelfLifeShipment = dto.getShelfLifeShipment();
		this.shelfLifeCustomer = dto.getShelfLifeCustomer();
		this.planogramLocation = dto.getPlanogramLocation();
		this.stockMin = dto.getStockMin();
		this.batchRequired = dto.isBatchRequired();
		this.expirationDateRequired = dto.isExpirationDateRequired();
		this.ageRestricted = dto.isAgeRestricted();
		this.returnReducesInventory = dto.isReturnReducesInventory();
		this.updateUpcNutritionInformationModel(dto.getNutritionalInformation());
		this.setUpcSellingChannels(dto.getSellingChannels().stream()
				.map(sc -> sc.parseToModel(sc).setUpcMaster(this))
				.collect(Collectors.toSet()));
		
		this.portionQty = dto.getPortionQty();
		this.alias = dto.getAlias();
		this.vcItem = dto.getVcItem();
		this.upcProductType = new CatalogModel(dto.getUpcProductTypeId());
		this.servedWith = dto.getServedWith();
		this.coreName = dto.getCoreName();
		this.recipeNumber = dto.getRecipeNumber();
		this.packageColor=dto.getPackageColor();

		
	}

	public void updateFinancialInformation(UpcMasterDTO upcMasterDTO) {
		this.upcMasterStatus = new CatalogModel(upcMasterDTO.getUpcMasterStatusId());

		List<UpcStorePriceModel> storePriceModels = upcMasterDTO.getStorePrices().stream()
				.filter(sp -> Objects.nonNull(sp.getSalePrice()))
				.map(sp -> sp.createModel(this.userCreate.getUserId()))
				.collect(Collectors.toUnmodifiableList());
		updateStorePrices(storePriceModels);
	}

	public void updateStorePrices(List<UpcStorePriceModel> storePriceModels) {
		Set<UpcStorePriceModel> storePrices = getStorePrices();
		storePrices.clear();
		storePriceModels.forEach(this::addStorePrice);
	}

	public void updateSalePriceForStore(Double salePrice, Long storeNumId) {
		if (Objects.isNull(storeNumId)) {
			return;
		}

		getStorePrices().stream()
				.filter(sp -> Objects.equals(sp.getStoreNumber().getStoreNumId(), storeNumId))
				.findFirst()
				.ifPresent(sp -> sp.updateSalePrice(salePrice));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof UpcMasterModel)) return false;

		UpcMasterModel that = (UpcMasterModel) o;

		return new EqualsBuilder().append(upcMasterId, that.upcMasterId).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(upcMasterId).toHashCode();
	}

	public void setTaxePrecentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public void setUpcDiscount(Double upcDiscount) {
		this.upcDiscount = upcDiscount;
	}

	public void setTaxePrecentageActive(Boolean upcTaxPercentageActive) {
		this.upcTaxPercentageActive = upcTaxPercentageActive;	
	}

	public void setFoodOption(FoodOptionModel foodOption) {
		this.foodOption = foodOption;
	}

	public void setGeniSuggest(String alias,String packageColor){
		this.alias =alias;
		this.packageColor=packageColor;
	}
}
