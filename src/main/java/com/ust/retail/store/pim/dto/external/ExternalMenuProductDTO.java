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
public class ExternalMenuProductDTO {

	private String sku;

	private String productName;

	private Long categoryId;

	private String categoryName;

	private String alias;

	public ExternalMenuProductDTO(UpcMasterModel model) {
		this.sku = model.getSku();
		this.productName = model.getProductName();
		this.categoryId = model.getProductCategory().getProductCategoryId();
		this.categoryName = model.getProductCategory().getProductCategoryName();
		this.alias = model.getAlias();
	}
}
