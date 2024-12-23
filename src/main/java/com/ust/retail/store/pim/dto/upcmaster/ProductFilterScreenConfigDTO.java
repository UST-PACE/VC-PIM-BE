package com.ust.retail.store.pim.dto.upcmaster;

import java.util.List;

import com.ust.retail.store.pim.dto.catalog.BrandOwnerDTO;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductFilterScreenConfigDTO {
	private List<BrandOwnerDTO> brandOwnerList;
	private List<CatalogDTO> productTypeList;

	public ProductFilterScreenConfigDTO(List<BrandOwnerDTO> brandOwnerList, List<CatalogDTO> productTypeList) {
		this.brandOwnerList = brandOwnerList;
		this.productTypeList = productTypeList;
	}
}
