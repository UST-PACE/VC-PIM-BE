package com.ust.retail.store.pim.dto.upcmaster;

import java.util.List;

import com.ust.retail.store.pim.common.bases.BaseDTO;
import com.ust.retail.store.pim.common.catalogs.CountryCatalog;
import com.ust.retail.store.pim.dto.catalog.BrandOwnerDTO;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.catalog.ProductGroupDTO;
import com.ust.retail.store.pim.dto.nutritionalInfo.NutritionalValuesForColorFlagsDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductScreenConfigDTO extends BaseDTO {
	private List<BrandOwnerDTO> brandOwnerList;
	private List<CatalogDTO> productTypeList;
	private List<CatalogDTO> mainEntryTypeList;
	private List<CatalogDTO> upcMasterStatusList;
	private List<CatalogDTO> upcMasterTypeList;
	private List<ProductGroupDTO> productGroupList;
	private List<CatalogDTO> productUnitList;
	private List<CatalogDTO> convertableUnitList;
	private List<CatalogDTO> countryOfOriginList;
	private List<CatalogDTO> packageTypeList;
	private List<CatalogDTO> upcSellingChannelList;
	private List<CatalogDTO> comboStatusList;
	private List<CatalogDTO> upcProductTypeList;
	private List<NutritionalValuesForColorFlagsDTO> nutritionalValuesForColorFlags;
	private Long countryOfOriginId;

	public ProductScreenConfigDTO(List<BrandOwnerDTO> brandOwnerList,
								  List<CatalogDTO> productTypeList,
								  List<CatalogDTO> mainEntryTypeList,
								  List<CatalogDTO> upcMasterTypeList,
								  List<ProductGroupDTO> productGroupList,
								  List<CatalogDTO> productUnitList,
								  List<CatalogDTO> convertableUnitList,
								  List<CatalogDTO> countryOfOriginList,
								  List<CatalogDTO> packageTypeList,
								  List<CatalogDTO> upcSellingChannelList,
								  List<CatalogDTO> comboStatusList,
								  List<CatalogDTO> upcProductTypeList,
								  List<NutritionalValuesForColorFlagsDTO> nutritionalValuesForColorFlags,
								  List<CatalogDTO> upcMasterStatusList,
								  Long countryOfOriginId) {
		this.brandOwnerList = brandOwnerList;
		this.productTypeList = productTypeList;
		this.mainEntryTypeList = mainEntryTypeList;
		this.upcMasterTypeList = upcMasterTypeList;
		this.productGroupList = productGroupList;
		this.productUnitList = productUnitList;
		this.convertableUnitList = convertableUnitList;
		this.countryOfOriginList = countryOfOriginList;
		this.packageTypeList = packageTypeList;
		this.upcSellingChannelList = upcSellingChannelList;
		this.comboStatusList = comboStatusList;
		this.upcProductTypeList = upcProductTypeList;
		this.nutritionalValuesForColorFlags = nutritionalValuesForColorFlags;
		this.upcMasterStatusList =upcMasterStatusList;
		this.countryOfOriginId=countryOfOriginId;
	}
}
