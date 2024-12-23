package com.ust.retail.store.pim.dto.external;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class ExternalMenuConfiguratorProductDTO {

	private Long upcMasterId;

	private String sku;

	private String productName;

	private String alias;

	private Long categoryId;

	private String categoryName;

	private String packageColor;

	private String upc;

	private String servedWith;

	private String type;

	public ExternalMenuConfiguratorProductDTO(UpcMasterModel model) {
		this.upcMasterId = model.getUpcMasterId();
		this.sku = model.getSku();
		this.alias = model.getAlias();
		this.productName = model.getProductName();
		this.type = model.getUpcProductType().getCatalogOptions();
		this.categoryId = model.getProductCategory().getProductCategoryId();
		this.categoryName = model.getProductCategory().getProductCategoryName();
		this.packageColor = model.getPackageColor();
		this.servedWith = model.getServedWith();
		this.upc = model.getPrincipalUpc();
	}

}
