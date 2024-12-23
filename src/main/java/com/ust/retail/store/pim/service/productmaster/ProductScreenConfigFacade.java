package com.ust.retail.store.pim.service.productmaster;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ust.retail.store.common.util.UnitConverter;
import com.ust.retail.store.pim.common.catalogs.ComboStatusCatalog;
import com.ust.retail.store.pim.common.catalogs.CountryCatalog;
import com.ust.retail.store.pim.common.catalogs.FoodOptionStatusCatelog;
import com.ust.retail.store.pim.common.catalogs.MainEntryTypeCatalog;
import com.ust.retail.store.pim.common.catalogs.PackageTypeCatalog;
import com.ust.retail.store.pim.common.catalogs.ProductTypeCatalog;
import com.ust.retail.store.pim.common.catalogs.ProductUnitCatalog;
import com.ust.retail.store.pim.common.catalogs.UpcMasterStatusCatalog;
import com.ust.retail.store.pim.common.catalogs.UpcMasterTypeCatalog;
import com.ust.retail.store.pim.common.catalogs.UpcProductTypeCatalog;
import com.ust.retail.store.pim.common.catalogs.UpcSellingChannelCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.upcmaster.ProductFilterScreenConfigDTO;
import com.ust.retail.store.pim.dto.upcmaster.ProductScreenConfigDTO;
import com.ust.retail.store.pim.service.catalog.BrandOwnerService;
import com.ust.retail.store.pim.service.catalog.ProductGroupService;
import com.ust.retail.store.pim.util.NutritionalValuesForColorFlags;

@Service
public class ProductScreenConfigFacade {
	private final BrandOwnerService brandOwnerService;
	private final ProductTypeCatalog productTypeCatalog;
	private final MainEntryTypeCatalog mainEntryTypeCatalog;
	private final UpcMasterTypeCatalog upcMasterTypeCatalog;
	private final ProductGroupService productGroupService;
	private final ProductUnitCatalog productUnitCatalog;
	private final CountryCatalog countryCatalog;
	private final PackageTypeCatalog packageTypeCatalog;
	private final UpcMasterStatusCatalog upcMasterStatusCatalog;
	private final UpcSellingChannelCatalog upcSellingChannelCatalog;
	private final UnitConverter unitConverter;
	private final FoodOptionStatusCatelog foodOptionStatusCatelog;
	private final ComboStatusCatalog comboStatusCatalog;
	private final UpcProductTypeCatalog upcProductTypeCatalog;
	private final NutritionalValuesForColorFlags nutritionalValuesForColorFlags;
	private final CountryCatalog countryOfOriginId;
	@Autowired
	public ProductScreenConfigFacade(BrandOwnerService brandOwnerService,
									 ProductTypeCatalog productTypeCatalog,
									 MainEntryTypeCatalog mainEntryTypeCatalog,
									 UpcMasterTypeCatalog upcMasterTypeCatalog,
									 ProductGroupService productGroupService,
									 ProductUnitCatalog productUnitCatalog,
									 CountryCatalog countryCatalog,
									 PackageTypeCatalog packageTypeCatalog,
									 UpcMasterStatusCatalog upcMasterStatusCatalog,
									 UpcSellingChannelCatalog upcSellingChannelCatalog,
									 UnitConverter unitConverter,
									 ComboStatusCatalog comboStatusCatalog,
									 FoodOptionStatusCatelog foodOptionStatusCatelog,
									 UpcProductTypeCatalog upcProductTypeCatalog,
									 NutritionalValuesForColorFlags nutritionalValuesForColorFlags,
									 CountryCatalog countryOfOriginId) {
		this.brandOwnerService = brandOwnerService;
		this.productTypeCatalog = productTypeCatalog;
		this.mainEntryTypeCatalog = mainEntryTypeCatalog;
		this.upcMasterTypeCatalog = upcMasterTypeCatalog;
		this.productGroupService = productGroupService;
		this.productUnitCatalog = productUnitCatalog;
		this.countryCatalog = countryCatalog;
		this.packageTypeCatalog = packageTypeCatalog;
		this.upcMasterStatusCatalog = upcMasterStatusCatalog;
		this.upcSellingChannelCatalog = upcSellingChannelCatalog;
		this.unitConverter = unitConverter;
		this.foodOptionStatusCatelog = foodOptionStatusCatelog;
		this.comboStatusCatalog = comboStatusCatalog;
		this.upcProductTypeCatalog = upcProductTypeCatalog;
		this.nutritionalValuesForColorFlags = nutritionalValuesForColorFlags;
		this.countryOfOriginId = countryOfOriginId;
	}

	public ProductScreenConfigDTO getScreenConfig() {
		return new ProductScreenConfigDTO(
				brandOwnerService.load(),
				productTypeCatalog.getCatalogOptions(),
				mainEntryTypeCatalog.getCatalogOptions(),
				upcMasterTypeCatalog.getCatalogOptions(),
				productGroupService.load(),
				productUnitCatalog.getCatalogOptions(),
				unitConverter.getConvertableUnits(),
				countryCatalog.getCatalogOptions(),
				packageTypeCatalog.getCatalogOptions(),
				upcSellingChannelCatalog.getCatalogOptions(),
				comboStatusCatalog.getCatalogOptions(),
				upcProductTypeCatalog.getCatalogOptions(),
				nutritionalValuesForColorFlags.getValues(),
				upcMasterStatusCatalog.getCatalogOptions(),
				countryOfOriginId.getCountryOriginId()
		);
	}

	public ProductFilterScreenConfigDTO getFilterScreenConfig() {
		return new ProductFilterScreenConfigDTO(
				brandOwnerService.getAutocomplete(""),
				productTypeCatalog.getCatalogOptions());
	}

	public List<CatalogDTO> getUpcMasterCatalogOptions() {
		return upcMasterStatusCatalog.getCatalogOptions();
	}
	
	public List<CatalogDTO> getFoodOptionStatusCatelog(){
		return foodOptionStatusCatelog.getCatalogOptions();
	}
}
