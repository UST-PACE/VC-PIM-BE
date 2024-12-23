package com.ust.retail.store.bistro.dto.recipes;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.catalogs.*;
import com.ust.retail.store.pim.dto.upcmaster.UpcSellingChannelDTO;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterSellingChannelModel;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class BistroUpcMasterDTO {

	@NotNull(message = "upcMasterId is Mandatory.", groups = { OnUpdate.class })
	private Long upcMasterId;

	@NotNull(message = "principalUpc is Mandatory.", groups = { OnUpdate.class, OnCreate.class })
	private String principalUpc;

	@NotNull(message = "productName is Mandatory.", groups = { OnUpdate.class, OnCreate.class })
	private String productName;

	private String sku;

	@NotNull(message = "description is Mandatory.", groups = { OnUpdate.class, OnCreate.class })
	private String productDescription;

	@NotNull(message = "productGroupId is Mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Long productGroupId;

	@NotNull(message = "productCategoryId is Mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Long productCategoryId;

	@NotNull(message = "productSubcategoryId is Mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Long productSubcategoryId;

	@NotNull(message = "productItemId is Mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Long productItemId ;

	private String picture;

	private List<UpcSellingChannelDTO> sellingChannels = new ArrayList<>();


	/*DEFAULT VALUES*/
	private Double productWeight = 0.0;
	private Double productLength = 0.0;
	private Double productHeight = 0.0;
	private Double productWidth = 0.0;
	private boolean soldInBulk = false;
	private Double contentPerUnit = 1d;
	private Long contentPerUnitUomId = ProductUnitCatalog.UNIT_UNIT;
	private Long inventoryUnitId = ProductUnitCatalog.UNIT_UNIT;
	private Integer shelfLifeWh = 0;
	private Integer shelfLifeShipment = 0;
	private Integer shelfLifeCustomer = 0;
	private Double stockMin = -1D;
	private String planogramLocation = "Bistro";
	private Long productTypeBuyingUnitId = ProductUnitCatalog.UNIT_UNIT;
	private Long productTypeSellingUnitId = ProductUnitCatalog.UNIT_UNIT;
	private boolean batchRequired = false;
	private boolean expirationDateRequired = false;
	private boolean ageRestricted = false;
	private boolean returnReducesInventory = false;
	private Long productTypeId = ProductTypeCatalog.PRODUCT_TYPE_FG;
	private Long sectionTypeId = null;
	private Long packageTypeId = PackageTypeCatalog.PACKAGE_TYPE_NONE;
	private Long brandOwnerId = 9999L;
	private Long countryOfOriginId = CountryCatalog.COUNTRY_US;

	public UpcMasterModel createModel(Long userId) {

		UpcMasterModel model = new UpcMasterModel(
				upcMasterId,
				principalUpc,
				sku,
				brandOwnerId,
				countryOfOriginId,
				UpcMasterTypeCatalog.BISTRO_TYPE,

				productGroupId,
				productCategoryId,
				productSubcategoryId,
				productItemId,
				productTypeId,
				sectionTypeId,
				packageTypeId,
				productName,
				productDescription,
				productWidth,
				productWeight,
				productLength,
				productHeight,

				productTypeBuyingUnitId,
				productTypeSellingUnitId,
				UpcMasterStatusCatalog.UPC_MASTER_STATUS_ACTIVE,

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
				null,
				null,
				userId,
				1,
				null,
				false,
				null,
				null,
				null,
				null,
				null

				
		);
		model.setUpcSellingChannels(this.sellingChannels.stream()
				.map(sc -> sc.parseToModel(sc).setUpcMaster(model))
				.collect(Collectors.toSet()));
		return model;
	}

	public BistroUpcMasterDTO parseToDTO(UpcMasterModel model) {

		this.upcMasterId = model.getUpcMasterId();
		this.principalUpc = model.getPrincipalUpc();
		this.sku = model.getSku();
		this.brandOwnerId  = model.getBrandOwner().getBrandOwnerId();
		this.countryOfOriginId = model.getCountryOfOrigin().getCatalogId();
		this.productGroupId = model.getProductGroup().getProductGroupId();
		this.productCategoryId = model.getProductCategory().getProductCategoryId();
		this.productSubcategoryId = model.getProductSubcategory().getProductSubcategoryId();
		this.productItemId = model.getProductItem().getProductItemId();
		this.productTypeId = model.getProductType().getCatalogId();
//		this.sectionTypeId = model.getSectionType().getCatalogId();
		this.packageTypeId = model.getPackageType().getCatalogId();
		this.productName = model.getProductName();
		this.productDescription = model.getProductDescription();
		this.picture = model.getWebsiteImageUrl();
		this.contentPerUnit = model.getContentPerUnit();
		this.contentPerUnitUomId = model.getContentPerUnitUom().getCatalogId();
		this.inventoryUnitId = model.getInventoryUnit().getCatalogId();
		this.productWidth = model.getProductWidth();
		this.productWeight = model.getProductWeight();
		this.productLength = model.getProductLength();
		this.productHeight = model.getProductHeight();
		this.productTypeBuyingUnitId = model.getProductTypeBuyingUnit().getCatalogId();
		this.productTypeSellingUnitId = model.getProductTypeSellingUnit().getCatalogId();
		this.shelfLifeWh = model.getShelfLifeWh();
		this.shelfLifeShipment = model.getShelfLifeShipment();
		this.shelfLifeCustomer = model.getShelfLifeCustomer();
		this.planogramLocation = model.getPlanogramLocation();
		this.stockMin = model.getStockMin();
		this.batchRequired = model.isBatchRequired();
		this.expirationDateRequired = model.isExpirationDateRequired();
		this.ageRestricted = model.isAgeRestricted();
		this.returnReducesInventory = model.isReturnReducesInventory();

		Set<UpcMasterSellingChannelModel> upcSellingChannels = Optional.ofNullable(model.getUpcSellingChannels()).orElse(new HashSet<>());

		this.sellingChannels = upcSellingChannels.stream()
				.filter(sc -> Objects.nonNull(sc.getChannel()))
				.map(sc -> new UpcSellingChannelDTO().modelToDTO(sc))
				.collect(Collectors.toList());

		return this;
	}

}
