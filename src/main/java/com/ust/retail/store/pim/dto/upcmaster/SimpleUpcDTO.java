package com.ust.retail.store.pim.dto.upcmaster;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class SimpleUpcDTO {
	private Long upcMasterId;
	private String principalUpc;
	private String productName;
	private Long categoryId;
	private String categoryName;
	

	public SimpleUpcDTO parseToDTO(UpcMasterModel model) {
		this.upcMasterId = model.getUpcMasterId();
		this.principalUpc = model.getPrincipalUpc();
		this.productName = model.getProductName();
		return this;
	}
	
	public SimpleUpcDTO(UpcMasterModel model) {
		this.upcMasterId = model.getUpcMasterId();
		this.productName = model.getProductName();
		this.principalUpc = model.getPrincipalUpc();
		this.categoryId = model.getProductCategory().getProductCategoryId();
		this.categoryName = model.getProductCategory().getProductCategoryName();
	}
}
