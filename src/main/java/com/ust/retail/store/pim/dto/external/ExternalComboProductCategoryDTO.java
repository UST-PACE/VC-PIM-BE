package com.ust.retail.store.pim.dto.external;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.model.upcmaster.ComboProductCategoryModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class ExternalComboProductCategoryDTO {

	private Long productCategoryId;

	private String productCategoryName;

	private Integer quantity;
	
	public ExternalComboProductCategoryDTO (ComboProductCategoryModel model) {
		this.productCategoryId = model.getProductCategory().getProductCategoryId();
		this.productCategoryName = model.getProductCategory().getProductCategoryName();
		this.quantity = model.getQuantity();
	}

}
